package rlib.network.packet.impl;

import static java.util.Objects.requireNonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

import rlib.network.packet.ReadablePacket;
import rlib.util.Util;
import rlib.util.pools.Reusable;

/**
 * The base implementation of the readable packet.
 *
 * @author JavaSaBr
 */
public abstract class AbstractReadablePacket<C> extends AbstractPacket<C> implements ReadablePacket, Reusable {

    /**
     * The current buffer for reading.
     */
    @Nullable
    protected volatile ByteBuffer buffer;

    /**
     * The memory barrier.
     */
    protected volatile int barrier;

    /**
     * The sink for the memory barrier.
     */
    protected int barrierSink;

    @Override
    public final int getAvailableBytes() {
        return getBuffer().remaining();
    }

    @NotNull
    @Override
    public ByteBuffer getBuffer() {
        return requireNonNull(buffer);
    }

    @Override
    public void setBuffer(@Nullable final ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public final boolean read() {
        notifyStartedPreparing();
        try {
            readImpl();
            return true;
        } catch (final Exception e) {
            handleException(e);
        } finally {
            notifyFinishedPreparing();
        }
        return false;
    }

    protected void handleException(@NotNull final Exception e) {
        final ByteBuffer toPrintBuffer = buffer;
        LOGGER.warning(this, e);
        if (toPrintBuffer == null) return;
        LOGGER.warning(this, "buffer " + toPrintBuffer + "\n" + Util.hexdump(toPrintBuffer.array(), toPrintBuffer.limit()));
    }

    /**
     * The process of reading the data for this packet.
     */
    protected void readImpl() {
        readImpl(getBuffer());
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
    }
}
