package javasabr.rlib.network.packet.impl;

import static javasabr.rlib.network.util.NetworkUtils.EMPTY_BUFFER;
import static javasabr.rlib.network.util.NetworkUtils.getRemoteAddress;
import static javasabr.rlib.network.util.NetworkUtils.hexDump;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import javasabr.rlib.common.function.NotNullBiConsumer;
import javasabr.rlib.common.function.NotNullConsumer;
import javasabr.rlib.common.function.NullableSupplier;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.PacketWriter;
import javasabr.rlib.network.packet.WritablePacket;
import javasabr.rlib.network.util.NetworkUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@RequiredArgsConstructor
public abstract class AbstractPacketWriter<W extends WritablePacket, C extends Connection<?, W>> implements
    PacketWriter {

  private static final Logger LOGGER = LoggerManager.getLogger(AbstractPacketWriter.class);

  private final CompletionHandler<Integer, WritablePacket> writeHandler = new CompletionHandler<>() {

    @Override
    public void completed(@NotNull Integer result, @NotNull WritablePacket packet) {
      handleSuccessfulWriting(result, packet);
    }

    @Override
    public void failed(@NotNull Throwable exc, @NotNull WritablePacket packet) {
      handleFailedWriting(exc, packet);
    }
  };

  protected final @NotNull AtomicBoolean isWriting = new AtomicBoolean();

  protected final @NotNull C connection;
  protected final @NotNull AsynchronousSocketChannel channel;
  protected final @NotNull BufferAllocator bufferAllocator;
  protected final @NotNull ByteBuffer firstWriteBuffer;
  protected final @NotNull ByteBuffer secondWriteBuffer;

  protected volatile @Nullable ByteBuffer firstWriteTempBuffer;
  protected volatile @Nullable ByteBuffer secondWriteTempBuffer;

  protected volatile @NotNull ByteBuffer writingBuffer = EMPTY_BUFFER;

  protected final @NotNull Runnable updateActivityFunction;
  protected final @NotNull NullableSupplier<WritablePacket> nextWritePacketSupplier;
  protected final @NotNull NotNullConsumer<WritablePacket> writtenPacketHandler;
  protected final @NotNull NotNullBiConsumer<WritablePacket, Boolean> sentPacketHandler;

  public AbstractPacketWriter(
      @NotNull C connection,
      @NotNull AsynchronousSocketChannel channel,
      @NotNull BufferAllocator bufferAllocator,
      @NotNull Runnable updateActivityFunction,
      @NotNull NullableSupplier<WritablePacket> packetProvider,
      @NotNull NotNullConsumer<WritablePacket> writtenPacketHandler,
      @NotNull NotNullBiConsumer<WritablePacket, Boolean> sentPacketHandler) {
    this.connection = connection;
    this.channel = channel;
    this.bufferAllocator = bufferAllocator;
    this.firstWriteBuffer = bufferAllocator.takeWriteBuffer();
    this.secondWriteBuffer = bufferAllocator.takeWriteBuffer();
    this.updateActivityFunction = updateActivityFunction;
    this.nextWritePacketSupplier = packetProvider;
    this.writtenPacketHandler = writtenPacketHandler;
    this.sentPacketHandler = sentPacketHandler;
  }

  @Override
  public void writeNextPacket() {

    if (connection.isClosed() || !isWriting.compareAndSet(false, true)) {
      return;
    }

    var waitPacket = nextWritePacketSupplier.get();

    if (waitPacket == null) {
      isWriting.set(false);
      return;
    }

    var resultBuffer = serialize(waitPacket);

    if (resultBuffer.limit() != 0) {
      writingBuffer = resultBuffer;

      LOGGER.debug(
          channel,
          resultBuffer,
          (ch, buf) -> "Write to channel \"" + getRemoteAddress(ch) + "\" data:\n" + hexDump(buf));

      channel.write(resultBuffer, waitPacket, writeHandler);
    } else {
      isWriting.set(false);
    }

    writtenPacketHandler.accept(waitPacket);
  }

  protected @NotNull ByteBuffer serialize(@NotNull WritablePacket packet) {

    if (packet instanceof WritablePacketWrapper) {
      packet = ((WritablePacketWrapper<?, ?>) packet).getPacket();
    }

    W resultPacket = (W) packet;

    var expectedLength = packet.getExpectedLength();
    var totalSize = expectedLength == -1 ? -1 : getTotalSize(packet, expectedLength);

    // if the packet is too big to use a write buffer
    if (expectedLength != -1 && totalSize > firstWriteBuffer.capacity()) {
      var first = bufferAllocator.takeBuffer(totalSize);
      var second = bufferAllocator.takeBuffer(totalSize);
      firstWriteTempBuffer = first;
      secondWriteTempBuffer = second;
      return serialize(resultPacket, expectedLength, totalSize, first, second);
    } else {
      return serialize(resultPacket, expectedLength, totalSize, firstWriteBuffer, secondWriteBuffer);
    }
  }

  /**
   * Get a total size of packet if it possible.
   *
   * @param packet the packet.
   * @param expectedLength the expected size.
   * @return the total size or -1.
   */
  protected abstract int getTotalSize(@NotNull WritablePacket packet, int expectedLength);

  /**
   * Serialize packet to byte buffer.
   *
   * @param packet the packet to serialize.
   * @param expectedLength the packet's expected size.
   * @param totalSize the packet's total size.
   * @param firstBuffer the first byte buffer.
   * @param secondBuffer the second byte buffer.
   * @return the buffer to write to channel.
   */
  protected @NotNull ByteBuffer serialize(
      @NotNull W packet,
      int expectedLength,
      int totalSize,
      @NotNull ByteBuffer firstBuffer,
      @NotNull ByteBuffer secondBuffer) {

    if (!onBeforeWrite(packet, expectedLength, totalSize, firstBuffer, secondBuffer)) {
      return firstBuffer
          .clear()
          .limit(0);
    } else if (!onWrite(packet, expectedLength, totalSize, firstBuffer, secondBuffer)) {
      return firstBuffer
          .clear()
          .limit(0);
    } else if (!onAfterWrite(packet, expectedLength, totalSize, firstBuffer, secondBuffer)) {
      return firstBuffer
          .clear()
          .limit(0);
    }

    return onResult(packet, expectedLength, totalSize, firstBuffer, secondBuffer);
  }

  /**
   * Handle a byte buffer before writing packet's data.
   *
   * @param packet the packet.
   * @param expectedLength the packet's expected size.
   * @param totalSize the packet's total size.
   * @param firstBuffer the first byte buffer.
   * @param secondBuffer the second byte buffer.
   * @return true if handling was successful.
   */
  protected boolean onBeforeWrite(
      @NotNull W packet,
      int expectedLength,
      int totalSize,
      @NotNull ByteBuffer firstBuffer,
      @NotNull ByteBuffer secondBuffer) {
    firstBuffer.clear();
    return true;
  }

  /**
   * Write a packet to byte buffer.
   *
   * @param packet the packet.
   * @param expectedLength the packet's expected size.
   * @param totalSize the packet's total size.
   * @param firstBuffer the first byte buffer.
   * @param secondBuffer the second byte buffer.
   * @return true if writing was successful.
   */
  protected boolean onWrite(
      @NotNull W packet,
      int expectedLength,
      int totalSize,
      @NotNull ByteBuffer firstBuffer,
      @NotNull ByteBuffer secondBuffer) {
    return packet.write(firstBuffer);
  }

  /**
   * Handle a byte buffer after writing packet's data.
   *
   * @param packet the packet.
   * @param expectedLength the packet's expected size.
   * @param totalSize the packet's total size.
   * @param firstBuffer the first byte buffer.
   * @param secondBuffer the second byte buffer.
   * @return true if handling was successful.
   */
  protected boolean onAfterWrite(
      @NotNull W packet,
      int expectedLength,
      int totalSize,
      @NotNull ByteBuffer firstBuffer,
      @NotNull ByteBuffer secondBuffer) {
    firstBuffer.flip();
    return true;
  }

  /**
   * Handle a final result byte buffer.
   *
   * @param packet the packet.
   * @param expectedLength the packet's expected size.
   * @param totalSize the packet's total size.
   * @param firstBuffer the first byte buffer.
   * @param secondBuffer the second byte buffer.
   * @return the same byte buffer.
   */
  protected @NotNull ByteBuffer onResult(
      @NotNull W packet,
      int expectedLength,
      int totalSize,
      @NotNull ByteBuffer firstBuffer,
      @NotNull ByteBuffer secondBuffer) {
    return firstBuffer.position(0);
  }

  protected @NotNull ByteBuffer writeHeader(@NotNull ByteBuffer buffer, int position, int value, int headerSize) {
    try {

      switch (headerSize) {
        case 1:
          buffer.put(position, (byte) value);
          break;
        case 2:
          buffer.putShort(position, (short) value);
          break;
        case 4:
          buffer.putInt(position, value);
          break;
        default:
          throw new IllegalStateException("Wrong packet's header size: " + headerSize);
      }

      return buffer;

    } catch (IndexOutOfBoundsException ex) {
      LOGGER.error(
          "Cannot write header by position " + position + " with header size " + headerSize + " to buffer " + buffer);
      throw ex;
    }
  }

  protected @NotNull ByteBuffer writeHeader(@NotNull ByteBuffer buffer, int value, int headerSize) {

    switch (headerSize) {
      case 1:
        buffer.put((byte) value);
        break;
      case 2:
        buffer.putShort((short) value);
        break;
      case 4:
        buffer.putInt(value);
        break;
      default:
        throw new IllegalStateException("Wrong packet's header size: " + headerSize);
    }

    return buffer;
  }

  /**
   * Handle successful wrote data.
   *
   * @param result the count of wrote bytes.
   * @param packet the sent packet.
   */
  protected void handleSuccessfulWriting(@NotNull Integer result, @NotNull WritablePacket packet) {
    updateActivityFunction.run();

    if (result == -1) {
      sentPacketHandler.accept(packet, Boolean.FALSE);
      connection.close();
      return;
    }

    var writingBuffer = this.writingBuffer;

    if (writingBuffer.remaining() > 0) {
      LOGGER.debug(
          writingBuffer,
          channel,
          (buf, ch) -> "Buffer was not consumed fully, " + "try to write else " + buf.remaining() + " bytes to channel "
              + NetworkUtils.getRemoteAddress(ch));
      channel.write(writingBuffer, packet, writeHandler);
      return;
    } else {
      LOGGER.debug(result, bytes -> "Done writing " + bytes + " bytes");
    }

    sentPacketHandler.accept(packet, Boolean.TRUE);

    if (isWriting.compareAndSet(true, false)) {

      // if we have temp buffers, we can remove it after finishing writing a packet
      if (firstWriteTempBuffer != null) {
        clearTempBuffers();
      }

      writeNextPacket();
    }
  }

  /**
   * Handle the exception during writing the packet.
   *
   * @param exception the exception.
   * @param packet the packet.
   */
  protected void handleFailedWriting(@NotNull Throwable exception, @NotNull WritablePacket packet) {
    LOGGER.error(new RuntimeException("Failed writing packet: " + packet, exception));

    if (!connection.isClosed()) {
      if (isWriting.compareAndSet(true, false)) {
        writeNextPacket();
      }
    }
  }

  @Override
  public void close() {

    bufferAllocator
        .putWriteBuffer(firstWriteBuffer)
        .putWriteBuffer(secondWriteBuffer);

    clearTempBuffers();

    writingBuffer = EMPTY_BUFFER;
  }

  protected void clearTempBuffers() {

    var secondWriteTempBuffer = this.secondWriteTempBuffer;
    var firstWriteTempBuffer = this.firstWriteTempBuffer;

    if (secondWriteTempBuffer != null) {
      this.secondWriteTempBuffer = null;
      bufferAllocator.putBuffer(secondWriteTempBuffer);
    }

    if (firstWriteTempBuffer != null) {
      this.firstWriteTempBuffer = null;
      bufferAllocator.putBuffer(firstWriteTempBuffer);
    }
  }
}
