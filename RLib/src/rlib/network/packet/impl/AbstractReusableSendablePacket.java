package rlib.network.packet.impl;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.util.pools.Foldable;

import java.nio.ByteBuffer;

/**
 * Баовая реализация переиспользуемого отправляемого сетевого пакета. Реализация
 * переипользуемости опирается на атомарный счетчик, который подсчитывает
 * скольким клиентам его нужно отправить, скольким он отправил, и позволяет
 * переопределить логику через {@link #completeImpl()}, в которой можно
 * обработать завершения работы этого пакета после отправки всем необходимым
 * клиентам данных.
 *
 * @author Ronn
 */
public abstract class AbstractReusableSendablePacket<C> extends AbstractSendablePacket<C> implements Foldable {

    /**
     * счетчик добавлений на отправку экземпляра пакета
     */
    protected final AtomicInteger counter;

    public AbstractReusableSendablePacket() {
        this.counter = new AtomicInteger();
    }

    /**
     * Обработка завершение одной отправки пакета.
     */
    public void complete() {
        if (counter.decrementAndGet() == 0) {
            completeImpl();
        }
    }

    /**
     * Обработка завершения всех отправок этого пакета.
     */
    protected abstract void completeImpl();

    /**
     * Уменьшить кол-во отправок этого пакета на 1.
     */
    public final void decreaseSends() {
        counter.decrementAndGet();
    }

    /**
     * Уменьшить кол-во отправок этого пакета на указанное кол-во.
     */
    public void decreaseSends(final int count) {
        counter.subAndGet(count);
    }

    /**
     * Увеличить кол-во отправок этого пакета на 1.
     */
    public void increaseSends() {
        counter.incrementAndGet();
    }

    /**
     * Увеличить кол-во отправок этого пакета на указанное кол-во.
     */
    public void increaseSends(final int count) {
        counter.addAndGet(count);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [counter=" + counter + ", owner=" + owner + ", name=" + name + "]";
    }

    @Override
    public void write(final ByteBuffer buffer) {

        if (counter.get() < 1) {
            LOGGER.warning(this, "write finished packet " + this + " on thread " + Thread.currentThread().getName());
            return;
        }

        super.write(buffer);
    }
}
