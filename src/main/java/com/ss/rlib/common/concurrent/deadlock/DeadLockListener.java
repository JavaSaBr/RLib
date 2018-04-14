package com.ss.rlib.common.concurrent.deadlock;

import org.jetbrains.annotations.NotNull;

import java.lang.management.ThreadInfo;

/**
 * The interface to implement a deadlock listener.
 *
 * @author JavaSaBr
 */
public interface DeadLockListener {

    /**
     * Notify about deadlock detecting.
     *
     * @param info the information about thread.
     */
    void onDetected(@NotNull ThreadInfo info);
}
