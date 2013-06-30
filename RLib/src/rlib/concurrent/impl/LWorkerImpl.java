package rlib.concurrent.impl;

import rlib.concurrent.interfaces.LRunnable;
import rlib.concurrent.interfaces.LThread;
import rlib.concurrent.interfaces.LThreadPoolExecutor;
import rlib.concurrent.interfaces.LWorker;

/**
 * Реализация работника исполнительного пула потоков.
 * 
 * @author Ronn
 *
 * @param <L> тип контейнера локальных объектов.
 */
public final class LWorkerImpl<L> implements LWorker<L>
{
	/** пул исполнительных потоков */
	private LThreadPoolExecutor<L> threadPoolExecutor;
	/** поток, на котором будут выполнятся задачи */
	private LThread<L> thread;
	
	/** первая задача воркера */
	private LRunnable<L> firstTask;
	
	/** счетчик выполненых задач */
	private volatile long completedTasks;

	public LWorkerImpl(LThreadPoolExecutor<L> threadPoolExecutor, LRunnable<L> firstTask)
	{
		this.firstTask = firstTask;
		this.threadPoolExecutor = threadPoolExecutor;
		this.thread = threadPoolExecutor.getThreadFactory().createTread(this);
	}
	
	@Override
	public long getCompletedTasks()
	{
		return completedTasks;
	}

	@Override
	public LRunnable<L> getFirstTask()
	{
		return firstTask;
	}
	
	@Override
	public LThread<L> getThread()
	{
		return thread;
	}
	
	public LThreadPoolExecutor<L> getThreadPoolExecutor()
	{
		return threadPoolExecutor;
	}
	
	@Override
	public void incrementCompleteTask()
	{
		completedTasks++;
	}
	
	@Override
	public void run(L localObjects)
	{
		getThreadPoolExecutor().runWorker(this, localObjects);
	}

	@Override
	public void setFirstTask(LRunnable<L> firstTask)
	{
		this.firstTask = firstTask;
	}

	@Override
	public void finalyze()
	{
		setFirstTask(null);
		setThread(null);
		setThreadPoolExecutor(null);
	}

	@Override
	public void reinit()
	{
		completedTasks = 0;
	}
	
	@Override
	public void setThread(LThread<L> thread)
	{
		this.thread = thread;
	}

	@Override
	public void setThreadPoolExecutor(LThreadPoolExecutor<L> threadPoolExecutor)
	{
		this.threadPoolExecutor = threadPoolExecutor;
		
		if(threadPoolExecutor != null)
			this.thread = threadPoolExecutor.getThreadFactory().createTread(this);
	}	
}
