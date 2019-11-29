package com.ss.rlib.common.concurrent.executor.impl;

import static java.util.Objects.requireNonNull;
import com.ss.rlib.common.concurrent.executor.PeriodicTaskExecutor;
import com.ss.rlib.common.concurrent.lock.LockFactory;
import com.ss.rlib.common.concurrent.lock.Lockable;
import com.ss.rlib.common.concurrent.task.PeriodicTask;
import com.ss.rlib.common.concurrent.util.ConcurrentUtils;
import com.ss.rlib.common.concurrent.util.ThreadUtils;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

/**
 * The implementation of single thread periodic executor.
 *
 * @author JavaSaBr
 */
@Getter(AccessLevel.PROTECTED)
public class SingleThreadPeriodicTaskExecutor<T extends PeriodicTask<L>, L> implements PeriodicTaskExecutor<T, L>,
    Runnable, Lockable {

    protected static final Logger LOGGER = LoggerManager.getLogger(SingleThreadPeriodicTaskExecutor.class);

    /**
     * The list of waiting tasks.
     */
    private final @NotNull Array<T> waitTasks;

    /**
     * The list of executing tasks.
     */
    private final @NotNull Array<T> executeTasks;

    /**
     * The list of finished tasks.
     */
    private final @NotNull Array<T> finishedTasks;

    /**
     * The executor thread.
     */
    private final @NotNull Thread thread;

    /**
     * The thread local objects.
     */
    private final @NotNull L localObjects;

    /**
     * The finishing function.
     */
    private final @NotNull Consumer<T> finishFunction = task -> task.onFinish(getLocalObjects());

    /**
     * The waiting flag.
     */
    private final @NotNull AtomicBoolean wait;

    /**
     * The synchronizator.
     */
    private final @NotNull Lock lock;

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
    protected void executeImpl(
        @NotNull Array<T> executeTasks,
        @NotNull Array<T> finishedTasks,
        @NotNull L local,
        long startExecuteTime
    ) {
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
     * @param executedTasks    the list of executed tasks.
     * @param local            the thread local objects.
     * @param startExecuteTime the start executing time.
     */
    protected void postExecute(@NotNull Array<T> executedTasks, @NotNull L local, long startExecuteTime) {
    }

    /**
     * Handle tasks before executing.
     *
     * @param executeTasks     the list of execute tasks.
     * @param local            the thread local objects.
     * @param startExecuteTime the start executing time.
     */
    protected void preExecute(@NotNull Array<T> executeTasks, @NotNull L local, long startExecuteTime) {
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
