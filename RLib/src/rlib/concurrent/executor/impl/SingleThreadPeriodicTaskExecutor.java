package rlib.concurrent.executor.impl;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

import rlib.concurrent.executor.PeriodicTaskExecutor;
import rlib.concurrent.lock.LockFactory;
import rlib.concurrent.task.PeriodicTask;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.concurrent.util.ThreadUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ClassUtils;
import rlib.util.Lockable;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * The implementation of single thread periodic executor.
 *
 * @author JavaSaBr
 */
public class SingleThreadPeriodicTaskExecutor<T extends PeriodicTask<L>, L> implements PeriodicTaskExecutor<T, L>, Runnable, Lockable {

    protected static final Logger LOGGER = LoggerManager.getLogger(SingleThreadPeriodicTaskExecutor.class);

    /**
     * The list of waiting tasks.
     */
    @NotNull
    private final Array<T> waitTasks;

    /**
     * The list of executing tasks.
     */
    @NotNull
    private final Array<T> executeTasks;

    /**
     * The list of finished tasks.
     */
    @NotNull
    private final Array<T> finishedTasks;

    /**
     * The executor thread.
     */
    @NotNull
    private final Thread thread;

    /**
     * The thread local objects.
     */
    @NotNull
    private final L localObjects;

    /**
     * The finishing function.
     */
    @NotNull
    private final Consumer<T> finishFunction = task -> task.onFinish(getLocalObjects());

    /**
     * The waiting flag.
     */
    @NotNull
    private final AtomicBoolean wait;

    /**
     * The synchronizator.
     */
    @NotNull
    private final Lock lock;

    /**
     * The update interval.
     */
    private final int interval;

    public SingleThreadPeriodicTaskExecutor(@NotNull final Class<? extends Thread> threadClass, final int priority,
                                            final int interval, @NotNull final String name,
                                            final Class<?> taskClass, @NotNull final L localObjects) {
        this.waitTasks = ArrayFactory.newArray(taskClass);
        this.executeTasks = ArrayFactory.newArray(taskClass);
        this.finishedTasks = ArrayFactory.newArray(taskClass);
        this.wait = new AtomicBoolean();
        this.lock = LockFactory.newAtomicLock();
        this.interval = interval;

        final Constructor<Thread> constructor = ClassUtils.getConstructor(threadClass, Runnable.class, String.class);

        this.thread = ClassUtils.newInstance(constructor, this, name);
        this.thread.setPriority(priority);
        this.thread.setDaemon(true);
        this.localObjects = check(localObjects, thread);
        this.thread.start();
    }

    @Override
    public void addTask(@NotNull final T task) {
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

    @NotNull
    protected L check(@NotNull final L localObjects, @NotNull final Thread thread) {
        return localObjects;
    }

    /**
     * Execute tasks.
     *
     * @param executeTasks     the execute tasks.
     * @param finishedTasks    the finished tasks.
     * @param local            the thread local objects.
     * @param startExecuteTime the start time.
     */
    protected void executeImpl(@NotNull final Array<T> executeTasks, @NotNull final Array<T> finishedTasks,
                               @NotNull final L local, final long startExecuteTime) {
        for (final T task : executeTasks.array()) {
            if (task == null) break;
            if (task.call(local, startExecuteTime) == Boolean.TRUE) {
                finishedTasks.add(task);
            }
        }
    }

    /**
     * @return the list of executing tasks.
     */
    @NotNull
    protected Array<T> getExecuteTasks() {
        return executeTasks;
    }

    /**
     * @return the list of finished tasks.
     */
    @NotNull
    protected Array<T> getFinishedTasks() {
        return finishedTasks;
    }

    /**
     * @return the update interval.
     */
    public int getInterval() {
        return interval;
    }

    /**
     * @return the thread local objects.
     */
    @NotNull
    protected L getLocalObjects() {
        return localObjects;
    }

    /**
     * @return the waiting flag.
     */
    @NotNull
    public AtomicBoolean getWait() {
        return wait;
    }

    /**
     * @return the list of waiting tasks.
     */
    @NotNull
    protected Array<T> getWaitTasks() {
        return waitTasks;
    }

    @Override
    public void lock() {
        lock.lock();
    }

    /**
     * Handle tasks after executing.
     *
     * @param executedTasks    the list of executed tasks.
     * @param local            the thread local objects.
     * @param startExecuteTime the start executing time.
     */
    protected void postExecute(@NotNull final Array<T> executedTasks, @NotNull final L local, final long startExecuteTime) {
    }

    /**
     * Handle tasks before executing.
     *
     * @param executeTasks     the list of execute tasks.
     * @param local            the thread local objects.
     * @param startExecuteTime the start executing time.
     */
    protected void preExecute(@NotNull final Array<T> executeTasks, @NotNull final L local, final long startExecuteTime) {
    }

    @Override
    public void removeTask(@NotNull final T task) {
        lock();
        try {
            waitTasks.fastRemove(task);
        } finally {
            unlock();
        }
    }

    @Override
    public void run() {

        final Array<T> waitTasks = getWaitTasks();
        final Array<T> executeTasks = getExecuteTasks();
        final Array<T> finishedTasks = getFinishedTasks();

        final L local = getLocalObjects();

        final int interval = getInterval();

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

            if (executeTasks.isEmpty()) continue;

            final long startExecuteTime = System.currentTimeMillis();

            preExecute(executeTasks, local, startExecuteTime);
            try {
                executeImpl(executeTasks, finishedTasks, local, startExecuteTime);
            } catch (final Exception e) {
                LOGGER.warning(getClass(), e);
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

            } catch (final Exception e) {
                LOGGER.warning(getClass(), e);
            }

            if (interval < 1) continue;
            final int result = interval - (int) (System.currentTimeMillis() - startExecuteTime);
            if (result < 1) continue;

            ThreadUtils.sleep(result);
        }
    }

    @Override
    public void unlock() {
        lock.unlock();
    }
}
