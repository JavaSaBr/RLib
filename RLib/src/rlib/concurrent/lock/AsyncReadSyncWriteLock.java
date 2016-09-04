package rlib.concurrent.lock;

/**
 * The interface for implementing async reading and sync writing lock.
 *
 * @author JavaSaBr
 */
public interface AsyncReadSyncWriteLock {

    /**
     * Lock any writing for reading.
     */
    public void asyncLock();

    /**
     * Finish this reading and unlock any writing if it is last reading.
     */
    public void asyncUnlock();

    /**
     * Lock any reading for writing.
     */
    public void syncLock();

    /**
     * Finish this writing and unlock any readings.
     */
    public void syncUnlock();
}
