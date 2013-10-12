package rlib.util;

import rlib.logging.Loggers;

/**
 * Класс для создания безопасного таска
 * 
 * @author Ronn
 * @created 22.04.2012
 */
public abstract class SafeTask implements Runnable {

	@Override
	public void run() {
		try {
			runImpl();
		} catch(final Exception e) {
			Loggers.warning(this, e);
		}
	}

	/**
	 * Безопасный запуск таска.
	 */
	protected abstract void runImpl();
}
