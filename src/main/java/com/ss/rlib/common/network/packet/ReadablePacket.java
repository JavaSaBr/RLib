package com.ss.rlib.common.network.packet;

import com.ss.rlib.common.network.ConnectionOwner;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The interface to implement a readable network packet.
 *
 * @author JavaSaBr
 */
public interface ReadablePacket extends Packet {

    /**
     * Read this packet.
     *
     * @param owner  the owner.
     * @param buffer the buffer to read data.
     * @return true if reading was success.
     */
    boolean read(@NotNull final ConnectionOwner owner, @NotNull final ByteBuffer buffer);

    /**
     * Read 1 byte from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 1 byte from this packet.
     */
    default int readByte(@NotNull final ByteBuffer buffer) {
        return buffer.get();
    }

    /**
     * Read the bytes array from this packet.
     *
     * @param buffer the buffer for reading.
     * @param array  the bytes array.
     */
    default void readBytes(@NotNull final ByteBuffer buffer, @NotNull final byte[] array) {
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
    default void readBytes(@NotNull final ByteBuffer buffer, @NotNull final byte[] array, final int offset,
                           final int length) {
        buffer.get(array, offset, length);
    }

    /**
     * Read 4 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 4 bytes from this packet.
     */
    default float readFloat(@NotNull final ByteBuffer buffer) {
        return buffer.getFloat();
    }

    /**
     * Read 4 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 4 bytes from this packet.
     */
    default int readInt(@NotNull final ByteBuffer buffer) {
        return buffer.getInt();
    }

    /**
     * Read 8 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 8 bytes from this packet.
     */
    default long readLong(@NotNull final ByteBuffer buffer) {
        return buffer.getLong();
    }

    /**
     * Read 2 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 2 bytes from this packet.
     */
    default int readShort(@NotNull final ByteBuffer buffer) {
        return buffer.getShort();
    }

    /**
     * Read a string form this packet.
     *
     * @param buffer the buffer.
     * @return the read string.
     */
    default @NotNull String readString(@NotNull final ByteBuffer buffer) {

        final int length = readInt(buffer);
        final char[] array = new char[length];

        for (int i = 0; i < length; i++) {
            array[i] = buffer.getChar();
        }

        return new String(array);
    }
}