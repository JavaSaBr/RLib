package rlib.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * Набор утильных методов по работе с I/O.
 *
 * @author Ronn
 */
public final class IOUtils {

    private static final Logger LOGGER = LoggerManager.getLogger(IOUtils.class);

    public static void close(final Closeable stream) {
        if (stream == null) return;

        try {
            stream.close();
        } catch (final IOException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * Копирование данных.
     *
     * @param in        поток-источник данных.
     * @param out       поток-место куда копировать.
     * @param buffer    буффер для копирования.
     * @param needClose нужно ли закрыть потоки.
     */
    public static void copy(final InputStream in, final OutputStream out, final byte[] buffer, final boolean needClose) throws IOException {

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
