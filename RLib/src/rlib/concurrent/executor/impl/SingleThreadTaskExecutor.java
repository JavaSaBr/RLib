package rlib.concurrent.executor.impl;

import java.lang.reflect.Constructor;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.executor.TaskExecutor;
import rlib.concurrent.lock.LockFactory;
import rlib.concurrent.task.CallableTask;
import rlib.concurrent.task.SimpleTask;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ClassUtils;
import rlib.util.Lockable;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * Реализация однопоточного исполнителя задач.
 *
 * @author JavaSaBr
 */
public class SingleThreadTaskExecutor<L> implements TaskExecutor<L>, Runnable, Lockable {

    protected static final Logger LOGGER = LoggerManager.getLogger(SingleThreadTaskExecutor.class);

    /**
     * Список ожидающих исполнение задач.
     */
    private final Array<CallableTask<?, L>> waitTasks;

    /**
     * Список задач которые будут исполнены.
     */
    private final Array<CallableTask<?, L>> executeTasks;

    /**
     * Поток, в котором происходит исполнение задач.
     */
    private final Thread thread;

    /**
     * Локальные объекты.
     */
    private final L localObjects;

    /**
     * Находится ли исполнитель в ожидании.
     */
    private final AtomicBoolean wait;

    /**
     * Блокировщик.
     */
    private final Lock lock;

    public SingleThreadTaskExecutor(final Class<? extends Thread> threadClass, final int priority, final String name, final L local) {
        this.waitTasks = ArrayFactory.newArray(SimpleTask.class);
        this.executeTasks = ArrayFactory.newArray(SimpleTask.class);
        this.wait = new AtomicBoolean();
        this.lock = LockFactory.newPrimitiveAtomicLock();

        final Constructor<Thread> constructor = ClassUtils.getConstructor(threadClass, Runnable.class, String.class);

        this.thread = ClassUtils.newInstance(constructor, this, name);
        this.thread.setPriority(priority);
        this.thread.setDaemon(true);
        this.localObjects = check(local, thread);
        this.thread.start();
    }

    protected L check(final L local, final Thread thread) {
        return local;
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
     * @return список задач которые будут исполнены.
     */
    protected Array<CallableTask<?, L>> getExecuteTasks() {
        return executeTasks;
    }

    /**
     * @return локальные объекты.
     */
    protected L getLocalObjects() {
        return localObjects;
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
    protected Array<CallableTask<?, L>> getWaitTasks() {
        return waitTasks;
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void run() {

        final Array<CallableTask<?, L>> waitTasks = getWaitTasks();
        final Array<CallableTask<?, L>> executeTasks = getExecuteTasks();

        final L local = getLocalObjects();

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
