package rlib.concurrent;

import rlib.concurrent.interfaces.Task;
import rlib.concurrent.interfaces.ExtThread;
import rlib.concurrent.interfaces.ExtThreadFactory;
import rlib.util.Copyable;

/**
 * Модель формирования группы потоков.
 * 
 * @author Ronn
 */
public class ExtGroupThreadFactory<L extends Copyable<L>> implements ExtThreadFactory<L> {

	/** группа потоков */
	private final ThreadGroup group;
	/** имя группы потоков */
	private final String name;
	/** контейнер локальных объектов */
	private final L localObjects;

	/** приоритет потоков */
	private final int priority;

	/** номер следующего потока */
	private int ordinal;

	/**
	 * @param localObjects контейнер локальных объектов.
	 * @param name название группы потоков.
	 * @param priority приоритет потоков в группе.
	 */
	public ExtGroupThreadFactory(final L localObjects, final String name, final int priority) {
		this.priority = priority;
		this.name = name;
		this.localObjects = localObjects;
		this.group = new ThreadGroup(name);
	}

	@Override
	public ExtThread<L> createTread(final Task<L> runnable) {
		final ExtThread<L> thread = ExtExecutors.createThread(getGroup(), runnable, getNextLocalObjects(), getNextName());
		thread.setPriority(getPriority());
		return thread;
	}

	/**
	 * @return группа потоков
	 */
	protected ThreadGroup getGroup() {
		return group;
	}

	/**
	 * @return новый контейнер локальных объектов.
	 */
	protected L getNextLocalObjects() {
		return localObjects.copy();
	}

	/**
	 * @return имя для нового потока.
	 */
	protected String getNextName() {
		return name + "-" + ordinal++;
	}

	/**
	 * @return приоритет потока.
	 */
	protected int getPriority() {
		return priority;
	}
}
