package rlib.concurrent.executor.impl;

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
 * Реализация многопоточного пакетного исполнителя задач. Использовать только в случае необходимости
 * выполнять большое кол-во задач с минимальной нагрузкой на GC либо необходимости часто
 * использовать локальные объекты, в остальных случаях рекамендуются {@link Executors} сервисы. Для
 * получение локальных объектов, необходимо переопределить метод {@link #getLocalObjects(Thread)}.
 *
 * @author JavaSaBr
 */
public class ThreadPoolTaskExecutor<L> implements TaskExecutor<L>, Runnable, Lockable {

    protected static final Logger LOGGER = LoggerManager.getLogger(ThreadPoolTaskExecutor.class);

    /**
     * Список ожидающих исполнение задач.
     */
    private final LinkedList<CallableTask<?, L>> waitTasks;

    /**
     * Список задействованных потоков.
     */
    private final Array<Thread> threads;

    /**
     * Находится ли исполнитель в ожидании.
     */
    private final AtomicBoolean wait;

    /**
     * Блокировщик.
     */
    private final Lock lock;

    /**
     * размер пакета выполняемых задач на поток
     */
    private final int packetSize;

    public ThreadPoolTaskExecutor(final GroupThreadFactory threadFactory, final int poolSize, final int packetSize) {
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
     * Получение контейнера локальных объектов для указанного потока.
     *
     * @param thread интересуемый поток.
     * @return контейнер локальных объектов для него.
     */
    protected L getLocalObjects(final Thread thread) {
        return null;
    }

    /**
     * @return размера пакета исполняемых задач.
     */
    public int getPacketSize() {
        return packetSize;
    }

    /**
     * @return находится ли исполнитель в ожидании.
     */
    public AtomicBoolean getWait() {
        return wait;
    }

    /**
     * @return список ожидающих исполнение задач.
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

    @Override
    public <R> Future<R> submit(final CallableTask<R, L> task) {
        throw new RuntimeException("not implemented.");
    }

    @Override
    public void unlock() {
        lock.unlock();
    }
}
