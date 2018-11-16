package com.ss.rlib.common.monitoring;

import static java.lang.ThreadLocal.withInitial;

import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The class to monitor a runtime state of a java process.
 *
 * @author JavaSaBr
 */
public class RuntimeMonitoring {

    @NotNull
    private static final ThreadLocal<SimpleDateFormat> LOCAL_DATE_FORMAT = withInitial(SimpleDateFormat::new);

    @NotNull
    private static final ThreadLocal<Date> LOCAL_DATE = withInitial(Date::new);

    /**
     * The OS manager.
     */
    @NotNull
    private final OperatingSystemMXBean operatingSystemMXBean;

    /**
     * The runtime manager.
     */
    @NotNull
    private final RuntimeMXBean runtimeMXBean;

    /**
     * Instantiates a new Runtime monitoring.
     */
    public RuntimeMonitoring() {
        this.operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        this.runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    }

    @Override
    public String toString() {

        final Date date = LOCAL_DATE.get();
        date.setTime(runtimeMXBean.getStartTime());

        final SimpleDateFormat format = LOCAL_DATE_FORMAT.get();

        final StringBuilder builder = new StringBuilder();
        builder.append("Operating System:").append('\n');
        builder.append("	").append("OS Name:				").append(operatingSystemMXBean.getName()).append('\n');
        builder.append("	").append("OS Arch:				").append(operatingSystemMXBean.getArch()).append('\n');
        builder.append("	").append("OS Version:				").append(operatingSystemMXBean.getVersion()).append('\n');
        builder.append("	").append("OS System Load:				").append(operatingSystemMXBean.getSystemLoadAverage()).append('\n');
        builder.append("	").append("OS Available Processors:		").append(operatingSystemMXBean.getAvailableProcessors()).append('\n');
        builder.append("Runtime:").append('\n');
        builder.append("	").append("JVM StartTime:				").append(format.format(date)).append('\n');
        builder.append("	").append("JVM UpTime:				").append(runtimeMXBean.getUptime() / 1000 / 60).append(" minuts").append('\n');
        builder.append("	").append("JVM Name:				").append(runtimeMXBean.getVmName()).append('\n');
        builder.append("	").append("JVM Vendor:				").append(runtimeMXBean.getVmVendor()).append('\n');
        builder.append("	").append("JVM Version:				").append(runtimeMXBean.getVmVersion()).append('\n');
        builder.append("	").append("JVM Java Version:			").append(runtimeMXBean.getSpecVersion());

        return builder.toString();
    }
}
