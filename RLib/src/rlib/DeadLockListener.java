package rlib;

import java.lang.management.ThreadInfo;

/**
 * Интерфейс для реализации прослушки обнаружений дедлоков.
 * 
 * @author Ronn
 */
public interface DeadLockListener
{
	/**
	 * Уведомление о дедлоке с предоставлением информации о заблоченном потоке.
	 * 
	 * @param info информация об потоке.
	 */
	public void onDetected(ThreadInfo info);
}
