package rlib.concurrent.model;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import rlib.concurrent.interfaces.LCallable;
import rlib.concurrent.interfaces.LFutureTask;

public final class FutureTaskSync<L, V> extends AbstractQueuedSynchronizer
{
	private static final long serialVersionUID = -7828117401763700385L;

	/** State value representing that task is ready to run */
	private static final int READY = 0;
	/** State value representing that task is running */
	private static final int RUNNING = 1;
	/** State value representing that task ran */
	private static final int RAN = 2;
	/** State value representing that task was cancelled */
	private static final int CANCELLED = 4;

	private LFutureTask<L, V> futureTask;
	/** The underlying callable */
	private LCallable<L, V> callable;
	/** The result to return from get() */
	private V result;
	/** The exception to throw from get() */
	private Throwable exception;

	/**
	 * The thread running task. When nulled after set/cancel, this indicates
	 * that the results are accessible. Must be volatile, to ensure visibility
	 * upon completion.
	 */
	private volatile Thread runner;

	public FutureTaskSync(LFutureTask<L, V> futureTask, LCallable<L, V> callable)
	{
		this.futureTask = futureTask;
		this.callable = callable;
	}

	public boolean innerCancel(boolean mayInterruptIfRunning)
	{
		for(;;)
		{
			int s = getState();
			if(ranOrCancelled(s))
				return false;
			if(compareAndSetState(s, CANCELLED))
				break;
		}
		if(mayInterruptIfRunning)
		{
			Thread r = runner;
			if(r != null)
				r.interrupt();
		}
		releaseShared(0);
		futureTask.done();
		return true;
	}

	public V innerGet() throws InterruptedException, ExecutionException
	{
		acquireSharedInterruptibly(0);
		if(getState() == CANCELLED)
			throw new CancellationException();
		if(exception != null)
			throw new ExecutionException(exception);
		return result;
	}

	public V innerGet(long nanosTimeout) throws InterruptedException, ExecutionException, TimeoutException
	{
		if(!tryAcquireSharedNanos(0, nanosTimeout))
			throw new TimeoutException();
		if(getState() == CANCELLED)
			throw new CancellationException();
		if(exception != null)
			throw new ExecutionException(exception);
		return result;
	}

	public boolean innerIsCancelled()
	{
		return getState() == CANCELLED;
	}

	public boolean innerIsDone()
	{
		return ranOrCancelled(getState()) && runner == null;
	}

	public void innerRun()
	{
		if(!compareAndSetState(READY, RUNNING))
			return;

		runner = Thread.currentThread();
		if(getState() == RUNNING)
		{ // recheck after setting thread
			V result;
			try
			{
				result = callable.call(null);
			}
			catch(Throwable ex)
			{
				futureTask.setException(ex);
				return;
			}
			futureTask.setResult(result);
		}
		else
		{
			releaseShared(0); // cancel
		}
	}

	public boolean innerRunAndReset()
	{
		if(!compareAndSetState(READY, RUNNING))
			return false;
		try
		{
			runner = Thread.currentThread();
			if(getState() == RUNNING)
				callable.call(null); // don't set result
			runner = null;
			return compareAndSetState(RUNNING, READY);
		}
		catch(Throwable ex)
		{
			futureTask.setException(ex);
			return false;
		}
	}

	public void innerSet(V v)
	{
		for(;;)
		{
			int s = getState();
			if(s == RAN)
				return;
			if(s == CANCELLED)
			{
				// aggressively release to set runner to null,
				// in case we are racing with a cancel request
				// that will try to interrupt runner
				releaseShared(0);
				return;
			}
			if(compareAndSetState(s, RAN))
			{
				result = v;
				releaseShared(0);
				futureTask.done();
				return;
			}
		}
	}

	public void innerSetException(Throwable t)
	{
		for(;;)
		{
			int s = getState();
			if(s == RAN)
				return;
			if(s == CANCELLED)
			{
				// aggressively release to set runner to null,
				// in case we are racing with a cancel request
				// that will try to interrupt runner
				releaseShared(0);
				return;
			}
			if(compareAndSetState(s, RAN))
			{
				exception = t;
				releaseShared(0);
				futureTask.done();
				return;
			}
		}
	}

	public boolean ranOrCancelled(int state)
	{
		return (state & (RAN | CANCELLED)) != 0;
	}

	/**
	 * Implements AQS base acquire to succeed if ran or cancelled
	 */
	public int tryAcquireShared(int ignore)
	{
		return innerIsDone() ? 1 : -1;
	}

	/**
	 * Implements AQS base release to always signal after setting final done
	 * status by nulling runner thread.
	 */
	public boolean tryReleaseShared(int ignore)
	{
		runner = null;
		return true;
	}
}
