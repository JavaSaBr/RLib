package javasabr.rlib.network.packet.impl;

import static javasabr.rlib.network.util.NetworkUtils.EMPTY_BUFFER;
import static javasabr.rlib.network.util.NetworkUtils.hexDump;

import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javasabr.rlib.functions.ObjBoolConsumer;
import javasabr.rlib.network.UnsafeConnection;
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
import org.jspecify.annotations.Nullable;

@CustomLog
@Accessors(fluent = true, chain = false)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractSslNetworkPacketWriter<
    W extends WritableNetworkPacket<C>,
    C extends UnsafeConnection<C>> extends AbstractNetworkPacketWriter<W, C> {

  private static final ByteBuffer[] EMPTY_BUFFERS = {
      NetworkUtils.EMPTY_BUFFER
  };

  final SSLEngine sslEngine;
  final Consumer<WritableNetworkPacket<C>> queueAtFirst;

  @Getter(AccessLevel.PROTECTED)
  @Nullable
  volatile ByteBuffer sslTempBuffer;

  @Getter(AccessLevel.PROTECTED)
  volatile ByteBuffer sslNetworkBuffer;

  public AbstractSslNetworkPacketWriter(
      C connection,
      Runnable updateActivityFunction,
      Supplier<WritableNetworkPacket<C>> packetProvider,
      Consumer<WritableNetworkPacket<C>> serializedToChannelPacketHandler,
      ObjBoolConsumer<WritableNetworkPacket<C>> sentPacketHandler,
      SSLEngine sslEngine,
      Consumer<WritableNetworkPacket<C>> queueAtFirst) {
    super(
        connection,
        updateActivityFunction,
        packetProvider,
        serializedToChannelPacketHandler,
        sentPacketHandler);
    this.sslEngine = sslEngine;
    this.queueAtFirst = queueAtFirst;
    this.sslNetworkBuffer = connection
        .bufferAllocator()
        .takeBuffer(sslEngine
            .getSession()
            .getPacketBufferSize());
  }

  @Override
  public boolean tryToSendNextPacket() {
    HandshakeStatus status = sslEngine.getHandshakeStatus();
    if (status == HandshakeStatus.NEED_UNWRAP) {
      return false;
    }
    return super.tryToSendNextPacket();
  }

  @Override
  protected boolean tryToSendNextPacketImpl() {
    HandshakeStatus status = sslEngine.getHandshakeStatus();
    if (SslUtils.isReadyToCrypt(status)) {
      return super.tryToSendNextPacketImpl();
    }

    ByteBuffer dataToSend = doHandshake(status);
    if (dataToSend != null) {
      return writeBuffer(dataToSend, null);
    }

    throw new IllegalStateException("Unexpected SSL state");
  }

  @Override
  protected ByteBuffer serialize(WritableNetworkPacket<C> packet) {
    if (packet instanceof SslWrapRequestNetworkPacket) {
      return EMPTY_BUFFER;
    }

    String remoteAddress = remoteAddress();
    ByteBuffer serialized = super.serialize(packet);
    logDataBeforeEncrypt(remoteAddress, serialized);

    ByteBuffer bufferToSend = sslNetworkBuffer();
    SSLEngineResult result = tryEncrypt(serialized, bufferToSend);

    if (result.getStatus() == SSLEngineResult.Status.OK && serialized.hasRemaining()) {
      log.debug(remoteAddress, serialized.remaining(),
          "[%s] Has remaining [%s] bytes after encrypting, will create temp big buffer"::formatted);

      int tempBufferSize = (int) ((bufferToSend.limit() + serialized.remaining()) * 1.2);
      ByteBuffer tempBuffer = connection
          .bufferAllocator()
          .takeBuffer(tempBufferSize);
      tempBuffer.put(bufferToSend.flip());

      while (serialized.hasRemaining()) {
        result = tryEncrypt(serialized, bufferToSend);
        if (result.getStatus() != SSLEngineResult.Status.OK) {
          break;
        }
        tempBuffer.put(bufferToSend.flip());
      }

      bufferToSend = tempBuffer;
      this.sslTempBuffer = tempBuffer;
    }

    return switch (result.getStatus()) {
      case BUFFER_UNDERFLOW, BUFFER_OVERFLOW ->
          throw new IllegalStateException("Unexpected ssl engine result");
      case OK -> {
        bufferToSend.flip();
        logEncryptedData(remoteAddress, bufferToSend);
        yield bufferToSend;
      }
      case CLOSED -> closeAndReturn();
    };
  }

  protected ByteBuffer closeAndReturn() {
    connection.close();
    return EMPTY_BUFFER;
  }

  protected SSLEngineResult tryEncrypt(ByteBuffer source, ByteBuffer destination) {
    try {
      return sslEngine.wrap(source, destination.clear());
    } catch (SSLException ex) {
      log.error(ex);
      throw new RuntimeException(ex);
    }
  }

  @Nullable
  protected ByteBuffer doHandshake(HandshakeStatus handshakeStatus) {
    String remoteAddress = remoteAddress();
    while (SslUtils.needToProcess(handshakeStatus)) {
      log.debug(remoteAddress, handshakeStatus, "[%s] Do handshake with status:[%s] "::formatted);
      ByteBuffer sslNetworkBuffer = sslNetworkBuffer();
      switch (handshakeStatus) {
        case NEED_WRAP: {
          SSLEngineResult result;
          try {
            result = sslEngine.wrap(EMPTY_BUFFERS, sslNetworkBuffer.clear());
          } catch (SSLException sslException) {
            log.error("A problem was encountered while processing the data that caused the SSLEngine "
                + "to abort. Will try to properly close connection...");
            sslEngine.closeOutbound();
            handshakeStatus = sslEngine.getHandshakeStatus();
            break;
          }
          switch (result.getStatus()) {
            case OK:
              sslNetworkBuffer.flip();
              logWrappedData(remoteAddress, sslNetworkBuffer, result);
              return sslNetworkBuffer;
            case BUFFER_OVERFLOW, BUFFER_UNDERFLOW: {
              throw new RuntimeException("Unexpected SSL result:" + result);
            }
            case CLOSED: {
              try {
                return EMPTY_BUFFER;
              } catch (Exception e) {
                log.error("Failed to send server's CLOSE message due to socket channel's failure.");
                handshakeStatus = sslEngine.getHandshakeStatus();
              }
              break;
            }
            default: {
              throw new IllegalStateException("Invalid SSL result:" + result);
            }
          }
          break;
        }
        case NEED_TASK: {
          handshakeStatus = SslUtils.executeSslTasks(sslEngine);
          break;
        }
        case NEED_UNWRAP: {
          return EMPTY_BUFFER;
        }
        default: {
          throw new IllegalStateException("Invalid SSL status:" + handshakeStatus);
        }
      }
    }

    return EMPTY_BUFFER;
  }

  @Override
  protected void clearTempBuffers() {
    super.clearTempBuffers();
    ByteBuffer sslTempBuffer = this.sslTempBuffer;
    if (sslTempBuffer != null) {
      connection
          .bufferAllocator()
          .putBuffer(sslTempBuffer);
      this.sslTempBuffer = null;
    }
  }

  @Override
  public void close() {
    sslEngine.closeOutbound();
    connection
        .bufferAllocator()
        .putBuffer(sslNetworkBuffer);
    super.close();
  }

  private static void logDataBeforeEncrypt(String remoteAddress, ByteBuffer serialized) {
    log.debug(remoteAddress, serialized,
        (address, buff) -> "[%s] Try to encrypt data:\n%s".formatted(address, hexDump(buff)));
  }

  private static void logEncryptedData(String remoteAddress, ByteBuffer bufferToSend) {
    log.debug(remoteAddress, bufferToSend,
        (address, buff) -> "[%s] Result encrypted data:\n%s".formatted(address, hexDump(buff)));
  }

  private static void logWrappedData(String remoteAddress, ByteBuffer sslNetworkBuffer, SSLEngineResult result) {
    log.debug(remoteAddress, sslNetworkBuffer, result,
        (address, buf, res) -> "[%s] Send wrapped data:\n%s".formatted(address, hexDump(buf, res)));
  }
}
