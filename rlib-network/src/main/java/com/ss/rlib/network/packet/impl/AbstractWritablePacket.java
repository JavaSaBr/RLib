package com.ss.rlib.network.packet.impl;

import org.jetbrains.annotations.NotNull;
import com.ss.rlib.network.packet.WritablePacket;

import java.nio.ByteBuffer;

/**
 * The base implementation of the {@link WritablePacket}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractWritablePacket extends AbstractPacket implements WritablePacket {

    @Override
    public boolean write(@NotNull ByteBuffer buffer) {
        try {
            writeImpl(buffer);
            return true;
        } catch (Exception e) {
            handleException(buffer, e);
            return false;
        }
    }

    /**
     * The process of writing this packet to the buffer.
     *
     * @param buffer the buffer
     */
    protected void writeImpl(@NotNull ByteBuffer buffer) {
    }
}
