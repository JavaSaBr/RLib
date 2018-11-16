package com.ss.rlib.network.packet;

import com.ss.rlib.network.annotation.PacketDescription;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Interface to implement a writable packet.
 *
 * @author JavaSaBr
 */
public interface WritablePacket extends Packet {

    /**
     * Write this packet to the buffer.
     *
     * @param buffer the buffer.
     */
    void write(@NotNull ByteBuffer buffer);

    /**
     * Write 1 byte to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value.
     */
    default void writeByte(@NotNull ByteBuffer buffer, int value) {
        buffer.put((byte) value);
    }

    /**
     * Write packet type id of this packet to the buffer.
     *
     * @param buffer the buffer.
     */
    default void writePacketId(@NotNull ByteBuffer buffer) {
        writeShort(buffer, getPacketId());
    }

    /**
     * Get the packet id of this packet.
     *
     * @return the packet id.
     */
    default int getPacketId() {
        return getClass()
            .getAnnotation(PacketDescription.class)
            .id();
    }

    /**
     * Write 2 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value.
     */
    default void writeChar(@NotNull ByteBuffer buffer, char value) {
        buffer.putChar(value);
    }

    /**
     * Write 2 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value.
     */
    default void writeChar(@NotNull final ByteBuffer buffer, final int value) {
        buffer.putChar((char) value);
    }

    /**
     * Write 4 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value.
     */
    default void writeFloat(@NotNull ByteBuffer buffer, float value) {
        buffer.putFloat(value);
    }

    /**
     * Write size of the packet data to the buffer.
     *
     * @param buffer     the buffer.
     * @param packetSize the result packet size.
     */
    default void writePacketSize(@NotNull ByteBuffer buffer, int packetSize) {
        buffer.putShort(0, (short) packetSize);
    }

    /**
     * Write 4 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value.
     */
    default void writeInt(@NotNull ByteBuffer buffer, int value) {
        buffer.putInt(value);
    }

    /**
     * Write 8 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value.
     */
    default void writeLong(@NotNull ByteBuffer buffer, long value) {
        buffer.putLong(value);
    }

    /**
     * Prepare the start position in the buffer to write data from this packet.
     *
     * @param buffer the buffer
     */
    default void prepareWritePosition(@NotNull ByteBuffer buffer) {
        buffer.position(2);
    }

    /**
     * Writes 2 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value for writing.
     */
    default void writeShort(@NotNull ByteBuffer buffer, int value) {
        buffer.putShort((short) value);
    }

    /**
     * Writes the string to the buffer.
     *
     * @param buffer the buffer.
     * @param string the string for writing.
     */
    default void writeString(@NotNull ByteBuffer buffer, @NotNull String string) {
        writeInt(buffer, string.length());

        for (int i = 0, length = string.length(); i < length; i++) {
            buffer.putChar(string.charAt(i));
        }
    }

    /**
     * Write a data buffer to packet buffer.
     *
     * @param buffer thr packet buffer.
     * @param data   the data buffer.
     */
    default void writeBuffer(@NotNull ByteBuffer buffer, @NotNull ByteBuffer data) {
        buffer.put(data.array(), data.position(), data.limit());
    }

    /**
     * Notify this packet that it was added to queue to send.
     */
    default void notifyAddedToSend() {
    }
}