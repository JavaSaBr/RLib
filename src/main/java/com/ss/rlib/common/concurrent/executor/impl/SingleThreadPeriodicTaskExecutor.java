package com.ss.rlib.common.concurrent.executor.impl;

import static java.util.Objects.requireNonNull;
import com.ss.rlib.common.concurrent.executor.PeriodicTaskExecutor;
import com.ss.rlib.common.concurrent.lock.LockFactory;
import com.ss.rlib.common.concurrent.lock.Lockable;
import com.ss.rlib.common.concurrent.task.PeriodicTask;
import com.ss.rlib.common.concurrent.util.ConcurrentUtils;
import com.ss.rlib.common.concurrent.util.ThreadUtils;
import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

/**
 * The implementation of single thread periodic executor.
 *
 * @param <T> the type parameter
 * @param <L> the type parameter
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
    private final Consumer<T> finishFunction =
            task -> task.onFinish(getLocalObjects());

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

    public SingleThreadPeriodicTaskExecutor(
            @NotNull Class<? extends Thread> threadClass,
            int priority,
            int interval,
            @NotNull String name,
            @NotNull Class<? super T> taskClass,
            @Nullable L localObjects
    ) {
        this.waitTasks = ArrayFactory.newArray(taskClass);
        this.executeTasks = ArrayFactory.newArray(taskClass);
        this.finishedTasks = ArrayFactory.newArray(taskClass);
        this.wait = new AtomicBoolean();
        this.lock = LockFactory.newAtomicLock();
        this.interval = interval;

        Constructor<? extends Thread> constructor =
                ClassUtils.getConstructor(threadClass, Runnable.class, String.class);

        this.thread = ClassUtils.newInstance(constructor, this, name);
        this.thread.setPriority(priority);
        this.thread.setDaemon(true);
        this.localObjects = check(localObjects, thread);
        this.thread.start();
    }

    @Override
    public void addTask(@NotNull T task) {
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
     * Check l.
     *
     * @param localObjects the local objects
     * @param thread       the thread
     * @return the l
     */
    protected @NotNull L check(@Nullable L localObjects, @NotNull Thread thread) {
        return requireNonNull(localObjects);
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
     * Gets execute tasks.
     *
     * @return the list of executing tasks.
     */
    @NotNull
    protected Array<T> getExecuteTasks() {
        return executeTasks;
    }

    /**
     * Gets finished tasks.
     *
     * @return the list of finished tasks.
     */
    @NotNull
    protected Array<T> getFinishedTasks() {
        return finishedTasks;
    }

    /**
     * Gets interval.
     *
     * @return the update interval.
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Gets local objects.
     *
     * @return the thread local objects.
     */
    @NotNull
    protected L getLocalObjects() {
        return localObjects;
    }

    /**
     * Gets wait.
     *
     * @return the waiting flag.
     */
    @NotNull
    public AtomicBoolean getWait() {
        return wait;
    }

    /**
     * Gets wait tasks.
     *
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
