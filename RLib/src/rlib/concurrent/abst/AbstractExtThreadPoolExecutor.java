package rlib.concurrent.abst;

import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.impl.ExtFutureTaskImpl;
import rlib.concurrent.interfaces.CallableTask;
import rlib.concurrent.interfaces.ExtThread;
import rlib.concurrent.interfaces.ExtThreadExceptionHandler;
import rlib.concurrent.interfaces.ExtThreadFactory;
import rlib.concurrent.interfaces.ExtThreadPoolExecutor;
import rlib.concurrent.interfaces.Task;
import rlib.concurrent.interfaces.Worker;
import rlib.concurrent.interfaces.WorkerFactory;
import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.linkedlist.LinkedList;
import rlib.util.linkedlist.LinkedLists;
import rlib.util.linkedlist.Node;

/**
 * Базовая реализация пула потоков.
 * 
 * @author Ronn
 */
public abstract class AbstractExtThreadPoolExecutor<L> extends AbstractExtExecutorService<L> implements ExtThreadPoolExecutor<L> {

	protected static final Logger LOGGER = Loggers.getLogger(ExtThreadPoolExecutor.class);

	/** фабрика потоков */
	protected final ExtThreadFactory<L> threadFactory;
	/** фабрика работников */
	protected final WorkerFactory<L> workerFactory;
	/** обработчик эксепшенов в потоках */
	protected final ExtThreadExceptionHandler handler;

	/** очередь ожидающих исполнения задач */
	protected final LinkedList<Task<L>> waitTasks;
	/** список исполнителей задач */
	protected final LinkedList<Worker<L>> workers;

	/** синхронизатор */
	protected final Lock sync;

	/** размер пула потоков */
	protected final int poolSize;

	/** кол-во выполненных задач */
	protected volatile long completedTasks;

	/** остановлен ли пул потоков */
	protected volatile boolean shutdown;

	public AbstractExtThreadPoolExecutor(final ExtThreadFactory<L> threadFactory, final WorkerFactory<L> workerFactory, final ExtThreadExceptionHandler handler, final LinkedList<Task<L>> waitTasks,
			final Lock sync, final int poolSize) {
		super();

		this.threadFactory = threadFactory;
		this.workerFactory = workerFactory;
		this.handler = handler;
		this.waitTasks = waitTasks;
		this.sync = sync;
		this.poolSize = poolSize;
		this.workers = LinkedLists.newLinkedList(Worker.class);
	}

	/**
	 * @param task ожидающая задача.
	 */
	protected void addWaitTask(final Task<L> task) {

		getWaitTasks().add(task);

		LinkedList<Worker<L>> workers = getWorkers();

		for(Node<Worker<L>> node = workers.getFirstNode(); node != null; node = node.getNext()) {

			Worker<L> worker = node.getItem();

			if(worker.isWaited()) {
				synchronized(worker) {
					if(worker.isWaited()) {
						worker.notifyAll();
						break;
					}
				}
			}
		}
	}

	/**
	 * Добавление в работу нового работника.
	 * 
	 * @param task новая задача.
	 */
	protected void addWorker(final Task<L> task) {

		final Worker<L> worker = getWorkerFactory().create(this, task);
		final ExtThread<L> thread = worker.getThread();

		getWorkers().add(worker);

		thread.start();
	}

	/**
	 * Действие после исполнения задачи.
	 * 
	 * @param task исполняемая задача.
	 * @param exception ошибка во время исполнения.
	 */
	protected void afterExecute(final Task<L> task, final Exception exception) {
	}

	/**
	 * Действие перед исполнением задачи.
	 * 
	 * @param thread поток, который будет исполнять.
	 * @param task исполняемая задача.
	 */
	protected void beforeExecute(final ExtThread<L> thread, final Task<L> task) {
	}

	/**
	 * @return есть ли ожидающие работники новых задач.
	 */
	protected boolean hasFreeWorker() {

		LinkedList<Worker<L>> workers = getWorkers();

		for(Node<Worker<L>> node = workers.getFirstNode(); node != null; node = node.getNext()) {

			Worker<L> worker = node.getItem();

			if(worker.isWaited()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void execute(final Task<L> task) {

		final Lock sync = getSync();

		sync.lock();
		try {

			final LinkedList<Worker<L>> workers = getWorkers();

			if(hasFreeWorker() || workers.size() == getPoolSize()) {
				addWaitTask(task);
			} else {
				addWorker(task);
			}

		} finally {
			sync.unlock();
		}
	}

	@Override
	public long getCompletedTasks() {
		return completedTasks;
	}

	/**
	 * @return размер пула потоков.
	 */
	protected final int getPoolSize() {
		return poolSize;
	}

	/**
	 * @return синхронизатор.
	 */
	protected final Lock getSync() {
		return sync;
	}

	@Override
	public final ExtThreadFactory<L> getThreadFactory() {
		return threadFactory;
	}

	/**
	 * @return ближайшее ожидающее задание.
	 */
	protected Task<L> getWaitTask() {

		final LinkedList<Task<L>> waitTasks = getWaitTasks();

		if(waitTasks.isEmpty()) {
			return null;
		}

		final Lock sync = getSync();

		sync.lock();
		try {
			return waitTasks.poll();
		} finally {
			sync.unlock();
		}
	}

	/**
	 * @return список ожидающих задач.
	 */
	public LinkedList<Task<L>> getWaitTasks() {
		return waitTasks;
	}

	/**
	 * @return фабрика работников.
	 */
	protected final WorkerFactory<L> getWorkerFactory() {
		return workerFactory;
	}

	/**
	 * @return список работников.
	 */
	protected final LinkedList<Worker<L>> getWorkers() {
		return workers;
	}

	@Override
	public final boolean isShutdown() {
		return shutdown;
	}

	public ExtThreadExceptionHandler getHandler() {
		return handler;
	}

	@Override
	public void runWorker(final Worker<L> worker, final L localObjects) {

		Task<L> firstTask = worker.getFirstTask();
		worker.setFirstTask(null);

		final ExtThreadExceptionHandler handler = getHandler();
		final ExtThread<L> thread = worker.getThread();

		Throwable throwable = null;

		try {

			while(!isShutdown()) {

				if(firstTask == null) {
					firstTask = getWaitTask();
				}

				for(Task<L> task = firstTask; task != null; task = getWaitTask()) {

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

				synchronized(worker) {
					worker.setWaited(true);
					try {
						worker.wait();
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
	public void shutdown() {
		this.shutdown = true;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * Обработка завершения работника потока.
	 * 
	 * @param worker завершенный работник.
	 * @param throwable ошибка, возникшая при работе.
	 */
	protected void workerFinish(final Worker<L> worker, final Throwable throwable) {

		final Lock sync = getSync();

		completedTasks += worker.getCompletedTasks();

		sync.lock();
		try {
			getWorkers().remove(worker);
			getWorkerFactory().safe(worker);
		} finally {
			sync.unlock();
		}
	}

	@Override
	public <R> Future<R> submit(CallableTask<L, R> task) {
		ExtFutureTaskImpl<L, R> future = new ExtFutureTaskImpl<L, R>(task);
		execute(future);
		return future;
	}

	@Override
	public boolean remove(final Task<L> task) {
		// TODO Auto-generated method stub
		return false;
	}
}
