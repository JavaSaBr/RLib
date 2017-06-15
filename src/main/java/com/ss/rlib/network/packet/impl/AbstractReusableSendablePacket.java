package com.ss.rlib.network.packet.impl;

import static java.util.Objects.requireNonNull;
import static com.ss.rlib.util.ClassUtils.unsafeCast;
import com.ss.rlib.util.ClassUtils;
import com.ss.rlib.util.pools.Reusable;
import com.ss.rlib.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ss.rlib.concurrent.atomic.AtomicInteger;

import java.nio.ByteBuffer;

/**
 * The reusable implementation of the {@link AbstractSendablePacket} using the counter for controlling the life cycle of
 * this packet.
 *
 * @author JavaSaBr
 */
public abstract class AbstractReusableSendablePacket extends AbstractSendablePacket implements Reusable {

    /**
     * The counter of pending sendings.
     */
    @NotNull
    protected final AtomicInteger counter;

    /**
     * The pool to store this packet after using.
     */
    @Nullable
    protected ReusablePool<AbstractReusableSendablePacket> pool;

    /**
     * Instantiates a new Abstract reusable sendable packet.
     */
    public AbstractReusableSendablePacket() {
        this.counter = new AtomicInteger();
    }

    /**
     * Handle completion of packet sending.
     */
    public void complete() {
        if (counter.decrementAndGet() == 0) {
            completeImpl();
        }
    }

    /**
     * Force complete this packet.
     */
    public void forceComplete() {
        counter.set(1);
        complete();
    }

    /**
     * @return the pool to store used packet.
     */
    @NotNull
    private ReusablePool<AbstractReusableSendablePacket> getPool() {
        if (pool != null) return pool;
        return requireNonNull(ClassUtils.unsafeCast(getPacketType().getPool()));
    }

    /**
     * Implementation of handling completion of packet sending.
     */
    protected void completeImpl() {
        getPool().put(this);
    }

    /**
     * New instance t.
     *
     * @param <T> the type parameter
     * @return the new instance.
     */
    @NotNull
    public final <T extends AbstractReusableSendablePacket> T newInstance() {
        final AbstractReusableSendablePacket result = getPool().take(getClass(), ClassUtils::newInstance);
        return requireNonNull(ClassUtils.unsafeCast(result));
    }

    /**
     * Sets pool.
     *
     * @param pool the pool to store used packet.
     */
    public final void setPool(@NotNull final ReusablePool<AbstractReusableSendablePacket> pool) {
        this.pool = pool;
    }

    /**
     * Decrease sending count.
     */
    public final void decreaseSends() {
        counter.decrementAndGet();
    }

    /**
     * Decrease sending count.
     *
     * @param count the count.
     */
    public void decreaseSends(final int count) {
        counter.subAndGet(count);
    }

    /**
     * Increase sending count.
     */
    public void increaseSends() {
        counter.incrementAndGet();
    }

    /**
     * Increase sending count.
     *
     * @param count the count.
     */
    public void increaseSends(final int count) {
        counter.addAndGet(count);
    }

    @Override
    public String toString() {
        return "AbstractReusableSendablePacket{" + "counter=" + counter + "} " + super.toString();
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
