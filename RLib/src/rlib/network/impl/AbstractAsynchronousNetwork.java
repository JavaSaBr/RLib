package rlib.network.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.AsynchronousNetwork;
import rlib.network.NetworkConfig;
import rlib.util.pools.Pool;
import rlib.util.pools.PoolFactory;

import static java.nio.ByteBuffer.allocate;

/**
 * Базовая реализация асинхронной сети.
 *
 * @author Ronn
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
        this.readBufferPool = PoolFactory.newAtomicPool(ByteBuffer.class);
        this.writeBufferPool = PoolFactory.newAtomicPool(ByteBuffer.class);
    }

    @Override
    public NetworkConfig getConfig() {
        return config;
    }

    /**
     * @return пул буфферов для чтения.
     */
    private Pool<ByteBuffer> getReadBufferPool() {
        return readBufferPool;
    }

    @Override
    public ByteBuffer takeReadBuffer() {

        final Pool<ByteBuffer> pool = getReadBufferPool();
        final ByteBuffer buffer = pool.take(config, conf -> allocate(conf.getReadBufferSize()).order(ByteOrder.LITTLE_ENDIAN));
        buffer.clear();

        return buffer;
    }

    /**
     * @return пул буфферов для записи.
     */
    private Pool<ByteBuffer> getWriteBufferPool() {
        return writeBufferPool;
    }

    @Override
    public ByteBuffer takeWriteBuffer() {

        final Pool<ByteBuffer> pool = getWriteBufferPool();
        final ByteBuffer buffer = pool.take(config, conf -> allocate(conf.getWriteBufferSize()).order(ByteOrder.LITTLE_ENDIAN));
        buffer.clear();

        return buffer;
    }

    @Override
    public void putReadBuffer(final ByteBuffer buffer) {

        if (buffer == null) {
            return;
        }

        final Pool<ByteBuffer> pool = getReadBufferPool();
        pool.put(buffer);
    }

    @Override
    public void putWriteBuffer(final ByteBuffer buffer) {

        if (buffer == null) {
            return;
        }

        final Pool<ByteBuffer> pool = getWriteBufferPool();
        pool.put(buffer);
    }

    @Override
    public String toString() {
        return "AbstractAsynchronousNetwork{" +
                "config=" + config +
                '}';
    }
}
