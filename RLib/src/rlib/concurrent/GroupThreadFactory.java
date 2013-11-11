package rlib.concurrent;

import java.lang.reflect.Constructor;
import java.util.concurrent.ThreadFactory;

import rlib.util.ClassUtil;

/**
 * Модель формирования группы потоков.
 * 
 * @author Ronn
 */
public class GroupThreadFactory implements ThreadFactory {

	/** приоритет потоков */
	private final int priority;
	/** номер следующего потока */
	private int ordinal;

	/** имя группы потоков */
	private final String name;
	/** группа потоков */
	private final ThreadGroup group;

	/** конструктор потока */
	private final Constructor<? extends Thread> constructor;

	/**
	 * @param name название группы потоков.
	 * @param cs класс потока, который будет использоваться.
	 * @param priority приоритет потоков в группе.
	 */
	public GroupThreadFactory(final String name, final Class<? extends Thread> cs, final int priority) {
		this.priority = priority;
		this.name = name;
		this.constructor = ClassUtil.getConstructor(cs, ThreadGroup.class, Runnable.class, String.class);
		this.group = new ThreadGroup(name);
	}

	@Override
	public Thread newThread(final Runnable runnable) {
		Thread thread = ClassUtil.newInstance(constructor, group, runnable, name + "-" + ordinal++);
		thread.setPriority(priority);
		return thread;
	}
}
