package javasabr.rlib.network.packet.impl;

import static javasabr.rlib.network.util.NetworkUtils.EMPTY_BUFFER;
import static javasabr.rlib.network.util.NetworkUtils.hexDump;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.common.function.NotNullBiConsumer;
import javasabr.rlib.common.function.NotNullConsumer;
import javasabr.rlib.common.function.NullableSupplier;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.WritablePacket;
import javasabr.rlib.network.util.NetworkUtils;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLException;
import org.jspecify.annotations.Nullable;

public abstract class AbstractSSLPacketWriter<W extends WritablePacket, C extends Connection<?, W>> extends
    AbstractPacketWriter<W, C> {

  private static final Logger LOGGER = LoggerManager.getLogger(AbstractSSLPacketWriter.class);

  private static final ByteBuffer[] EMPTY_BUFFERS = {
      NetworkUtils.EMPTY_BUFFER
  };

  protected final SSLEngine sslEngine;
  protected final NotNullConsumer<WritablePacket> packetWriter;
  protected final NotNullConsumer<WritablePacket> queueAtFirst;

  protected volatile ByteBuffer sslNetworkBuffer;

  public AbstractSSLPacketWriter(
      C connection,
      AsynchronousSocketChannel channel,
      BufferAllocator bufferAllocator,
      Runnable updateActivityFunction,
      NullableSupplier<WritablePacket> packetProvider,
      NotNullConsumer<WritablePacket> writtenPacketHandler,
      NotNullBiConsumer<WritablePacket, Boolean> sentPacketHandler,
      SSLEngine sslEngine,
      NotNullConsumer<WritablePacket> packetWriter,
      NotNullConsumer<WritablePacket> queueAtFirst) {
    super(
        connection,
        channel,
        bufferAllocator,
        updateActivityFunction,
        packetProvider,
        writtenPacketHandler,
        sentPacketHandler);
    this.sslEngine = sslEngine;
    this.packetWriter = packetWriter;
    this.queueAtFirst = queueAtFirst;
    this.sslNetworkBuffer = bufferAllocator.takeBuffer(sslEngine
        .getSession()
        .getPacketBufferSize());
  }

  @Override
  public void writeNextPacket() {

    var status = sslEngine.getHandshakeStatus();

    switch (status) {
      case NEED_UNWRAP:
        return;
    }

    super.writeNextPacket();
  }

  @Override
  protected ByteBuffer serialize(WritablePacket packet) {

    var status = sslEngine.getHandshakeStatus();

    if (status == HandshakeStatus.FINISHED || status == HandshakeStatus.NOT_HANDSHAKING) {

      if (packet instanceof SSLWritablePacket) {
        return EMPTY_BUFFER;
      }

      var dataBuffer = super.serialize(packet);

      LOGGER.debug(dataBuffer, buff -> "Try to encrypt data:\n" + hexDump(buff));

      SSLEngineResult result;
      try {
        result = sslEngine.wrap(dataBuffer, sslNetworkBuffer.clear());
      } catch (SSLException e) {
        throw new RuntimeException(e);
      }

      switch (result.getStatus()) {
        case BUFFER_UNDERFLOW:
          increaseNetworkBuffer();
          break;
        case BUFFER_OVERFLOW:
          throw new IllegalStateException("Unexpected ssl engine result");
        case OK:
          return sslNetworkBuffer.flip();
        case CLOSED:
          closeConnection();
          return EMPTY_BUFFER;
      }
    }

    var bufferToWrite = doHandshake(packet);

    if (bufferToWrite != null) {
      return bufferToWrite;
    }

    throw new IllegalStateException();
  }

  protected @Nullable ByteBuffer doHandshake(WritablePacket packet) {

    if (!(packet instanceof SSLWritablePacket)) {
      LOGGER.debug(packet, pck -> "Return packet " + pck + " to queue as first");
      queueAtFirst.accept(packet);
    }

    var handshakeStatus = sslEngine.getHandshakeStatus();

    while (handshakeStatus != HandshakeStatus.FINISHED && handshakeStatus != HandshakeStatus.NOT_HANDSHAKING) {

      SSLEngineResult result;

      switch (handshakeStatus) {

        case NEED_WRAP:
          try {
            // check result
            result = sslEngine.wrap(EMPTY_BUFFERS, sslNetworkBuffer.clear());
            handshakeStatus = result.getHandshakeStatus();
          } catch (SSLException sslException) {
            LOGGER.error("A problem was encountered while processing the data that caused the SSLEngine "
                + "to abort. Will try to properly close connection...");
            sslEngine.closeOutbound();
            handshakeStatus = sslEngine.getHandshakeStatus();
            break;
          }
          switch (result.getStatus()) {
            case OK:

              sslNetworkBuffer.flip();

              if (handshakeStatus == HandshakeStatus.NEED_WRAP) {
                LOGGER.debug("Send command to wrap data again");
                queueAtFirst.accept(SSLWritablePacket.getInstance());
              }

              LOGGER.debug(sslNetworkBuffer, result, (buf, res) -> "Send wrapped data:\n" + hexDump(buf, res));

              return sslNetworkBuffer;
            case BUFFER_OVERFLOW:
              sslNetworkBuffer = NetworkUtils.enlargePacketBuffer(bufferAllocator, sslEngine);
              break;
            case BUFFER_UNDERFLOW:
              throw new IllegalStateException("Unexpected ssl engine result");
            case CLOSED:
              try {
                return EMPTY_BUFFER;
              } catch (Exception e) {
                LOGGER.error("Failed to send server's CLOSE message due to socket channel's failure.");
                handshakeStatus = sslEngine.getHandshakeStatus();
              }
              break;
            default:
              throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
          }
          break;
        case NEED_TASK:
          Runnable task;
          while ((task = sslEngine.getDelegatedTask()) != null) {
            LOGGER.debug(task, t -> "Execute SSL Engine's task: " + t.getClass());
            task.run();
          }
          handshakeStatus = sslEngine.getHandshakeStatus();
          break;
        case NEED_UNWRAP:
          break;
        default:
          throw new IllegalStateException("Invalid SSL status: " + handshakeStatus);
      }
    }

    return EMPTY_BUFFER;
  }

  private void increaseNetworkBuffer() {
    sslNetworkBuffer = NetworkUtils.increasePacketBuffer(sslNetworkBuffer, bufferAllocator, sslEngine);
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
