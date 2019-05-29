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

/**
 * @author JavaSaBr
 */
@ToString
public class DefaultBufferAllocator implements BufferAllocator {

    private static final Logger LOGGER = LoggerManager.getLogger(DefaultBufferAllocator.class);

    protected final Pool<ByteBuffer> readBufferPool;
    protected final Pool<ByteBuffer> pendingBufferPool;
    protected final Pool<ByteBuffer> writeBufferPool;

    protected final NetworkConfig config;

    public DefaultBufferAllocator(@NotNull NetworkConfig config) {
        this.config = config;
        this.readBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
        this.pendingBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
        this.writeBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
    }

    @Override
    public @NotNull ByteBuffer takeReadBuffer() {
        var bufferSize = config.getReadBufferSize();
        LOGGER.debug(bufferSize, size -> "Allocate a new read buffer with size:" + size);
        return config.isDirectByteBuffer()? ByteBuffer.allocateDirect(bufferSize) : ByteBuffer.allocate(bufferSize)
            .order(config.getByteOrder())
            .clear();
    }

    @Override
    public @NotNull ByteBuffer takePendingBuffer() {
        var bufferSize = config.getPendingBufferSize();
        LOGGER.debug(bufferSize, size -> "Allocate a new pending buffer with size:" + size);
        return config.isDirectByteBuffer()? ByteBuffer.allocateDirect(bufferSize) : ByteBuffer.allocate(bufferSize)
            .order(config.getByteOrder())
            .clear();
    }

    @Override
    public @NotNull ByteBuffer takeWriteBuffer() {
        var bufferSize = config.getWriteBufferSize();
        LOGGER.debug(bufferSize, size -> "Allocate a new write buffer with size:" + size);
        return config.isDirectByteBuffer()? ByteBuffer.allocateDirect(bufferSize) : ByteBuffer.allocate(bufferSize)
            .order(config.getByteOrder())
            .clear();
    }

    @Override
    public @NotNull DefaultBufferAllocator putReadBuffer(@NotNull ByteBuffer buffer) {
        LOGGER.debug("Skip storing a read buffer.");
        return this;
    }

    @Override
    public @NotNull DefaultBufferAllocator putPendingBuffer(@NotNull ByteBuffer buffer) {
        LOGGER.debug("Skip storing a read buffer.");
        return this;
    }

    @Override
    public @NotNull DefaultBufferAllocator putWriteBuffer(@NotNull ByteBuffer buffer) {
        LOGGER.debug("Skip storing a read buffer.");
        return this;
    }
}
