package rlib.concurrent.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rlib.concurrent.LExecutors;
import rlib.concurrent.interfaces.LCallable;
import rlib.concurrent.interfaces.LFutureTask;
import rlib.concurrent.interfaces.LRunnable;
import rlib.concurrent.model.FutureTaskSync;

/**
 * @author Ronn
 */
public class LFutureTaskImpl<L, V> implements LFutureTask<L, V>
{
	/** синхронизатор */
	private final FutureTaskSync<L, V> sync;

	public LFutureTaskImpl()
	{
		this.sync = new FutureTaskSync<L, V>(this);
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning)
	{
		return getSync().innerCancel(mayInterruptIfRunning);
	}

	@Override
	public void done(){}

	@Override
	public void finalyze()
	{
		getSync().finalyze();
	}

	@Override
	public V get() throws InterruptedException, ExecutionException
	{
		return getSync().innerGet();
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
	{
		return getSync().innerGet(unit.toNanos(timeout));
	}

	protected final FutureTaskSync<L, V> getSync()
	{
		return sync;
	}

	public void init(LCallable<L, V> callable)
	{
		getSync().setCallable(callable);
	}

	public void init(LRunnable<L> runnable, V result)
	{
		getSync().setCallable(LExecutors.runnableToCallable(runnable, result));
	}

	@Override
	public boolean isCancelled()
	{
		return getSync().innerIsCancelled();
	}

	@Override
	public boolean isDone()
	{
		return getSync().innerIsDone();
	}

	@Override
	public void reinit()
	{
		getSync().reinit();
	}

	@Override
	public void run(L localObjects)
	{
		getSync().innerRun(localObjects);
	}

	@Override
	public void setException(Throwable throwable)
	{
		getSync().innerSetException(throwable);
	}

	@Override
	public void setResult(V result)
	{
		getSync().innerSet(result);
	}
}
