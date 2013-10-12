package rlib.concurrent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadFactory;

import rlib.logging.Loggers;

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
	private Constructor<? extends Thread> constructor;

	/**
	 * @param name название группы потоков.
	 * @param cs класс потока, который будет использоваться.
	 * @param priority приоритет потоков в группе.
	 */
	public GroupThreadFactory(final String name, final Class<? extends Thread> cs, final int priority) {
		this.priority = priority;
		this.name = name;

		try {
			constructor = cs.getConstructor(ThreadGroup.class, Runnable.class, String.class);
		} catch(NoSuchMethodException | SecurityException e) {
			Loggers.warning("GroupThreadFactory", e);
		}

		group = new ThreadGroup(name);
	}

	@Override
	public Thread newThread(final Runnable runnable) {

		Thread thread = null;

		try {
			thread = constructor.newInstance(group, runnable, name + "-" + ordinal++);
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Loggers.warning("GroupThreadFactory", e);
		}

		thread.setPriority(priority);

		return thread;
	}
}
