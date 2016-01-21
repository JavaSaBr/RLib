package rlib.util;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;

import java.io.Closeable;
import java.io.IOException;

/**
 * Набор утильных методов по работе с I/O.
 * 
 * @author Ronn
 */
public final class IOUtils {

	private static final Logger LOGGER = LoggerManager.getLogger(IOUtils.class);

	public static final void close(final Closeable stream) {

        if(stream == null) {
            return;
        }

		try {
			stream.close();
		} catch(final IOException e) {
			LOGGER.warning(e);
		}
	}

	private IOUtils() {
		throw new RuntimeException();
	}
}
