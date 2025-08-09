package javasabr.rlib.common.concurrent.lock.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import javasabr.rlib.common.concurrent.atomic.ReusableAtomicInteger;
import javasabr.rlib.common.concurrent.lock.AsyncReadSyncWriteLock;
import org.jspecify.annotations.NullMarked;

/**
 * The implementation of the {@link AsyncReadSyncWriteLock} using the several {@link ReusableAtomicInteger} without supporting
 * reentrant calls.
 *
 * @author JavaSaBr
 */
@NullMarked
public class AtomicReadWriteLock implements AsyncReadSyncWriteLock, Lock {

  private static final int STATUS_WRITE_LOCKED = 1;
  private static final int STATUS_WRITE_UNLOCKED = 0;

  private static final int STATUS_READ_UNLOCKED = 0;
  private static final int STATUS_READ_LOCKED = -200000;

  /**
   * The status of write lock.
   */
  protected final ReusableAtomicInteger writeStatus;

  /**
   * The count of writers.
   */
  protected final ReusableAtomicInteger writeCount;

  /**
   * The count of readers.
   */
  protected final ReusableAtomicInteger readCount;

  /**
   * The field for consuming CPU.
   */
  protected int sink;

  /**
   * Instantiates a new Atomic read write lock.
   */
  public AtomicReadWriteLock() {
    this.writeCount = new ReusableAtomicInteger(0);
    this.writeStatus = new ReusableAtomicInteger(0);
    this.readCount = new ReusableAtomicInteger(0);
    this.sink = 1;
  }

  @Override
  public void asyncLock() {
    while (!tryReadLock())
      consumeCPU();
  }

  @Override
  public void asyncUnlock() {
    readCount.decrementAndGet();
  }

  /**
   * Consume cpu.
   */
  protected void consumeCPU() {

    final int value = sink;
    int newValue = value * value;
    newValue += value >>> 1;
    newValue += value & newValue;
    newValue += value >>> 1;
    newValue += value & newValue;
    newValue += value >>> 1;
    newValue += value & newValue;

    sink = newValue;
  }

  @Override
  public void lock() {
    syncLock();
  }

  @Override
  public void lockInterruptibly() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Condition newCondition() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void syncLock() {
    writeCount.incrementAndGet();
    while (tryToLockReading()) {
      consumeCPU();
    }
    while (tryToLockWriting()) {
      consumeCPU();
    }
  }

  /**
   * Try to lock writing boolean.
   *
   * @return the boolean
   */
  protected boolean tryToLockWriting() {
    return writeStatus.get() != STATUS_WRITE_UNLOCKED || !writeStatus.compareAndSet(
        STATUS_WRITE_UNLOCKED,
        STATUS_WRITE_LOCKED);
  }

  /**
   * Try to lock reading boolean.
   *
   * @return the boolean
   */
  protected boolean tryToLockReading() {
    return readCount.get() != STATUS_READ_UNLOCKED || !readCount.compareAndSet(
        STATUS_READ_UNLOCKED,
        STATUS_READ_LOCKED);
  }

  @Override
  public void syncUnlock() {
    writeStatus.set(STATUS_WRITE_UNLOCKED);
    readCount.set(STATUS_READ_UNLOCKED);
    writeCount.decrementAndGet();
  }

  @Override
  public boolean tryLock() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) {
    throw new UnsupportedOperationException();
  }

  /**
   * Try to get read lock.
   */
  private boolean tryReadLock() {
    if (writeCount.get() != 0) {
      return false;
    }
    int value = readCount.get();
    return value != STATUS_READ_LOCKED && readCount.compareAndSet(value, value + 1);
  }

  @Override
  public void unlock() {
    syncUnlock();
  }

  @Override
  public String toString() {
    return "AtomicReadWriteLock{" + "readCount=" + readCount + ", writeCount=" + writeCount + ", writeStatus="
        + writeStatus + '}';
  }
}
