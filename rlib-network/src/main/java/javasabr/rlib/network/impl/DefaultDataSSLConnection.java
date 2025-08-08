package javasabr.rlib.network.impl;

import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.packet.PacketReader;
import javasabr.rlib.network.packet.PacketWriter;
import javasabr.rlib.network.packet.ReadablePacket;
import javasabr.rlib.network.packet.WritablePacket;
import javasabr.rlib.network.packet.impl.DefaultSSLPacketReader;
import javasabr.rlib.network.packet.impl.DefaultSSLPacketWriter;
import javax.net.ssl.SSLContext;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author JavaSaBr
 */
@Getter(AccessLevel.PROTECTED)
public abstract class DefaultDataSSLConnection<R extends ReadablePacket, W extends WritablePacket> extends
    AbstractSSLConnection<R, W> {

  private final PacketReader packetReader;
  private final PacketWriter packetWriter;

  private final int packetLengthHeaderSize;

  public DefaultDataSSLConnection(
      Network<? extends Connection<R, W>> network,
      AsynchronousSocketChannel channel,
      BufferAllocator bufferAllocator,
      SSLContext sslContext,
      int maxPacketsByRead,
      int packetLengthHeaderSize,
      boolean clientMode) {
    super(network, channel, bufferAllocator, sslContext, maxPacketsByRead, clientMode);
    this.packetLengthHeaderSize = packetLengthHeaderSize;
    this.packetReader = createPacketReader();
    this.packetWriter = createPacketWriter();
  }

  protected PacketReader createPacketReader() {
    return new DefaultSSLPacketReader<>(
        this,
        channel,
        bufferAllocator,
        this::updateLastActivity,
        this::handleReceivedPacket,
        value -> createReadablePacket(),
        sslEngine,
        this::sendImpl,
        packetLengthHeaderSize,
        maxPacketsByRead);
  }

  protected PacketWriter createPacketWriter() {
    return new DefaultSSLPacketWriter<W, Connection<R, W>>(
        this,
        channel,
        bufferAllocator,
        this::updateLastActivity,
        this::nextPacketToWrite,
        this::onWrittenPacket,
        this::onSentPacket,
        sslEngine,
        this::sendImpl,
        this::queueAtFirst,
        packetLengthHeaderSize);
  }

  protected abstract R createReadablePacket();
}
