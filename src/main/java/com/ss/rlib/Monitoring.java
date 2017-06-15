package com.ss.rlib;

import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;

import com.ss.rlib.util.Utils;

/**
 * The class with method to monitor process state.
 *
 * @author JavaSaBr
 */
public final class Monitoring {

    @NotNull
    private static final Monitoring INSTANCE = new Monitoring();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    @NotNull
    public static Monitoring getInstance() {
        return INSTANCE;
    }

    /**
     * The memory manager.
     */
    @NotNull
    private final MemoryMXBean memoryMXBean;

    /**
     * The OS manager.
     */
    @NotNull
    private final OperatingSystemMXBean operatingSystemMXBean;

    /**
     * The runtime manager.
     */
    @NotNull
    private final RuntimeMXBean runtimeMxBean;

    /**
     * The thread manager.
     */
    @NotNull
    private final ThreadMXBean threadMXBean;

    /**
     * Gets daemon thread count.
     *
     * @return the daemon thread count.
     */
    public int getDaemonThreadCount() {
        return threadMXBean.getDaemonThreadCount();
    }

    /**
     * Gets java version.
     *
     * @return the java version.
     */
    @NotNull
    public String getJavaVersion() {
        return runtimeMxBean.getSpecVersion();
    }

    /**
     * Gets processor count.
     *
     * @return the process count.
     */
    public int getProcessorCount() {
        return operatingSystemMXBean.getAvailableProcessors();
    }

    /**
     * Gets start date.
     *
     * @return the startup date.
     */
    @NotNull
    public String getStartDate() {
        return Utils.formatTime(runtimeMxBean.getStartTime());
    }

    /**
     * Gets start time.
     *
     * @return время старта процесса.
     */
    public long getStartTime() {
        return runtimeMxBean.getStartTime();
    }

    /**
     * Gets system arch.
     *
     * @return the arch of system.
     */
    @NotNull
    public String getSystemArch() {
        return operatingSystemMXBean.getArch();
    }

    /**
     * Gets system load average.
     *
     * @return the system load.
     */
    public double getSystemLoadAverage() {
        return operatingSystemMXBean.getSystemLoadAverage();
    }

    /**
     * Gets system name.
     *
     * @return the system name.
     */
    @NotNull
    public String getSystemName() {
        return operatingSystemMXBean.getName();
    }

    /**
     * Gets system version.
     *
     * @return the system version.
     */
    @NotNull
    public String getSystemVersion() {
        return operatingSystemMXBean.getVersion();
    }

    /**
     * Gets thread count.
     *
     * @return the count of created threads.
     */
    public int getThreadCount() {
        return threadMXBean.getThreadCount();
    }

    /**
     * Gets up time.
     *
     * @return the up time.
     */
    public long getUpTime() {
        return runtimeMxBean.getUptime();
    }

    /**
     * Gets used memory.
     *
     * @return the memory usage (mb).
     */
    public int getUsedMemory() {
        return (int) (memoryMXBean.getHeapMemoryUsage().getUsed() / 1024 / 1024);
    }

    /**
     * Gets vm name.
     *
     * @return the VM name.
     */
    @NotNull
    public String getVMName() {
        return runtimeMxBean.getVmName();
    }

    private Monitoring() {
        memoryMXBean = ManagementFactory.getMemoryMXBean();
        operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        threadMXBean = ManagementFactory.getThreadMXBean();
    }
}
