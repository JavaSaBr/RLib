package javasabr.rlib.common.concurrent.executor.impl;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Constructor;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import javasabr.rlib.common.concurrent.executor.TaskExecutor;
import javasabr.rlib.common.concurrent.lock.LockFactory;
import javasabr.rlib.common.concurrent.lock.Lockable;
import javasabr.rlib.common.concurrent.task.CallableTask;
import javasabr.rlib.common.concurrent.task.SimpleTask;
import javasabr.rlib.common.concurrent.util.ConcurrentUtils;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The implementation of single thread task executor.
 *
 * @param <L> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public class SingleThreadTaskExecutor<L> implements TaskExecutor<L>, Runnable, Lockable {

  protected static final Logger LOGGER = LoggerManager.getLogger(SingleThreadTaskExecutor.class);

  /**
   * The list of waiting tasks.
   */
  private final Array<CallableTask<?, L>> waitTasks;

  /**
   * The list of executing task.
   */
  private final Array<CallableTask<?, L>> executeTasks;

  /**
   * The executor thread.
   */
  private final Thread thread;

  /**
   * The thread local objects.
   */
  private final L localObjects;

  /**
   * The waiting flag.
   */
  private final AtomicBoolean wait;

  /**
   * The synchronizer.
   */
  private final Lock lock;

  public SingleThreadTaskExecutor(
      Class<? extends Thread> threadClass,
      int priority,
      String name,
      @Nullable L local) {
    this.waitTasks = ArrayFactory.newArray(CallableTask.class);
    this.executeTasks = ArrayFactory.newArray(CallableTask.class);
    this.wait = new AtomicBoolean();
    this.lock = LockFactory.newAtomicLock();

    Constructor<Thread> constructor = ClassUtils.tryGetConstructor(threadClass, Runnable.class, String.class);

    this.thread = ClassUtils.newInstance(requireNonNull(constructor), this, name);
    this.thread.setPriority(priority);
    this.thread.setDaemon(true);
    this.localObjects = check(local, thread);
    this.thread.start();
  }

  /**
   * Check l.
   *
   * @param local the local
   * @param thread the thread
   * @return the l
   */
  protected L check(@Nullable L local, Thread thread) {
    return requireNonNull(local);
  }

  @Override
  public void execute(final SimpleTask<L> task) {
    lock();
    try {

      waitTasks.add(task);

      if (wait.get()) {
        synchronized (wait) {
          if (wait.compareAndSet(true, false)) {
            ConcurrentUtils.notifyAllInSynchronize(wait);
          }
        }
      }

    } finally {
      unlock();
    }
  }

  /**
   * Gets execute tasks.
   *
   * @return the list of executing task.
   */
  protected Array<CallableTask<?, L>> getExecuteTasks() {
    return executeTasks;
  }

  /**
   * Gets local objects.
   *
   * @return the thread local objects.
   */
  protected L getLocalObjects() {
    return localObjects;
  }

  /**
   * Gets wait.
   *
   * @return the waiting flag.
   */
  public AtomicBoolean getWait() {
    return wait;
  }

  /**
   * Gets wait tasks.
   *
   * @return the list of waiting tasks.
   */
  protected Array<CallableTask<?, L>> getWaitTasks() {
    return waitTasks;
  }

  @Override
  public void lock() {
    lock.lock();
  }

  @Override
  public void run() {

    Array<CallableTask<?, L>> waitTasks = getWaitTasks();
    Array<CallableTask<?, L>> executeTasks = getExecuteTasks();

    L local = getLocalObjects();

    while (true) {

      executeTasks.clear();

      lock();
      try {

        if (waitTasks.isEmpty()) {
          wait.getAndSet(true);
        } else {
          executeTasks.addAll(waitTasks);
          waitTasks.clear();
        }

      } finally {
        unlock();
      }

      if (wait.get()) {
        synchronized (wait) {
          if (wait.get()) {
            ConcurrentUtils.waitInSynchronize(wait);
          }
        }
      }

      if (executeTasks.isEmpty()) {
        continue;
      }

      try {

        long currentTime = System.currentTimeMillis();

        for (CallableTask<?, L> task : executeTasks.array()) {
          if (task == null) {
            break;
          }
          task.call(local, currentTime);
        }

      } catch (final Exception e) {
        LOGGER.warning(e);
      }
    }
  }

  @Override
  public <R> Future<R> submit(CallableTask<R, L> task) {
    throw new RuntimeException("not implemented.");
  }

  @Override
  public void unlock() {
    lock.unlock();
  }
}
