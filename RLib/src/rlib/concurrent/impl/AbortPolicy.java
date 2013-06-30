package rlib.concurrent.impl;

import java.util.concurrent.RejectedExecutionException;

import rlib.concurrent.interfaces.LRejectedExecutionHandler;
import rlib.concurrent.interfaces.LRunnable;
import rlib.concurrent.interfaces.LThreadPoolExecutor;

/**
 * @author Ronn
 */
public final class AbortPolicy implements LRejectedExecutionHandler
{
	@Override
	public void rejectedExecution(LRunnable<?> runnable, LThreadPoolExecutor<?> threadPoolExecutor)
	{
		throw new RejectedExecutionException("Task " + runnable.toString() + " rejected from " + threadPoolExecutor.toString());
	}
}
