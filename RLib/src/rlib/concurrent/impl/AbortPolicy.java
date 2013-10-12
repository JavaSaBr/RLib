package rlib.concurrent.impl;

import java.util.concurrent.RejectedExecutionException;

import rlib.concurrent.interfaces.ExtRejectedExecutionHandler;
import rlib.concurrent.interfaces.Task;
import rlib.concurrent.interfaces.ExtThreadPoolExecutor;

/**
 * @author Ronn
 */
public final class AbortPolicy implements ExtRejectedExecutionHandler {

	@Override
	public void rejectedExecution(final Task<?> runnable, final ExtThreadPoolExecutor<?> threadPoolExecutor) {
		throw new RejectedExecutionException("Task " + runnable.toString() + " rejected from " + threadPoolExecutor.toString());
	}
}
