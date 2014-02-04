package rlib.network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.array.Array;
import rlib.util.array.Arrays;

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
		this.readBufferPool = Arrays.toConcurrentArray(ByteBuffer.class);
		this.writeBufferPool = Arrays.toConcurrentArray(ByteBuffer.class);
	}

	@Override
	public NetworkConfig getConfig() {
		return config;
	}

	@Override
	public ByteBuffer getReadByteBuffer() {
		readBufferPool.writeLock();
		try {

			ByteBuffer buffer = readBufferPool.pop();

			if(buffer == null) {
				buffer = ByteBuffer.allocate(config.getReadBufferSize()).order(ByteOrder.LITTLE_ENDIAN);
			}

			return buffer;
		} finally {
			readBufferPool.writeUnlock();
		}
	}

	@Override
	public ByteBuffer getWriteByteBuffer() {
		writeBufferPool.writeLock();
		try {

			ByteBuffer buffer = writeBufferPool.pop();

			if(buffer == null) {
				buffer = ByteBuffer.allocate(config.getWriteBufferSize()).order(ByteOrder.LITTLE_ENDIAN);
			}

			return buffer;
		} finally {
			writeBufferPool.writeUnlock();
		}
	}

	@Override
	public void putReadByteBuffer(ByteBuffer buffer) {

		if(buffer == null) {
			return;
		}

		readBufferPool.add(buffer);
	}

	@Override
	public void putWriteByteBuffer(ByteBuffer buffer) {

		if(buffer == null) {
			return;
		}

		writeBufferPool.add(buffer);
	}
}
