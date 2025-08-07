package javasabr.rlib.network.packet.impl;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.IntFunction;
import javasabr.rlib.common.function.NotNullConsumer;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.ReadablePacket;
import javasabr.rlib.network.packet.WritablePacket;
import javax.net.ssl.SSLEngine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @param <R> the readable packet's type.
 * @param <C> the connections' type.
 * @author JavaSaBR
 */
public class DefaultSSLPacketReader<R extends ReadablePacket, C extends Connection<R, ?>> extends
    AbstractSSLPacketReader<R, C> {

  private final IntFunction<R> readPacketFactory;
  private final int packetLengthHeaderSize;

  public DefaultSSLPacketReader(
      @NotNull C connection,
      @NotNull AsynchronousSocketChannel channel,
      @NotNull BufferAllocator bufferAllocator,
      @NotNull Runnable updateActivityFunction,
      @NotNull NotNullConsumer<R> readPacketHandler,
      @NotNull IntFunction<R> readPacketFactory,
      @NotNull SSLEngine sslEngine,
      @NotNull NotNullConsumer<WritablePacket> packetWriter,
      int packetLengthHeaderSize,
      int maxPacketsByRead) {
    super(
        connection,
        channel,
        bufferAllocator,
        updateActivityFunction,
        readPacketHandler,
        sslEngine,
        packetWriter,
        maxPacketsByRead);
    this.readPacketFactory = readPacketFactory;
    this.packetLengthHeaderSize = packetLengthHeaderSize;
  }

  @Override
  protected boolean canStartReadPacket(@NotNull ByteBuffer buffer) {
    return buffer.remaining() >= packetLengthHeaderSize;
  }

  @Override
  protected int readPacketLength(@NotNull ByteBuffer buffer) {
    return readHeader(buffer, packetLengthHeaderSize);
  }

  @Override
  protected @Nullable R createPacketFor(
      @NotNull ByteBuffer buffer,
      int startPacketPosition,
      int packetLength,
      int dataLength) {
    return readPacketFactory.apply(dataLength);
  }
}
