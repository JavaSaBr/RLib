package rlib.concurrent.executor.impl;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.GroupThreadFactory;
import rlib.concurrent.executor.TaskExecutor;
import rlib.concurrent.lock.LockFactory;
import rlib.concurrent.task.CallableTask;
import rlib.concurrent.task.SimpleTask;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.Lockable;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.linkedlist.LinkedList;
import rlib.util.linkedlist.LinkedListFactory;

/**
 * Реализация многопоточного пакетного исполнителя задач. Использовать только в случае необходимости выполнять большое
 * кол-во задач с минимальной нагрузкой на GC либо необходимости часто использовать локальные объекты, в остальных
 * случаях рекамендуются {@link Executors} сервисы. Для получение локальных объектов, необходимо переопределить метод
 * {@link #getLocalObjects(Thread)}.
 *
 * @author JavaSaBr
 */
public class ThreadPoolTaskExecutor<L> implements TaskExecutor<L>, Runnable, Lockable {

    protected static final Logger LOGGER = LoggerManager.getLogger(ThreadPoolTaskExecutor.class);

    /**
     * The list of waiting tasks.
     */
    @NotNull
    private final LinkedList<CallableTask<?, L>> waitTasks;

    /**
     * The list of working threads.
     */
    @NotNull
    private final Array<Thread> threads;

    /**
     * The waiting flag.
     */
    @NotNull
    private final AtomicBoolean wait;

    /**
     * The synchronizer.
     */
    @NotNull
    private final Lock lock;

    /**
     * The count of executing tasks per thread.
     */
    private final int packetSize;

    public ThreadPoolTaskExecutor(@NotNull final GroupThreadFactory threadFactory, final int poolSize, final int packetSize) {
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
    public void execute(@NotNull final SimpleTask<L> task) {
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
    @NotNull
    protected L getLocalObjects(@NotNull final Thread thread) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the count of executing tasks per thread.
     */
    public int getPacketSize() {
        return packetSize;
    }

    /**
     * @return the waiting flag.
     */
    @NotNull
    protected AtomicBoolean getWait() {
        return wait;
    }

    /**
     * @return the list of waiting tasks.
     */
    @NotNull
    protected LinkedList<CallableTask<?, L>> getWaitTasks() {
        return waitTasks;
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void run() {

        final Thread thread = Thread.currentThread();

        final LinkedList<CallableTask<?, L>> waitTasks = getWaitTasks();
        final Array<CallableTask<?, L>> executeTasks = ArrayFactory.newArray(CallableTask.class);

        final L local = getLocalObjects(thread);
        final int packetSize = getPacketSize();

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

            if (executeTasks.isEmpty()) continue;
            try {

                final long currentTime = System.currentTimeMillis();

                for (final CallableTask<?, L> task : executeTasks.array()) {
                    if (task == null) break;
                    task.call(local, currentTime);
                }

            } catch (final Exception e) {
                LOGGER.warning(e);
            }
        }
    }

    @NotNull
    @Override
    public <R> Future<R> submit(@NotNull final CallableTask<R, L> task) {
        throw new RuntimeException("not implemented.");
    }

    @Override
    public void unlock() {
        lock.unlock();
    }
}
