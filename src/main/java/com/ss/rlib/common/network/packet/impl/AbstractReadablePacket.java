package com.ss.rlib.common.network.packet.impl;

import com.ss.rlib.common.network.ConnectionOwner;
import com.ss.rlib.common.network.packet.ReadablePacket;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The base implementation of {@link ReadablePacket}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractReadablePacket extends AbstractPacket implements ReadablePacket, Reusable {

    protected AbstractReadablePacket() {
    }

    @Override
    public boolean read(@NotNull ConnectionOwner owner, @NotNull ByteBuffer buffer) {
        try {
            readImpl(owner, buffer);
            return true;
        } catch (Exception e) {
            handleException(buffer, e);
            return false;
        }
    }

    /**
     * The process of reading the data for this packet.
     *
     * @param owner  the packet's owner.
     * @param buffer the buffer for reading.
     */
    protected abstract void readImpl(@NotNull ConnectionOwner owner, @NotNull ByteBuffer buffer);

    /**
     * Handle the exception.
     *
     * @param buffer    the data buffer.
     * @param exception the exception.
     */
    protected void handleException(@NotNull ByteBuffer buffer, @NotNull Exception exception) {

        LOGGER.warning(this, exception);

        if (buffer.isDirect()) {
            byte[] array = new byte[buffer.limit()];
            buffer.get(array, 0, buffer.limit());
            LOGGER.warning(this, "buffer " +
                    buffer + "\n" + Utils.hexdump(array, array.length));
        } else {
            LOGGER.warning(this, "buffer " +
                    buffer + "\n" + Utils.hexdump(buffer.array(), buffer.limit()));
        }
    }
}
