package com.ss.rlib.common.monitoring;

import org.jetbrains.annotations.NotNull;

/**
 * The class to monitor a state a Java process.
 *
 * @author JavaSaBr
 */
public final class MonitoringManager {

    @NotNull
    private static final MonitoringManager INSTANCE = new MonitoringManager();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    @NotNull
    public static MonitoringManager getInstance() {
        return INSTANCE;
    }

    /**
     * The memory monitoring.
     */
    @NotNull
    private final MemoryMonitoring memoryMonitoring;

    /**
     * The runtime monitoring.
     */
    @NotNull
    private final RuntimeMonitoring runtimeMonitoring;

    /**
     * The threads monitoring.
     */
    @NotNull
    private final ThreadMonitoring threadMonitoring;

    private MonitoringManager() {
        this.memoryMonitoring = new MemoryMonitoring();
        this.runtimeMonitoring = new RuntimeMonitoring();
        this.threadMonitoring = new ThreadMonitoring();
    }

    /**
     * Gets memory monitoring.
     *
     * @return the memory monitoring.
     */
    @NotNull
    public MemoryMonitoring getMemoryMonitoring() {
        return memoryMonitoring;
    }

    /**
     * Gets runtime monitoring.
     *
     * @return the runtime monitoring.
     */
    @NotNull
    public RuntimeMonitoring getRuntimeMonitoring() {
        return runtimeMonitoring;
    }

    /**
     * Gets thread monitoring.
     *
     * @return the threads monitoring.
     */
    @NotNull
    public ThreadMonitoring getThreadMonitoring() {
        return threadMonitoring;
    }

    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder("\n");
        builder.append("#==========Java Process Info==========#").append('\n');
        builder.append("#=============Memory Info=============#");
        builder.append('\n').append(getMemoryMonitoring()).append('\n');
        builder.append("#============Runtime Info=============#");
        builder.append('\n').append(getRuntimeMonitoring()).append('\n');
        // builder.append("#============Current state============#");
        // builder.append('\n').append(getThreadMonitoring()).append('\n');
        builder.append("#=====================================#");

        return builder.toString();
    }
}
