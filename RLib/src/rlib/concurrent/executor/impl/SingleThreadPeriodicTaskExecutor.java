package rlib.concurrent.executor.impl;

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
import rlib.util.Synchronized;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * Реализация однопоточного исполнителя обновлений задач.
 *
 * @author Ronn
 */
public class SingleThreadPeriodicTaskExecutor<T extends PeriodicTask<L>, L> implements PeriodicTaskExecutor<T, L>, Runnable, Synchronized {

    protected static final Logger LOGGER = LoggerManager.getLogger(SingleThreadPeriodicTaskExecutor.class);

    /**
     * Список ожидающих исполнение задач.
     */
    private final Array<T> waitTasks;

    /**
     * Список задач которые будут исполнены.
     */
    private final Array<T> executeTasks;

    /**
     * Список завершенных задач.
     */
    private final Array<T> finishedTasks;

    /**
     * Поток, в котором происходит исполнение задач.
     */
    private final Thread thread;

    /**
     * Локальные объекты.
     */
    private final L localObjects;

    private final Consumer<T> finishFunc = task -> task.onFinish(getLocalObjects());

    /**
     * Находится ли исполнитель в ожидании.
     */
    private final AtomicBoolean wait;

    /**
     * Блокировщик.
     */
    private final Lock lock;

    /**
     * Интервал обновлений.
     */
    private final int interval;

    public SingleThreadPeriodicTaskExecutor(final Class<? extends Thread> threadClass, final int priority, final int interval, final String name, final Class<?> taskClass, final L localObjects) {
        this.waitTasks = ArrayFactory.newArray(taskClass);
        this.executeTasks = ArrayFactory.newArray(taskClass);
        this.finishedTasks = ArrayFactory.newArray(taskClass);
        this.wait = new AtomicBoolean();
        this.lock = LockFactory.newPrimitiveAtomicLock();
        this.interval = interval;

        final Constructor<Thread> constructor = ClassUtils.getConstructor(threadClass, Runnable.class, String.class);

        this.thread = ClassUtils.newInstance(constructor, this, name);
        this.thread.setPriority(priority);
        this.thread.setDaemon(true);
        this.localObjects = check(localObjects, thread);
        this.thread.start();
    }

    @Override
    public void addTask(final T task) {
        lock();
        try {

            final Array<T> waitTasks = getWaitTasks();
            waitTasks.add(task);

            final AtomicBoolean wait = getWait();

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
     * Проверка и обработка переданного контейнера локальных объектов.
     *
     * @param localObjects контейнер локальных объектов.
     * @param thread       поток, который будет исполнять задачи.
     * @return провереный контейнер.
     */
    protected L check(final L localObjects, final Thread thread) {
        return localObjects;
    }

    /**
     * Реализация обработки исполнения задач, выполняется в безопасной области.
     *
     * @param executeTasks     список задач на исполнение.
     * @param finishedTasks    список зафинишированных задач.
     * @param local            контейнер локальных объектов.
     * @param startExecuteTime время начало исполнения.
     */
    protected void executeImpl(final Array<T> executeTasks, final Array<T> finishedTasks, final L local, final long startExecuteTime) {

        final long currentTime = startExecuteTime;

        for (final T task : executeTasks.array()) {

            if (task == null) {
                break;
            }

            if (task.call(local, currentTime) == Boolean.TRUE) {
                finishedTasks.add(task);
            }
        }
    }

    /**
     * @return список задач которые будут исполнены.
     */
    protected Array<T> getExecuteTasks() {
        return executeTasks;
    }

    /**
     * @return список завершенных задач.
     */
    protected Array<T> getFinishedTasks() {
        return finishedTasks;
    }

    /**
     * @return интервал обновлений.
     */
    public int getInterval() {
        return interval;
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
    protected Array<T> getWaitTasks() {
        return waitTasks;
    }

    @Override
    public void lock() {
        lock.lock();
    }

    /**
     * Выполнение действий после исполнения задач.
     *
     * @param executeTasks     список задач, которые были исполнены, но не обязательно
     *                         финишированы.
     * @param local            контейнер локальных объектов.
     * @param startExecuteTime время начало исполнения задач.
     */
    protected void postExecute(final Array<T> executeTasks, final L local, final long startExecuteTime) {
    }

    /**
     * Выполнение действий перед обновлением.
     *
     * @param executeTasks     список задач, которые будут исполнятся.
     * @param local            контейнер локальных объектов.
     * @param startExecuteTime время начало исполнения задач.
     */
    protected void preExecute(final Array<T> executeTasks, final L local, final long startExecuteTime) {
    }

    @Override
    public void removeTask(final T task) {
        lock();
        try {
            final Array<T> waitTasks = getWaitTasks();
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
        final AtomicBoolean wait = getWait();

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

            if (executeTasks.isEmpty()) {
                continue;
            }

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

                    finishedTasks.forEach(finishFunc);
                }

            } catch (final Exception e) {
                LOGGER.warning(getClass(), e);
            }

            if (interval < 1) {
                continue;
            }

            final int result = interval - (int) (System.currentTimeMillis() - startExecuteTime);

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
