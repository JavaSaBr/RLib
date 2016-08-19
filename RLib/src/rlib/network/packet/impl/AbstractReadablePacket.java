package rlib.network.packet.impl;

import java.nio.ByteBuffer;

import rlib.network.packet.ReadablePacket;
import rlib.util.Util;
import rlib.util.pools.Reusable;

/**
 * Базовая реализация читаемого пакета.
 *
 * @author JavaSaBr
 */
public abstract class AbstractReadablePacket<C> extends AbstractPacket<C> implements ReadablePacket<C>, Reusable {

    /**
     * Буффер данных читаемого пакета.
     */
    protected volatile ByteBuffer buffer;

    @Override
    public final int getAvailableBytes() {
        return buffer.remaining();
    }

    @Override
    public ByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void setBuffer(final ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public final boolean read() {

        try {
            readImpl();
            return true;
        } catch (final Exception e) {
            LOGGER.warning(this, e);
            LOGGER.warning(this, "buffer " + buffer + "\n" + Util.hexdump(buffer.array(), buffer.limit()));
        }

        return false;
    }

    /**
     * Процесс чтения пакета.
     */
    protected abstract void readImpl();
}
