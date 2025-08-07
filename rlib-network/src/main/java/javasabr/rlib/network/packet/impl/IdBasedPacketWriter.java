package javasabr.rlib.network.packet.impl;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.common.function.NotNullBiConsumer;
import javasabr.rlib.common.function.NotNullConsumer;
import javasabr.rlib.common.function.NullableSupplier;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.IdBasedWritablePacket;
import javasabr.rlib.network.packet.WritablePacket;
import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
public class IdBasedPacketWriter<W extends IdBasedWritablePacket, C extends Connection<?, W>> extends
    DefaultPacketWriter<W, C> {

  protected final int packetIdHeaderSize;

  public IdBasedPacketWriter(
      @NotNull C connection,
      @NotNull AsynchronousSocketChannel channel,
      @NotNull BufferAllocator bufferAllocator,
      @NotNull Runnable updateActivityFunction,
      @NotNull NullableSupplier<WritablePacket> nextWritePacketSupplier,
      @NotNull NotNullConsumer<WritablePacket> writtenPacketHandler,
      @NotNull NotNullBiConsumer<WritablePacket, Boolean> sentPacketHandler,
      int packetLengthHeaderSize,
      int packetIdHeaderSize) {
    super(
        connection,
        channel,
        bufferAllocator,
        updateActivityFunction,
        nextWritePacketSupplier,
        writtenPacketHandler,
        sentPacketHandler,
        packetLengthHeaderSize);
    this.packetIdHeaderSize = packetIdHeaderSize;
  }

  @Override
  protected boolean onWrite(
      @NotNull W packet,
      int expectedLength,
      int totalSize,
      @NotNull ByteBuffer firstBuffer,
      @NotNull ByteBuffer secondBuffer) {
    writeHeader(firstBuffer, packet.getPacketId(), packetIdHeaderSize);
    return super.onWrite(packet, expectedLength, totalSize, firstBuffer, secondBuffer);
  }
}
