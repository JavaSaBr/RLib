package javasabr.rlib.network.packet.impl;

import static javasabr.rlib.network.util.NetworkUtils.hexDump;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.common.function.NotNullConsumer;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.ReadablePacket;
import javasabr.rlib.network.packet.WritablePacket;
import javasabr.rlib.network.util.NetworkUtils;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLException;

/**
 * @param <R> the readable packet's type.
 * @param <C> the connection's type.
 */
public abstract class AbstractSSLPacketReader<R extends ReadablePacket, C extends Connection<R, ?>> extends
    AbstractPacketReader<R, C> {

  private static final Logger LOGGER = LoggerManager.getLogger(AbstractSSLPacketReader.class);

  private static final ByteBuffer[] EMPTY_BUFFERS = {
      NetworkUtils.EMPTY_BUFFER
  };

  private static final int SKIP_READ_PACKETS = -1;

  protected final SSLEngine sslEngine;
  protected final NotNullConsumer<WritablePacket> packetWriter;

  protected volatile ByteBuffer sslNetworkBuffer;
  protected volatile ByteBuffer sslDataBuffer;

  protected AbstractSSLPacketReader(
      C connection,
      AsynchronousSocketChannel channel,
      BufferAllocator bufferAllocator,
      Runnable updateActivityFunction,
      NotNullConsumer<? super R> readPacketHandler,
      SSLEngine sslEngine,
      NotNullConsumer<WritablePacket> packetWriter,
      int maxPacketsByRead) {
    super(connection, channel, bufferAllocator, updateActivityFunction, readPacketHandler, maxPacketsByRead);
    this.sslEngine = sslEngine;
    this.sslDataBuffer = bufferAllocator.takeBuffer(sslEngine
        .getSession()
        .getApplicationBufferSize());
    this.sslNetworkBuffer = bufferAllocator.takeBuffer(sslEngine
        .getSession()
        .getPacketBufferSize());
    this.packetWriter = packetWriter;
  }

  @Override
  protected ByteBuffer getBufferToReadFromChannel() {
    return sslNetworkBuffer;
  }

  @Override
  protected void handleReceivedData(Integer receivedBytes, ByteBuffer readingBuffer) {

    if (receivedBytes == -1) {
      doHandshake(readingBuffer, -1);
      return;
    }

    super.handleReceivedData(receivedBytes, readingBuffer);
  }

  @Override
  protected int readPackets(ByteBuffer receivedBuffer) {

    var handshakeStatus = sslEngine.getHandshakeStatus();

    // ssl engine is ready to decrypt
    if (handshakeStatus == HandshakeStatus.FINISHED || handshakeStatus == HandshakeStatus.NOT_HANDSHAKING) {
      return decryptAndRead(receivedBuffer);
    } else {
      return doHandshake(receivedBuffer, receivedBuffer.limit());
    }
  }

  protected int doHandshake(ByteBuffer receivedBuffer, int receivedBytes) {

    var handshakeStatus = sslEngine.getHandshakeStatus();

    while (handshakeStatus != HandshakeStatus.FINISHED && handshakeStatus != HandshakeStatus.NOT_HANDSHAKING) {
      LOGGER.debug(handshakeStatus, status -> "Do handshake with status: " + status);

      SSLEngineResult result;

      switch (handshakeStatus) {
        case NEED_UNWRAP: {

          if (receivedBytes == -1) {

            if (sslEngine.isInboundDone() && sslEngine.isOutboundDone()) {
              return SKIP_READ_PACKETS;
            }

            try {
              sslEngine.closeInbound();
            } catch (SSLException e) {
              LOGGER.error("This engine was forced to close inbound, without having received the "
                  + "proper SSL/TLS close notification message from the peer, due to end of stream.");
            }

            sslEngine.closeOutbound();
            handshakeStatus = sslEngine.getHandshakeStatus();
            break;

          } else if (!receivedBuffer.hasRemaining()) {
            receivedBuffer.clear();
            return SKIP_READ_PACKETS;
          }

          try {
            LOGGER.debug(receivedBuffer, buff -> "Try to unwrap data:\n" + hexDump(buff));
            result = sslEngine.unwrap(receivedBuffer, EMPTY_BUFFERS);
            handshakeStatus = result.getHandshakeStatus();
            LOGGER.debug(handshakeStatus, status -> "Handshake status: " + status + " after unwrapping");
          } catch (SSLException sslException) {
            LOGGER.error("A problem was encountered while processing the data that caused the "
                + "SSLEngine to abort. Will try to properly close connection...");
            sslEngine.closeOutbound();
            handshakeStatus = sslEngine.getHandshakeStatus();
            break;
          }

          switch (result.getStatus()) {
            case OK:
              break;
            case BUFFER_OVERFLOW:
              throw new IllegalStateException("Unexpected ssl engine result");
            case BUFFER_UNDERFLOW:
              LOGGER.debug("Increase ssl network buffer");
              increaseNetworkBuffer();
              break;
            case CLOSED:
              if (sslEngine.isOutboundDone()) {
                return SKIP_READ_PACKETS;
              } else {
                sslEngine.closeOutbound();
                handshakeStatus = sslEngine.getHandshakeStatus();
                break;
              }
            default:
              throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
          }
          break;
        }
        case NEED_WRAP:
          LOGGER.debug("Send command to wrap data");
          packetWriter.accept(SSLWritablePacket.getInstance());
          sslNetworkBuffer.clear();
          return SKIP_READ_PACKETS;
        case NEED_TASK:

          Runnable task;

          while ((task = sslEngine.getDelegatedTask()) != null) {
            LOGGER.debug(task, t -> "Execute SSL Engine's task: " + t.getClass());
            task.run();
          }

          handshakeStatus = sslEngine.getHandshakeStatus();

          LOGGER.debug(handshakeStatus, status -> "Handshake status: " + status + " after engine tasks");

          if (handshakeStatus == HandshakeStatus.NEED_UNWRAP && !receivedBuffer.hasRemaining()) {
            sslNetworkBuffer.clear();
            return SKIP_READ_PACKETS;
          }

          break;
        default:
          throw new IllegalStateException("Invalid SSL status: " + handshakeStatus);
      }
    }

    if (!receivedBuffer.hasRemaining()) {

      // if buffer is empty and status is FINISHED then we can notify writer
      if (handshakeStatus == HandshakeStatus.FINISHED) {
        packetWriter.accept(SSLWritablePacket.getInstance());
      }

      receivedBuffer.clear();

      return SKIP_READ_PACKETS;
    }

    return decryptAndRead(receivedBuffer);
  }

  protected int decryptAndRead(ByteBuffer receivedBuffer) {

    int total = 0;

    while (receivedBuffer.hasRemaining()) {

      SSLEngineResult result;
      try {
        LOGGER.debug(receivedBuffer, buf -> "Try to decrypt data:\n" + hexDump(buf));
        result = sslEngine.unwrap(receivedBuffer, sslDataBuffer.clear());
      } catch (SSLException e) {
        var handshakeStatus = sslEngine.getHandshakeStatus();
        throw new IllegalStateException(e);
      }

      switch (result.getStatus()) {
        case OK:
          sslDataBuffer.flip();
          LOGGER.debug(sslDataBuffer, buf -> "Decrypted data:\n" + hexDump(buf));
          total += readPackets(sslDataBuffer, pendingBuffer);
          break;
        case BUFFER_OVERFLOW:
          increaseDataBuffer();
          return decryptAndRead(receivedBuffer);
        case CLOSED:
          closeConnection();
          return SKIP_READ_PACKETS;
        default:

          if (receivedBuffer.position() > 0) {
            receivedBuffer.compact();
            return total;
          }

          throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
      }
    }

    receivedBuffer.clear();

    return total;
  }

  private void increaseNetworkBuffer() {
    sslNetworkBuffer = NetworkUtils.increasePacketBuffer(sslNetworkBuffer, bufferAllocator, sslEngine);
  }

  private void increaseDataBuffer() {
    sslDataBuffer = NetworkUtils.increaseApplicationBuffer(sslDataBuffer, bufferAllocator, sslEngine);
  }

  protected void closeConnection() {
    try {
      sslEngine.closeOutbound();
      channel.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
