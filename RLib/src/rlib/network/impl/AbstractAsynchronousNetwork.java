package rlib.network.impl;

import java.nio.ByteBuffer;
import java.util.function.Function;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.AsynchronousNetwork;
import rlib.network.NetworkConfig;
import rlib.util.pools.Pool;
import rlib.util.pools.PoolFactory;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

/**
 * Базовая реализация асинхронной сети.
 *
 * @author JavaSaBr
 */
public abstract class AbstractAsynchronousNetwork implements AsynchronousNetwork {

    protected static final Logger LOGGER = LoggerManager.getLogger(AsynchronousNetwork.class);

    /**
     * Пул буфферов для чтения.
     */
    protected final Pool<ByteBuffer> readBufferPool;

    /**
     * Пул буфферов для записи.
     */
    protected final Pool<ByteBuffer> writeBufferPool;

    /**
     * Конфигурация сети.
     */
    protected final NetworkConfig config;

    protected AbstractAsynchronousNetwork(final NetworkConfig config) {
        this.config = config;
        this.readBufferPool = PoolFactory.newConcurrentAtomicARSWLockPool(ByteBuffer.class);
        this.writeBufferPool = PoolFactory.newConcurrentAtomicARSWLockPool(ByteBuffer.class);
    }

    @Override
    public NetworkConfig getConfig() {
        return config;
    }

    @Override
    public ByteBuffer takeReadBuffer() {

        final ByteBuffer buffer = readBufferPool.take(config, readBufferFactory());
        buffer.clear();

        return buffer;
    }

    /**
     * @return фабрика новыйх буфферов для чтения.
     */
    protected Function<NetworkConfig, ByteBuffer> readBufferFactory() {
        return conf -> (conf.isDirectByteBuffer() ?
                allocateDirect(conf.getReadBufferSize()) : allocate(conf.getReadBufferSize())).order(LITTLE_ENDIAN);
    }

    @Override
    public ByteBuffer takeWriteBuffer() {

        final ByteBuffer buffer = writeBufferPool.take(config, writeBufferFactory());
        buffer.clear();

        return buffer;
    }

    /**
     * @return фабрика новыйх буфферов для записи.
     */
    protected Function<NetworkConfig, ByteBuffer> writeBufferFactory() {
        return conf -> (conf.isDirectByteBuffer() ?
                allocateDirect(conf.getWriteBufferSize()) : allocate(conf.getWriteBufferSize())).order(LITTLE_ENDIAN);
    }

    @Override
    public void putReadBuffer(final ByteBuffer buffer) {
        readBufferPool.put(buffer);
    }

    @Override
    public void putWriteBuffer(final ByteBuffer buffer) {
        writeBufferPool.put(buffer);
    }

    @Override
    public String toString() {
        return "AbstractAsynchronousNetwork{" +
                "config=" + config +
                '}';
    }
}
