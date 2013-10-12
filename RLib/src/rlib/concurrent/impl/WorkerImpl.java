package rlib.concurrent.impl;

import rlib.concurrent.interfaces.ExtThread;
import rlib.concurrent.interfaces.ExtThreadPoolExecutor;
import rlib.concurrent.interfaces.Task;
import rlib.concurrent.interfaces.Worker;

/**
 * Реализация работника исполнительного пула потоков.
 * 
 * @author Ronn
 * 
 * @param <L> тип контейнера локальных объектов.
 */
public final class WorkerImpl<L> implements Worker<L> {

	/** пул исполнительных потоков */
	private ExtThreadPoolExecutor<L> threadPoolExecutor;
	/** поток, на котором будут выполнятся задачи */
	private ExtThread<L> thread;

	/** первая задача воркера */
	private Task<L> firstTask;

	/** счетчик выполненых задач */
	private volatile long completedTasks;

	/** ожидает ли работник новых задач */
	private boolean waited;

	public WorkerImpl(final ExtThreadPoolExecutor<L> threadPoolExecutor, final Task<L> firstTask) {
		this.firstTask = firstTask;
		this.threadPoolExecutor = threadPoolExecutor;
		this.thread = threadPoolExecutor.getThreadFactory().createTread(this);
	}

	@Override
	public void finalyze() {
		setFirstTask(null);
		setThread(null);
		setThreadPoolExecutor(null);
	}

	@Override
	public long getCompletedTasks() {
		return completedTasks;
	}

	@Override
	public Task<L> getFirstTask() {
		return firstTask;
	}

	@Override
	public ExtThread<L> getThread() {
		return thread;
	}

	public ExtThreadPoolExecutor<L> getThreadPoolExecutor() {
		return threadPoolExecutor;
	}

	@Override
	public void incrementCompleteTask() {
		completedTasks++;
	}

	@Override
	public boolean isWaited() {
		return waited;
	}

	@Override
	public void reinit() {
		completedTasks = 0;
	}

	@Override
	public void run(final L localObjects) {
		getThreadPoolExecutor().runWorker(this, localObjects);
	}

	@Override
	public void setFirstTask(final Task<L> firstTask) {
		this.firstTask = firstTask;
	}

	@Override
	public void setThread(final ExtThread<L> thread) {
		this.thread = thread;
	}

	@Override
	public void setThreadPoolExecutor(final ExtThreadPoolExecutor<L> threadPoolExecutor) {
		this.threadPoolExecutor = threadPoolExecutor;

		if(threadPoolExecutor != null) {
			this.thread = threadPoolExecutor.getThreadFactory().createTread(this);
		}
	}

	@Override
	public void setWaited(final boolean waited) {
		this.waited = waited;
	}
}
