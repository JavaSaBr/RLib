package com.ss.rlib.network.packet.impl;

import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.IdBasedWritablePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.Supplier;

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
        @NotNull Supplier<@Nullable W> nextWritePacketSupplier,
        int packetLengthHeaderSize,
        int packetIdHeaderSize
    ) {
        super(
            connection,
            channel,
            bufferAllocator,
            updateActivityFunction,
            nextWritePacketSupplier,
            packetLengthHeaderSize
        );
        this.packetIdHeaderSize = packetIdHeaderSize;
    }

    @Override
    protected boolean onWrite(@NotNull W packet, int expectedLength, int totalSize, @NotNull ByteBuffer buffer) {
        writeHeader(buffer, packet.getPacketId(), packetIdHeaderSize);
        return super.onWrite(packet, expectedLength, totalSize, buffer);
    }
}
