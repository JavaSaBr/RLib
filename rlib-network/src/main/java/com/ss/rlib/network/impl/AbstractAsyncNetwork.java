package com.ss.rlib.network.impl;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.AsyncNetwork;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.packet.ReadablePacketRegistry;
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
    protected final Pool<ByteBuffer> waitBufferPool;
    protected final Pool<ByteBuffer> writeBufferPool;
    protected final ReadablePacketRegistry registry;
    protected final NetworkConfig config;

    protected AbstractAsyncNetwork(@NotNull NetworkConfig config, @NotNull ReadablePacketRegistry registry) {
        this.config = config;
        this.registry = registry;
        this.readBufferPool = PoolFactory.newConcurrentAtomicARSWLockPool(ByteBuffer.class);
        this.waitBufferPool = PoolFactory.newConcurrentAtomicARSWLockPool(ByteBuffer.class);
        this.writeBufferPool = PoolFactory.newConcurrentAtomicARSWLockPool(ByteBuffer.class);
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
        var buffer = readBufferPool.take(config, readBufferFactory());
        buffer.clear();
        return buffer;
    }

    /**
     * Get the read buffers factory.
     *
     * @return the read buffers factory.
     */
    protected @NotNull Function<NetworkConfig, ByteBuffer> readBufferFactory() {
        return conf -> (conf.isDirectByteBuffer() ?
            allocateDirect(conf.getReadBufferSize()) : allocate(conf.getReadBufferSize()))
            .order(LITTLE_ENDIAN);
    }

    @Override
    public @NotNull ByteBuffer takeWaitBuffer() {
        var buffer = writeBufferPool.take(config, waitBufferFactory());
        buffer.clear();
        return buffer;
    }

    /**
     * Get the wait buffers factory.
     *
     * @return the wait buffers factory.
     */
    protected @NotNull Function<NetworkConfig, ByteBuffer> waitBufferFactory() {
        return conf -> (conf.isDirectByteBuffer() ?
            allocateDirect(conf.getReadBufferSize() * 2) : allocate(conf.getReadBufferSize() * 2))
            .order(LITTLE_ENDIAN);
    }

    @Override
    public @NotNull ByteBuffer takeWriteBuffer() {
        var buffer = writeBufferPool.take(config, writeBufferFactory());
        buffer.clear();
        return buffer;
    }

    /**
     * Get the write buffers factory.
     *
     * @return the write buffers factory.
     */
    protected Function<NetworkConfig, ByteBuffer> writeBufferFactory() {
        return conf -> (conf.isDirectByteBuffer() ?
            allocateDirect(conf.getWriteBufferSize()) : allocate(conf.getWriteBufferSize()))
            .order(LITTLE_ENDIAN);
    }

    @Override
    public @NotNull AbstractAsyncNetwork putReadBuffer(@NotNull ByteBuffer buffer) {
        readBufferPool.put(buffer);
        return this;
    }

    @Override
    public @NotNull AbstractAsyncNetwork putWaitBuffer(@NotNull ByteBuffer buffer) {
        waitBufferPool.put(buffer);
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
