package rlib.network.packet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The interface for implementing readable network packets.
 *
 * @author JavaSaBr
 */
public interface ReadablePacket extends Packet {

    /**
     * Notifies about started preparing data for reading of this packet.
     */
    default void notifyStartedPreparing() {
    }

    /**
     * Notifies about finished preparing data for reading of this packet.
     */
    default void notifyFinishedPreparing() {
    }

    /**
     * Notifies about started reading data of this packet.
     */
    default void notifyStartedReading() {
    }

    /**
     * Notifies about finished reading data of this packet.
     */
    default void notifyFinishedReading() {
    }

    /**
     * @return count of available bytes fro reading.
     */
    int getAvailableBytes();

    /**
     * Gets the current read buffer.
     *
     * @return the current read buffer or {@link NullPointerException} if this packet has no buffer.
     */
    @NotNull
    ByteBuffer getBuffer();

    /**
     * @param buffer the current read buffer.
     */
    void setBuffer(@Nullable final ByteBuffer buffer);

    /**
     * Reads this packet.
     *
     * @return true if reading was success.
     */
    boolean read();

    /**
     * Reads 1 byte from this packet.
     *
     * @return 1 byte from this packet.
     */
    default int readByte() {
        return readByte(getBuffer());
    }

    /**
     * Reads 1 byte from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 1 byte from this packet.
     */
    default int readByte(@NotNull final ByteBuffer buffer) {
        return buffer.get();
    }

    /**
     * Reads the bytes array from this packet.
     *
     * @param array the bytes array.
     */
    default void readBytes(@NotNull final byte[] array) {
        readBytes(getBuffer(), array);
    }

    /**
     * Reads the bytes array from this packet.
     *
     * @param buffer the buffer for reading.
     * @param array  the bytes array.
     */
    default void readBytes(@NotNull final ByteBuffer buffer, @NotNull final byte[] array) {
        buffer.get(array);
    }

    /**
     * Reads the bytes array from this packet.
     *
     * @param array  the bytes array.
     * @param offset the offset for reading.
     * @param length the length for reading.
     */
    default void readBytes(@NotNull final byte[] array, final int offset, final int length) {
        readBytes(getBuffer(), array, offset, length);
    }

    /**
     * Reads the bytes array from this packet.
     *
     * @param buffer the buffer for reading.
     * @param array  the bytes array.
     * @param offset the offset for reading.
     * @param length the length for reading.
     */
    default void readBytes(@NotNull final ByteBuffer buffer, @NotNull final byte[] array,
                           final int offset, final int length) {
        buffer.get(array, offset, length);
    }

    /**
     * Reads 4 bytes from this packet.
     *
     * @return 4 bytes from this packet.
     */
    default float readFloat() {
        return readFloat(getBuffer());
    }

    /**
     * Reads 4 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 4 bytes from this packet.
     */
    default float readFloat(@NotNull final ByteBuffer buffer) {
        return buffer.getFloat();
    }

    /**
     * Reads 4 bytes from this packet.
     *
     * @return 4 bytes from this packet.
     */
    default int readInt() {
        return readInt(getBuffer());
    }

    /**
     * Reads 4 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 4 bytes from this packet.
     */
    default int readInt(@NotNull final ByteBuffer buffer) {
        return buffer.getInt();
    }

    /**
     * Reads 8 bytes from this packet.
     *
     * @return 8 bytes from this packet.
     */
    default long readLong() {
        return readLong(getBuffer());
    }

    /**
     * Reads 8 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 8 bytes from this packet.
     */
    default long readLong(@NotNull final ByteBuffer buffer) {
        return buffer.getLong();
    }

    /**
     * Reads 2 bytes from this packet.
     *
     * @return 2 bytes from this packet.
     */
    default int readShort() {
        return readShort(getBuffer());
    }

    /**
     * Reads 2 bytes from this packet.
     *
     * @param buffer the buffer for reading.
     * @return 2 bytes from this packet.
     */
    default int readShort(@NotNull final ByteBuffer buffer) {
        return buffer.getShort();
    }

    /**
     * Reads the string form this packet.
     *
     * @return the read string.
     */
    default String readString() {
        return readString(getBuffer());
    }

    /**
     * Reads the string form this packet.
     *
     * @param buffer the buffer for reading.
     * @return the read string.
     */
    default String readString(@NotNull final ByteBuffer buffer) {

        final StringBuilder builder = new StringBuilder();
        char cha;

        while (buffer.remaining() > 1) {
            cha = buffer.getChar();
            if (cha == 0) break;
            builder.append(cha);
        }

        return builder.toString();
    }

    /**
     * Reads the string form this packet.
     *
     * @param length the length of the string.
     * @return the read string.
     */
    default String readString(final int length) {
        return readString(getBuffer(), length);
    }

    /**
     * Reads the string form this packet.
     *
     * @param buffer the buffer for reading.
     * @param length the length of the string.
     * @return the read string.
     */
    default String readString(@NotNull final ByteBuffer buffer, final int length) {

        final char[] array = new char[length];

        for (int i = 0; i < length && buffer.remaining() > 1; i++) {
            array[i] = buffer.getChar();
        }

        return new String(array);
    }
}