package com.ss.rlib.common.concurrent.barrier;

import com.ss.rlib.common.concurrent.barrier.impl.VolatileBasedBarrier;
import com.ss.rlib.common.concurrent.barrier.impl.VolatileBasedBarrier;
import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
public class BarrierFactory {

    /**
     * Creates a new volatile based barrier.
     *
     * @return the new volatile based barrier.
     */
    public static @NotNull Barrier newVolatileBased() {
        return new VolatileBasedBarrier();
    }
}
