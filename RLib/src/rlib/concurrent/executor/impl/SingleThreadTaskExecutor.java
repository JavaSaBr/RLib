package rlib.concurrent.executor.impl;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.executor.TaskExecutor;
import rlib.concurrent.lock.LockFactory;
import rlib.concurrent.lock.Lockable;
import rlib.concurrent.task.CallableTask;
import rlib.concurrent.task.SimpleTask;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ClassUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * The implementation of single thread task executor.
 *
 * @author JavaSaBr
 */
public class SingleThreadTaskExecutor<L> implements TaskExecutor<L>, Runnable, Lockable {

    protected static final Logger LOGGER = LoggerManager.getLogger(SingleThreadTaskExecutor.class);

    /**
     * The list of waiting tasks.
     */
    @NotNull
    private final Array<CallableTask<?, L>> waitTasks;

    /**
     * The list of executing task.
     */
    @NotNull
    private final Array<CallableTask<?, L>> executeTasks;

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
     * The waiting flag.
     */
    @NotNull
    private final AtomicBoolean wait;

    /**
     * The synchronizer.
     */
    @NotNull
    private final Lock lock;

    public SingleThreadTaskExecutor(@NotNull final Class<? extends Thread> threadClass, final int priority,
                                    @NotNull final String name, @NotNull final L local) {
        this.waitTasks = ArrayFactory.newArray(SimpleTask.class);
        this.executeTasks = ArrayFactory.newArray(SimpleTask.class);
        this.wait = new AtomicBoolean();
        this.lock = LockFactory.newAtomicLock();

        final Constructor<Thread> constructor = ClassUtils.getConstructor(threadClass, Runnable.class, String.class);

        this.thread = ClassUtils.newInstance(constructor, this, name);
        this.thread.setPriority(priority);
        this.thread.setDaemon(true);
        this.localObjects = check(local, thread);
        this.thread.start();
    }

    @NotNull
    protected L check(@NotNull final L local, @NotNull final Thread thread) {
        return local;
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
     * @return the list of executing task.
     */
    @NotNull
    protected Array<CallableTask<?, L>> getExecuteTasks() {
        return executeTasks;
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
