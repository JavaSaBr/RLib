package rlib.network.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import rlib.concurrent.GroupThreadFactory;
import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.network.NetworkConfig;
import rlib.util.array.Array;
import rlib.util.array.Arrays;

/**
 * Базовая модель асинхронной сети.
 * 
 * @author Ronn
 */
public final class DefaultServerNetwork implements ServerNetwork {

	protected static final Logger LOGGER = Loggers.getLogger(ServerNetwork.class);

	/** пул буфферов для чтения */
	private final Array<ByteBuffer> readBufferPool;
	/** пул буфферов для записи */
	private final Array<ByteBuffer> writeBufferPool;

	/** группа асинхронных каналов */
	private final AsynchronousChannelGroup channelGroup;
	/** асинронный серверый канал */
	private final AsynchronousServerSocketChannel serverChannel;

	/** конфигурация сети */
	private final NetworkConfig config;

	/** обработчик новых подключений */
	private final AcceptHandler acceptHandler;

	public DefaultServerNetwork(NetworkConfig config, AcceptHandler acceptHandler) throws IOException {
		this.config = config;
		this.readBufferPool = Arrays.toConcurrentArray(ByteBuffer.class);
		this.writeBufferPool = Arrays.toConcurrentArray(ByteBuffer.class);
		this.channelGroup = AsynchronousChannelGroup.withFixedThreadPool(config.getGroupSize(), new GroupThreadFactory(config.getGroupName(), config.getThreadClass(), config.getThreadPriority()));
		this.serverChannel = AsynchronousServerSocketChannel.open(channelGroup);
		this.acceptHandler = acceptHandler;
	}

	@Override
	public <A> void accept(A attachment, CompletionHandler<AsynchronousSocketChannel, ? super A> handler) {
		serverChannel.accept(attachment, handler);
	}

	@Override
	public void bind(SocketAddress address) throws IOException {
		serverChannel.bind(address);
		serverChannel.accept(serverChannel, acceptHandler);
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
