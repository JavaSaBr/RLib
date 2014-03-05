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
	private final AsynchronousChannelGroup group;
	/** асинронный серверый канал */
	private final AsynchronousServerSocketChannel channel;
	/** обработчик новых подключений */
	private final AcceptHandler acceptHandler;
	/** конфигурация сети */
	private final NetworkConfig config;

	public DefaultServerNetwork(NetworkConfig config, AcceptHandler acceptHandler) throws IOException {
		this.config = config;
		this.readBufferPool = Arrays.toConcurrentArray(ByteBuffer.class);
		this.writeBufferPool = Arrays.toConcurrentArray(ByteBuffer.class);
		this.group = AsynchronousChannelGroup.withFixedThreadPool(config.getGroupSize(), new GroupThreadFactory(config.getGroupName(), config.getThreadClass(), config.getThreadPriority()));
		this.channel = AsynchronousServerSocketChannel.open(group);
		this.acceptHandler = acceptHandler;
	}

	@Override
	public <A> void accept(A attachment, CompletionHandler<AsynchronousSocketChannel, ? super A> handler) {
		channel.accept(attachment, handler);
	}

	@Override
	public void bind(SocketAddress address) throws IOException {
		channel.bind(address);
		channel.accept(channel, acceptHandler);
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
