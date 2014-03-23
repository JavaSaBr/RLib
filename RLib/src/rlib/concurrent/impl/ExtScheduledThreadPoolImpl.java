package rlib.concurrent.impl;

import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.abst.AbstractExtThreadPoolExecutor;
import rlib.concurrent.interfaces.CallableTask;
import rlib.concurrent.interfaces.ExtScheduledExecutorService;
import rlib.concurrent.interfaces.ExtScheduledFuture;
import rlib.concurrent.interfaces.ExtThread;
import rlib.concurrent.interfaces.ExtThreadExceptionHandler;
import rlib.concurrent.interfaces.ExtThreadFactory;
import rlib.concurrent.interfaces.Task;
import rlib.concurrent.interfaces.Worker;
import rlib.concurrent.interfaces.WorkerFactory;
import rlib.util.linkedlist.LinkedList;
import rlib.util.linkedlist.LinkedListFactory;

/**
 * Реализация сервиса по исполнению отложенных задач.
 * 
 * @author Ronn
 */
public class ExtScheduledThreadPoolImpl<L> extends AbstractExtThreadPoolExecutor<L> implements ExtScheduledExecutorService<L> {

	/** список запланированных задач */
	private final LinkedList<Delayed> scheduledTasks;

	public ExtScheduledThreadPoolImpl(ExtThreadFactory<L> threadFactory, WorkerFactory<L> workerFactory, ExtThreadExceptionHandler handler, LinkedList<Task<L>> waitTasks, Lock sync, int poolSize) {
		super(threadFactory, workerFactory, handler, waitTasks, sync, poolSize);
		this.scheduledTasks = LinkedListFactory.newSortedLinkedList(ExtScheduledFuture.class);
	}

	protected void execute(ExtScheduledFuture<L, ?> scheduledTask) {

	}

	/**
	 * @return ближайшая отложенная задача.
	 */
	protected Delayed getDelayedTask() {

		LinkedList<Delayed> delayedTasks = getScheduledTasks();

		if(delayedTasks.isEmpty()) {
			return null;
		}

		return delayedTasks.getFirst();
	}

	/**
	 * @return ближайшая отложенная задача.
	 */
	@SuppressWarnings("unchecked")
	protected Task<L> getScheduledTask() {

		final LinkedList<Delayed> scheduledTasks = getScheduledTasks();

		if(scheduledTasks.isEmpty()) {
			return null;
		}

		return (Task<L>) scheduledTasks.poll();
	}

	/**
	 * @return список отложенных задач.
	 */
	protected LinkedList<Delayed> getScheduledTasks() {
		return scheduledTasks;
	}

	@Override
	public void runWorker(Worker<L> worker, L localObjects) {

		Task<L> firstTask = worker.getFirstTask();
		worker.setFirstTask(null);

		final ExtThreadExceptionHandler handler = getHandler();
		final ExtThread<L> thread = worker.getThread();

		final Lock sync = getSync();

		Throwable throwable = null;

		try {

			while(!isShutdown()) {

				if(firstTask == null) {
					firstTask = getWaitTask(true);
				}

				// выполняем доступные обычные задачи
				for(Task<L> task = firstTask; task != null; task = getWaitTask(true)) {

					Exception exception = null;

					try {

						beforeExecute(thread, task);
						try {
							task.run(localObjects);
						} catch(final Exception e) {
							handler.handle(thread, e);
							exception = e;
						} catch(final Throwable t) {
							throwable = t;
							throw t;
						} finally {
							afterExecute(task, exception);
						}

					} finally {
						worker.incrementCompleteTask();
					}
				}

				long wait = 0;

				// выполняем доступные отложенные задачи
				for(Delayed delayed = getDelayedTask(); delayed != null; delayed = getDelayedTask()) {

					long delay = delayed.getDelay(TimeUnit.MILLISECONDS);

					if(delay > 1) {
						wait = delay;
						break;
					}

					Task<L> task = null;

					sync.lock();
					try {

						delayed = getDelayedTask();
						delay = delayed.getDelay(TimeUnit.MILLISECONDS);

						if(delay > 1) {
							wait = delay;
							break;
						}

						task = getScheduledTask();
					} finally {
						sync.unlock();
					}

					if(task != null) {

						Exception exception = null;

						try {

							beforeExecute(thread, task);
							try {
								task.run(localObjects);
							} catch(final Exception e) {
								handler.handle(thread, e);
								exception = e;
							} catch(final Throwable t) {
								throwable = t;
								throw t;
							} finally {
								afterExecute(task, exception);
							}

						} finally {
							worker.incrementCompleteTask();
						}
					}
				}

				synchronized(worker) {
					worker.setWaited(true);
					try {
						worker.wait(wait);
					} catch(InterruptedException e) {
						LOGGER.warning(e);
					} finally {
						worker.setWaited(false);
					}
				}
			}

		} finally {
			workerFinish(worker, throwable);
		}
	}

	@Override
	public <V, T extends CallableTask<L, V>> ScheduledFuture<V> schedule(CallableTask<L, V> callable, long delay, TimeUnit unit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Task<L>> ScheduledFuture<T> schedule(T command, long delay, TimeUnit unit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Task<L>> ScheduledFuture<T> scheduleAtFixedRate(T task, long initialDelay, long period, TimeUnit unit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Task<L>> ScheduledFuture<T> scheduleWithFixedDelay(T task, long initialDelay, long delay, TimeUnit unit) {
		// TODO Auto-generated method stub
		return null;
	}
}
