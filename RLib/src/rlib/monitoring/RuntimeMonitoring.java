package rlib.monitoring;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Реализация мониторинга работы Java процесса и его окружения.
 *
 * @author Ronn
 */
public class RuntimeMonitoring {

    private static final ThreadLocal<SimpleDateFormat> LOCAL_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };

    private static final ThreadLocal<Date> LOCAL_DATE = new ThreadLocal<Date>() {

        @Override
        protected Date initialValue() {
            return new Date();
        }
    };

    /**
     * Менеджер по анализу работы ОС, на которой запущен Java процесс.
     */
    private final OperatingSystemMXBean operatingSystemMXBean;

    /**
     * Менеджер по работе процесса.
     */
    private final RuntimeMXBean runtimeMXBean;

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
