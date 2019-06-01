package com.ss.rlib.network.packet.impl;

import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.common.util.Utils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The base implementation of {@link ReadablePacket}.
 *
 * @author JavaSaBr
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractReadablePacket extends AbstractPacket implements ReadablePacket {

    @Override
    public boolean read(@NotNull Connection<?, ?> connection, @NotNull ByteBuffer buffer, int length) {
        var oldLimit = buffer.limit();
        try {
            buffer.limit(buffer.position() + length);
            readImpl(connection, buffer);
            return true;
        } catch (Exception e) {
            handleException(buffer, e);
            return false;
        } finally {
            buffer.limit(oldLimit);
        }
    }

    /**
     * Read this packet.
     *
     * @param connection the network connection.
     * @param buffer     the buffer with received.
     */
    protected abstract void readImpl(@NotNull Connection<?, ?> connection, @NotNull ByteBuffer buffer);

    /**
     * Read 1 byte from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 1 byte from this packet.
     */
    protected int readByte(@NotNull ByteBuffer buffer) {
        return buffer.get();
    }

    /**
     * Read the bytes array from this packet.
     *
     * @param buffer the buffer for reading.
     * @param array  the bytes array.
     */
    protected void readBytes(@NotNull ByteBuffer buffer, @NotNull byte[] array) {
        buffer.get(array);
    }

    /**
     * Read the bytes array from this packet.
     *
     * @param buffer the buffer for reading.
     * @param array  the bytes array.
     * @param offset the offset for reading.
     * @param length the length for reading.
     */
    protected void readBytes(@NotNull ByteBuffer buffer, @NotNull byte[] array, int offset, int length) {
        buffer.get(array, offset, length);
    }

    /**
     * Read 4 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 4 bytes from this packet.
     */
    protected float readFloat(@NotNull ByteBuffer buffer) {
        return buffer.getFloat();
    }

    /**
     * Read 4 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 4 bytes from this packet.
     */
    protected int readInt(@NotNull ByteBuffer buffer) {
        return buffer.getInt();
    }

    /**
     * Read 8 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 8 bytes from this packet.
     */
    protected long readLong(@NotNull ByteBuffer buffer) {
        return buffer.getLong();
    }

    /**
     * Read 2 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 2 bytes from this packet.
     */
    protected int readShort(@NotNull ByteBuffer buffer) {
        return buffer.getShort();
    }

    /**
     * Read a string form this packet.
     *
     * @param buffer the buffer.
     * @return the read string.
     */
    protected @NotNull String readString(@NotNull ByteBuffer buffer) {

        var length = readInt(buffer);
        var array = new char[length];

        for (int i = 0; i < length; i++) {
            array[i] = buffer.getChar();
        }

        return new String(array);
    }
}
