package javasabr.rlib.common.concurrent.executor.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import javasabr.rlib.common.concurrent.GroupThreadFactory;
import javasabr.rlib.common.concurrent.executor.TaskExecutor;
import javasabr.rlib.common.concurrent.lock.LockFactory;
import javasabr.rlib.common.concurrent.lock.Lockable;
import javasabr.rlib.common.concurrent.task.CallableTask;
import javasabr.rlib.common.concurrent.task.SimpleTask;
import javasabr.rlib.common.concurrent.util.ConcurrentUtils;
import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.common.util.linkedlist.LinkedList;
import javasabr.rlib.common.util.linkedlist.LinkedListFactory;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import org.jspecify.annotations.NullMarked;

/**
 * Реализация многопоточного пакетного исполнителя задач. Использовать только в случае необходимости выполнять большое
 * кол-во задач с минимальной нагрузкой на GC либо необходимости часто использовать локальные объекты, в остальных
 * случаях рекамендуются {@link Executors} сервисы. Для получение локальных объектов, необходимо переопределить метод
 * {@link #getLocalObjects(Thread)}.
 *
 * @param <L> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public class ThreadPoolTaskExecutor<L> implements TaskExecutor<L>, Runnable, Lockable {

  /**
   * The constant LOGGER.
   */
  protected static final Logger LOGGER = LoggerManager.getLogger(ThreadPoolTaskExecutor.class);

  /**
   * The list of waiting tasks.
   */
  private final LinkedList<CallableTask<?, L>> waitTasks;

  /**
   * The list of working threads.
   */
  private final Array<Thread> threads;

  /**
   * The waiting flag.
   */
  private final AtomicBoolean wait;

  /**
   * The synchronizer.
   */
  private final Lock lock;

  /**
   * The count of executing tasks per thread.
   */
  private final int packetSize;

  /**
   * Instantiates a new Thread pool task executor.
   *
   * @param threadFactory the thread factory
   * @param poolSize the pool size
   * @param packetSize the packet size
   */
  public ThreadPoolTaskExecutor(
      GroupThreadFactory threadFactory,
      int poolSize,
      int packetSize) {
    this.waitTasks = LinkedListFactory.newLinkedList(CallableTask.class);
    this.wait = new AtomicBoolean();
    this.lock = LockFactory.newAtomicLock();
    this.threads = ArrayFactory.newArray(Thread.class);
    this.packetSize = packetSize;

    for (int i = 0; i < poolSize; i++) {

      final Thread thread = threadFactory.newThread(this);
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

  /**
   * Get a local object container.
   *
   * @param thread the thread.
   * @return the local object container of the thread.
   */
  protected L getLocalObjects(Thread thread) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets packet size.
   *
   * @return the count of executing tasks per thread.
   */
  public int getPacketSize() {
    return packetSize;
  }

  /**
   * Gets wait.
   *
   * @return the waiting flag.
   */
  protected AtomicBoolean getWait() {
    return wait;
  }

  /**
   * Gets wait tasks.
   *
   * @return the list of waiting tasks.
   */
  protected LinkedList<CallableTask<?, L>> getWaitTasks() {
    return waitTasks;
  }

  @Override
  public void lock() {
    lock.lock();
  }

  @Override
  public void run() {

    Thread thread = Thread.currentThread();

    LinkedList<CallableTask<?, L>> waitTasks = getWaitTasks();
    Array<CallableTask<?, L>> executeTasks = ArrayFactory.newArray(CallableTask.class);

    L local = getLocalObjects(thread);
    int packetSize = getPacketSize();

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

        long currentTime = System.currentTimeMillis();

        for (CallableTask<?, L> task : executeTasks.array()) {
          if (task == null) {
            break;
          }
          task.call(local, currentTime);
        }

      } catch (Exception e) {
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
