package com.ss.rlib.network.packet.impl;

import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.WritablePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.Supplier;

/**
 * @author JavaSaBr
 */
public class DefaultPacketWriter<W extends WritablePacket, C extends Connection<?, W>> extends
    AbstractPacketWriter<W, C> {

    protected final int packetLengthHeaderSize;

    public DefaultPacketWriter(
        @NotNull C connection,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull Runnable updateActivityFunction,
        @NotNull Supplier<@Nullable W> nextWritePacketSupplier,
        int packetLengthHeaderSize
    ) {
        super(
            connection,
            channel,
            bufferAllocator,
            updateActivityFunction,
            nextWritePacketSupplier
        );
        this.packetLengthHeaderSize = packetLengthHeaderSize;
    }

    @Override
    protected int getTotalSize(@NotNull W packet, int expectedLength) {
        return expectedLength + packetLengthHeaderSize;
    }

    @Override
    protected boolean onBeforeWrite(
        @NotNull W packet,
        int expectedLength,
        int totalSize,
        @NotNull ByteBuffer firstBuffer,
        @NotNull ByteBuffer secondBuffer
    ) {
        firstBuffer.clear().position(packetLengthHeaderSize);
        return true;
    }

    @Override
    protected @NotNull ByteBuffer onResult(
        @NotNull W packet,
        int expectedLength,
        int totalSize,
        @NotNull ByteBuffer firstBuffer,
        @NotNull ByteBuffer secondBuffer
    ) {
        return writePacketLength(firstBuffer, firstBuffer.limit()).position(0);
    }

    protected @NotNull ByteBuffer writePacketLength(@NotNull ByteBuffer buffer, int packetLength) {
        return writeHeader(buffer, 0, packetLength, packetLengthHeaderSize);
    }
}
