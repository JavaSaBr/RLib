package javasabr.rlib.network.packet.impl;

import static javasabr.rlib.network.util.NetworkUtils.hexDump;

import java.nio.ByteBuffer;
import java.util.function.Consumer;
import javasabr.rlib.common.util.BufferUtils;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.UnsafeConnection;
import javasabr.rlib.network.packet.ReadableNetworkPacket;
import javasabr.rlib.network.packet.WritableNetworkPacket;
import javasabr.rlib.network.util.NetworkUtils;
import javasabr.rlib.network.util.SslUtils;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLException;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * @author JavaSaBr
 */
@CustomLog
@Accessors(fluent = true, chain = false)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractSslNetworkPacketReader<
    R extends ReadableNetworkPacket<C>,
    C extends UnsafeConnection<C>> extends AbstractNetworkPacketReader<R, C> {

  private static final ByteBuffer[] EMPTY_BUFFERS = {
      NetworkUtils.EMPTY_BUFFER
  };

  private static final int SKIP_READ_PACKETS = -1;

  final SSLEngine sslEngine;
  final Consumer<WritableNetworkPacket<C>> packetWriter;

  @Getter(value = AccessLevel.PROTECTED)
  volatile ByteBuffer sslNetworkBuffer;
  @Getter(value = AccessLevel.PROTECTED)
  volatile ByteBuffer sslDataBuffer;
  @Getter(value = AccessLevel.PROTECTED)
  volatile ByteBuffer sslDataPendingBuffer;

  protected AbstractSslNetworkPacketReader(
      C connection,
      Runnable updateActivityFunction,
      Consumer<? super R> validPacketHandler,
      Consumer<? super R> invalidPacketHandler,
      SSLEngine sslEngine,
      Consumer<WritableNetworkPacket<C>> packetWriter,
      int maxPacketsByRead) {
    super(connection, updateActivityFunction, validPacketHandler, invalidPacketHandler, maxPacketsByRead);
    BufferAllocator bufferAllocator = connection.bufferAllocator();
    this.sslEngine = sslEngine;
    this.sslDataBuffer = bufferAllocator.takeBuffer(sslEngine
        .getSession()
        .getApplicationBufferSize());
    this.sslDataPendingBuffer = bufferAllocator.takeBuffer(sslEngine
        .getSession()
        .getApplicationBufferSize() * 2);
    this.sslNetworkBuffer = bufferAllocator
        .takeBuffer(sslEngine
        .getSession()
        .getPacketBufferSize())
        .limit(0);
    this.packetWriter = packetWriter;
  }

  @Override
  protected void handleReceivedData(int receivedBytes, ByteBuffer readingBuffer) {
    if (receivedBytes == -1) {
      doHandshake(sslNetworkBuffer(), -1);
      connection.close();
      return;
    }
    super.handleReceivedData(receivedBytes, readingBuffer);
  }

  @Override
  protected int readPackets(ByteBuffer readingBuffer) {
    ByteBuffer networkBuffer = moveDataToNetworkBuffer(readingBuffer);
    HandshakeStatus handshakeStatus = sslEngine.getHandshakeStatus();
    if (SslUtils.isReadyToCrypt(handshakeStatus)) {
      return decryptAndRead(networkBuffer);
    } else {
      return doHandshake(networkBuffer, networkBuffer.limit());
    }
  }

  protected int doHandshake(ByteBuffer networkBuffer, int receivedBytes) {
    HandshakeStatus handshakeStatus = sslEngine.getHandshakeStatus();
    String remoteAddress = remoteAddress();
    while (SslUtils.needToProcess(handshakeStatus)) {
      log.debug(remoteAddress, handshakeStatus, "[%s] Do handshake with status:[%s] "::formatted);
      switch (handshakeStatus) {
        case NEED_UNWRAP: {
          SSLEngineResult result;
          if (receivedBytes == -1) {
            if (sslEngine.isInboundDone() && sslEngine.isOutboundDone()) {
              return SKIP_READ_PACKETS;
            }
            try {
              sslEngine.closeInbound();
            } catch (SSLException e) {
              log.error("This engine was forced to close inbound, without having received the "
                  + "proper SSL/TLS close notification message from the peer, due to end of stream.");
            }
            sslEngine.closeOutbound();
            handshakeStatus = sslEngine.getHandshakeStatus();
            break;
          } else if (!networkBuffer.hasRemaining()) {
            NetworkUtils.cleanNetworkBuffer(networkBuffer);
            return SKIP_READ_PACKETS;
          }
          try {
            logDataBeforeUnwrap(remoteAddress, networkBuffer);
            result = sslEngine.unwrap(networkBuffer, EMPTY_BUFFERS);
            handshakeStatus = result.getHandshakeStatus();
            log.debug(remoteAddress, handshakeStatus, "[%s] Handshake status:[%s] after unwrapping"::formatted);
          } catch (SSLException sslException) {
            log.error("A problem was encountered while processing the data that caused the "
                + "SSLEngine to abort. Will try to properly close connection...");
            sslEngine.closeOutbound();
            handshakeStatus = sslEngine.getHandshakeStatus();
            break;
          }
          switch (result.getStatus()) {
            case OK: {
              break;
            }
            case BUFFER_OVERFLOW: {
              throw new IllegalStateException("Unexpected SSL Engine result:" + result.getStatus());
            }
            case BUFFER_UNDERFLOW: {
              log.debug(remoteAddress, "[%s] Wait for more received data..."::formatted);
              NetworkUtils.compactNetworkBufferIfNeed(networkBuffer);
              return SKIP_READ_PACKETS;
            }
            case CLOSED: {
              if (sslEngine.isOutboundDone()) {
                return SKIP_READ_PACKETS;
              } else {
                sslEngine.closeOutbound();
                handshakeStatus = sslEngine.getHandshakeStatus();
                break;
              }
            }
            default: {
              throw new IllegalStateException("Invalid SSL Engine result:" + result.getStatus());
            }
          }
          break;
        }
        case NEED_WRAP: {
          log.debug(remoteAddress, "[%s] Send command to wrap data"::formatted);
          packetWriter.accept(SslWrapRequestNetworkPacket.getInstance());
          if (networkBuffer.hasRemaining()) {
            return decryptAndRead(networkBuffer);
          }
          NetworkUtils.cleanNetworkBuffer(networkBuffer);
          return SKIP_READ_PACKETS;
        }
        case NEED_TASK: {
          handshakeStatus = SslUtils.executeSslTasks(sslEngine);
          log.debug(remoteAddress, handshakeStatus, "[%s] Handshake status:[%s] after engine tasks"::formatted);
          if (handshakeStatus == HandshakeStatus.NEED_UNWRAP && !networkBuffer.hasRemaining()) {
            NetworkUtils.cleanNetworkBuffer(networkBuffer);
            return SKIP_READ_PACKETS;
          }
          break;
        }
        default: {
          throw new IllegalStateException("Invalid SSL status:" + handshakeStatus);
        }
      }
    }

    if (!networkBuffer.hasRemaining()) {
      // if buffer is empty and status is FINISHED then we can notify writer
      if (handshakeStatus == HandshakeStatus.FINISHED) {
        packetWriter.accept(SslWrapRequestNetworkPacket.getInstance());
      }
      NetworkUtils.cleanNetworkBuffer(networkBuffer);
      return SKIP_READ_PACKETS;
    }

    return decryptAndRead(networkBuffer);
  }

  protected int decryptAndRead(ByteBuffer receivedBuffer) {
    String remoteAddress = remoteAddress();
    int total = 0;
    while (receivedBuffer.hasRemaining()) {
      ByteBuffer sslDataBuffer = sslDataBuffer();
      SSLEngineResult result;
      try {
        logDataBeforeDecrypt(remoteAddress, receivedBuffer);
        result = sslEngine.unwrap(receivedBuffer, sslDataBuffer.clear());
      } catch (SSLException e) {
        throw new IllegalStateException(e);
      }
      switch (result.getStatus()) {
        case OK: {
          if (result.bytesConsumed() == 0 && result.bytesProduced() == 0) {
            log.debug(remoteAddress, "[%s] No progress during decryption, stop processing"::formatted);
            return total;
          }
          sslDataBuffer.flip();
          logDataAfterDecrypt(remoteAddress, sslDataBuffer);
          total += readPackets(sslDataBuffer, sslDataPendingBuffer);
          break;
        }
        case BUFFER_OVERFLOW: {
          log.debug(remoteAddress, "[%s] Increase SSL data buffer and try again..."::formatted);
          increaseDataBuffer();
          return decryptAndRead(receivedBuffer);
        }
        case BUFFER_UNDERFLOW: {
          log.debug(remoteAddress, "[%s] Wait for more received data..."::formatted);
          NetworkUtils.compactNetworkBufferIfNeed(receivedBuffer);
          return SKIP_READ_PACKETS;
        }
        case CLOSED: {
          connection.close();
          return SKIP_READ_PACKETS;
        }
        default: {
          throw new IllegalStateException("Invalid SSL status:" + result.getStatus());
        }
      }
    }

    log.debug(remoteAddress, "[%s] Clear SSL network buffer"::formatted);
    NetworkUtils.cleanNetworkBuffer(receivedBuffer);
    return total;
  }

  protected ByteBuffer moveDataToNetworkBuffer(ByteBuffer readingBuffer) {
    String remoteAddress = remoteAddress();
    logAcceptedNewDataPart(remoteAddress, readingBuffer);

    ByteBuffer sslNetworkBuffer = sslNetworkBuffer();
    int availableSpace = sslNetworkBuffer.capacity() - sslNetworkBuffer.limit();
    if (availableSpace >= readingBuffer.limit()) {
      BufferUtils.appendAndClear(sslNetworkBuffer, readingBuffer);
    } else {
      sslNetworkBuffer = increaseNetworkBuffer(readingBuffer.limit());
      BufferUtils.appendAndClear(sslNetworkBuffer, readingBuffer);
    }

    logPendingNetworkData(remoteAddress, sslNetworkBuffer);
    return sslNetworkBuffer;
  }

  protected synchronized ByteBuffer increaseNetworkBuffer(int extra) {
    BufferAllocator bufferAllocator = connection.bufferAllocator();
    ByteBuffer current = sslNetworkBuffer();
    int newSize = (int) Math.max(current.capacity() * 1.3, current.capacity() + extra);
    sslNetworkBuffer = NetworkUtils
        .increaseBuffer(current, bufferAllocator, newSize)
        .flip();
    return sslNetworkBuffer;
  }

  protected synchronized void increaseDataBuffer() {
    BufferAllocator allocator = connection.bufferAllocator();
    int newSize = sslEngine
        .getSession()
        .getApplicationBufferSize();
    sslDataBuffer = NetworkUtils
        .increaseBuffer(sslDataBuffer, allocator, newSize)
        .flip();
    sslDataPendingBuffer = NetworkUtils
        .increaseBuffer(sslDataPendingBuffer, allocator, newSize * 2)
        .flip();
  }

  @Override
  public void close() {
    sslEngine.closeOutbound();
    connection
        .bufferAllocator()
        .putBuffer(sslDataBuffer)
        .putBuffer(sslDataPendingBuffer)
        .putBuffer(sslNetworkBuffer);
    super.close();
  }

  private static void logDataBeforeUnwrap(String remoteAddress, ByteBuffer networkBuffer) {
    log.debug(remoteAddress, networkBuffer,
        (address, buff) -> "[%s] Try to unwrap data:\n%s".formatted(address, hexDump(buff)));
  }

  private static void logDataBeforeDecrypt(String remoteAddress, ByteBuffer receivedBuffer) {
    log.debug(remoteAddress, receivedBuffer,
        (address, buf) -> "[%s] Try to decrypt data:\n%s".formatted(address, hexDump(buf)));
  }

  private static void logDataAfterDecrypt(String remoteAddress, ByteBuffer sslDataBuffer) {
    log.debug(remoteAddress, sslDataBuffer,
        (address, buf) -> "[%s] Decrypted data:\n%s".formatted(address, hexDump(buf)));
  }

  private static void logAcceptedNewDataPart(String remoteAddress, ByteBuffer readingBuffer) {
    log.debug(remoteAddress, readingBuffer,
        (address, buf) -> "[%s] Append new part of received data:\n%s".formatted(address, hexDump(buf)));
  }

  private static void logPendingNetworkData(String remoteAddress, ByteBuffer sslNetworkBuffer) {
    log.debug(remoteAddress, sslNetworkBuffer,
        (address, buf) -> "[%s] Result pending received network data:\n%s".formatted(address, hexDump(buf)));
  }
}
