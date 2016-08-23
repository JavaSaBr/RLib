package rlib.network.packet.impl;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.util.pools.Reusable;

/**
 * The reusable implementation of the {@link AbstractSendablePacket} using the counter for
 * controlling the life cycle of this packet.
 *
 * @author JavaSaBr
 */
public abstract class AbstractReusableSendablePacket<C> extends AbstractSendablePacket<C> implements Reusable {

    /**
     * Counter with the number of pending sendings.
     */
    protected final AtomicInteger counter;

    public AbstractReusableSendablePacket() {
        this.counter = new AtomicInteger();
    }

    /**
     * Handles send completion.
     */
    public void complete() {
        if (counter.decrementAndGet() == 0) {
            completeImpl();
        }
    }

    /**
     * Handles send completion.
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
        return "AbstractReusableSendablePacket{" +
                "counter=" + counter +
                "} " + super.toString();
    }

    @Override
    public void write(@NotNull final ByteBuffer buffer) {

        if (counter.get() < 1) {
            LOGGER.warning(this, "write finished packet " + this + " on thread " + Thread.currentThread().getName());
            return;
        }

        super.write(buffer);
    }
}
