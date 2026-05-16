package javasabr.rlib.concurrent.executor.impl;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.concurrent.executor.TaskExecutor;
import javasabr.rlib.concurrent.lock.LockFactory;
import javasabr.rlib.concurrent.lock.Lockable;
import javasabr.rlib.concurrent.task.CallableTask;
import javasabr.rlib.concurrent.task.SimpleTask;
import javasabr.rlib.concurrent.util.ConcurrentUtils;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.experimental.FieldDefaults;

/**
 * @author JavaSaBr
 */
@CustomLog
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class SingleThreadTaskExecutor<L> implements TaskExecutor<L>, Runnable, Lockable {
  
  MutableArray<CallableTask<?, L>> waitTasks;

  Thread thread;
  L local;
  AtomicBoolean wait;
  Lock lock;

  public SingleThreadTaskExecutor(
      int priority,
      String name,
      BiFunction<Runnable, String, Thread> threadFactory,
      L local) {
    this.waitTasks = ArrayFactory.mutableArray(CallableTask.class);
    this.wait = new AtomicBoolean();
    this.lock = LockFactory.atomicLock();
    this.thread = threadFactory.apply(this, name);
    this.thread.setPriority(priority);
    this.thread.setDaemon(true);
    this.local = check(local, thread);
    this.thread.start();
  }

  protected L check(L local, Thread thread) {
    return requireNonNull(local);
  }

  @Override
  public void execute(SimpleTask<L> task) {
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
  public void run() {

    MutableArray<CallableTask<?, L>> executeTasks = ArrayFactory.mutableArray(CallableTask.class);

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
        executeTasks
            .iterations()
            .forEach(local, System.currentTimeMillis(), CallableTask::call);
      } catch (final Exception e) {
        log.warn(e);
      }
    }
  }

  @Override
  public <R> CompletionStage<R> submit(CallableTask<R, L> task) {
    throw new UnsupportedOperationException();
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
