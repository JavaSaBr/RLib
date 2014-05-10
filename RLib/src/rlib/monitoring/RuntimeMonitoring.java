package rlib.monitoring;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

import rlib.util.Util;

/**
 * Реализация мониторинга работы Java процесса и его окружения.
 * 
 * @author Ronn
 */
public class RuntimeMonitoring {

	/** менеджер по анализу работы ОС, на которой запущен Java процесс */
	private final OperatingSystemMXBean operatingSystemMXBean;
	/** менеджер по работе процесса */
	private final RuntimeMXBean runtimeMXBean;

	public RuntimeMonitoring() {
		this.operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		this.runtimeMXBean = ManagementFactory.getRuntimeMXBean();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("Operating System:").append('\n');
		builder.append("	").append("Name:").append(operatingSystemMXBean.getName()).append('\n');
		builder.append("	").append("Arch:").append(operatingSystemMXBean.getArch()).append('\n');
		builder.append("	").append("Version:").append(operatingSystemMXBean.getVersion()).append('\n');
		builder.append("	").append("System Load:").append(operatingSystemMXBean.getSystemLoadAverage()).append('\n');
		builder.append("	").append("Available Processors:").append(operatingSystemMXBean.getAvailableProcessors()).append('\n');
		builder.append("Runtime:").append('\n');
		builder.append("	").append("StartTime:").append(Util.formatTime(runtimeMXBean.getStartTime())).append('\n');
		builder.append("	").append("UpTime:").append(runtimeMXBean.getUptime() / 1000 / 60).append(" minuts").append('\n');
		builder.append("	").append("JVM Name:").append(runtimeMXBean.getVmName()).append('\n');
		builder.append("	").append("JVM Vendor:").append(runtimeMXBean.getVmVendor()).append('\n');
		builder.append("	").append("JVM Version:").append(runtimeMXBean.getVmVersion()).append('\n');
		builder.append("	").append("Java Version:").append(runtimeMXBean.getSpecVersion()).append('\n');

		return builder.toString();
	}

}
