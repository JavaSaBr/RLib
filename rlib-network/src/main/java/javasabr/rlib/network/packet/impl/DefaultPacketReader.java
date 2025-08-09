package javasabr.rlib.network.packet.impl;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.IntFunction;
import javasabr.rlib.common.function.NotNullConsumer;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.ReadablePacket;
import org.jspecify.annotations.Nullable;

/**
 * @param <R> the readable packet's type.
 * @param <C> the connections' type.
 * @author JavaSaBR
 */
public class DefaultPacketReader<R extends ReadablePacket, C extends Connection<R, ?>> extends
    AbstractPacketReader<R, C> {

  private final IntFunction<R> readPacketFactory;
  private final int packetLengthHeaderSize;

  public DefaultPacketReader(
      C connection,
      AsynchronousSocketChannel channel,
      BufferAllocator bufferAllocator,
      Runnable updateActivityFunction,
      NotNullConsumer<R> readPacketHandler,
      IntFunction<R> readPacketFactory,
      int packetLengthHeaderSize,
      int maxPacketsByRead) {
    super(connection, channel, bufferAllocator, updateActivityFunction, readPacketHandler, maxPacketsByRead);
    this.readPacketFactory = readPacketFactory;
    this.packetLengthHeaderSize = packetLengthHeaderSize;
  }

  @Override
  protected boolean canStartReadPacket(ByteBuffer buffer) {
    return buffer.remaining() >= packetLengthHeaderSize;
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
    return readPacketFactory.apply(dataLength);
  }
}
