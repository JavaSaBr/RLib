package rlib.concurrent.interfaces;

import rlib.util.pools.Foldable;

/**
 * Интерфейс для реализации воркера исполнительного пула потоков.
 * 
 * @author Ronn
 */
public interface Worker<L> extends Task<L>, Foldable {

	/**
	 * @return кол-во выполненных задач.
	 */
	public long getCompletedTasks();

	/**
	 * @return стартовая задача воркера.
	 */
	public Task<L> getFirstTask();

	/**
	 * @return поток, который используется этим воркером.
	 */
	public ExtThread<L> getThread();

	/**
	 * Увеличение счетчика выполненных задач.
	 */
	public void incrementCompleteTask();

	/**
	 * @return ожидает ли он новых задач.
	 */
	public boolean isWaited();

	/**
	 * @param firstTask стартовая задача воркера.
	 */
	public void setFirstTask(Task<L> firstTask);

	/**
	 * @param thread поток, в котором идет работа.
	 */
	public void setThread(ExtThread<L> thread);

	/**
	 * @param threadPoolExecutor пул потоков, в котром идет работа.
	 */
	public void setThreadPoolExecutor(ExtThreadPoolExecutor<L> threadPoolExecutor);

	/**
	 * @param waited ожидает ли он новых задач.
	 */
	public void setWaited(boolean waited);
}
