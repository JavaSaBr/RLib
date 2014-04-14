package rlib.concurrent.util;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * Набор утильных методов по работе с потоками.
 * 
 * @author Ronn
 */
public class ThreadUtils {

	private static final Logger LOGGER = LoggerManager.getLogger(ThreadUtils.class);

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch(InterruptedException e) {
			LOGGER.warning(e);
		}
	}
}
