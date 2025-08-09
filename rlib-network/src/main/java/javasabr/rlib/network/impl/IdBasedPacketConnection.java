package javasabr.rlib.network.impl;

import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.packet.IdBasedReadablePacket;
import javasabr.rlib.network.packet.IdBasedWritablePacket;
import javasabr.rlib.network.packet.PacketReader;
import javasabr.rlib.network.packet.PacketWriter;
import javasabr.rlib.network.packet.impl.IdBasedPacketReader;
import javasabr.rlib.network.packet.impl.IdBasedPacketWriter;
import javasabr.rlib.network.packet.registry.ReadablePacketRegistry;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author JavaSaBr
 */
@Getter(AccessLevel.PROTECTED)
public class IdBasedPacketConnection<R extends IdBasedReadablePacket<R>, W extends IdBasedWritablePacket> extends
    AbstractConnection<R, W> {

  private final PacketReader packetReader;
  private final PacketWriter packetWriter;
  private final ReadablePacketRegistry<R> packetRegistry;

  private final int packetLengthHeaderSize;
  private final int packetIdHeaderSize;

  public IdBasedPacketConnection(
      Network<? extends Connection<R, W>> network,
      AsynchronousSocketChannel channel,
      BufferAllocator bufferAllocator,
      ReadablePacketRegistry<R> packetRegistry,
      int maxPacketsByRead,
      int packetLengthHeaderSize,
      int packetIdHeaderSize) {
    super(network, channel, bufferAllocator, maxPacketsByRead);
    this.packetRegistry = packetRegistry;
    this.packetLengthHeaderSize = packetLengthHeaderSize;
    this.packetIdHeaderSize = packetIdHeaderSize;
    this.packetReader = createPacketReader();
    this.packetWriter = createPacketWriter();
  }

  protected PacketReader createPacketReader() {
    return new IdBasedPacketReader<>(
        this,
        channel,
        bufferAllocator,
        this::updateLastActivity,
        this::handleReceivedPacket,
        packetLengthHeaderSize,
        maxPacketsByRead,
        packetIdHeaderSize,
        packetRegistry);
  }

  protected PacketWriter createPacketWriter() {
    return new IdBasedPacketWriter<>(
        this,
        channel,
        bufferAllocator,
        this::updateLastActivity,
        this::nextPacketToWrite,
        this::onWrittenPacket,
        this::onSentPacket,
        packetLengthHeaderSize,
        packetIdHeaderSize);
  }
}
