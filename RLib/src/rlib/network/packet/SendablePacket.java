package rlib.network.packet;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Interface for implementing sendable packets.
 *
 * @author JavaSaBr
 */
public interface SendablePacket extends Packet {

    /**
     * Notifies about started preparing data for writing of this packet.
     */
    default void notifyStartedPreparing() {
    }

    /**
     * Notifies about finished preparing data for writing of this packet.
     */
    default void notifyFinishedPreparing() {
    }

    /**
     * Notifies about started writing data of this packet.
     */
    default void notifyStartedWriting() {
    }

    /**
     * Notifies about finished writing data of this packet.
     */
    default void notifyFinishedWriting() {
    }

    /**
     * Writes this packet to the buffer.
     *
     * @param buffer the buffer for writing.
     */
    void write(@NotNull ByteBuffer buffer);

    /**
     * Writes 1 byte to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value for writing.
     */
    default void writeByte(@NotNull final ByteBuffer buffer, final int value) {
        buffer.put((byte) value);
    }

    /**
     * Writes 2 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value for writing.
     */
    default void writeChar(@NotNull final ByteBuffer buffer, final char value) {
        buffer.putChar(value);
    }

    /**
     * Writes 2 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value for writing.
     */
    default void writeChar(@NotNull final ByteBuffer buffer, final int value) {
        buffer.putChar((char) value);
    }

    /**
     * Writes 4 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value for writing.
     */
    default void writeFloat(@NotNull final ByteBuffer buffer, final float value) {
        buffer.putFloat(value);
    }

    /**
     * Write the header of this packet to the buffer.
     *
     * @param buffer the buffer.
     * @param length the result length of this packet.
     */
    void writeHeader(@NotNull ByteBuffer buffer, int length);

    /**
     * Writes 4 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value for writing.
     */
    default void writeInt(@NotNull final ByteBuffer buffer, final int value) {
        buffer.putInt(value);
    }

    /**
     * Writes 8 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value for writing.
     */
    default void writeLong(@NotNull final ByteBuffer buffer, final long value) {
        buffer.putLong(value);
    }

    /**
     * Prepares the position of the buffer for writing data from this packet.
     */
    void prepareWritePosition(@NotNull ByteBuffer buffer);

    /**
     * Writes 2 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value for writing.
     */
    default void writeShort(@NotNull final ByteBuffer buffer, final int value) {
        buffer.putShort((short) value);
    }

    /**
     * Writes the string to the buffer.
     *
     * @param buffer the buffer.
     * @param string the string for writing.
     */
    default void writeString(@NotNull final ByteBuffer buffer, @NotNull final String string) {
        for (int i = 0, length = string.length(); i < length; i++) {
            buffer.putChar(string.charAt(i));
        }
    }
}