package rlib.network.impl;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import org.jetbrains.annotations.NotNull;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.AsynchronousNetwork;
import rlib.network.NetworkConfig;
import rlib.util.pools.Pool;
import rlib.util.pools.PoolFactory;

import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * The base implementation of a async network.
 *
 * @author JavaSaBr
 */
public abstract class AbstractAsynchronousNetwork implements AsynchronousNetwork {

    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(AsynchronousNetwork.class);

    /**
     * The pool with read buffers.
     */
    @NotNull
    protected final Pool<ByteBuffer> readBufferPool;

    /**
     * The pool with write buffers.
     */
    @NotNull
    protected final Pool<ByteBuffer> writeBufferPool;

    /**
     * The network config.
     */
    @NotNull
    protected final NetworkConfig config;

    protected AbstractAsynchronousNetwork(@NotNull final NetworkConfig config) {
        this.config = config;
        this.readBufferPool = PoolFactory.newConcurrentAtomicARSWLockPool(ByteBuffer.class);
        this.writeBufferPool = PoolFactory.newConcurrentAtomicARSWLockPool(ByteBuffer.class);
    }

    @NotNull
    @Override
    public NetworkConfig getConfig() {
        return config;
    }

    @NotNull
    @Override
    public ByteBuffer takeReadBuffer() {

        final ByteBuffer buffer = readBufferPool.take(config, readBufferFactory());
        buffer.clear();

        return buffer;
    }

    /**
     * @return the read buffer factory.
     */
    @NotNull
    protected Function<NetworkConfig, ByteBuffer> readBufferFactory() {
        return conf -> (conf.isDirectByteBuffer() ?
                allocateDirect(conf.getReadBufferSize()) : allocate(conf.getReadBufferSize())).order(LITTLE_ENDIAN);
    }

    @NotNull
    @Override
    public ByteBuffer takeWriteBuffer() {

        final ByteBuffer buffer = writeBufferPool.take(config, writeBufferFactory());
        buffer.clear();

        return buffer;
    }

    /**
     * @return the write buffer factory.
     */
    protected Function<NetworkConfig, ByteBuffer> writeBufferFactory() {
        return conf -> (conf.isDirectByteBuffer() ?
                allocateDirect(conf.getWriteBufferSize()) : allocate(conf.getWriteBufferSize())).order(LITTLE_ENDIAN);
    }

    @Override
    public void putReadBuffer(@NotNull final ByteBuffer buffer) {
        readBufferPool.put(buffer);
    }

    @Override
    public void putWriteBuffer(@NotNull final ByteBuffer buffer) {
        writeBufferPool.put(buffer);
    }

    @Override
    public String toString() {
        return "AbstractAsynchronousNetwork{" +
                "config=" + config +
                '}';
    }
}
