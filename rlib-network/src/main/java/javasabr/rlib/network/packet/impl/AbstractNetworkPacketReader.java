package javasabr.rlib.network.packet.impl;

import static javasabr.rlib.common.util.ObjectUtils.notNull;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import javasabr.rlib.common.util.BufferUtils;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.NetworkConfig;
import javasabr.rlib.network.UnsafeConnection;
import javasabr.rlib.network.exception.MalformedProtocolException;
import javasabr.rlib.network.packet.NetworkPacketReader;
import javasabr.rlib.network.packet.ReadableNetworkPacket;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@CustomLog
@Accessors(fluent = true, chain = false)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractNetworkPacketReader<
    R extends ReadableNetworkPacket<C>,
    C extends UnsafeConnection<C>> implements NetworkPacketReader {

  final CompletionHandler<Integer, ByteBuffer> readChannelHandler = new CompletionHandler<>() {

    @Override
    public void completed(Integer receivedBytes, ByteBuffer readingBuffer) {
      try {
        handleReceivedData(receivedBytes, readingBuffer);
      } catch (RuntimeException ex) {
        handleFailedReceiving(ex, readingBuffer);
      }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer readingBuffer) {
      handleFailedReceiving(exc, readingBuffer);
    }
  };

  final AtomicBoolean reading = new AtomicBoolean(false);
  final AtomicInteger emptyReadsCounter = new AtomicInteger(0);

  final C connection;

  final ByteBuffer readBuffer;
  final ByteBuffer pendingBuffer;

  final Runnable updateActivityFunction;
  final Consumer<? super R> validPacketHandler;
  final Consumer<? super R> invalidPacketHandler;

  @Getter(AccessLevel.PROTECTED)
  @Setter(AccessLevel.PROTECTED)
  @Nullable
  volatile ByteBuffer tempBigBuffer;

  final int maxPacketsByRead;

  protected AbstractNetworkPacketReader(
      C connection,
      Runnable updateActivityFunction,
      Consumer<? super R> validPacketHandler,
      Consumer<? super R> invalidPacketHandler,
      int maxPacketsByRead) {
    this.connection = connection;
    this.readBuffer = connection.bufferAllocator().takeReadBuffer();
    this.pendingBuffer = connection.bufferAllocator().takePendingBuffer();
    this.updateActivityFunction = updateActivityFunction;
    this.validPacketHandler = validPacketHandler;
    this.invalidPacketHandler = invalidPacketHandler;
    this.maxPacketsByRead = maxPacketsByRead;
  }

  protected ByteBuffer bufferToReadFromChannel() {
    return readBuffer;
  }

  protected String remoteAddress() {
    return connection.remoteAddress();
  }

  @Override
  public void startRead() {
    if (connection.closed()) {
      log.warn(connection.remoteAddress(), "[%s] Connection is already closed"::formatted);
      return;
    } else if (!reading.compareAndSet(false, true)) {
      log.debug(connection.remoteAddress(), "[%s] Connection is already waiting for new data from channel"::formatted);
      return;
    }
    startReadImpl();
  }

  protected void startReadImpl() {
    log.debug(remoteAddress(), "[%s] Start waiting for new data from channel..."::formatted);
    ByteBuffer buffer = bufferToReadFromChannel();
    try {
      connection.channel().read(buffer, buffer, readChannelHandler);
    } catch (RuntimeException ex) {
      log.error(ex);
      if (reading.compareAndSet(true, false)) {
        retryReadLater();
      }
    }
  }

  protected void retryReadLater() {
    Network<?> network = connection.network();
    NetworkConfig config = network.config();
    network
        .scheduledExecutor()
        .schedule(this::startRead, config.retryDelayInMs(), TimeUnit.MILLISECONDS);
  }

  /**
   * Reads packets from the buffer with received data.
   *
   * @param receivedBuffer the buffer with received data.
   * @return count of read packets.
   */
  protected int readPackets(ByteBuffer receivedBuffer) {
    return readPackets(receivedBuffer, pendingBuffer);
  }

  /**
   * Reads packets from the buffer with received data.
   *
   * @param receivedBuffer the buffer with received data.
   * @param pendingBuffer the buffer with pending data from prev. received buffer.
   * @return count of read packets.
   */
  protected int readPackets(ByteBuffer receivedBuffer, ByteBuffer pendingBuffer) {
    String remoteAddress = remoteAddress();
    NetworkConfig networkConfig = connection
        .network()
        .config();

    log.debug(remoteAddress, receivedBuffer,
        "[%s] Start reading packets from received buffer:[%s]"::formatted);

    int waitedBytes = pendingBuffer.position();

    ByteBuffer bufferToRead = receivedBuffer;
    ByteBuffer tempBigBuffer = tempBigBuffer();

    // if we have a temp big buffer it means that we are reading a big packet now
    if (tempBigBuffer != null) {
      // check and extends temp buffer if need
      if (tempBigBuffer.remaining() < receivedBuffer.remaining()) {
        reAllocTempBigBuffers(tempBigBuffer.flip(), tempBigBuffer.capacity());
        tempBigBuffer = notNull(tempBigBuffer());
      }

      log.debug(
          remoteAddress, receivedBuffer, tempBigBuffer,
          "[%s] Put received buffer:[%s] to temp big buffer:[%s]"::formatted);

      bufferToRead = BufferUtils.putToAndFlip(tempBigBuffer, receivedBuffer);
    }
    // if we have some pending data we need to append the received buffer to the pending buffer
    // and start to read pending buffer with result received data
    else if (waitedBytes > 0) {
      if (pendingBuffer.remaining() < receivedBuffer.remaining()) {
        log.debug(
            remoteAddress,
            pendingBuffer,
            receivedBuffer,
            "[%s] Pending buffer:[%s] is too small to append received buffer:[%s], allocate new temp bug buffer for this"::formatted);

        allocTempBigBuffers(pendingBuffer.flip(), pendingBuffer.capacity());

        log.debug(remoteAddress, pendingBuffer, "[%s] Clear pending buffer:[%s]"::formatted);
        pendingBuffer.clear();

        tempBigBuffer = notNull(tempBigBuffer());

        log.debug(
            remoteAddress, receivedBuffer, tempBigBuffer,
            "[%s] Put received buffer:[%s] to temp big buffer:[%s]"::formatted);
        bufferToRead = BufferUtils.putToAndFlip(tempBigBuffer, receivedBuffer);
      } else {
        log.debug(
            remoteAddress, receivedBuffer, pendingBuffer,
            "[%s] Put received buffer:[%s] to pending buffer:[%s]"::formatted);
        bufferToRead = BufferUtils.putToAndFlip(pendingBuffer, receivedBuffer);
      }
    }

    int maxPacketsByRead = maxPacketsByRead();
    int readPackets = 0;
    int endPosition = 0;

    while (canStartReadPacket(bufferToRead) && readPackets < maxPacketsByRead) {
      // set position to start reading a next packet
      bufferToRead.position(endPosition);

      int positionBeforeRead = endPosition;
      int packetFullLength = readFullPacketLength(bufferToRead);
      if (packetFullLength > networkConfig.maxPacketSize()) {
        throw new MalformedProtocolException(
            "Received to big packet:[" + packetFullLength + ">" + networkConfig.maxPacketSize() + "]");
      }

      int alreadyReadBytes = bufferToRead.position() - endPosition;
      int packetDataLength = calculatePacketDataLength(packetFullLength, alreadyReadBytes, bufferToRead);

      log.debug(
          remoteAddress, positionBeforeRead, packetFullLength,
          "[%s] Found next packet from position:[%s] with length:[%s] "::formatted);

      // calculate position of the end of next packet
      endPosition += packetFullLength;

      // if the packet isn't full presented in this buffer
      if (packetFullLength == -1 || endPosition > bufferToRead.limit()) {
        bufferToRead.position(positionBeforeRead);

        // if we read the received buffer we need to put not yet read data to the pending
        // buffer or temp big buffer
        if (bufferToRead == receivedBuffer) {
          if (packetFullLength <= pendingBuffer.capacity()) {
            pendingBuffer.put(receivedBuffer);
            log.debug(
                remoteAddress, pendingBuffer,
                "[%s] Put pending data form received buffer to pending buffer:[%s]"::formatted);
          } else {
            allocTempBigBuffers(receivedBuffer, packetFullLength);
          }
        }
        // if we already read this pending buffer we need to compact it
        else if (bufferToRead == pendingBuffer) {
          if (packetFullLength <= pendingBuffer.capacity()) {
            pendingBuffer.compact();
            log.debug(remoteAddress, pendingBuffer, "[%s] Compact pending buffer:[%s]"::formatted);
          } else {
            allocTempBigBuffers(pendingBuffer, packetFullLength);
            log.debug(remoteAddress, pendingBuffer, "[%s] Clear pending buffer:[%s]"::formatted);
            pendingBuffer.clear();
          }

        } else if (bufferToRead == tempBigBuffer) {
          // if not yet read data is less than pending buffer, then we can switch to use the pending buffer
          if (Math.max(packetFullLength, tempBigBuffer.remaining()) <= pendingBuffer.capacity()) {
            pendingBuffer.clear().put(tempBigBuffer);
            log.debug(
                remoteAddress, pendingBuffer,
                "[%s] Moved pending data from temp big buffer to pending buffer:[%s]"::formatted);
            freeTempBigBuffers();
          }
          // if a new packet is bigger than current temp big buffer
          else if (packetFullLength > tempBigBuffer.capacity()) {
            reAllocTempBigBuffers(tempBigBuffer, packetFullLength);
          }
          // or just compact this current temp big buffer
          else {
            tempBigBuffer.compact();
            log.debug(remoteAddress, tempBigBuffer, "[%s] Compact temp big buffer:[%s]]"::formatted);
          }
        }

        log.debug(
            remoteAddress,
            readPackets,
            connection.remoteAddress(),
            ("[%s] Read [%s] packets from received buffer of [%s], "
                 + "but 1 packet is still waiting for receiving additional data.")::formatted);

        receivedBuffer.clear();
        return readPackets;
      }

      R readablePacket = createPacketFor(
          bufferToRead,
          positionBeforeRead,
          packetFullLength,
          packetDataLength);

      if (readablePacket != null) {
        int remainingDataLength = endPosition - bufferToRead.position();
        log.debug(remoteAddress, readablePacket, "[%s] Created instance of packet to read data:[%s]"::formatted);
        readAndHandlePacket(bufferToRead, remainingDataLength, readablePacket);
        log.debug(remoteAddress, readablePacket, "[%s] Finished reading data for packet:[%s]"::formatted);
        readPackets++;
      } else {
        log.warn(remoteAddress, "[%s] Cannot create any instance of packet to read data"::formatted);
      }
    }

    bufferToRead.position(endPosition);

    if (bufferToRead.hasRemaining()) {
      if (bufferToRead == receivedBuffer) {
        pendingBuffer.put(receivedBuffer);
        log.debug(remoteAddress, "[%s] Found not yet read data from receive buffer, will put it to pending buffer"::formatted);
      } else {
        bufferToRead.compact();
      }
    } else if (bufferToRead == pendingBuffer) {
      pendingBuffer.clear();
    } else if (tempBigBuffer != null) {
      freeTempBigBuffers();
    }

    log.debug(remoteAddress, readPackets, "[%s] Read [%s] packets from received buffer"::formatted);
    receivedBuffer.clear();
    return readPackets;
  }

  protected void readAndHandlePacket(ByteBuffer bufferToRead, int remainingDataLength, R packetInstance) {
    if (packetInstance.read(connection, bufferToRead, remainingDataLength)) {
      validPacketHandler.accept(packetInstance);
    } else {
      invalidPacketHandler.accept(packetInstance);
    }
  }

  /**
   * Checks the buffer's data.
   *
   * @return true if this buffer has enough data to start initial reading.
   */
  protected abstract boolean canStartReadPacket(ByteBuffer buffer);

  /**
   * Calculates size of packet data.
   *
   * @param packetLength the full packet length.
   * @param alreadyReadBytes the count of already read bytes from buffer to get packet length.
   * @param buffer the data buffer.
   * @return the length of packet data part.
   */
  protected int calculatePacketDataLength(int packetLength, int alreadyReadBytes, ByteBuffer buffer) {
    return packetLength - alreadyReadBytes;
  }

  /**
   * Gets the packet's full length of next packet in the buffer.
   *
   * @param buffer the buffer with received data.
   * @return the packet length or -1 if we have no enough data to read length.
   */
  protected abstract int readFullPacketLength(ByteBuffer buffer);

  protected void reAllocTempBigBuffers(ByteBuffer sourceBuffer, int fullPacketLength) {
    log.debug(remoteAddress(), sourceBuffer.capacity(), fullPacketLength,
        "[%s] Resize temp big buffer from:[%s] to:[%s]"::formatted);

    ByteBuffer newTempBuffer = connection
        .bufferAllocator()
        .takeBuffer(fullPacketLength + readBuffer.capacity());

    log.debug(remoteAddress(), sourceBuffer, newTempBuffer,
        "[%s] Moved data from old temp big buffer:[%s] to new:[%s]"::formatted);

    newTempBuffer.put(sourceBuffer);

    freeTempBigBuffers();
    this.tempBigBuffer = newTempBuffer;
  }

  protected void allocTempBigBuffers(ByteBuffer sourceBuffer, int fullPacketLength) {
    int notConsumeBytes = sourceBuffer.remaining();

    log.debug(
        notConsumeBytes,
        fullPacketLength,
        "Request temp big buffer to store part:[%s] of big packet with length:[%s]"::formatted);

    ByteBuffer tempBigBuffer = connection
        .bufferAllocator()
        .takeBuffer(fullPacketLength + readBuffer.capacity());

    log.debug(sourceBuffer, tempBigBuffer, "Put data from old temp big buffer:[%s] to new:[%s]"::formatted);

    tempBigBuffer.put(sourceBuffer);
    this.tempBigBuffer = tempBigBuffer;
  }

  protected void freeTempBigBuffers() {
    ByteBuffer tempBuffer = tempBigBuffer();
    if (tempBuffer != null) {
      tempBigBuffer(null);
      connection
          .bufferAllocator()
          .putBuffer(tempBuffer);
    }
  }

  /**
   * Handles received data from the channel.
   */
  protected void handleReceivedData(int receivedBytes, ByteBuffer readingBuffer) {
    updateActivityFunction.run();
    if (receivedBytes == -1) {
      handleEmptyReadFromChannel();
    } else {
      log.debug(remoteAddress(), receivedBytes, "[%s] Received [%s] bytes from channel"::formatted);
      readingBuffer.flip();
      emptyReadsCounter.set(0);
      try {
        readPackets(readingBuffer);
      } catch (MalformedProtocolException e) {
        handleFailedReceiving(e, readingBuffer);
        return;
      }
      startReadImpl();
    }
  }

  protected void handleEmptyReadFromChannel() {
    log.debug(remoteAddress(), "[%s] Received empty bytes from channel"::formatted);

    if (connection.closed()) {
      reading.compareAndSet(true, false);
      return;
    } else if (!connection.channel().isOpen()) {
      connection.close();
      return;
    }

    NetworkConfig config = connection
        .network()
        .config();

    if (emptyReadsCounter.incrementAndGet() > config.maxEmptyReadsBeforeClose()) {
      connection.close();
      return;
    }

    if (reading.compareAndSet(true, false)) {
      retryReadLater();
    }
  }

  /**
   * Handles the exception during receiving data from the channel.
   *
   * @param exception the exception.
   * @param readingBuffer the currently reading buffer.
   */
  protected void handleFailedReceiving(Throwable exception, ByteBuffer readingBuffer) {
    switch (exception) {
      case InterruptedByTimeoutException ex -> {
        if (reading.compareAndSet(true, false)) {
          retryReadLater();
        }
      }
      case AsynchronousCloseException ex -> {
        log.info(remoteAddress(), "[%s] Connection was closed"::formatted);
        connection.close();
      }
      case ClosedChannelException ex -> {
        log.info(remoteAddress(), "[%s] Connection was closed"::formatted);
        connection.close();
      }
      default -> {
        log.error(exception);
        connection.close();
      }
    }
  }

  /**
   * Gets how many packets can be read by the one method call {@link #readPackets(ByteBuffer, ByteBuffer)}}.
   */
  protected int maxPacketsByRead() {
    return maxPacketsByRead;
  }

  protected int readHeader(ByteBuffer buffer, int headerSize) {
    return switch (headerSize) {
      case 1 -> buffer.get() & 0xFF;
      case 2 -> buffer.getShort() & 0xFFFF;
      case 4 -> buffer.getInt();
      default -> throw new MalformedProtocolException("Wrong packet's header size:" + headerSize);
    };
  }

  /**
   * Creates a packet to read the received data.
   *
   * @param buffer the buffer with received data.
   * @param startPacketPosition the start position of the packet in the buffer.
   * @param packetFullLength the length of packet.
   * @param packetDataLength length of packet's data.
   * @return the readable packet.
   */
  @Nullable
  protected abstract R createPacketFor(
      ByteBuffer buffer,
      int startPacketPosition,
      int packetFullLength,
      int packetDataLength);

  @Override
  public void close() {
    connection
        .bufferAllocator()
        .putReadBuffer(readBuffer)
        .putPendingBuffer(pendingBuffer);
    freeTempBigBuffers();
    reading.compareAndSet(true, false);
  }
}
