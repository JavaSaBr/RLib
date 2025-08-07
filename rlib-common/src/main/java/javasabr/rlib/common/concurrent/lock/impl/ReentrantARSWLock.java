package javasabr.rlib.common.concurrent.lock.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javasabr.rlib.common.concurrent.lock.AsyncReadSyncWriteLock;
import javasabr.rlib.common.concurrent.lock.LockFactory;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * The wrapper of {@link ReentrantReadWriteLock} for implementing the interface {@link AsyncReadSyncWriteLock}*.
 *
 * @author JavaSaBr
 */
@NullMarked
public final class ReentrantARSWLock implements AsyncReadSyncWriteLock {

  /**
   * The locker of writing.
   */
  private final Lock readLock;

  /**
   * The locker of reading.
   */
  private final Lock writeLock;

  /**
   * Instantiates a new Reentrant arsw lock.
   */
  public ReentrantARSWLock() {
    ReadWriteLock readWriteLock = LockFactory.newReentrantRWLock();
    readLock = readWriteLock.readLock();
    writeLock = readWriteLock.writeLock();
  }

  @Override
  public void asyncLock() {
    readLock.lock();
  }

  @Override
  public void asyncUnlock() {
    readLock.unlock();
  }

  @Override
  public void syncLock() {
    writeLock.lock();
  }

  @Override
  public void syncUnlock() {
    writeLock.unlock();
  }
}
