package com.ss.rlib.common.monitoring;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;

/**
 * The class to monitor memory usage.
 *
 * @author JavaSaBr
 */
public class MemoryMonitoring {

    /**
     * The constant MEMORY_POOL_CODE_CACHE.
     */
    public static final String MEMORY_POOL_CODE_CACHE = "Code Cache";
    /**
     * The constant MEMORY_POOL_COMPRESSED_CLASS_SPACE.
     */
    public static final String MEMORY_POOL_COMPRESSED_CLASS_SPACE = "Compressed Class Space";
    /**
     * The constant MEMORY_POOL_METASPACE.
     */
    public static final String MEMORY_POOL_METASPACE = "Metaspace";
    /**
     * The constant MEMORY_POOL_EDEN_SPACE.
     */
    public static final String MEMORY_POOL_EDEN_SPACE = "PS Eden Space";
    /**
     * The constant MEMORY_POOL_SURVIVOR_SPACE.
     */
    public static final String MEMORY_POOL_SURVIVOR_SPACE = "PS Survivor Space";
    /**
     * The constant MEMORY_POOL_OLD_GEN.
     */
    public static final String MEMORY_POOL_OLD_GEN = "PS Old Gen";

    /**
     * The list of memory pools.
     */
    @NotNull
    private final List<MemoryPoolMXBean> memoryPoolMXBeans;

    /**
     * The memory manager.
     */
    @NotNull
    private final MemoryMXBean memoryMXBean;

    /**
     * Instantiates a new Memory monitoring.
     */
    public MemoryMonitoring() {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
    }

    /**
     * Find a memory pool been by a name.
     *
     * @param name the name.
     * @return the found bean or null.
     */
    @Nullable
    private MemoryPoolMXBean findMemoryPoolMXBeen(@NotNull final String name) {
        return memoryPoolMXBeans.stream()
                .filter(bean -> bean.getName().contains(name))
                .findAny().orElse(null);
    }

    /**
     * Get max heap size.
     *
     * @return the max heap size.
     */
    public long getHeapMaxSize() {
        return getHeapMaxSize(memoryMXBean.getHeapMemoryUsage());
    }

    private long getHeapMaxSize(@NotNull final MemoryUsage memoryUsage) {
        return memoryUsage.getMax();
    }

    /**
     * Get current heap size.
     *
     * @return the current heap size.
     */
    public long getHeapSize() {
        return getHeapSize(memoryMXBean.getHeapMemoryUsage());
    }

    private long getHeapSize(@NotNull final MemoryUsage memoryUsage) {
        return memoryUsage.getCommitted();
    }

    /**
     * Get heap usage percent.
     *
     * @return the heap usage percent.
     */
    public double getHeapUsagePercent() {
        return getHeapUsagePercent(memoryMXBean.getHeapMemoryUsage());
    }

    private double getHeapUsagePercent(@NotNull final MemoryUsage memoryUsage) {
        return getHeapUsedSize(memoryUsage) / (double) getHeapSize(memoryUsage) * 100;
    }

    /**
     * Get heap usage size.
     *
     * @return the heap usage size.
     */
    public long getHeapUsedSize() {
        return getHeapUsedSize(memoryMXBean.getHeapMemoryUsage());
    }

    private long getHeapUsedSize(@NotNull final MemoryUsage memoryUsage) {
        return memoryUsage.getUsed();
    }

    /**
     * Get max size of a memory pool.
     *
     * @param name the name of a memory pool.
     * @return the max size.
     */
    public long getMemoryPoolMaxSize(@NotNull final String name) {

        final MemoryPoolMXBean mxBean = findMemoryPoolMXBeen(name);

        if (mxBean == null) {
            throw new IllegalArgumentException("unknown memory pool name \"" + name + "\".");
        }

        final MemoryUsage usage = mxBean.getUsage();
        return usage.getMax();
    }

    /**
     * Get current size a memory pool.
     *
     * @param name the name of a memory pool.
     * @return the current size.
     */
    public long getMemoryPoolSize(@NotNull final String name) {

        final MemoryPoolMXBean mxBean = findMemoryPoolMXBeen(name);

        if (mxBean == null) {
            throw new IllegalArgumentException("unknown memory pool name \"" + name + "\".");
        }

        final MemoryUsage usage = mxBean.getUsage();
        return usage.getCommitted();
    }

    /**
     * Get used size a memory pool.
     *
     * @param name the name of a memory pool.
     * @return the used size.
     */
    public long getMemoryPoolUsed(@NotNull final String name) {

        final MemoryPoolMXBean mxBean = findMemoryPoolMXBeen(name);

        if (mxBean == null) {
            throw new IllegalArgumentException("unknown memory pool name \"" + name + "\".");
        }

        final MemoryUsage usage = mxBean.getUsage();
        return usage.getUsed();
    }

    @Override
    public String toString() {

        final MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();

        final StringBuilder builder = new StringBuilder();
        builder.append("HeapMaxSize:		").append(getHeapMaxSize(memoryUsage) / 1024 / 1024).append(" MiB.\n");
        builder.append("HeapSize:		").append(getHeapSize(memoryUsage) / 1024 / 1024).append(" MiB.\n");
        builder.append("HeapUsedSize:		").append(getHeapUsedSize(memoryUsage) / 1024 / 1024).append(" MiB.\n");
        builder.append("HeapUsagePercent:	").append(getHeapUsagePercent(memoryUsage)).append("%.");

        if (!memoryPoolMXBeans.isEmpty()) {
            builder.append('\n').append("Memory Pools:");
        }

        for (final MemoryPoolMXBean mxBeen : memoryPoolMXBeans) {

            final MemoryUsage usage = mxBeen.getUsage();

            builder.append('\n').append(mxBeen.getName()).append(":\n");
            builder.append("	").append("MemoryType:	").append(mxBeen.getType()).append('\n');
            builder.append("	").append("MemoryMaxSize:	").append(usage.getMax() / 1024 / 1024).append(" MiB.\n");
            builder.append("	").append("MemorySize:	").append(usage.getCommitted() / 1024 / 1024).append(" MiB.\n");
            builder.append("	").append("MemoryUsed:	").append(usage.getUsed() / 1024 / 1024).append(" MiB.");
        }

        return builder.toString();
    }
}
