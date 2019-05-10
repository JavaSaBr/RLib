package com.ss.rlib.network.impl;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.AsyncNetwork;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.packet.registry.ReadablePacketRegistry;
import com.ss.rlib.common.util.pools.PoolFactory;
import org.jetbrains.annotations.NotNull;
import com.ss.rlib.common.util.pools.Pool;

import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * The base implementation of {@link AsyncNetwork}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractAsyncNetwork implements AsyncNetwork {

    protected static final Logger LOGGER = LoggerManager.getLogger(AsyncNetwork.class);

    protected final Pool<ByteBuffer> readBufferPool;
    protected final Pool<ByteBuffer> pendingBufferPool;
    protected final Pool<ByteBuffer> writeBufferPool;

    protected final ReadablePacketRegistry registry;
    protected final NetworkConfig config;

    protected AbstractAsyncNetwork(@NotNull NetworkConfig config, @NotNull ReadablePacketRegistry registry) {
        this.config = config;
        this.registry = registry;
        this.readBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
        this.pendingBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
        this.writeBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
    }

    @Override
    public @NotNull ReadablePacketRegistry getPacketRegistry() {
        return registry;
    }

    @Override
    public @NotNull NetworkConfig getConfig() {
        return config;
    }

    @Override
    public @NotNull ByteBuffer takeReadBuffer() {
        return readBufferPool.take(config, readBufferFactory()).clear();
    }

    @Override
    public @NotNull ByteBuffer takePendingBuffer() {
        return pendingBufferPool.take(config, pendingBufferFactory()).clear();
    }

    @Override
    public @NotNull ByteBuffer takeWriteBuffer() {
        return writeBufferPool.take(config, writeBufferFactory()).clear();
    }

    /**
     * Get a pending buffers factory.
     *
     * @return the pending buffers factory.
     */
    protected @NotNull Function<NetworkConfig, ByteBuffer> pendingBufferFactory() {
        return conf -> {
            if (conf.isDirectByteBuffer()) {
                return allocateDirect(conf.getReadBufferSize() * 2).order(LITTLE_ENDIAN);
            } else {
                return allocate(conf.getReadBufferSize() * 2).order(LITTLE_ENDIAN);
            }
        };
    }


    /**
     * Get a read buffers factory.
     *
     * @return the read buffers factory.
     */
    protected @NotNull Function<NetworkConfig, ByteBuffer> readBufferFactory() {
        return conf -> {
            if (conf.isDirectByteBuffer()) {
                return allocateDirect(conf.getReadBufferSize()).order(LITTLE_ENDIAN);
            } else {
                return allocate(conf.getReadBufferSize()).order(LITTLE_ENDIAN);
            }
        };
    }

    /**
     * Get a write buffers factory.
     *
     * @return the write buffers factory.
     */
    protected @NotNull Function<NetworkConfig, ByteBuffer> writeBufferFactory() {
        return conf -> {
            if (conf.isDirectByteBuffer()) {
                return allocateDirect(conf.getWriteBufferSize()).order(LITTLE_ENDIAN);
            } else {
                return allocate(conf.getWriteBufferSize()).order(LITTLE_ENDIAN);
            }
        };
    }

    @Override
    public @NotNull AbstractAsyncNetwork putReadBuffer(@NotNull ByteBuffer buffer) {
        readBufferPool.put(buffer);
        return this;
    }

    @Override
    public @NotNull AbstractAsyncNetwork putPendingBuffer(@NotNull ByteBuffer buffer) {
        pendingBufferPool.put(buffer);
        return this;
    }

    @Override
    public @NotNull AbstractAsyncNetwork putWriteBuffer(@NotNull ByteBuffer buffer) {
        writeBufferPool.put(buffer);
        return this;
    }

    @Override
    public String toString() {
        return "AbstractAsynchronousNetwork{" +
                "config=" + config +
                '}';
    }
}
