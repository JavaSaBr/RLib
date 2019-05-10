package com.ss.rlib.common.util;

import static java.lang.Math.min;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
@UtilityClass
public class BufferUtils {

    /**
     * Append an additional buffer to a source buffer.
     *
     * @param source     the source buffer to which need append data.
     * @param additional the additional buffer with appended data.
     */
    public @NotNull ByteBuffer append(@NotNull ByteBuffer source, @NotNull ByteBuffer additional) {
        var prevPos = source.position();
        source.limit(source.capacity());
        source.put(additional);
        source.limit(source.position());
        source.position(prevPos);
        return source;
    }

    /**
     * Load a part of data from a source buffer to a target buffer which can be to get into the target buffer's size.
     *
     * @param target the target buffer.
     * @param source the source buffer
     * @return the target buffer.
     */
    public @NotNull ByteBuffer loadFrom(@NotNull ByteBuffer source, @NotNull ByteBuffer target) {

        var prevLimit = source.limit();
        try {

            target.clear();

            var skip = source.remaining() - target.remaining();

            if (skip > 0) {
                source.limit(prevLimit - skip);
            }

            target.put(source);
            target.flip();

        } finally {
            source.limit(prevLimit);
        }

        return target;
    }

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
     * @param buffer               the source buffer.
     * @param destination          the destination buffer.
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
     * @param buffer               the source buffer.
     * @param destination          the destination buffer.
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
