package rlib.concurrent.impl;

import java.util.concurrent.locks.Lock;

import rlib.concurrent.abst.AbstractLThreadPoolExecutor;
import rlib.concurrent.interfaces.LRunnable;
import rlib.concurrent.interfaces.LThreadExceptionHandler;
import rlib.concurrent.interfaces.LThreadFactory;
import rlib.concurrent.interfaces.LWorkerFactory;
import rlib.util.linkedlist.LinkedList;

/**
 * @author Ronn
 */
public class LThreadPoolExecutorImpl<L> extends AbstractLThreadPoolExecutor<L>
{
	public LThreadPoolExecutorImpl(LThreadFactory<L> threadFactory, LWorkerFactory<L> workerFactory, LThreadExceptionHandler handler,
			LinkedList<LRunnable<L>> waitTasks, Lock sync, int timeout, int poolSize)
	{
		super(threadFactory, workerFactory, handler, waitTasks, sync, timeout, poolSize);
	}

	@Override
	public boolean remove(LRunnable<L> task)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
