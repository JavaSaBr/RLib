package com.ss.rlib.network.packet;

import com.ss.rlib.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The interface for implementing readable network packets.
 *
 * @author JavaSaBr
 */
public interface ReadablePacket extends Packet {

    /**
     * Notify about started preparing data to read this packet.
     */
    default void notifyStartedPreparing() {
    }

    /**
     * Notify about finished preparing data to read this packet.
     */
    default void notifyFinishedPreparing() {
    }

    /**
     * Notify about started reading data of this packet.
     */
    default void notifyStartedReading() {
    }

    /**
     * Notify about finished reading data of this packet.
     */
    default void notifyFinishedReading() {
    }

    /**
     * Read this packet.
     *
     * @param buffer the buffer to read data.
     * @return true if reading was success.
     */
    boolean read(@NotNull final ByteBuffer buffer);

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
    @NotNull
    default String readString(@NotNull final ByteBuffer buffer) {

        final int length = readInt(buffer);
        final char[] array = new char[length];

        for (int i = 0; i < length; i++) {
            array[i] = buffer.getChar();
        }

        return new String(array);
    }

    /**
     * Get a packet type of this packet.
     *
     * @return the packet type.
     */
    @NotNull
    default ReadablePacketType<? extends ReadablePacket> getPacketType() {
        throw new UnsupportedOperationException();
    }

    /**
     * Create a new instance of this packet.
     *
     * @return the new instance.
     */
    @NotNull
    default ReadablePacket newInstance() {
        return ClassUtils.newInstance(getClass());
    }
}