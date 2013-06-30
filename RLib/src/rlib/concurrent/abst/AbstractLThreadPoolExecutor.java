package rlib.concurrent.abst;

import java.util.concurrent.locks.Lock;

import rlib.concurrent.interfaces.LRunnable;
import rlib.concurrent.interfaces.LThread;
import rlib.concurrent.interfaces.LThreadExceptionHandler;
import rlib.concurrent.interfaces.LThreadFactory;
import rlib.concurrent.interfaces.LThreadPoolExecutor;
import rlib.concurrent.interfaces.LWorker;
import rlib.concurrent.interfaces.LWorkerFactory;
import rlib.util.array.Array;
import rlib.util.array.Arrays;
import rlib.util.linkedlist.LinkedList;

/**
 * @author Ronn
 */
public abstract class AbstractLThreadPoolExecutor<L> extends AbstractLExecutorService<L> implements LThreadPoolExecutor<L>
{
	/** фабрика потоков */
	protected final LThreadFactory<L> threadFactory;
	/** фабрика работников */
	protected final LWorkerFactory<L> workerFactory;
	/** обработчик эксепшенов в потоках */
	protected final LThreadExceptionHandler handler;
	
	/** очередь ожидающих исполнения задач */
	protected final LinkedList<LRunnable<L>> waitTasks;
	/** список исполнителей задач */
	protected final Array<LWorker<L>> workers;

	/** синхронизатор */
	protected final Lock sync;
	
	/** время отклика на поступление новой задачи */
	protected final int timeout;
	/** размер пула потоков */
	protected final int poolSize;
	
	/** кол-во выполненных задач */
	protected volatile long completedTasks;
	
	/** остановлен ли пул потоков */
	protected volatile boolean shutdown;
	
	public AbstractLThreadPoolExecutor(LThreadFactory<L> threadFactory, LWorkerFactory<L> workerFactory, LThreadExceptionHandler handler, LinkedList<LRunnable<L>> waitTasks, Lock sync, int timeout, int poolSize)
	{
		super();
		
		this.threadFactory = threadFactory;
		this.workerFactory = workerFactory;
		this.handler = handler;
		this.waitTasks = waitTasks;
		this.sync = sync;
		this.timeout = timeout;
		this.poolSize = poolSize;
		this.workers = Arrays.toArray(LWorker.class);
	}
	
	@Override
	public void execute(LRunnable<L> runnable)
	{
		Lock sync = getSync();
		
		sync.lock();
		try
		{
			Array<LWorker<L>> workers = getWorkers();
			
			if(workers.size() == getPoolSize())
				addWaitTask(runnable);
			else
				addWorker(runnable);
		}
		finally
		{
			sync.unlock();
		}
	}
	
	protected void addWorker(LRunnable<L> runnable)
	{
		LWorker<L> worker = getWorkerFactory().create(this, runnable);
		LThread<L> thread = worker.getThread();
		
		getWorkers().add(worker);
		
		thread.start();
	}
	
	@Override
	public final boolean isShutdown()
	{
		return shutdown;
	}
	
	@Override
	public final LThreadFactory<L> getThreadFactory()
	{
		return threadFactory;
	}
	
	/**
	 * @return список работников.
	 */
	protected final Array<LWorker<L>> getWorkers()
	{
		return workers;
	}
	
	/**
	 * @return фабрика работников.
	 */
	protected final LWorkerFactory<L> getWorkerFactory()
	{
		return workerFactory;
	}
	
	/**
	 * @return размер пула потоков.
	 */
	protected final int getPoolSize()
	{
		return poolSize;
	}
	
	/**
	 * @return синхронизатор.
	 */
	protected final Lock getSync()
	{
		return sync;
	}
	
	/**
	 * @param runnable ожидающая задача.
	 */
	protected final void addWaitTask(LRunnable<L> runnable)
	{
		waitTasks.add(runnable);
	}
	
	/**
	 * @return время отклика на поступление новой задачи.
	 */
	public final int getTimeout()
	{
		return timeout;
	}
	
	/**
	 * @return список ожидающих задач.
	 */
	public LinkedList<LRunnable<L>> getWaitTasks()
	{
		return waitTasks;
	}
	
	/**
	 * @return ближайшее ожидающее задание.
	 */
	protected LRunnable<L> getWaitTask()
	{
		LinkedList<LRunnable<L>> waitTasks = getWaitTasks();
		
		Lock sync = getSync();
		
		if(!waitTasks.isEmpty())
		{
			sync.lock();
			try
			{
				LRunnable<L> task = waitTasks.poll();
				
				if(task != null)
					return task;
			}
			finally
			{
				sync.unlock();
			}
		}
		
		while(true)
		{
			if(!waitTasks.isEmpty())
			{
				sync.lock();
				try
				{
					LRunnable<L> task = waitTasks.poll();
					
					if(task != null)
						return task;
				}
				finally
				{
					sync.unlock();
				}
			}
			
			try
			{
				Thread.sleep(getTimeout());
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void runWorker(LWorker<L> worker, L localObjects)
	{
		// получаем стартовое задание работника
		LRunnable<L> firstTask = worker.getFirstTask();

		worker.setFirstTask(null);

		if(firstTask == null)
			firstTask = getWaitTask();

		LThread<L> thread = worker.getThread();
		
		Throwable throwable = null;
		
		try
		{
			for(LRunnable<L> task = firstTask; task != null && !isShutdown(); task = getWaitTask())
			{
				try
				{
					Exception exception = null;
					
					beforeExecute(thread, firstTask);
					try
					{
						firstTask.run(localObjects);
					}
					catch(Exception e)
					{
						handler.handle(thread, e);
					}
					catch(Throwable t)
					{
						throwable = t;
						throw t;
					}
					finally
					{
						afterExecute(firstTask, exception);
					}
				}
				finally
				{
					worker.incrementCompleteTask();
				}
			}
		}
		finally
		{
			workerFinish(worker, throwable);
		}
	}

	@Override
	public long getCompletedTasks()
	{
		return completedTasks;
	}
	
	protected void afterExecute(LRunnable<L> firstTask, Exception exception)
	{
		// TODO Auto-generated method stub
	}

	protected void beforeExecute(LThread<L> thread, LRunnable<L> firstTask)
	{
		// TODO Auto-generated method stub
	}
	
	/**
	 * Обработка завершения работника потока.
	 * 
	 * @param worker завершенный работник.
	 * @param throwable ошибка, возникшая при работе.
	 */
	protected void workerFinish(LWorker<L> worker, Throwable throwable)
	{
		Lock sync = getSync();
		
		completedTasks += worker.getCompletedTasks();
		
		sync.lock();
		try
		{
			getWorkers().fastRemove(worker);
			getWorkerFactory().safe(worker);
		}
		finally
		{
			sync.unlock();
		}
	}
	
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	@Override
	public void shutdown()
	{
		this.shutdown = shutdown;
	}
}
