package rlib.concurrent.lock;

/**
 * Интерфейс для реализации блокировщика c синхронной записью и асинхронным
 * чтением.
 *
 * @author Ronn
 */
public interface AsyncReadSyncWriteLock {

    /**
     * Блокировка для ассинхронных действий.
     */
    public void asyncLock();

    /**
     * Убрать блокировку для ассинхронных действий.
     */
    public void asyncUnlock();

    /**
     * Блокировка для синхронных действий.
     */
    public void syncLock();

    /**
     * Убрать блокировку для синхронных действий.
     */
    public void syncUnlock();
}
