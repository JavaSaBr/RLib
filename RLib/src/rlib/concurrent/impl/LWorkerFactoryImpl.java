package rlib.concurrent.impl;

import rlib.concurrent.interfaces.LRunnable;
import rlib.concurrent.interfaces.LThreadPoolExecutor;
import rlib.concurrent.interfaces.LWorker;
import rlib.concurrent.interfaces.LWorkerFactory;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.Pools;

/**
 * @author Ronn
 */
public class LWorkerFactoryImpl<L> implements LWorkerFactory<L>
{
	/** пул воркеров */
	private final FoldablePool<LWorker<L>> pool;
	
	public LWorkerFactoryImpl()
	{
		this.pool = Pools.newFoldablePool(LWorker.class);
	}
	
	private FoldablePool<LWorker<L>> getPool()
	{
		return pool;
	}
	
	@Override
	public LWorker<L> create(LThreadPoolExecutor<L> threadPoolExecutor, LRunnable<L> firstTask)
	{
		LWorker<L> worker = getPool().take();
		
		if(worker == null)
			worker = new LWorkerImpl<L>(threadPoolExecutor, firstTask);
		else
		{
			worker.setFirstTask(firstTask);
			worker.setThreadPoolExecutor(threadPoolExecutor);
		}
		
		return worker;
	}

	@Override
	public void safe(LWorker<L> worker)
	{
		getPool().put(worker);
	}
}
