package javasabr.rlib.common.concurrent.executor.impl;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import javasabr.rlib.common.concurrent.executor.PeriodicTaskExecutor;
import javasabr.rlib.common.concurrent.lock.LockFactory;
import javasabr.rlib.common.concurrent.lock.Lockable;
import javasabr.rlib.common.concurrent.task.PeriodicTask;
import javasabr.rlib.common.concurrent.util.ConcurrentUtils;
import javasabr.rlib.common.concurrent.util.ThreadUtils;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import lombok.AccessLevel;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The implementation of single thread periodic executor.
 *
 * @author JavaSaBr
 */
@NullMarked
@Getter(AccessLevel.PROTECTED)
public class SingleThreadPeriodicTaskExecutor<T extends PeriodicTask<L>, L> implements PeriodicTaskExecutor<T, L>,
    Runnable, Lockable {

  protected static final Logger LOGGER = LoggerManager.getLogger(SingleThreadPeriodicTaskExecutor.class);

  /**
   * The list of waiting tasks.
   */
  private final Array<T> waitTasks;

  /**
   * The list of executing tasks.
   */
  private final Array<T> executeTasks;

  /**
   * The list of finished tasks.
   */
  private final Array<T> finishedTasks;

  /**
   * The executor thread.
   */
  private final Thread thread;

  /**
   * The thread local objects.
   */
  private final L localObjects;

  /**
   * The finishing function.
   */
  private final Consumer<T> finishFunction = task -> task.onFinish(getLocalObjects());

  /**
   * The waiting flag.
   */
  private final AtomicBoolean wait;

  /**
   * The synchronizator.
   */
  private final Lock lock;

  /**
   * The update interval.
   */
  private final int interval;

  public SingleThreadPeriodicTaskExecutor(
      Class<? extends Thread> threadClass,
      int priority,
      int interval,
      String name,
      Class<? super T> taskClass,
      @Nullable L localObjects) {
    this.waitTasks = ArrayFactory.newArray(taskClass);
    this.executeTasks = ArrayFactory.newArray(taskClass);
    this.finishedTasks = ArrayFactory.newArray(taskClass);
    this.wait = new AtomicBoolean();
    this.lock = LockFactory.newAtomicLock();
    this.interval = interval;

    Constructor<? extends Thread> constructor = ClassUtils.getConstructor(threadClass, Runnable.class, String.class);

    this.thread = ClassUtils.newInstance(constructor, this, name);
    this.thread.setPriority(priority);
    this.thread.setDaemon(true);
    this.localObjects = check(localObjects, thread);
    this.thread.start();
  }

  @Override
  public void addTask(T task) {
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

  protected L check(@Nullable L localObjects, Thread thread) {
    return requireNonNull(localObjects);
  }

  /**
   * Execute tasks.
   *
   * @param executeTasks the execute tasks.
   * @param finishedTasks the finished tasks.
   * @param local the thread local objects.
   * @param startExecuteTime the start time.
   */
  protected void executeImpl(Array<T> executeTasks, Array<T> finishedTasks, L local, long startExecuteTime) {
    for (var task : executeTasks.array()) {

      if (task == null) {
        break;
      }

      if (task.call(local, startExecuteTime) == Boolean.TRUE) {
        finishedTasks.add(task);
      }
    }
  }

  /**
   * @return the update interval.
   */
  public int getInterval() {
    return interval;
  }

  @Override
  public void lock() {
    lock.lock();
  }

  /**
   * Handle tasks after executing.
   *
   * @param executedTasks the list of executed tasks.
   * @param local the thread local objects.
   * @param startExecuteTime the start executing time.
   */
  protected void postExecute(Array<T> executedTasks, L local, long startExecuteTime) {
  }

  /**
   * Handle tasks before executing.
   *
   * @param executeTasks the list of execute tasks.
   * @param local the thread local objects.
   * @param startExecuteTime the start executing time.
   */
  protected void preExecute(Array<T> executeTasks, L local, long startExecuteTime) {
  }

  @Override
  public void removeTask(T task) {
    lock();
    try {
      waitTasks.fastRemove(task);
    } finally {
      unlock();
    }
  }

  @Override
  public void run() {

    var waitTasks = getWaitTasks();
    var executeTasks = getExecuteTasks();
    var finishedTasks = getFinishedTasks();

    var local = getLocalObjects();
    var interval = getInterval();

    while (true) {

      executeTasks.clear();
      finishedTasks.clear();

      lock();
      try {

        if (waitTasks.isEmpty()) {
          wait.getAndSet(true);
        } else {
          executeTasks.addAll(waitTasks);
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

      var startExecuteTime = System.currentTimeMillis();

      preExecute(executeTasks, local, startExecuteTime);
      try {
        executeImpl(executeTasks, finishedTasks, local, startExecuteTime);
      } catch (Exception exc) {
        LOGGER.warning(exc);
      } finally {
        postExecute(executeTasks, local, startExecuteTime);
      }

      try {

        if (!finishedTasks.isEmpty()) {
          lock();
          try {
            waitTasks.removeAll(finishedTasks);
          } finally {
            unlock();
          }

          finishedTasks.forEach(finishFunction);
        }

      } catch (Exception exc) {
        LOGGER.warning(exc);
      }

      if (interval < 1) {
        continue;
      }

      var result = interval - (int) (System.currentTimeMillis() - startExecuteTime);

      if (result < 1) {
        continue;
      }

      ThreadUtils.sleep(result);
    }
  }

  @Override
  public void unlock() {
    lock.unlock();
  }
}
