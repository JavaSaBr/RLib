package com.ss.rlib.network.impl;

import com.ss.rlib.common.util.pools.Pool;
import com.ss.rlib.common.util.pools.PoolFactory;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.NetworkConfig;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * @author JavaSaBr
 */
@ToString
public abstract class ReuseBufferAllocator implements BufferAllocator {

    protected static final Logger LOGGER = LoggerManager.getLogger(ReuseBufferAllocator.class);

    protected final Pool<ByteBuffer> readBufferPool;
    protected final Pool<ByteBuffer> pendingBufferPool;
    protected final Pool<ByteBuffer> writeBufferPool;

    protected final NetworkConfig config;

    protected ReuseBufferAllocator(@NotNull NetworkConfig config) {
        this.config = config;
        this.readBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
        this.pendingBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
        this.writeBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
    }

    @Override
    public @NotNull ByteBuffer takeReadBuffer() {
        return readBufferPool.take(config, readBufferFactory())
            .clear();
    }

    @Override
    public @NotNull ByteBuffer takePendingBuffer() {
        return pendingBufferPool.take(config, pendingBufferFactory())
            .clear();
    }

    @Override
    public @NotNull ByteBuffer takeWriteBuffer() {
        return writeBufferPool.take(config, writeBufferFactory())
            .clear();
    }

    protected @NotNull Function<NetworkConfig, ByteBuffer> pendingBufferFactory() {
        return config -> {
            var bufferSize = config.getPendingBufferSize();
            LOGGER.debug(bufferSize, size -> "Allocate a new pending buffer with size:" + size);
            return config.isDirectByteBuffer()? ByteBuffer.allocateDirect(bufferSize) : ByteBuffer.allocate(bufferSize)
                .order(config.getByteOrder())
                .clear();
        };
    }

    protected @NotNull Function<NetworkConfig, ByteBuffer> readBufferFactory() {
        return config -> {
            var bufferSize = config.getReadBufferSize();
            LOGGER.debug(bufferSize, size -> "Allocate a new read buffer with size:" + size);
            return config.isDirectByteBuffer()? ByteBuffer.allocateDirect(bufferSize) : ByteBuffer.allocate(bufferSize)
                .order(config.getByteOrder())
                .clear();
        };
    }

    protected @NotNull Function<NetworkConfig, ByteBuffer> writeBufferFactory() {
        return config -> {
            var bufferSize = config.getWriteBufferSize();
            LOGGER.debug(bufferSize, size -> "Allocate a new write buffer with size:" + size);
            return config.isDirectByteBuffer()? ByteBuffer.allocateDirect(bufferSize) : ByteBuffer.allocate(bufferSize)
                .order(config.getByteOrder())
                .clear();
        };
    }

    @Override
    public @NotNull ReuseBufferAllocator putReadBuffer(@NotNull ByteBuffer buffer) {
        readBufferPool.put(buffer);
        return this;
    }

    @Override
    public @NotNull ReuseBufferAllocator putPendingBuffer(@NotNull ByteBuffer buffer) {
        pendingBufferPool.put(buffer);
        return this;
    }

    @Override
    public @NotNull ReuseBufferAllocator putWriteBuffer(@NotNull ByteBuffer buffer) {
        writeBufferPool.put(buffer);
        return this;
    }
}
