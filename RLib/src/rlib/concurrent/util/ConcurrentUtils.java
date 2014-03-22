package rlib.concurrent.util;

import rlib.logging.Logger;
import rlib.logging.Loggers;

/**
 * Набор утильных методов для работы в сфере кокнуренции.
 * 
 * @author Ronn
 */
public final class ConcurrentUtils {

	private static final Logger LOGGER = Loggers.getLogger(ConcurrentUtils.class);

	/**
	 * Отпускание ожидающих потоков на этом объекте.
	 */
	public static void notifyAll(Object object) {
		synchronized(object) {
			object.notifyAll();
		}
	}

	/**
	 * Отпускание ожидающих потоков на этом объекте.
	 */
	public static void notifyAllInSynchronize(Object object) {
		object.notifyAll();
	}

	/**
	 * Отпускание ожидающих потоков на этом объекте и становится самому в
	 * ожидание.
	 */
	public static void notifyAndWait(Object object) {
		synchronized(object) {
			notifyAllInSynchronize(object);
			waitInSynchronize(object);
		}
	}

	/**
	 * Ождивать на этом объекте.
	 */
	public static void wait(Object object) {
		synchronized(object) {
			try {
				object.wait();
			} catch(InterruptedException e) {
				LOGGER.warning(e);
			}
		}
	}

	/**
	 * Ожидать определнное время на этом объекте.
	 */
	public static void wait(Object object, long time) {
		synchronized(object) {
			try {
				object.wait(time);
			} catch(InterruptedException e) {
				LOGGER.warning(e);
			}
		}
	}

	/**
	 * Ождивать на этом объекте.
	 */
	public static void waitInSynchronize(Object object) {
		try {
			object.wait();
		} catch(InterruptedException e) {
			LOGGER.warning(e);
		}
	}

	private ConcurrentUtils() {
		throw new RuntimeException();
	}
}
