package rlib.util;

import static java.lang.Math.min;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The class with utility methods.
 *
 * @author JavaSaBr
 */
public class BufferUtils {

    /**
     * Move data and flip from a buffer to a destination.
     *
     * @param buffer      the source buffer.
     * @param destination the destination buffer.
     */
    public static void moveAndFlip(@NotNull final ByteBuffer buffer, @NotNull final ByteBuffer destination) {
        destination.put(buffer);
        destination.flip();
    }

    /**
     * Move data and flip from a buffer to a destination.
     *
     * @param buffer      the source buffer.
     * @param destination the destination buffer.
     * @param needClearDestination true if need to clear the destination buffer before moving.
     */
    public static void moveAndFlip(@NotNull final ByteBuffer buffer, @NotNull final ByteBuffer destination,
                                   final boolean needClearDestination) {
        if (needClearDestination) destination.clear();
        moveAndFlip(buffer, destination);
    }

    /**
     * Copy data and flip from a buffer to a destination.
     *
     * @param buffer      the source buffer.
     * @param destination the destination buffer.
     */
    public static void copyAndFlip(@NotNull final ByteBuffer buffer, @NotNull final ByteBuffer destination) {
        destination.put(buffer.array(), buffer.position(), min(destination.remaining(), buffer.remaining()));
        destination.flip();
    }

    /**
     * Copy data from a buffer to a destination.
     *
     * @param buffer      the source buffer.
     * @param destination the destination buffer.
     */
    public static void copy(@NotNull final ByteBuffer buffer, @NotNull final ByteBuffer destination) {
        destination.put(buffer.array(), buffer.position(), min(destination.remaining(), buffer.remaining()));
    }

    /**
     * Copy data and flip from a buffer to a destination.
     *
     * @param buffer      the source buffer.
     * @param destination the destination buffer.
     * @param needClearDestination true if need to clear the destination buffer before copying.
     */
    public static void copyAndFlip(@NotNull final ByteBuffer buffer, @NotNull final ByteBuffer destination,
                                   final boolean needClearDestination) {
        if (needClearDestination) destination.clear();
        copyAndFlip(buffer, destination);
    }

    /**
     * Move data from a buffer to a destination.
     *
     * @param buffer      the source buffer.
     * @param destination the destination buffer.
     */
    public static void move(@NotNull final ByteBuffer buffer, @NotNull final ByteBuffer destination) {
        destination.put(buffer);
    }
}
