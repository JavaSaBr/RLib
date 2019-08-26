package com.ss.rlib.common.util;

import static java.lang.ThreadLocal.withInitial;
import com.ss.rlib.common.function.SafeSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * The class with utility methods.
 *
 * @author JavaSaBr
 */
public final class IOUtils {

    private static final ThreadLocal<char[]> LOCAL_CHAR_BUFFER = withInitial(() -> new char[1024]);

    /**
     * Close a closeable object.
     *
     * @param closeable the closeable object.
     */
    public static void close(@Nullable Closeable closeable) {

        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Convert the input stream to a string using UTF-8 encoding.
     *
     * @param in the input stream.
     * @return the result string.
     * @throws UncheckedIOException if input stream thrown an io exception.
     */
    public static @NotNull String toString(@NotNull InputStream in) {

        var result = new StringBuilder();

        try (var reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            toString(result, reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return result.toString();
    }

    /**
     * Convert the input stream to a string using UTF-8 encoding.
     *
     * @param inFactory the input stream.
     * @return the result string.
     * @throws UncheckedIOException if input stream thrown an io exception.
     * @throws RuntimeException     if happened something wrong with the supplier.
     */
    public static @NotNull String toString(@NotNull SafeSupplier<InputStream> inFactory) {

        var result = new StringBuilder();

        try (var reader = new InputStreamReader(inFactory.get(), StandardCharsets.UTF_8)) {
            toString(result, reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result.toString();
    }

    private static void toString(@NotNull StringBuilder result, @NotNull InputStreamReader reader) throws IOException {
        for (var buffer = CharBuffer.allocate(100); reader.read(buffer) != -1; ) {
            buffer.flip();
            result.append(buffer.array(), 0, buffer.limit());
        }
    }

    /**
     * Copy data from a source stream to a destination stream.
     *
     * @param in        the source stream.
     * @param out       the destination stream.
     * @param buffer    the buffer.
     * @param needClose true if need to close streams.
     * @throws IOException the io exception
     */
    public static void copy(
        @NotNull InputStream in,
        @NotNull OutputStream out,
        @NotNull byte[] buffer,
        boolean needClose
    ) throws IOException {

        for (int i = in.read(buffer); i != -1; i = in.read(buffer)) {
            out.write(buffer, 0, i);
        }

        if (needClose) {
            close(in);
            close(out);
        }
    }

    /**
     * Read the reader to the result string.
     *
     * @param reader the reader.
     * @return the result string.
     * @throws UncheckedIOException if reader thrown an io exception.
     */
    public static @NotNull String toStringUsingTLB(@NotNull Reader reader) {
        return toString(reader, LOCAL_CHAR_BUFFER.get());
    }

    private static @NotNull String toString(@NotNull Reader reader, @NotNull char[] buffer) {

        var builder = new StringBuilder();
        try {

            for (int read = reader.read(buffer); read != -1; read = reader.read(buffer)) {
                builder.append(buffer, 0, read);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return builder.toString();
    }

    private IOUtils() {
        throw new RuntimeException();
    }
}
