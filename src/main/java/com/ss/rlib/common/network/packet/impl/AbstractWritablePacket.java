package com.ss.rlib.common.network.packet.impl;

import com.ss.rlib.common.network.annotation.PacketDescription;
import com.ss.rlib.common.network.annotation.PacketDescription;
import org.jetbrains.annotations.NotNull;
import com.ss.rlib.common.network.packet.WritablePacket;
import com.ss.rlib.common.util.Utils;

import java.nio.ByteBuffer;

/**
 * The base implementation of the {@link WritablePacket}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractWritablePacket extends AbstractPacket implements WritablePacket {

    /**
     * The packet id.
     */
    private int packetId;

    protected AbstractWritablePacket() {
        final PacketDescription description = getClass().getAnnotation(PacketDescription.class);
        this.packetId = description.id();
    }

    @Override
    public void write(@NotNull final ByteBuffer buffer) {
        try {
            writeImpl(buffer);
        } catch (final Exception e) {
            LOGGER.warning(this, e);
            LOGGER.warning(this, "Buffer " + buffer + "\n" + Utils.hexdump(buffer.array(), buffer.position()));
        }
    }

    /**
     * The process of writing this packet to the buffer.
     *
     * @param buffer the buffer
     */
    protected void writeImpl(@NotNull final ByteBuffer buffer) {
        writePacketId(buffer);
    }

    @Override
    public int getPacketId() {
        return packetId;
    }
}
