package com.ss.rlib.common.concurrent.barrier.impl;

import com.ss.rlib.common.concurrent.barrier.Barrier;

/**
 * The simple implementation of a barrier using volatile field.
 *
 * @author JavaSaBr
 */
public class VolatileBasedBarrier implements Barrier {

    /**
     * The synchronization action.
     */
    private volatile int barrier;

    /**
     * The sink.
     */
    private int sink;

    @Override
    public void loadChanges() {
        this.sink = barrier + 1;
    }

    @Override
    public void commitChanges() {
        this.barrier = sink + 1;
    }
}
