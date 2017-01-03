package rlib.network.packet.impl;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

import rlib.network.packet.SendablePacket;
import rlib.util.Util;

/**
 * The base implementation of the {@link SendablePacket}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractSendablePacket<C> extends AbstractPacket<C> implements SendablePacket {

    /**
     * The memory barrier.
     */
    protected volatile int barrier;

    /**
     * The sink for the memory barrier.
     */
    protected int barrierSink;

    @Override
    public void write(@NotNull final ByteBuffer buffer) {
        notifyStartedWriting();
        try {
            writeImpl(buffer);
        } catch (final Exception e) {
            LOGGER.warning(this, e);
            LOGGER.warning(this, "Buffer " + buffer + "\n" + Util.hexdump(buffer.array(), buffer.position()));
        } finally {
            notifyFinishedWriting();
        }
    }

    /**
     * The process of writing this packet to the buffer.
     */
    protected void writeImpl(@NotNull final ByteBuffer buffer) {
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
    public void notifyStartedWriting() {
        barrierSink = barrier;
    }

    @Override
    public void notifyFinishedWriting() {
        barrier = barrierSink + 1;
    }
}
