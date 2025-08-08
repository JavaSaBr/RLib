package javasabr.rlib.network.packet.impl;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.common.function.NotNullBiConsumer;
import javasabr.rlib.common.function.NotNullConsumer;
import javasabr.rlib.common.function.NullableSupplier;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.WritablePacket;
import javax.net.ssl.SSLEngine;

/**
 * @author JavaSaBr
 */
public class DefaultSSLPacketWriter<W extends WritablePacket, C extends Connection<?, W>> extends
    AbstractSSLPacketWriter<W, C> {

  protected final int packetLengthHeaderSize;

  public DefaultSSLPacketWriter(
      C connection,
      AsynchronousSocketChannel channel,
      BufferAllocator bufferAllocator,
      Runnable updateActivityFunction,
      NullableSupplier<WritablePacket> nextWritePacketSupplier,
      NotNullConsumer<WritablePacket> writtenPacketHandler,
      NotNullBiConsumer<WritablePacket, Boolean> sentPacketHandler,
      SSLEngine sslEngine,
      NotNullConsumer<WritablePacket> packetWriter,
      NotNullConsumer<WritablePacket> queueAtFirst,
      int packetLengthHeaderSize) {
    super(
        connection,
        channel,
        bufferAllocator,
        updateActivityFunction,
        nextWritePacketSupplier,
        writtenPacketHandler,
        sentPacketHandler,
        sslEngine,
        packetWriter,
        queueAtFirst);
    this.packetLengthHeaderSize = packetLengthHeaderSize;
  }

  @Override
  protected int getTotalSize(WritablePacket packet, int expectedLength) {
    return expectedLength + packetLengthHeaderSize;
  }

  @Override
  protected boolean onBeforeWrite(
      W packet,
      int expectedLength,
      int totalSize,
      ByteBuffer firstBuffer,
      ByteBuffer secondBuffer) {
    firstBuffer
        .clear()
        .position(packetLengthHeaderSize);
    return true;
  }

  @Override
  protected ByteBuffer onResult(
      W packet,
      int expectedLength,
      int totalSize,
      ByteBuffer firstBuffer,
      ByteBuffer secondBuffer) {
    return writePacketLength(firstBuffer, firstBuffer.limit()).position(0);
  }

  protected ByteBuffer writePacketLength(ByteBuffer buffer, int packetLength) {
    return writeHeader(buffer, 0, packetLength, packetLengthHeaderSize);
  }
}
