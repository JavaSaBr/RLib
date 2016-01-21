package rlib.network.packet;

import java.nio.ByteBuffer;

/**
 * Интерфейс для реализации читаемого пакета.
 *
 * @author Ronn
 */
public interface ReadeablePacket<C> extends Packet<C> {

    /**
     * @return кол-во не прочитанных байтов.
     */
    public int getAvailableBytes();

    /**
     * @return буффер данных.
     */
    public ByteBuffer getBuffer();

    /**
     * @param buffer буффер данных.
     */
    public void setBuffer(ByteBuffer buffer);

    /**
     * Прочитать присланную информацию.
     *
     * @return успешно ли прочитано.
     */
    public boolean read();

    /**
     * Чтение одного байта из буфера.
     */
    public default int readByte() {
        final ByteBuffer buffer = getBuffer();
        return buffer.get() & 0xFF;
    }

    /**
     * Наполнение указанного массива байтов, байтами из буфера.
     *
     * @param array наполняемый массив байтов.
     */
    public default void readBytes(final byte[] array) {
        final ByteBuffer buffer = getBuffer();
        buffer.get(array);
    }

    /**
     * Наполнение указанного массива байтов, байтами из буфера.
     *
     * @param array  наполняемый массив байтов.
     * @param offset отступ в массиве байтов.
     * @param length кол-во записываемых байтов в массив.
     */
    public default void readBytes(final byte[] array, final int offset, final int length) {
        final ByteBuffer buffer = getBuffer();
        buffer.get(array, offset, length);
    }

    /**
     * Чтение 4х байтов в виде float из буфера.
     */
    public default float readFloat() {
        final ByteBuffer buffer = getBuffer();
        return buffer.getFloat();
    }

    /**
     * Чтение 4х байтов в виде int из буфера.
     */
    public default int readInt() {
        final ByteBuffer buffer = getBuffer();
        return buffer.getInt();
    }

    /**
     * Чтение 8ми байтов в виде long из буфера.
     */
    public default long readLong() {
        final ByteBuffer buffer = getBuffer();
        return buffer.getLong();
    }

    /**
     * Чтение 2х байтов в виде short из буфера.
     */
    public default int readShort() {
        final ByteBuffer buffer = getBuffer();
        return buffer.getShort() & 0xFFFF;
    }

    /**
     * Чтение строки из буфера по ближайшего нулевого символа.
     */
    public default String readString() {

        final StringBuilder builder = new StringBuilder();
        final ByteBuffer buffer = getBuffer();

        char cha;

        while (buffer.remaining() > 1) {

            cha = buffer.getChar();

            if (cha == 0) {
                break;
            }

            builder.append(cha);
        }

        return builder.toString();
    }

    /**
     * Чтение строки из буфера указанной длинны.
     */
    public default String readString(final int length) {

        final ByteBuffer buffer = getBuffer();

        final char[] array = new char[length];

        for (int i = 0; i < length && buffer.remaining() > 1; i++) {
            array[i] = buffer.getChar();
        }

        return new String(array);
    }
}