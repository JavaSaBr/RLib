package rlib.network.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.AsynchronousNetwork;
import rlib.network.NetworkConfig;
import rlib.util.pools.Pool;
import rlib.util.pools.PoolFactory;

/**
 * Базовая реализация асинхронной сети.
 * 
 * @author Ronn
 */
public abstract class AbstractAsynchronousNetwork implements AsynchronousNetwork {

	protected static final Logger LOGGER = LoggerManager.getLogger(AsynchronousNetwork.class);

	/** пул буфферов для чтения */
	protected final Pool<ByteBuffer> readBufferPool;
	/** пул буфферов для записи */
	protected final Pool<ByteBuffer> writeBufferPool;

	/** конфигурация сети */
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
	public ByteBuffer getReadByteBuffer() {

		final Pool<ByteBuffer> pool = getReadBufferPool();

		ByteBuffer buffer = pool.take();

		if(buffer != null) {
			buffer.clear();
		} else {
			buffer = ByteBuffer.allocate(config.getReadBufferSize()).order(ByteOrder.LITTLE_ENDIAN);
		}

		return buffer;
	}

	/**
	 * @return пул буфферов для записи.
	 */
	private Pool<ByteBuffer> getWriteBufferPool() {
		return writeBufferPool;
	}

	@Override
	public ByteBuffer getWriteByteBuffer() {

		final Pool<ByteBuffer> pool = getWriteBufferPool();

		ByteBuffer buffer = pool.take();

		if(buffer != null) {
			buffer.clear();
		} else {
			buffer = ByteBuffer.allocate(config.getWriteBufferSize()).order(ByteOrder.LITTLE_ENDIAN);
		}

		return buffer;
	}

	@Override
	public void putReadByteBuffer(final ByteBuffer buffer) {

		if(buffer == null) {
			return;
		}

		final Pool<ByteBuffer> pool = getReadBufferPool();
		pool.put(buffer);
	}

	@Override
	public void putWriteByteBuffer(final ByteBuffer buffer) {

		if(buffer == null) {
			return;
		}

		final Pool<ByteBuffer> pool = getWriteBufferPool();
		pool.put(buffer);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [config=" + config + "]";
	}
}
