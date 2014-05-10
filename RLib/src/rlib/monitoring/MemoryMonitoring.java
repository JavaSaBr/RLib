package rlib.monitoring;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;

/**
 * Реализация мониторинга использования памяти Java процессом, дает информацияю
 * о использовании Java Heap в целом, так и информация о использовании
 * конкретных областей Java Heap и native memory.
 * 
 * @author Ronn
 */
public class MemoryMonitoring {

	public static final String MEMORY_POOL_CODE_CACHE = "Code Cache";
	public static final String MEMORY_POOL_COMPRESSED_CLASS_SPACE = "Compressed Class Space";
	public static final String MEMORY_POOL_METASPACE = "Metaspace";
	public static final String MEMORY_POOL_EDEN_SPACE = "PS Eden Space";
	public static final String MEMORY_POOL_SURVIVOR_SPACE = "PS Survivor Space";
	public static final String MEMORY_POOL_OLD_GEN = "PS Old Gen";

	/** менеджеры работы областей памяти */
	private final List<MemoryPoolMXBean> memoryPoolMXBeens;

	/** менеджер работ общего управления памятью */
	private final MemoryMXBean memoryMXBean;

	public MemoryMonitoring() {
		this.memoryMXBean = ManagementFactory.getMemoryMXBean();
		this.memoryPoolMXBeens = ManagementFactory.getMemoryPoolMXBeans();
	}

	/**
	 * Поиск нужного бина области памяти по его названию.
	 * 
	 * @param name название области памяти.
	 * @return бин этой области.
	 */
	private MemoryPoolMXBean findMemoryPoolMXBeen(final String name) {

		for(final MemoryPoolMXBean mxBean : memoryPoolMXBeens) {
			if(mxBean.getName().contains(name)) {
				return mxBean;
			}
		}

		return null;
	}

	/**
	 * @return максимальный размр Java Heap для текущего Java процесса.
	 */
	public long getHeapMaxSize() {
		return getHeapMaxSize(memoryMXBean.getHeapMemoryUsage());
	}

	private long getHeapMaxSize(final MemoryUsage memoryUsage) {
		return memoryUsage.getMax();
	}

	/**
	 * @return текущий размера Java Heap.
	 */
	public long getHeapSize() {
		return getHeapSize(memoryMXBean.getHeapMemoryUsage());
	}

	private long getHeapSize(final MemoryUsage memoryUsage) {
		return memoryUsage.getCommitted();
	}

	/**
	 * Рассчиттывает сколько было использовано под хранения данных в Java Heap
	 * памяти, от текущего размера Java Heap.
	 * 
	 * @return процент использования Java Heap.
	 */
	public double getHeapUsagePercent() {
		return getHeapUsagePercent(memoryMXBean.getHeapMemoryUsage());
	}

	private double getHeapUsagePercent(final MemoryUsage memoryUsage) {
		return getHeapUsedSize(memoryUsage) / (double) getHeapSize(memoryUsage) * 100;
	}

	/**
	 * @return кол-во используемой памяти для хранения данных из Java Heap.
	 */
	public long getHeapUsedSize() {
		return getHeapUsedSize(memoryMXBean.getHeapMemoryUsage());
	}

	private long getHeapUsedSize(final MemoryUsage memoryUsage) {
		return memoryUsage.getUsed();
	}

	/**
	 * Получение максимального размера области памяти указанного названия.
	 * 
	 * @param name название области памяти.
	 * @return максимальный размер этой области.
	 */
	public long getMemoryPoolMaxSize(final String name) {

		final MemoryPoolMXBean mxBean = findMemoryPoolMXBeen(name);

		if(mxBean == null) {
			throw new IllegalArgumentException("unknown memory pool name \"" + name + "\".");
		}

		final MemoryUsage usage = mxBean.getUsage();

		return usage.getMax();
	}

	/**
	 * Получение текущего размера области памяти указанного названия.
	 * 
	 * @param name название области памяти.
	 * @return текущий размер этой области.
	 */
	public long getMemoryPoolSize(final String name) {

		final MemoryPoolMXBean mxBean = findMemoryPoolMXBeen(name);

		if(mxBean == null) {
			throw new IllegalArgumentException("unknown memory pool name \"" + name + "\".");
		}

		final MemoryUsage usage = mxBean.getUsage();

		return usage.getCommitted();
	}

	/**
	 * Получение использованного размера области памяти указанного названия.
	 * 
	 * @param name название области памяти.
	 * @return использованный размер этой области.
	 */
	public long getMemoryPoolUsed(final String name) {

		final MemoryPoolMXBean mxBean = findMemoryPoolMXBeen(name);

		if(mxBean == null) {
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

		if(!memoryPoolMXBeens.isEmpty()) {
			builder.append('\n').append("Memory Pools:");
		}

		for(final MemoryPoolMXBean mxBeen : memoryPoolMXBeens) {

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
