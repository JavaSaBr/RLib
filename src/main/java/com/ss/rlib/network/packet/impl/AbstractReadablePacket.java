package com.ss.rlib.network.packet.impl;

import static java.util.Objects.requireNonNull;
import static com.ss.rlib.util.ClassUtils.getConstructor;
import static com.ss.rlib.util.ClassUtils.unsafeCast;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.util.ClassUtils;
import com.ss.rlib.util.pools.Reusable;
import com.ss.rlib.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;
import com.ss.rlib.util.Utils;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.function.Supplier;

/**
 * The base implementation of the readable packet.
 *
 * @author JavaSaBr
 */
public abstract class AbstractReadablePacket extends AbstractPacket implements ReadablePacket, Reusable {

    /**
     * The constructor of this packet.
     */
    @NotNull
    private final Supplier<AbstractReadablePacket> constructor;

    /**
     * The memory barrier.
     */
    protected volatile int barrier;

    /**
     * The sink for the memory barrier.
     */
    protected int barrierSink;

    /**
     * Instantiates a new Abstract readable packet.
     */
    protected AbstractReadablePacket() {
        this.constructor = createConstructor();
    }

    /**
     * Create a constructor of this packet type.
     *
     * @return the constructor.
     */
    @NotNull
    protected Supplier<AbstractReadablePacket> createConstructor() {
        final Constructor<AbstractReadablePacket> constructor = requireNonNull(ClassUtils.getConstructor(getClass()));
        return () -> ClassUtils.newInstance(constructor);
    }

    @Override
    public boolean read(@NotNull final ByteBuffer buffer) {
        notifyStartedPreparing();
        try {
            readImpl(buffer);
            return true;
        } catch (final Exception e) {
            handleException(buffer, e);
        } finally {
            notifyFinishedPreparing();
        }
        return false;
    }

    /**
     * Handle exception.
     *
     * @param buffer the buffer
     * @param e      the e
     */
    protected void handleException(@NotNull final ByteBuffer buffer, @NotNull final Exception e) {
        LOGGER.warning(this, e);
        if (buffer.isDirect()) {
            final byte[] array = new byte[buffer.limit()];
            buffer.get(array, 0, buffer.limit());
            LOGGER.warning(this, "buffer " + buffer + "\n" + Utils.hexdump(array, array.length));
        } else {
            LOGGER.warning(this, "buffer " + buffer + "\n" + Utils.hexdump(buffer.array(), buffer.limit()));
        }
    }

    /**
     * The process of reading the data for this packet.
     *
     * @param buffer the buffer for reading.
     */
    protected void readImpl(@NotNull final ByteBuffer buffer) {
    }

    @Override
    public void notifyStartedPreparing() {
        barrierSink = barrier;
    }

    @Override
    public void notifyFinishedPreparing() {
        barrier = barrierSink + 1;
    }

    @Override
    public void notifyStartedReading() {
        barrierSink = barrier;
    }

    @Override
    public void notifyFinishedReading() {
        barrier = barrierSink + 1;
        getPool().put(this);
    }

    @NotNull
    @Override
    public AbstractReadablePacket newInstance() {
        return constructor.get();
    }

    /**
     * Get the pool for storing executed packets.
     *
     * @return the pool for storing executed packets or null.
     */
    @NotNull
    protected ReusablePool<AbstractReadablePacket> getPool() {
        return requireNonNull(ClassUtils.unsafeCast(getPacketType().getPool()));
    }
}
