package rlib.util;

import rlib.logging.Loggers;

/**
 * Класс для создания безопасного руннейбл таска
 *
 * @author Ronn
 * @created 22.04.2012
 */
public abstract class SafeTask implements Runnable
{
	@Override
	public void run()
	{
		try
		{
			runImpl();
		}
		catch(Exception e)
		{
			Loggers.warning(this, e);
		}
	}
	
	/**
	 * Безопасный запуск таска.
	 */
	protected abstract void runImpl();
}
