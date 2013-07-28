package rlib.concurrent;

/**
 * Интерфейс для реализации блокировщика для синхронной записи и асинхронного
 * чтения.
 * 
 * @author Ronn
 */
public interface AsynReadSynWriteLock
{
	/**
	 * Блокировка для ассинхронных действий.
	 */
	public void asynLock();

	/**
	 * Убрать блокировку для ассинхронных действий.
	 */
	public void asynUnlock();

	/**
	 * Блокировка для синхронных действий.
	 */
	public void synLock();

	/**
	 * Убрать блокировку для синхронных действий.
	 */
	public void synUnlock();
}
