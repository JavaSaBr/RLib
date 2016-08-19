package rlib.util;

import java.nio.ByteBuffer;

import static java.lang.Math.min;

/**
 * Класс с утильными методами для работы с буферами.
 *
 * @author JavaSaBr
 */
public class BufferUtils {

    /**
     * Переместить данные указанного буфера в другой указанный буфер.
     *
     * @param buffer      буфер чьи данные надо перенести.
     * @param destination буффер в который надо перенести эти данные.
     */
    public static void moveAndFlip(final ByteBuffer buffer, final ByteBuffer destination) {
        destination.put(buffer);
        destination.flip();
    }

    /**
     * Переместить данные указанного буфера в другой указанный буфер.
     *
     * @param buffer               буфер чьи данные надо перенести.
     * @param destination          буффер в который надо перенести эти данные.
     * @param needClearDestination нужно ли очищать целевой буффер перед переносом в него данных.
     */
    public static void moveAndFlip(final ByteBuffer buffer, final ByteBuffer destination, final boolean needClearDestination) {
        if (needClearDestination) destination.clear();
        moveAndFlip(buffer, destination);
    }

    /**
     * Скопировать данные указанного буфера в другой указанный буфер.
     *
     * @param buffer      буфер чьи данные надо скопировать.
     * @param destination буффер в который надо скопировать эти данные.
     */
    public static void copyAndFlip(final ByteBuffer buffer, final ByteBuffer destination) {
        destination.put(buffer.array(), buffer.position(), min(destination.remaining(), buffer.remaining()));
        destination.flip();
    }

    /**
     * Скопировать данные указанного буфера в другой указанный буфер.
     *
     * @param buffer      буфер чьи данные надо скопировать.
     * @param destination буффер в который надо скопировать эти данные.
     */
    public static void copy(final ByteBuffer buffer, final ByteBuffer destination) {
        destination.put(buffer.array(), buffer.position(), min(destination.remaining(), buffer.remaining()));
    }

    /**
     * Скопировать данные указанного буфера в другой указанный буфер.
     *
     * @param buffer               буфер чьи данные надо скопировать.
     * @param destination          буффер в который надо скопировать эти данные.
     * @param needClearDestination нужно ли очищать целевой буффер перед копированием в него данных.
     */
    public static void copyAndFlip(final ByteBuffer buffer, final ByteBuffer destination, final boolean needClearDestination) {
        if (needClearDestination) destination.clear();
        copyAndFlip(buffer, destination);
    }

    /**
     * Перемещение данных из буффера в указанный буффер.
     *
     * @param buffer      буффер данные которого надо перенести.
     * @param destination целевой буффер для переноса.
     */
    public static void move(final ByteBuffer buffer, final ByteBuffer destination) {
        destination.put(buffer);
    }
}
