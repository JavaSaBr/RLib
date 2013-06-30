package rlib.concurrent;

import rlib.concurrent.interfaces.LRunnable;
import rlib.concurrent.interfaces.LThread;
import rlib.concurrent.interfaces.LThreadFactory;
import rlib.util.Copyable;

/**
 * Модель формирования группы потоков.
 * 
 * @author Ronn
 */
public class LGroupThreadFactory<L extends Copyable<L>> implements LThreadFactory<L>
{
	/** группа потоков */
	private final ThreadGroup group;
	/** имя группы потоков */
	private final String name;
	/** контейнер локальных объектов */
	private final L localObjects;
	
	/** приоритет потоков */
	private int priority;
	/** номер следующего потока */
	private int ordinal;
	
	/**
	 * @param localObjects контейнер локальных объектов.
	 * @param name название группы потоков.
	 * @param priority приоритет потоков в группе.
	 */
	public LGroupThreadFactory(L localObjects, String name, int priority)
	{
		this.priority = priority;
		this.name = name;
		this.localObjects = localObjects;
		this.group = new ThreadGroup(name);
	}

	protected ThreadGroup getGroup()
	{
		return group;
	}
	
	protected L getNextLocalObjects()
	{
		return localObjects.copy();
	}
	
	protected int getPriority()
	{
		return priority;
	}
	
	@Override
	public LThread<L> createTread(LRunnable<L> runnable)
	{
		LThread<L> thread = LExecutors.createThread(getGroup(), runnable, getNextLocalObjects(), getNextName());
		
		thread.setPriority(getPriority());
		
		return thread;
	}
	
	protected String getNextName()
	{
		return name + "-" + ordinal++;
	}
}
