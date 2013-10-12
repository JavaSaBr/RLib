package rlib;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.text.DateFormat;
import java.util.Date;

/**
 * Модель мониторинга состояния процесса.
 * 
 * @author Ronn
 * @created 24.04.2012
 */
public abstract class Monitoring {

	/** менеджер распределения памяти */
	private static MemoryMXBean memoryMXBean;
	/** менеджер ОС на которой запущена программа */
	private static OperatingSystemMXBean operatingSystemMXBean;
	/** менеджер запущенного процесса */
	private static RuntimeMXBean runtimeMxBean;
	/** менеджер потоков */
	private static ThreadMXBean threadMXBean;

	/**
	 * @return кол-во демон потоков.
	 */
	public static final int getDeamonThreadCount() {
		return threadMXBean.getDaemonThreadCount();
	}

	/**
	 * @return версия java, на которой запущен процесс.
	 */
	public static final String getJavaVersion() {
		return runtimeMxBean.getSpecVersion();
	}

	/**
	 * @return кол-во доступных процессорных ядер
	 */
	public static final int getProcessorCount() {
		return operatingSystemMXBean.getAvailableProcessors();
	}

	/**
	 * @return дата старта процесса.
	 */
	public static final String getStartDate() {
		return DateFormat.getInstance().format(new Date(runtimeMxBean.getStartTime()));
	}

	/**
	 * @return время старта процесса.
	 */
	public static final long getStartTime() {
		return runtimeMxBean.getStartTime();
	}

	/**
	 * @return архитектуру системы, на которой запущен процесс.
	 */
	public static final String getSystemArch() {
		return operatingSystemMXBean.getArch();
	}

	/**
	 * @return процентную нагрузку процесса на систему.
	 */
	public static final double getSystemLoadAverage() {
		return operatingSystemMXBean.getSystemLoadAverage();
	}

	/**
	 * @return название системы.
	 */
	public static final String getSystemName() {
		return operatingSystemMXBean.getName();
	}

	/**
	 * @return версию системы.
	 */
	public static final String getSystemVersion() {
		return operatingSystemMXBean.getVersion();
	}

	/**
	 * @return кол-во созданных потоков.
	 */
	public static final int getThreadCount() {
		return threadMXBean.getThreadCount();
	}

	/**
	 * @return сколько уже милисекунд работает процесс.
	 */
	public static final long getUpTime() {
		return runtimeMxBean.getUptime();
	}

	/**
	 * @return кол-во использованных МБ оперативной памяти.
	 */
	public static final int getUsedMemory() {
		return (int) (memoryMXBean.getHeapMemoryUsage().getUsed() / 1024 / 1024);
	}

	/**
	 * @return название виртуальной машины.
	 */
	public static final String getVMName() {
		return runtimeMxBean.getVmName();
	}

	/**
	 * Иницилазиация.
	 */
	public static final void init() {
		memoryMXBean = ManagementFactory.getMemoryMXBean();
		operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		threadMXBean = ManagementFactory.getThreadMXBean();
	}
}
