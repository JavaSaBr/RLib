package rlib.util;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * Интерфейс для реализации безопасно исполняющейся задачи.
 * 
 * @author Ronn
 * @created 22.04.2012
 */
public interface SafeTask extends Runnable {

	@Override
	public default void run() {
		try {
			runImpl();
		} catch(final Exception e) {
			Logger logger = LoggerManager.getDefaultLogger();
			logger.warning(this, e);
		}
	}

	/**
	 * Безопасный исполнение задачи.
	 */
	public void runImpl();
}
