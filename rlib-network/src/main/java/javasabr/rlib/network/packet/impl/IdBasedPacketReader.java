package javasabr.rlib.network.packet.impl;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.common.function.NotNullConsumer;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.IdBasedReadablePacket;
import javasabr.rlib.network.packet.registry.ReadablePacketRegistry;
import org.jspecify.annotations.Nullable;

/**
 * @param <R> the readable packet's type.
 * @param <C> the connection's type.
 * @author JavaSaBr
 */
public class IdBasedPacketReader<R extends IdBasedReadablePacket<R>, C extends Connection<R, ?>> extends
    AbstractPacketReader<R, C> {

  private final ReadablePacketRegistry<R> packetRegistry;
  private final int packetLengthHeaderSize;
  private final int packetIdHeaderSize;

  public IdBasedPacketReader(
      C connection,
      AsynchronousSocketChannel channel,
      BufferAllocator bufferAllocator,
      Runnable updateActivityFunction,
      NotNullConsumer<R> readPacketHandler,
      int packetLengthHeaderSize,
      int maxPacketsByRead,
      int packetIdHeaderSize,
      ReadablePacketRegistry<R> packetRegistry) {
    super(connection, channel, bufferAllocator, updateActivityFunction, readPacketHandler, maxPacketsByRead);
    this.packetLengthHeaderSize = packetLengthHeaderSize;
    this.packetIdHeaderSize = packetIdHeaderSize;
    this.packetRegistry = packetRegistry;
  }

  @Override
  protected boolean canStartReadPacket(ByteBuffer buffer) {
    return buffer.remaining() > packetLengthHeaderSize;
  }

  @Override
  protected int readPacketLength(ByteBuffer buffer) {
    return readHeader(buffer, packetLengthHeaderSize);
  }

  @Override
  protected @Nullable R createPacketFor(
      ByteBuffer buffer,
      int startPacketPosition,
      int packetLength,
      int dataLength) {
    return packetRegistry
        .findById(readHeader(buffer, packetIdHeaderSize))
        .newInstance();
  }
}
