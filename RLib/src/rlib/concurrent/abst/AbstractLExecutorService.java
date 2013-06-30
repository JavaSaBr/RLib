package rlib.concurrent.abst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import rlib.concurrent.interfaces.LCallable;
import rlib.concurrent.interfaces.LExecutorService;
import rlib.concurrent.interfaces.LRunnable;
import rlib.util.array.Array;
import rlib.util.array.Arrays;

/**
 * @author Ronn
 */
public abstract class AbstractLExecutorService<L> implements LExecutorService<L>
{
	@Override
	public void execute(Array<LRunnable<L>> tasks)
	{
		for(LRunnable<L> task : tasks.array())
		{
			if(task == null)
				break;

			execute(task);
		}
	}

	@Override
	public void execute(Collection<LRunnable<L>> tasks)
	{
		for(LRunnable<L> task : tasks)
			execute(task);
	}

	@Override
	public void execute(LRunnable<L>[] tasks)
	{
		for(LRunnable<L> task : tasks)
			execute(task);
	}

	@Override
	public <T> Array<Future<T>> invokeAll(Array<? extends LCallable<L, T>> tasks) throws InterruptedException
	{
		Array<Future<T>> container = Arrays.toArray(Future.class, tasks.size());

		return invokeAll(tasks, container);
	}

	@Override
	public <T, C extends Array<Future<T>>> C invokeAll(Array<? extends LCallable<L, T>> tasks, C container) throws InterruptedException
	{
		for(LCallable<L, T> task : tasks.array())
		{
			if(task == null)
				break;

			Future<T> future = submit(task);

			if(future != null)
				container.add(future);
		}

		return container;
	}

	@Override
	public <T> Collection<Future<T>> invokeAll(Collection<? extends LCallable<L, T>> tasks) throws InterruptedException
	{
		return invokeAll(tasks, new ArrayList<Future<T>>(tasks.size()));
	}

	@Override
	public <T, C extends Collection<Future<T>>> C invokeAll(Collection<? extends LCallable<L, T>> tasks, C container)
			throws InterruptedException
	{
		for(LCallable<L, T> task : tasks)
		{
			Future<T> future = submit(task);

			if(future != null)
				container.add(future);
		}

		return container;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Future<T>[] invokeAll(LCallable<L, T>[] tasks) throws InterruptedException
	{
		return invokeAll(tasks, new Future[tasks.length]);
	}

	@Override
	public <T> Future<T>[] invokeAll(LCallable<L, T>[] tasks, Future<T>[] container) throws InterruptedException
	{
		for(int i = 0, length = tasks.length; i < length; i++)
			container[i] = submit(tasks[i]);

		return container;
	}
	
	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
	{
		throw new RuntimeException("unssuported method.");
	}
	
	@Override
	public void execute(LRunnable<L> task)
	{
		throw new RuntimeException("unssuported method.");
	}
	
	@Override
	public boolean isShutdown()
	{
		throw new RuntimeException("unssuported method.");
	}
	
	@Override
	public boolean isTerminated()
	{
		throw new RuntimeException("unssuported method.");
	}
	
	@Override
	public void shutdown()
	{
		throw new RuntimeException("unssuported method.");
	}
	
	@Override
	public Array<LRunnable<L>> shutdownNow()
	{
		throw new RuntimeException("unssuported method.");
	}
	
	@Override
	public <T> Future<T> submit(LCallable<L, T> task)
	{
		throw new RuntimeException("unssuported method.");
	}
	
	@Override
	public <T> Future<T> submit(LRunnable<L> task)
	{
		throw new RuntimeException("unssuported method.");
	}
	
	@Override
	public <T> Future<T> submit(LRunnable<L> task, T result)
	{
		throw new RuntimeException("unssuported method.");
	}
}
