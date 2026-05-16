package javasabr.rlib.concurrent.executor.impl;

import java.util.Deque;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.collections.deque.DequeFactory;
import javasabr.rlib.common.util.GroupThreadFactory;
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
public class ThreadPoolTaskExecutor<L> implements TaskExecutor<L>, Runnable, Lockable {
  
  Deque<CallableTask<?, L>> waitTasks;
  MutableArray<Thread> threads;

  Function<Thread, L> localObjectsFactory;
  AtomicBoolean wait;
  Lock lock;
  int packetSize;

  public ThreadPoolTaskExecutor(
      GroupThreadFactory threadFactory,
      Function<Thread, L> localObjectsFactory,
      int poolSize,
      int packetSize) {
    this.localObjectsFactory = localObjectsFactory;
    this.waitTasks = DequeFactory.linkedListBased();
    this.wait = new AtomicBoolean();
    this.lock = LockFactory.atomicLock();
    this.threads = ArrayFactory.mutableArray(Thread.class);
    this.packetSize = packetSize;

    for (int i = 0; i < poolSize; i++) {

      Thread thread = threadFactory.newThread(this);
      thread.setDaemon(true);
      thread.start();

      threads.add(thread);
    }
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

    Thread thread = Thread.currentThread();
    MutableArray<CallableTask<?, L>> executeTasks = ArrayFactory.mutableArray(CallableTask.class);
    L local = localObjectsFactory.apply(thread);

    while (true) {
      executeTasks.clear();
      lock();
      try {
        if (waitTasks.isEmpty()) {
          wait.getAndSet(true);
        } else {
          for (int i = 0; i < packetSize && !waitTasks.isEmpty(); i++) {
            executeTasks.add(waitTasks.poll());
          }
        }
      } finally {
        unlock();
      }

      if (executeTasks.isEmpty() && wait.get()) {
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
      } catch (Exception e) {
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
