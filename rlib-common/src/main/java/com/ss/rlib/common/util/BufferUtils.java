package com.ss.rlib.common.util;

import static java.lang.Math.min;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
@UtilityClass
public class BufferUtils {

    private static final EnumSet<StandardOpenOption> FILE_CHANNEL_OPTS = EnumSet.of(
        StandardOpenOption.READ,
        StandardOpenOption.WRITE,
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.DELETE_ON_CLOSE
    );

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
     * Allocate a new read/write mapped byte buffer.
     *
     * @param size the byte buffer's size.
     * @return the new mapped byte buffer.
     */
    public @NotNull MappedByteBuffer allocateRWMappedByteBuffer(int size) {
        try {

            var tempFile = Files.createTempFile("rlib_common_util_", "_buffer_utils.bin");

            try (var fileChannel = (FileChannel) Files.newByteChannel(tempFile, FILE_CHANNEL_OPTS)) {
                return fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, size);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public @NotNull ByteBuffer putToAndFlip(@NotNull ByteBuffer buffer, @NotNull ByteBuffer additional) {
        return buffer.put(additional)
            .flip();
    }

    /**
     * Move data and flip from a buffer to a destination.
     *
     * @param buffer      the source buffer.
     * @param destination the destination buffer.
     */
    @Deprecated(forRemoval = true)
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
    @Deprecated(forRemoval = true)
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
    @Deprecated(forRemoval = true)
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
    @Deprecated(forRemoval = true)
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
    @Deprecated(forRemoval = true)
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
    @Deprecated(forRemoval = true)
    public static void move(@NotNull final ByteBuffer buffer, @NotNull final ByteBuffer destination) {
        destination.put(buffer);
    }
}
