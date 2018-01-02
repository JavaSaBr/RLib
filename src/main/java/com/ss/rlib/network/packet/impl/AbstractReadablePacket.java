package com.ss.rlib.network.packet.impl;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.util.ClassUtils;
import com.ss.rlib.util.Utils;
import com.ss.rlib.util.pools.Reusable;
import com.ss.rlib.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.function.Supplier;

/**
 * The base implementation of {@link ReadablePacket}.
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
    
    protected AbstractReadablePacket() {
        this.constructor = createConstructor();
    }

    /**
     * Create a constructor of this packet type.
     *
     * @return the constructor.
     */
    protected @NotNull Supplier<AbstractReadablePacket> createConstructor() {
        final Constructor<AbstractReadablePacket> constructor = notNull(ClassUtils.getConstructor(getClass()));
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
     * Handle the exception.
     *
     * @param buffer    the data buffer.
     * @param exception the exception.
     */
    protected void handleException(@NotNull final ByteBuffer buffer, @NotNull final Exception exception) {
        LOGGER.warning(this, exception);
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

    @Override
    public @NotNull AbstractReadablePacket newInstance() {
        return constructor.get();
    }

    /**
     * Get the pool for storing executed packets.
     *
     * @return the pool for storing executed packets or null.
     */
    protected @NotNull ReusablePool<AbstractReadablePacket> getPool() {
        return notNull(ClassUtils.unsafeCast(getPacketType().getPool()));
    }
}
