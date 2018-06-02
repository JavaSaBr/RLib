package com.ss.rlib.common.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The class with utility methods.
 *
 * @author JavaSaBr
 */
public final class IOUtils {

    /**
     * Close a closeable object.
     *
     * @param closeable the closeable object.
     */
    public static void close(@Nullable final Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
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
    public static void copy(@NotNull final InputStream in, @NotNull final OutputStream out, @NotNull final byte[] buffer,
                            final boolean needClose) throws IOException {

        for (int i = in.read(buffer); i != -1; i = in.read(buffer)) {
            out.write(buffer, 0, i);
        }

        if (needClose) {
            close(in);
            close(out);
        }
    }

    private IOUtils() {
        throw new RuntimeException();
    }
}
