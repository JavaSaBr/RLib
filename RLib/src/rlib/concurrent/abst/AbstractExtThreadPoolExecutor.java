package rlib.concurrent.abst;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
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
import rlib.concurrent.util.ConcurrentUtils;
import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.linkedlist.LinkedList;
import rlib.util.linkedlist.LinkedListFactory;

/**
 * Базовая реализация пула потоков.
 * 
 * @author Ronn
 */
public abstract class AbstractExtThreadPoolExecutor<L> extends AbstractExtExecutorService<L> implements ExtThreadPoolExecutor<L> {

	public static final int NEED_EXECUTE = 1;

	public static final int WAIT = -2;

	public static final int NEED_WAIT = -1;

	public static final int EXECUTE = 1;

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
	/** состояние ожидания */
	protected final AtomicInteger state;
	/** кол-во выполненных задач */
	protected final AtomicLong completedTasks;

	/** размер пула потоков */
	protected final int poolSize;

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
		this.workers = LinkedListFactory.newLinkedList(Worker.class);
		this.state = new AtomicInteger(EXECUTE);
		this.completedTasks = new AtomicLong(0);
	}

	/**
	 * @param task ожидающая задача.
	 */
	protected void addWaitTask(final Task<L> task) {

		final LinkedList<Task<L>> waitTasks = getWaitTasks();
		waitTasks.add(task);

		AtomicInteger state = getState();

		if(state.compareAndSet(WAIT, NEED_EXECUTE)) {
			synchronized(this) {
				if(state.compareAndSet(NEED_EXECUTE, EXECUTE)) {
					notifyAll();
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

		final WorkerFactory<L> workerFactory = getWorkerFactory();
		final Worker<L> worker = workerFactory.create(this, task);

		final LinkedList<Worker<L>> workers = getWorkers();
		workers.add(worker);

		final ExtThread<L> thread = worker.getThread();
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

	@Override
	public void execute(final Task<L> task) {

		final LinkedList<Worker<L>> workers = getWorkers();
		final Lock sync = getSync();

		sync.lock();
		try {

			if(workers.size() == getPoolSize()) {
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
		return completedTasks.get();
	}

	public ExtThreadExceptionHandler getHandler() {
		return handler;
	}

	/**
	 * @return размер пула потоков.
	 */
	protected final int getPoolSize() {
		return poolSize;
	}

	@Override
	public AtomicInteger getState() {
		return state;
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
	protected Task<L> getWaitTask(boolean remove) {

		final LinkedList<Task<L>> waitTasks = getWaitTasks();
		final Lock sync = getSync();

		sync.lock();
		try {
			return remove ? waitTasks.poll() : waitTasks.peek();
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

	@Override
	public boolean remove(final Task<L> task) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void runWorker(final Worker<L> worker, final L localObjects) {

		final ExtThreadExceptionHandler handler = getHandler();
		final LinkedList<Task<L>> waitTasks = getWaitTasks();
		final ExtThread<L> thread = worker.getThread();
		final Lock sync = getSync();

		final AtomicInteger state = getState();

		Task<L> firstTask = worker.getFirstTask();
		worker.setFirstTask(null);

		Throwable throwable = null;

		try {

			while(!isShutdown()) {

				if(firstTask == null) {
					firstTask = getWaitTask(true);
				}

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

				synchronized(this) {

					Task<L> task = getWaitTask(false);

					if(task == null) {
						state.getAndSet(WAIT);
						ConcurrentUtils.waitInSynchronize(this);
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
	public <R> Future<R> submit(CallableTask<L, R> task) {
		ExtFutureTaskImpl<L, R> future = new ExtFutureTaskImpl<L, R>(task);
		execute(future);
		return future;
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

		final WorkerFactory<L> workerFactory = getWorkerFactory();
		final LinkedList<Worker<L>> workers = getWorkers();
		final Lock sync = getSync();

		completedTasks.getAndSet(completedTasks.get() + worker.getCompletedTasks());

		sync.lock();
		try {
			workers.remove(worker);
			workerFactory.safe(worker);
		} finally {
			sync.unlock();
		}
	}
}
