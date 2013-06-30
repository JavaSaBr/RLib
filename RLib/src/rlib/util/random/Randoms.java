package rlib.util.random;

import rlib.logging.Loggers;

/**
 * Фарика рандоминайзеров.
 *
 * @author Ronn
 */
public final class Randoms
{
	/**
	 * @return создание быстрого псевдо генератора.
	 */
	public static Random newFastRandom()
	{
		return new FastRandom();
	}

	/**
	 * @return создание медленного реалистичного генератора.
	 */
	public static Random newRealRandom()
	{
		return new RealRandom();
	}

	private Randoms()
	{
		try
		{
			throw new Exception();
		}
		catch(Exception e)
		{
			Loggers.warning(this, e);
		}
	}
}
