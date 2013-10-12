package rlib.concurrent.impl;

import java.util.concurrent.locks.Lock;

import rlib.concurrent.abst.AbstractExtThreadPoolExecutor;
import rlib.concurrent.interfaces.ExtThreadExceptionHandler;
import rlib.concurrent.interfaces.ExtThreadFactory;
import rlib.concurrent.interfaces.Task;
import rlib.concurrent.interfaces.WorkerFactory;
import rlib.util.linkedlist.LinkedList;

/**
 * Реализация пула потоков исполнителей.
 * 
 * @author Ronn
 */
public class ExtThreadPoolExecutorImpl<L> extends AbstractExtThreadPoolExecutor<L> {

	public ExtThreadPoolExecutorImpl(final ExtThreadFactory<L> threadFactory, final WorkerFactory<L> workerFactory, final ExtThreadExceptionHandler handler, final LinkedList<Task<L>> waitTasks,
			final Lock sync, final int poolSize) {
		super(threadFactory, workerFactory, handler, waitTasks, sync, poolSize);
	}
}
