package com.ss.rlib.common.concurrent.deadlock;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import lombok.Getter;
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
    private final @Getter @NotNull ConcurrentArray<DeadLockListener> listeners;

    /**
     * The bean with information about threads.
     */
    private final @NotNull ThreadMXBean mxThread;

    /**
     * The scheduler.
     */
    private final @NotNull ScheduledExecutorService executorService;

    /**
     * The reference to a task.
     */
    private volatile @Getter @Nullable ScheduledFuture<?> schedule;

    /**
     * The checking interval.
     */
    private final int interval;

    public DeadLockDetector(int interval) {

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
    public void addListener(@NotNull DeadLockListener listener) {
        listeners.runInWriteLock(listener, Array::add);
    }

    @Override
    public void run() {

        var threadIds = mxThread.findDeadlockedThreads();

        if (threadIds.length < 1) {
            return;
        }

        var listeners = getListeners();

        for (var id : threadIds) {

            var info = mxThread.getThreadInfo(id);

            if (listeners.isEmpty()) {
                continue;
            }

            listeners.runInReadLock(info, (list, inf) -> list.forEachR(inf, DeadLockListener::onDetected));

            LOGGER.warning("DeadLock detected! : " + info);
        }
    }

    /**
     * Start.
     */
    public synchronized void start() {

        if (schedule != null) {
            return;
        }

        schedule = executorService.scheduleAtFixedRate(this, interval, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * Stop.
     */
    public synchronized void stop() {

        var schedule = getSchedule();

        if (schedule == null) {
            return;
        }

        schedule.cancel(false);

        this.schedule = null;
    }
}
