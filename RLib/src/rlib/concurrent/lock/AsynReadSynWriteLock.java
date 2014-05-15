package rlib.concurrent.lock;

/**
 * Интерфейс для реализации блокировщика c синхронной записью и асинхронным
 * чтением.
 * 
 * @author Ronn
 */
public interface AsynReadSynWriteLock {

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
