package javasabr.rlib.concurrent.executor.impl;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.common.util.ThreadUtils;
import javasabr.rlib.concurrent.executor.PeriodicTaskExecutor;
import javasabr.rlib.concurrent.lock.LockFactory;
import javasabr.rlib.concurrent.lock.Lockable;
import javasabr.rlib.concurrent.task.PeriodicTask;
import javasabr.rlib.concurrent.util.ConcurrentUtils;
import javasabr.rlib.functions.LongObjConsumer;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@CustomLog
@Accessors(fluent = true, chain = false)
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class SingleThreadPeriodicTaskExecutor<T extends PeriodicTask<L>, L> implements
    PeriodicTaskExecutor<T, L>, Runnable, Lockable {
  
  MutableArray<T> waitTasks;
  MutableArray<T> executeTasks;
  MutableArray<T> finishedTasks;

  Thread thread;
  L local;
  LongObjConsumer<T> finishFunction;

  AtomicBoolean wait;
  Lock lock;
  int interval;

  public SingleThreadPeriodicTaskExecutor(
      int priority,
      int interval,
      String name,
      BiFunction<Runnable, String, Thread> threadFactory,
      Class<? super T> taskClass,
      L local) {
    this.waitTasks = ArrayFactory.mutableArray(taskClass);
    this.executeTasks = ArrayFactory.mutableArray(taskClass);
    this.finishedTasks = ArrayFactory.mutableArray(taskClass);
    this.wait = new AtomicBoolean();
    this.lock = LockFactory.atomicLock();
    this.interval = interval;
    this.thread = threadFactory.apply(this, name);
    this.thread.setPriority(priority);
    this.thread.setDaemon(true);
    this.local = check(local, thread);
    this.thread.start();
    this.finishFunction = (cycleTime, task) -> task.onFinish(local, cycleTime);
  }

  @Override
  public void registerTask(T task) {
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

  @Override
  public void registerTasks(Array<T> tasks) {
    lock();
    try {
      waitTasks.addAll(tasks);
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

  @Override
  public void registerTasks(Collection<T> tasks) {
    lock();
    try {
      waitTasks.addAll(tasks);
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

  protected L check(L localObjects, Thread thread) {
    return requireNonNull(localObjects);
  }

  protected void executeImpl(
      Array<T> executeTasks,
      MutableArray<T> finishedTasks,
      L local,
      long startExecuteTime) {

    @Nullable T[] unsafe = executeTasks
        .asUnsafe()
        .wrapped();

    for (int i = 0, length = executeTasks.size(); i < length; i++) {
      T task = unsafe[i];
      if (task.call(local, startExecuteTime)) {
        finishedTasks.add(task);
      }
    }
  }

  protected void postExecute(Array<T> executedTasks, L local, long startExecuteTime) {}
  protected void preExecute(Array<T> executeTasks, L local, long startExecuteTime) {}

  @Override
  public void unregisterTask(T task) {
    lock();
    try {
      waitTasks.remove(task);
    } finally {
      unlock();
    }
  }

  @Override
  public void run() {

    MutableArray<T> tasksToExecute = ArrayFactory.mutableArray(waitTasks.type());
    MutableArray<T> finishedTasks = ArrayFactory.mutableArray(waitTasks.type());

    while (true) {
      tasksToExecute.clear();
      finishedTasks.clear();
      lock();
      try {
        if (waitTasks.isEmpty()) {
          wait.getAndSet(true);
        } else {
          tasksToExecute.addAll(waitTasks);
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

      if (tasksToExecute.isEmpty()) {
        continue;
      }

      long startExecuteTime = System.currentTimeMillis();

      preExecute(tasksToExecute, local, startExecuteTime);
      try {
        executeImpl(tasksToExecute, finishedTasks, local, startExecuteTime);
      } catch (Exception exc) {
        log.warn(exc);
      } finally {
        postExecute(tasksToExecute, local, startExecuteTime);
      }

      try {

        if (!finishedTasks.isEmpty()) {
          lock();
          try {
            waitTasks.removeAll(finishedTasks);
          } finally {
            unlock();
          }
          finishedTasks
              .iterations()
              .forEach(local, startExecuteTime, PeriodicTask::onFinish);
        }

      } catch (Exception exc) {
        log.warn(exc);
      }

      if (interval < 1) {
        continue;
      }

      int result = interval - (int) (System.currentTimeMillis() - startExecuteTime);
      if (result < 1) {
        continue;
      }

      ThreadUtils.sleep(result);
    }
  }

  @Override
  public void lock() {
    lock.lock();
  }

  @Override
  public void unlock() {
    lock.unlock();
  }
}
