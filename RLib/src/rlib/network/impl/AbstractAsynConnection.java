package rlib.network.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.LockFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.AsynConnection;
import rlib.network.AsynchronousNetwork;
import rlib.network.NetworkConfig;
import rlib.util.linkedlist.LinkedList;
import rlib.util.linkedlist.LinkedListFactory;

/**
 * Базовая модель асинхронного конекта.
 * 
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractAsynConnection<N extends AsynchronousNetwork, R, S> implements AsynConnection<R, S> {

	protected static final Logger LOGGER = LoggerManager.getLogger(AsynConnection.class);

	/**
	 * Обработчик чтения пакетов.
	 */
	private final CompletionHandler<Integer, AbstractAsynConnection> readHandler = new CompletionHandler<Integer, AbstractAsynConnection>() {

		@Override
		public void completed(final Integer result, final AbstractAsynConnection connection) {

			if(result.intValue() == -1) {
				finish();
				return;
			}

			setLastActive(System.currentTimeMillis());

			final ByteBuffer buffer = getReadBuffer();
			buffer.flip();
			try {

				if(isReady(buffer)) {
					readPacket(buffer);
				}

			} catch(final Exception e) {
				LOGGER.error(this, e);
			} finally {
				buffer.clear();
			}

			final AsynchronousSocketChannel channel = getChannel();
			channel.read(buffer, connection, this);
		}

		@Override
		public void failed(final Throwable exc, final AbstractAsynConnection attachment) {

			if(config.isVisibleReadException()) {
				LOGGER.warning(this, exc);
			}

			if(isClosed()) {
				return;
			}

			finish();
		}
	};

	/**
	 * Обработчик записи пакетов.
	 */
	private final CompletionHandler<Integer, S> writeHandler = new CompletionHandler<Integer, S>() {

		@Override
		public void completed(final Integer result, final S packet) {

			if(result.intValue() == -1) {
				finish();
				return;
			}

			final ByteBuffer buffer = getWriteBuffer();

			if(buffer.remaining() > 0) {
				final AsynchronousSocketChannel channel = getChannel();
				channel.write(buffer, packet, this);
				return;
			}

			final AtomicInteger writeCounter = getWriteCounter();
			writeCounter.decrementAndGet();

			setLastActive(System.currentTimeMillis());
			writeNextPacket();
		}

		@Override
		public void failed(final Throwable exc, final S packet) {

			if(config.isVisibleWriteException()) {
				LOGGER.warning(this, new Exception("incorrect write packet " + packet, exc));
			}

			if(isClosed()) {
				return;
			}

			final AtomicInteger writeCounter = getWriteCounter();
			writeCounter.decrementAndGet();

			writeNextPacket();
		}
	};

	/** модель сети, в которой этот коннект */
	protected final N network;

	/** список ожидающих пакетов на отправку */
	protected final LinkedList<S> waitPackets;

	/** канал конекта */
	protected final AsynchronousSocketChannel channel;

	/** промежуточный буффер с присылаемыми данными */
	protected final ByteBuffer readBuffer;
	/** промежуточный буффер с отсылаемыми данными */
	protected final ByteBuffer writeBuffer;

	/** счетчик ожидающих отправки пакетов */
	protected final AtomicInteger writeCounter;
	/** флаг закрытости конекта */
	protected final AtomicBoolean closed;

	/** конфиг сети */
	protected final NetworkConfig config;
	/** блокировщик */
	protected final Lock lock;

	/** время последней активности конекта */
	protected volatile long lastActive;

	public AbstractAsynConnection(final N network, final AsynchronousSocketChannel channel, final Class<S> sendableType) {
		this.lock = LockFactory.newLock();
		this.channel = channel;
		this.waitPackets = LinkedListFactory.newLinkedList(sendableType);
		this.network = network;
		this.readBuffer = network.getReadByteBuffer();
		this.writeBuffer = network.getWriteByteBuffer();
		this.config = network.getConfig();
		this.writeCounter = new AtomicInteger();
		this.closed = new AtomicBoolean(false);
	}

	@Override
	public void close() {

		if(isClosed()) {
			return;
		}

		lock();
		try {

			if(isClosed()) {
				return;
			}

			setClosed(true);

			final AsynchronousSocketChannel channel = getChannel();

			if(channel.isOpen()) {
				channel.close();
			}

		} catch(final IOException e) {
			LOGGER.warning(this, e);
		} finally {
			unlock();
		}

		clearPackets(getWaitPackets());

		final N network = getNetwork();
		network.putReadByteBuffer(getReadBuffer());
		network.putWriteByteBuffer(getWriteBuffer());
	}

	/**
	 * Очистка от ожидающих отправки пакетов.
	 */
	protected void clearPackets(final LinkedList<S> waitPackets) {
		waitPackets.clear();
	}

	/**
	 * Обработка остановки коннекта.
	 */
	protected abstract void finish();

	/**
	 * @return канал конекта.
	 */
	protected AsynchronousSocketChannel getChannel() {
		return channel;
	}

	@Override
	public final long getLastActive() {
		return lastActive;
	}

	/**
	 * @return блокировщик.
	 */
	public Lock getLock() {
		return lock;
	}

	/**
	 * @return модель сети, в которой этот коннект.
	 */
	protected N getNetwork() {
		return network;
	}

	/**
	 * @return буффер для чтения пакета.
	 */
	protected final ByteBuffer getReadBuffer() {
		return readBuffer;
	}

	/**
	 * @return Обработчик чтения пакетов.
	 */
	protected CompletionHandler<Integer, AbstractAsynConnection> getReadHandler() {
		return readHandler;
	}

	/**
	 * @return список ожидающих пакетов на отправку.
	 */
	protected LinkedList<S> getWaitPackets() {
		return waitPackets;
	}

	/**
	 * @return буффер для записи пакета.
	 */
	protected final ByteBuffer getWriteBuffer() {
		return writeBuffer;
	}

	/**
	 * @return счетчик ожидающих отправки пакетов.
	 */
	protected AtomicInteger getWriteCounter() {
		return writeCounter;
	}

	/**
	 * @return Обработчик записи пакетов.
	 */
	protected CompletionHandler<Integer, S> getWriteHandler() {
		return writeHandler;
	}

	@Override
	public final boolean isClosed() {
		return closed.get();
	}

	/**
	 * Проверка готовности пакета к обработке.
	 * 
	 * @param buffer прочтенный пакет.
	 * @return можно ли начинать обработку.
	 */
	protected boolean isReady(final ByteBuffer buffer) {
		return true;
	}

	@Override
	public void lock() {
		lock.lock();
	}

	/**
	 * Перенос данных с пакета в буффер.
	 * 
	 * @param packet отправляемый пакет.
	 * @param buffer буффер, в который надо перенести.
	 * @return этот же буффер.
	 */
	protected abstract ByteBuffer movePacketToBuffer(S packet, ByteBuffer buffer);

	/**
	 * Обработка завершения отправки пакета.
	 * 
	 * @param packet отправленый пакет.
	 */
	protected abstract void onWrited(S packet);

	/**
	 * Чтение и обработка клиентского пакета.
	 * 
	 * @param buffer буффер данных.
	 */
	protected abstract void readPacket(ByteBuffer buffer);

	@Override
	public final void sendPacket(final S packet) {

		if(isClosed()) {
			return;
		}

		lock();
		try {

			if(isClosed()) {
				return;
			}

			final LinkedList<S> waitPackets = getWaitPackets();
			waitPackets.add(packet);

		} finally {
			unlock();
		}

		writeNextPacket();
	}

	/**
	 * @param closed закрыт ли конект.
	 */
	protected final void setClosed(final boolean closed) {
		this.closed.getAndSet(closed);
	}

	@Override
	public final void setLastActive(final long lastActive) {
		this.lastActive = lastActive;
	}

	@Override
	public final void startRead() {
		readBuffer.clear();
		channel.read(readBuffer, this, readHandler);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [network=" + network + ", channel=" + channel + ", closed=" + closed + ", lastActive=" + lastActive + "]";
	}

	@Override
	public void unlock() {
		lock.unlock();
	}

	/**
	 * Запись следующего ожидающего пакета.
	 */
	protected final void writeNextPacket() {

		if(isClosed()) {
			return;
		}

		final CompletionHandler<Integer, S> writeHandler = getWriteHandler();
		final AtomicInteger writeCounter = getWriteCounter();

		lock();
		try {

			if(isClosed() || writeCounter.get() > 0) {
				return;
			}

			final LinkedList<S> waitPackets = getWaitPackets();
			final S waitPacket = waitPackets.poll();

			if(waitPacket == null) {
				return;
			}

			writeCounter.incrementAndGet();

			final AsynchronousSocketChannel channel = getChannel();
			channel.write(movePacketToBuffer(waitPacket, getWriteBuffer()), waitPacket, writeHandler);

			onWrited(waitPacket);

		} finally {
			unlock();
		}
	}
}
