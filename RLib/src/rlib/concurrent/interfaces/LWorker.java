package rlib.concurrent.interfaces;

import rlib.util.pools.Foldable;

/**
 * Интерфейс для реализации воркера исполнительного пула потоков.
 * 
 * @author Ronn
 */
public interface LWorker<L> extends LRunnable<L>, Foldable
{
	/**
	 * @return стартовая задача воркера.
	 */
	public LRunnable<L> getFirstTask();
	
	/**
	 * @param firstTask стартовая задача воркера.
	 */
	public void setFirstTask(LRunnable<L> firstTask);
	
	/**
	 * Увеличение счетчика выполненных задач.
	 */
	public void incrementCompleteTask();
	
	/**
	 * @return поток, который используется этим воркером.
	 */
	public LThread<L> getThread();
	
	/**
	 * @return кол-во выполненных задач.
	 */
	public long getCompletedTasks();
	
	/**
	 * @param thread поток, в котором идет работа.
	 */
	public void setThread(LThread<L> thread);
	
	/**
	 * @param threadPoolExecutor пул потоков, в котром идет работа.
	 */
	public void setThreadPoolExecutor(LThreadPoolExecutor<L> threadPoolExecutor);
}
