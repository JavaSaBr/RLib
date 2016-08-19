package rlib.network.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.lock.LockFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.AsyncConnection;
import rlib.network.AsynchronousNetwork;
import rlib.network.NetworkConfig;
import rlib.util.linkedlist.LinkedList;
import rlib.util.linkedlist.LinkedListFactory;

/**
 * Базовая модель асинхронного конекта.
 *
 * @author JavaSaBr
 */
public abstract class AbstractAsyncConnection<N extends AsynchronousNetwork, R, S> implements AsyncConnection<R, S> {

    protected static final Logger LOGGER = LoggerManager.getLogger(AsyncConnection.class);

    /**
     * Модель сети, в которой это соединение.
     */
    protected final N network;

    /**
     * Список ожидающих пакетов на отправку
     */
    protected final LinkedList<S> waitPackets;

    /**
     * Канал соединения.
     */
    protected final AsynchronousSocketChannel channel;

    /**
     * Промежуточный буффер с присылаемыми данными
     */
    protected final ByteBuffer readBuffer;

    /**
     * Промежуточный буффер с отсылаемыми данными
     */
    protected final ByteBuffer writeBuffer;

    /**
     * Находится ли соединение в ожидании отправки пакетов.
     */
    protected final AtomicBoolean isWriting;

    /**
     * Флаг закрытости соединения.
     */
    protected final AtomicBoolean closed;

    /**
     * Настройки сети.
     */
    protected final NetworkConfig config;

    /**
     * Блокировщик
     */
    protected final Lock lock;

    /**
     * Время последней активности соединения.
     */
    protected volatile long lastActive;

    /**
     * Обработчик чтения пакетов.
     */
    private final CompletionHandler<Integer, AbstractAsyncConnection> readHandler = new CompletionHandler<Integer, AbstractAsyncConnection>() {

        @Override
        public void completed(final Integer result, final AbstractAsyncConnection connection) {
            setLastActive(System.currentTimeMillis());

            if (result == -1) {
                finish();
                return;
            }

            final ByteBuffer buffer = getReadBuffer();
            buffer.flip();
            try {
                if (isReady(buffer)) readPacket(buffer);
            } catch (final Exception e) {
                LOGGER.error(this, e);
            } finally {
                buffer.clear();
            }

            startRead();
        }

        @Override
        public void failed(final Throwable exc, final AbstractAsyncConnection attachment) {

            if (config.isVisibleReadException()) {
                LOGGER.warning(this, exc);
            }

            if (!isClosed()) {
                finish();
            }
        }
    };

    /**
     * Обработчик записи пакетов.
     */
    private final CompletionHandler<Integer, S> writeHandler = new CompletionHandler<Integer, S>() {

        @Override
        public void completed(final Integer result, final S packet) {
            setLastActive(System.currentTimeMillis());

            if (result == -1) {
                finish();
                return;
            }

            final ByteBuffer buffer = getWriteBuffer();

            if (buffer.remaining() > 0) {
                channel.write(buffer, packet, this);
                return;
            }

            if (isWriting.compareAndSet(true, false)) writeNextPacket();
        }

        @Override
        public void failed(final Throwable exc, final S packet) {

            if (config.isVisibleWriteException()) {
                LOGGER.warning(this, new Exception("incorrect write packet " + packet, exc));
            }

            if (isClosed()) {
                return;
            }

            if (isWriting.compareAndSet(true, false)) writeNextPacket();
        }
    };

    public AbstractAsyncConnection(final N network, final AsynchronousSocketChannel channel, final Class<S> sendableType) {
        this.lock = LockFactory.newLock();
        this.channel = channel;
        this.waitPackets = LinkedListFactory.newLinkedList(sendableType);
        this.network = network;
        this.readBuffer = network.takeReadBuffer();
        this.readBuffer.clear();
        this.writeBuffer = network.takeWriteBuffer();
        this.config = network.getConfig();
        this.isWriting = new AtomicBoolean(false);
        this.closed = new AtomicBoolean(false);
    }

    /**
     * Очистка от ожидающих отправки пакетов.
     */
    protected void clearWaitPackets() {
        waitPackets.clear();
    }

    @Override
    public void close() {
        lock();
        try {

            if (isClosed()) {
                return;
            }

            setClosed(true);

            final AsynchronousSocketChannel channel = getChannel();

            if (channel.isOpen()) {
                channel.close();
            }

            clearWaitPackets();

            final N network = getNetwork();
            network.putReadBuffer(getReadBuffer());
            network.putWriteBuffer(getWriteBuffer());

        } catch (final IOException e) {
            LOGGER.warning(this, e);
        } finally {
            unlock();
        }
    }

    /**
     * Обработка остановки коннекта.
     */
    protected void finish() {
        close();
    }

    /**
     * @return канал соединения.
     */
    protected AsynchronousSocketChannel getChannel() {
        return channel;
    }

    @Override
    public final long getLastActive() {
        return lastActive;
    }

    @Override
    public final void setLastActive(final long lastActive) {
        this.lastActive = lastActive;
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
    protected CompletionHandler<Integer, AbstractAsyncConnection> getReadHandler() {
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
     * @param closed закрыт ли конект.
     */
    protected final void setClosed(final boolean closed) {
        this.closed.getAndSet(closed);
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
    protected abstract ByteBuffer writePacketToBuffer(S packet, ByteBuffer buffer);

    /**
     * Обработка завершения отправки пакета.
     *
     * @param packet отправленый пакет.
     */
    protected abstract void completed(S packet);

    /**
     * Чтение и обработка клиентского пакета.
     *
     * @param buffer буффер данных.
     */
    protected abstract void readPacket(ByteBuffer buffer);

    @Override
    public final void sendPacket(final S packet) {

        if (isClosed()) {
            return;
        }

        lock();
        try {
            waitPackets.add(packet);
        } finally {
            unlock();
        }

        writeNextPacket();
    }

    @Override
    public final void startRead() {
        channel.read(readBuffer, this, readHandler);
    }

    @Override
    public String toString() {
        return "AbstractAsyncConnection{" +
                "network=" + network +
                ", waitPackets=" + waitPackets +
                ", channel=" + channel +
                ", isWriting=" + isWriting +
                ", closed=" + closed +
                ", lastActive=" + lastActive +
                '}';
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    /**
     * Запись следующего ожидающего пакета.
     */
    protected final void writeNextPacket() {
        lock();
        try {

            if (isClosed() || !isWriting.compareAndSet(false, true)) {
                return;
            }

            final S waitPacket = waitPackets.poll();

            if (waitPacket == null) {
                isWriting.set(false);
                return;
            }

            final AsynchronousSocketChannel channel = getChannel();
            channel.write(writePacketToBuffer(waitPacket, getWriteBuffer()), waitPacket, getWriteHandler());

            completed(waitPacket);

        } finally {
            unlock();
        }
    }
}
