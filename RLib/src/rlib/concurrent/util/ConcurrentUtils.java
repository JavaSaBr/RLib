package rlib.concurrent.util;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * Набор утильных методов для работы в сфере кокнуренции.
 * 
 * @author Ronn
 */
public final class ConcurrentUtils {

	private static final Logger LOGGER = LoggerManager.getLogger(ConcurrentUtils.class);

	/**
	 * Отпускание ожидающих потоков на этом объекте.
	 */
	public static void notifyAll(final Object object) {
		synchronized(object) {
			object.notifyAll();
		}
	}

	/**
	 * Отпускание ожидающих потоков на этом объекте.
	 */
	public static void notifyAllInSynchronize(final Object object) {
		object.notifyAll();
	}

	/**
	 * Отпускание ожидающих потоков на этом объекте и становится самому в
	 * ожидание.
	 */
	public static void notifyAndWait(final Object object) {
		synchronized(object) {
			notifyAllInSynchronize(object);
			waitInSynchronize(object);
		}
	}

	/**
	 * Ождивать на этом объекте.
	 */
	public static void wait(final Object object) {
		synchronized(object) {
			try {
				object.wait();
			} catch(final InterruptedException e) {
				LOGGER.warning(e);
			}
		}
	}

	/**
	 * Ожидать определнное время на этом объекте.
	 */
	public static void wait(final Object object, final long time) {
		synchronized(object) {
			try {
				object.wait(time);
			} catch(final InterruptedException e) {
				LOGGER.warning(e);
			}
		}
	}

	/**
	 * Ождивать на этом объекте.
	 */
	public static void waitInSynchronize(final Object object) {
		try {
			object.wait();
		} catch(final InterruptedException e) {
			LOGGER.warning(e);
		}
	}

	private ConcurrentUtils() {
		throw new RuntimeException();
	}
}
