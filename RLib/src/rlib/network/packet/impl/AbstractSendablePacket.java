package rlib.network.packet.impl;

import org.jetbrains.annotations.NotNull;
import rlib.network.packet.SendablePacket;
import rlib.util.Utils;

import java.nio.ByteBuffer;

/**
 * The base implementation of the {@link SendablePacket}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractSendablePacket extends AbstractPacket implements SendablePacket {

    /**
     * The memory barrier.
     */
    protected volatile int barrier;

    /**
     * The sink for the memory barrier.
     */
    protected int barrierSink;

    protected AbstractSendablePacket() {
        getPacketType();
    }

    @Override
    public void write(@NotNull final ByteBuffer buffer) {
        notifyStartedWriting();
        try {
            writeImpl(buffer);
        } catch (final Exception e) {
            LOGGER.warning(this, e);
            LOGGER.warning(this, "Buffer " + buffer + "\n" + Utils.hexdump(buffer.array(), buffer.position()));
        } finally {
            notifyFinishedWriting();
        }
    }

    /**
     * The process of writing this packet to the buffer.
     */
    protected void writeImpl(@NotNull final ByteBuffer buffer) {
        writePacketTypeId(buffer);
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
