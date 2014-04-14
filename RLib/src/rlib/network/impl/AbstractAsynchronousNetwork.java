package rlib.network.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.AsynchronousNetwork;
import rlib.network.NetworkConfig;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * Базовая модель асинхронной сети.
 * 
 * @author Ronn
 */
public abstract class AbstractAsynchronousNetwork implements AsynchronousNetwork {

	protected static final Logger LOGGER = LoggerManager.getLogger(AsynchronousNetwork.class);

	/** пул буфферов для чтения */
	protected final Array<ByteBuffer> readBufferPool;
	/** пул буфферов для записи */
	protected final Array<ByteBuffer> writeBufferPool;

	/** конфигурация сети */
	protected final NetworkConfig config;

	protected AbstractAsynchronousNetwork(NetworkConfig config) {
		this.config = config;
		this.readBufferPool = ArrayFactory.newArray(ByteBuffer.class);
		this.writeBufferPool = ArrayFactory.newArray(ByteBuffer.class);
	}

	@Override
	public NetworkConfig getConfig() {
		return config;
	}

	/**
	 * @return пул буфферов для чтения.
	 */
	private Array<ByteBuffer> getReadBufferPool() {
		return readBufferPool;
	}

	@Override
	public ByteBuffer getReadByteBuffer() {

		Array<ByteBuffer> pool = getReadBufferPool();

		ByteBuffer buffer = null;

		synchronized(pool) {
			buffer = pool.pop();
		}

		if(buffer == null) {
			buffer = ByteBuffer.allocate(config.getReadBufferSize()).order(ByteOrder.LITTLE_ENDIAN);
		}

		return buffer;
	}

	/**
	 * @return пул буфферов для записи.
	 */
	private Array<ByteBuffer> getWriteBufferPool() {
		return writeBufferPool;
	}

	@Override
	public ByteBuffer getWriteByteBuffer() {

		Array<ByteBuffer> pool = getWriteBufferPool();
		ByteBuffer buffer = null;

		synchronized(pool) {
			buffer = pool.pop();
		}

		if(buffer == null) {
			buffer = ByteBuffer.allocate(config.getWriteBufferSize()).order(ByteOrder.LITTLE_ENDIAN);
		}

		return buffer;
	}

	@Override
	public void putReadByteBuffer(ByteBuffer buffer) {

		if(buffer == null) {
			return;
		}

		Array<ByteBuffer> pool = getReadBufferPool();

		synchronized(pool) {
			pool.add(buffer);
		}
	}

	@Override
	public void putWriteByteBuffer(ByteBuffer buffer) {

		if(buffer == null) {
			return;
		}

		Array<ByteBuffer> pool = getWriteBufferPool();

		synchronized(pool) {
			pool.add(buffer);
		}
	}
}
