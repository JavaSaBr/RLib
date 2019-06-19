package com.ss.rlib.network.packet.impl;

import com.ss.rlib.common.util.ClassUtils;
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
public abstract class AbstractReadablePacket<C extends Connection<?, ?>> extends AbstractPacket implements ReadablePacket {

    @Override
    public boolean read(@NotNull Connection<?, ?> connection, @NotNull ByteBuffer buffer, int length) {
        var oldLimit = buffer.limit();
        try {
            buffer.limit(buffer.position() + length);
            readImpl(ClassUtils.unsafeNNCast(connection), buffer);
            return true;
        } catch (Exception e) {
            handleException(buffer, e);
            return false;
        } finally {
            buffer.limit(oldLimit);
        }
    }

    /**
     * Read packet's data from byte buffer.
     *
     * @param connection the network connection.
     * @param buffer     the buffer with received data.
     */
    protected void readImpl(@NotNull C connection, @NotNull ByteBuffer buffer) {
    }

    /**
     * Read 1 byte from buffer.
     *
     * @param buffer the buffer to read.
     * @return 1 byte from the buffer.
     */
    protected int readByte(@NotNull ByteBuffer buffer) {
        return buffer.get();
    }

    /**
     * Fill byte array with data from received buffer.
     *
     * @param buffer the buffer to read.
     * @param array  the filled byte array.
     */
    protected void readBytes(@NotNull ByteBuffer buffer, @NotNull byte[] array) {
        buffer.get(array);
    }

    /**
     * Fill byte array with data from received buffer.
     *
     * @param buffer the buffer to read.
     * @param array  the byte array.
     * @param offset the offset to fill the byte array.
     * @param length the length to fill the byte array.
     */
    protected void readBytes(@NotNull ByteBuffer buffer, @NotNull byte[] array, int offset, int length) {
        buffer.get(array, offset, length);
    }

    /**
     * Read 4 bytes from buffer.
     *
     * @param buffer the buffer to read.
     * @return 4 bytes as <code>float</code> from the buffer.
     */
    protected float readFloat(@NotNull ByteBuffer buffer) {
        return buffer.getFloat();
    }

    /**
     * Read 8 bytes from buffer.
     *
     * @param buffer the buffer to read.
     * @return 4 bytes as <code>double</code> from the buffer.
     */
    protected double readDouble(@NotNull ByteBuffer buffer) {
        return buffer.getDouble();
    }

    /**
     * Read 4 bytes from buffer.
     *
     * @param buffer the buffer to read.
     * @return 4 bytes as <code>int</code> from the buffer.
     */
    protected int readInt(@NotNull ByteBuffer buffer) {
        return buffer.getInt();
    }

    /**
     * Read 8 bytes from buffer.
     *
     * @param buffer the buffer to read.
     * @return 8 bytes as <code>long</code> from buffer.
     */
    protected long readLong(@NotNull ByteBuffer buffer) {
        return buffer.getLong();
    }

    /**
     * Read 2 bytes from buffer.
     *
     * @param buffer the buffer to read.
     * @return 2 bytes as <code>short</code> from buffer.
     */
    protected int readShort(@NotNull ByteBuffer buffer) {
        return buffer.getShort();
    }

    /**
     * Read a string from buffer.
     *
     * @param buffer the buffer to read.
     * @return the read string from the buffer.
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
