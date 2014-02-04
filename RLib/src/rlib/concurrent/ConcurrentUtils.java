package rlib.concurrent;

import rlib.logging.Logger;
import rlib.logging.Loggers;

/**
 * Набор утильных методов для работы в сфере кокнуренции.
 * 
 * @author Ronn
 */
public final class ConcurrentUtils {

	private static final Logger LOGGER = Loggers.getLogger(ConcurrentUtils.class);

	private ConcurrentUtils() {
		throw new RuntimeException();
	}

	public static void wait(Object object) {
		synchronized(object) {
			try {
				object.wait();
			} catch(InterruptedException e) {
				LOGGER.warning(e);
			}
		}
	}

	public static void wait(Object object, long time) {
		synchronized(object) {
			try {
				object.wait(time);
			} catch(InterruptedException e) {
				LOGGER.warning(e);
			}
		}
	}

	public static void waitInSynchronize(Object object) {
		try {
			object.wait();
		} catch(InterruptedException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * Отпускание ожидающих потоков на этом объекте.
	 * 
	 * @param object
	 */
	public static void notifyAll(Object object) {
		synchronized(object) {
			object.notifyAll();
		}
	}

	/**
	 * Отпускание ожидающих потоков на этом объекте.
	 * 
	 * @param object
	 */
	public static void notifyAllInSynchronize(Object object) {
		object.notifyAll();
	}

	public static void notifyAndWait(Object object) {
		synchronized(object) {
			notifyAllInSynchronize(object);
			waitInSynchronize(object);
		}
	}
}
