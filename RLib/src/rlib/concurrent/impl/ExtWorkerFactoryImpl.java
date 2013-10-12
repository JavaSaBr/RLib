package rlib.concurrent.impl;

import rlib.concurrent.interfaces.ExtThreadPoolExecutor;
import rlib.concurrent.interfaces.WorkerFactory;
import rlib.concurrent.interfaces.Task;
import rlib.concurrent.interfaces.Worker;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.Pools;

/**
 * Базовая реализация фабрики работников потоков.
 * 
 * @author Ronn
 */
public class ExtWorkerFactoryImpl<L> implements WorkerFactory<L> {

	/** пул работников */
	private final FoldablePool<Worker<L>> pool;

	public ExtWorkerFactoryImpl() {
		this.pool = Pools.newFoldablePool(Worker.class);
	}

	@Override
	public Worker<L> create(final ExtThreadPoolExecutor<L> threadPoolExecutor, final Task<L> firstTask) {

		Worker<L> worker = getPool().take();

		if(worker == null) {
			worker = new WorkerImpl<L>(threadPoolExecutor, firstTask);
		} else {
			worker.setFirstTask(firstTask);
			worker.setThreadPoolExecutor(threadPoolExecutor);
		}

		return worker;
	}

	/**
	 * @return пул работников.
	 */
	private FoldablePool<Worker<L>> getPool() {
		return pool;
	}

	@Override
	public void safe(final Worker<L> worker) {
		getPool().put(worker);
	}
}
