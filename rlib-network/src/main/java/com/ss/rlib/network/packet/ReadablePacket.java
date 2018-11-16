package com.ss.rlib.network.packet;

import com.ss.rlib.network.ConnectionOwner;
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
    boolean read(@NotNull ConnectionOwner owner, @NotNull ByteBuffer buffer);

    /**
     * Read 1 byte from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 1 byte from this packet.
     */
    default int readByte(@NotNull ByteBuffer buffer) {
        return buffer.get();
    }

    /**
     * Read the bytes array from this packet.
     *
     * @param buffer the buffer for reading.
     * @param array  the bytes array.
     */
    default void readBytes(@NotNull ByteBuffer buffer, @NotNull byte[] array) {
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
    default void readBytes(@NotNull ByteBuffer buffer, @NotNull byte[] array, int offset, int length) {
        buffer.get(array, offset, length);
    }

    /**
     * Read 4 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 4 bytes from this packet.
     */
    default float readFloat(@NotNull ByteBuffer buffer) {
        return buffer.getFloat();
    }

    /**
     * Read 4 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 4 bytes from this packet.
     */
    default int readInt(@NotNull ByteBuffer buffer) {
        return buffer.getInt();
    }

    /**
     * Read 8 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 8 bytes from this packet.
     */
    default long readLong(@NotNull ByteBuffer buffer) {
        return buffer.getLong();
    }

    /**
     * Read 2 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 2 bytes from this packet.
     */
    default int readShort(@NotNull ByteBuffer buffer) {
        return buffer.getShort();
    }

    /**
     * Read a string form this packet.
     *
     * @param buffer the buffer.
     * @return the read string.
     */
    default @NotNull String readString(@NotNull ByteBuffer buffer) {

        var length = readInt(buffer);
        var array = new char[length];

        for (int i = 0; i < length; i++) {
            array[i] = buffer.getChar();
        }

        return new String(array);
    }
}