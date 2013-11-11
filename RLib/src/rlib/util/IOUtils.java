package rlib.util;

import java.io.Closeable;
import java.io.IOException;

import rlib.logging.Logger;
import rlib.logging.Loggers;

/**
 * Набор утильных методов по работе с i/o.
 * 
 * @author Ronn
 */
public final class IOUtils {

	private static final Logger LOGGER = Loggers.getLogger(IOUtils.class);

	private IOUtils() {
		throw new RuntimeException();
	}

	public static final void close(Closeable stream) {
		try {
			stream.close();
		} catch(IOException e) {
			LOGGER.warning(e);
		}
	}
}
