package rlib.concurrent;

import rlib.logging.Logger;
import rlib.logging.Loggers;

/**
 * Набор утильных методов по работе с потоками.
 * 
 * @author Ronn
 */
public class ThreadUtils {

	private static final Logger LOGGER = Loggers.getLogger(ThreadUtils.class);

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch(InterruptedException e) {
			LOGGER.warning(e);
		}
	}
}
