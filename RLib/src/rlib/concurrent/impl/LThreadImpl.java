package rlib.concurrent.impl;

import rlib.concurrent.interfaces.LRunnable;
import rlib.concurrent.interfaces.LThread;

/**
 * Реализация потока с контейнером локальных объектов.
 * 
 * @author Ronn
 */
public final class LThreadImpl<L> extends Thread implements LThread<L>, LRunnable<L>
{
	/** контейнер локальных объектов */
	private L localObjects;
	
	/** задача потока */
	private LRunnable<L> task;
	
	public LThreadImpl(ThreadGroup group, LRunnable<L> task, L localObjects, String name)
	{
		super(group, null, name);
		
		setLocalObjects(localObjects);
		setTask(task);
	}
	
	public LThreadImpl(LRunnable<L> task, L localObjects, String name)
	{
		super(null, null, name);
		
		setLocalObjects(localObjects);
		setTask(task);
	}
	
	@Override
	public void run()
	{
		run(getLocalObjects());
	}
	
	@Override
	public void run(L localObjects)
	{
		LRunnable<L> task = getTask();
		
		if(task != null)
			task.run(localObjects);
	}
	
	private void setTask(LRunnable<L> task)
	{
		this.task = task;
	}
	
	private void setLocalObjects(L localObjects)
	{
		this.localObjects = localObjects;
	}
	
	private LRunnable<L> getTask()
	{
		return task;
	}

	@Override
	public L getLocalObjects()
	{
		return localObjects;
	}

	@Override
	public Thread getThread()
	{
		return this;
	}
}
