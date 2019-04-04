package com.ss.rlib.common.concurrent.deadlock;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The implementation of a deadlock detector.
 *
 * @author JavaSaBr
 */
public class DeadLockDetector implements Runnable {

    private static final Logger LOGGER = LoggerManager.getLogger(DeadLockDetector.class);

    /**
     * The list of listeners.
     */
    @NotNull
    private final ConcurrentArray<DeadLockListener> listeners;

    /**
     * The bean with information about threads.
     */
    @NotNull
    private final ThreadMXBean mxThread;

    /**
     * The scheduler.
     */
    @NotNull
    private final ScheduledExecutorService executorService;

    /**
     * The reference to a task.
     */
    @Nullable
    private volatile ScheduledFuture<?> schedule;

    /**
     * The checking interval.
     */
    private final int interval;

    /**
     * Instantiates a new Dead lock detector.
     *
     * @param interval the checking interval.
     */
    public DeadLockDetector(final int interval) {

        if (interval < 1) {
            throw new IllegalArgumentException("negative interval.");
        }

        this.listeners = ArrayFactory.newConcurrentReentrantRWLockArray(DeadLockListener.class);
        this.mxThread = ManagementFactory.getThreadMXBean();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.interval = interval;
    }

    /**
     * Add a new listener.
     *
     * @param listener the new listener.
     */
    public void addListener(@NotNull final DeadLockListener listener) {
        ArrayUtils.runInWriteLock(listeners, listener, Array::add);
    }

    /**
     * Gets listeners.
     *
     * @return the list of listeners.
     */
    @NotNull
    public ConcurrentArray<DeadLockListener> getListeners() {
        return listeners;
    }

    @Override
    public void run() {

        final long[] threadIds = mxThread.findDeadlockedThreads();
        if (threadIds.length < 1) return;

        final ConcurrentArray<DeadLockListener> listeners = getListeners();

        for (final long id : threadIds) {

            final ThreadInfo info = mxThread.getThreadInfo(id);
            if (listeners.isEmpty()) continue;

            ArrayUtils.runInReadLock(listeners, info,
                    (deadLockListeners, threadInfo) ->
                            deadLockListeners.forEach(threadInfo, DeadLockListener::onDetected));

            LOGGER.warning("DeadLock detected! : " + info);
        }
    }

    /**
     * @return the reference to a task.
     */
    @Nullable
    private ScheduledFuture<?> getSchedule() {
        return schedule;
    }

    /**
     * Start.
     */
    public synchronized void start() {
        if (schedule != null) return;
        schedule = executorService.scheduleAtFixedRate(this, interval, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * Stop.
     */
    public synchronized void stop() {

        final ScheduledFuture<?> schedule = getSchedule();
        if (schedule == null) return;
        schedule.cancel(false);

        this.schedule = null;
    }
}
