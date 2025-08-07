package javasabr.rlib.common.concurrent.lock.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import javasabr.rlib.common.concurrent.atomic.ReusableAtomicInteger;
import javasabr.rlib.common.concurrent.atomic.ReusableAtomicReference;
import org.jspecify.annotations.NullMarked;

/**
 * The implementation of the {@link Lock} based on using {@link ReusableAtomicInteger} with supporting reentrant calls.
 *
 * @author JavaSaBr
 */
@NullMarked
public class ReentrantAtomicLock implements Lock {

  /**
   * The status of lock.
   */
  private final ReusableAtomicReference<Thread> status;

  /**
   * The level of locking.
   */
  private final ReusableAtomicInteger level;

  /**
   * The field for consuming CPU.
   */
  private int sink;

  /**
   * Instantiates a new Reentrant atomic lock.
   */
  public ReentrantAtomicLock() {
    this.status = new ReusableAtomicReference<>();
    this.level = new ReusableAtomicInteger();
    this.sink = 1;
  }

  @Override
  public void lock() {
    Thread thread = Thread.currentThread();
    try {
      if (status.get() == thread) {
        return;
      }
      while (!status.compareAndSet(null, thread))
        consumeCPU();
    } finally {
      level.incrementAndGet();
    }
  }

  /**
   * Consume cpu.
   */
  protected void consumeCPU() {

    final int value = sink;
    int newValue = value * value;
    newValue += value >>> 1;
    newValue += value & newValue;
    newValue += value ^ newValue;
    newValue += newValue << value;
    newValue += newValue | value;

    sink = newValue;
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
  public boolean tryLock() {

    Thread currentThread = Thread.currentThread();

    if (status.get() == currentThread) {
      level.incrementAndGet();
      return true;
    }

    if (status.compareAndSet(null, currentThread)) {
      level.incrementAndGet();
      return true;
    }

    return false;
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void unlock() {

    final Thread thread = Thread.currentThread();
    if (status.get() != thread) {
      return;
    }

    if (level.decrementAndGet() == 0) {
      status.set(null);
    }
  }
}
