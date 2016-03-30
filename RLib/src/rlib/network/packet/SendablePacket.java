package rlib.network.packet;

import java.nio.ByteBuffer;

import rlib.util.StringUtils;

/**
 * Интерфейс для реализации отправляемых пакетов.
 *
 * @author Ronn
 */
public interface SendablePacket<C> extends Packet<C> {

    /**
     * Записать пакет в указанный буфер.
     *
     * @param buffer локальный буффер.
     */
    public void write(ByteBuffer buffer);

    /**
     * Записать 1 байт в указанный буффер.
     */
    public default void writeByte(final ByteBuffer buffer, final int value) {
        buffer.put((byte) value);
    }

    /**
     * Записать 2 байта в указанный буффер.
     */
    public default void writeChar(final ByteBuffer buffer, final char value) {
        buffer.putChar(value);
    }

    /**
     * Записать 2 байта в указанный буффер.
     */
    public default void writeChar(final ByteBuffer buffer, final int value) {
        buffer.putChar((char) value);
    }

    /**
     * Записать 4 байта в указанный буффер.
     */
    public default void writeFloat(final ByteBuffer buffer, final float value) {
        buffer.putFloat(value);
    }

    /**
     * Записать зоголовок пакета в указанный буффер.
     *
     * @param length размер пакета.
     */
    public void writeHeader(ByteBuffer buffer, int length);

    /**
     * Записать 4 байта в указанный буффер.
     */
    public default void writeInt(final ByteBuffer buffer, final int value) {
        buffer.putInt(value);
    }

    /**
     * Записать 8 байт в указанный буффер.
     */
    public default void writeLong(final ByteBuffer buffer, final long value) {
        buffer.putLong(value);
    }

    /**
     * Установка на позицию для записи пакета.
     */
    public void writePosition(ByteBuffer buffer);

    /**
     * Записать 2 байта в указанный буффер.
     */
    public default void writeShort(final ByteBuffer buffer, final int value) {
        buffer.putShort((short) value);
    }

    /**
     * Записать строку в указанный буффер.
     */
    public default void writeString(final ByteBuffer buffer, String string) {

        if (string == null) {
            string = StringUtils.EMPTY;
        }

        for (int i = 0, length = string.length(); i < length; i++) {
            buffer.putChar(string.charAt(i));
        }
    }
}