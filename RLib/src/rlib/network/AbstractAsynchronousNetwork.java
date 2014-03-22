package rlib.network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.array.Array;
import rlib.util.array.ArrayUtils;

/**
 * Базовая модель асинхронной сети.
 * 
 * @author Ronn
 */
public abstract class AbstractAsynchronousNetwork implements AsynchronousNetwork {

	protected static final Logger LOGGER = Loggers.getLogger(AsynchronousNetwork.class);

	/** пул буфферов для чтения */
	protected final Array<ByteBuffer> readBufferPool;
	/** пул буфферов для записи */
	protected final Array<ByteBuffer> writeBufferPool;

	/** конфигурация сети */
	protected final NetworkConfig config;

	protected AbstractAsynchronousNetwork(NetworkConfig config) {
		this.config = config;
		this.readBufferPool = ArrayUtils.toConcurrentArray(ByteBuffer.class);
		this.writeBufferPool = ArrayUtils.toConcurrentArray(ByteBuffer.class);
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
		ByteBuffer buffer = pool.pop();

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

		Array<ByteBuffer> writeBufferPool = getWriteBufferPool();
		ByteBuffer buffer = writeBufferPool.pop();

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
		pool.add(buffer);
	}

	@Override
	public void putWriteByteBuffer(ByteBuffer buffer) {

		if(buffer == null) {
			return;
		}

		Array<ByteBuffer> pool = getWriteBufferPool();
		pool.add(buffer);
	}
}
