package com.ss.rlib.network.packet.impl;

import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.IdBasedReadablePacket;
import com.ss.rlib.network.packet.registry.ReadablePacketRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.Consumer;

public class IdBasedPacketReader<R extends IdBasedReadablePacket<R>, C extends Connection<R, ?>> extends
    AbstractPacketReader<R, C> {

    private final ReadablePacketRegistry<R> packetRegistry;
    private final int packetIdHeaderSize;

    public IdBasedPacketReader(
        @NotNull C connection,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull Runnable updateActivityFunction,
        @NotNull Consumer<R> readPacketHandler,
        int packetLengthHeaderSize,
        int maxPacketsByRead,
        int packetIdHeaderSize,
        @NotNull ReadablePacketRegistry<R> packetRegistry
    ) {
        super(
            connection,
            channel,
            bufferAllocator,
            updateActivityFunction,
            readPacketHandler,
            packetLengthHeaderSize,
            maxPacketsByRead
        );
        this.packetIdHeaderSize = packetIdHeaderSize;
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected @Nullable R createPacketFor(@NotNull ByteBuffer buffer, int length) {
        return packetRegistry.findById(readHeader(buffer, packetIdHeaderSize))
            .newInstance();
    }
}
