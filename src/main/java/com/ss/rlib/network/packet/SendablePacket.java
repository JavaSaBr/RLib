package com.ss.rlib.network.packet;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Interface for implementing sendable packets.
 *
 * @author JavaSaBr
 */
public interface SendablePacket extends Packet {

    /**
     * Notify about started preparing data to write this packet.
     */
    default void notifyStartedPreparing() {
    }

    /**
     * Notify about finished preparing data to write this packet.
     */
    default void notifyFinishedPreparing() {
    }

    /**
     * Notify about started writing data of this packet.
     */
    default void notifyStartedWriting() {
    }

    /**
     * Notify about finished writing data of this packet.
     */
    default void notifyFinishedWriting() {
    }

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
    default void writeByte(@NotNull final ByteBuffer buffer, final int value) {
        buffer.put((byte) value);
    }

    /**
     * Write packet type id of this packet to the buffer.
     *
     * @param buffer the buffer.
     */
    default void writePacketTypeId(@NotNull final ByteBuffer buffer) {
        writeShort(buffer, getPacketType().getId());
    }

    /**
     * Write 2 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value.
     */
    default void writeChar(@NotNull final ByteBuffer buffer, final char value) {
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
    default void writeFloat(@NotNull final ByteBuffer buffer, final float value) {
        buffer.putFloat(value);
    }

    /**
     * Write size of the packet data to the buffer.
     *
     * @param buffer     the buffer.
     * @param packetSize the result packet size.
     */
    default void writePacketSize(@NotNull final ByteBuffer buffer, final int packetSize) {
        buffer.putShort(0, (short) packetSize);
    }

    /**
     * Write 4 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value.
     */
    default void writeInt(@NotNull final ByteBuffer buffer, final int value) {
        buffer.putInt(value);
    }

    /**
     * Write 8 bytes to the buffer.
     *
     * @param buffer the buffer.
     * @param value  the value.
     */
    default void writeLong(@NotNull final ByteBuffer buffer, final long value) {
        buffer.putLong(value);
    }

    /**
     * Prepare the start position in the buffer to write data from this packet.
     *
     * @param buffer the buffer
     */
    default void prepareWritePosition(@NotNull final ByteBuffer buffer) {
        buffer.position(2);
    }

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
    default void writeBuffer(@NotNull final ByteBuffer buffer, @NotNull final ByteBuffer data) {
        buffer.put(data.array(), data.position(), data.limit());
    }

    /**
     * Get a packet type of this packet.
     *
     * @return the packet type.
     */
    @NotNull
    default SendablePacketType<? extends SendablePacket> getPacketType() {
        throw new UnsupportedOperationException();
    }
}