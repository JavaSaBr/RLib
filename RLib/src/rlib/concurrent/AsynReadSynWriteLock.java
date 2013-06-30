package rlib.concurrent;

/**
 * Интерфейс для реализации блокировщика для синхронной записи и асинхронного чтения.
 * 
 * @author Ronn
 */
public interface AsynReadSynWriteLock
{
	/**
	 * Блокировать запись для чтения.
	 */
	public void readLock();
	
	/**
	 * Разблокировать запись для чтения.
	 */
	public void readUnlock();
	
	/**
	 * Блокировать чтения для записи.
	 */
	public void writeLock();
	
	/**
	 * Разблокировать чтение для записи.
	 */
	public void writeUnlock();
}
