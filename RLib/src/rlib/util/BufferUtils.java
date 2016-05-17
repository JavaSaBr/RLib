package rlib.util;

import java.nio.ByteBuffer;

import static java.lang.Math.min;

/**
 * Класс с утильными методами для работы с буферами.
 *
 * @author Ronn
 */
public class BufferUtils {

    /**
     * Переместить данные указанного буфера в другой указанный буфер.
     *
     * @param buffer      буфер чьи данные надо перенести.
     * @param destination буффер в который надо перенести эти данные.
     * @return все ли данные были перенесены.
     */
    public static boolean moveAndFlip(final ByteBuffer buffer, final ByteBuffer destination) {

        final int originalLength = buffer.remaining();
        final int length = min(destination.remaining(), originalLength);

        destination.put(buffer.array(), buffer.position(), length);
        destination.flip();

        return length == originalLength;
    }

    /**
     * Переместить данные указанного буфера в другой указанный буфер.
     *
     * @param buffer               буфер чьи данные надо перенести.
     * @param destination          буффер в который надо перенести эти данные.
     * @param needClearDestination нужно ли очищать целевой буффер перед переносом в него данных.
     * @return все ли данные были перенесены.
     */
    public static boolean moveAndFlip(final ByteBuffer buffer, final ByteBuffer destination, final boolean needClearDestination) {

        if (needClearDestination) destination.clear();

        final int originalLength = buffer.remaining();
        final int length = min(destination.remaining(), originalLength);

        destination.put(buffer.array(), buffer.position(), length);
        destination.flip();

        return length == originalLength;
    }
}
