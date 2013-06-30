package rlib.concurrent.model;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import rlib.concurrent.interfaces.LCallable;
import rlib.concurrent.interfaces.LFutureTask;
import rlib.util.pools.Foldable;

public final class FutureTaskSync<L, V> extends AbstractQueuedSynchronizer implements Foldable
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
	
	/** ссылка на задачу */
	private final LFutureTask<L, V> futureTask;
	
	/** поток, в котором выполняется задача */
	private volatile Thread runner;
	
	/** обрабатываемая задача */
	private LCallable<L, V> callable;
	/** ошибка, вылетевшая во время ожидания и исполнения задачи */
	private Throwable exception;
	/** резульбтат выполнения задачи */
	private V result;
	
	public FutureTaskSync(LFutureTask<L, V> futureTask)
	{
		this.futureTask = futureTask;
	}

	/**
	 * @param callable обрабатываемая задача.
	 */
	public void setCallable(LCallable<L, V> callable)
	{
		this.callable = callable;
	}
	
	protected final Thread getRunner()
	{
		return runner;
	}
	
	/**
	 * Обработка отмены выполнения задачи.
	 * 
	 * @param mayInterruptIfRunning прерывать ли исполняющий поток.
	 * @return произошла ли отмена.
	 */
	public boolean innerCancel(boolean mayInterruptIfRunning)
	{
		while(true)
		{
			int state = getState();
			
			if(ranOrCancelled(state))
				return false;
			
			if(compareAndSetState(state, CANCELLED))
				break;
		}
		
		if(mayInterruptIfRunning)
		{
			Thread runner = getRunner();
			
			if(runner != null)
				runner.interrupt();
		}
		
		releaseShared(0);
		getFutureTask().done();
		return true;
	}

	protected final LFutureTask<L, V> getFutureTask()
	{
		return futureTask;
	}

	public V innerGet() throws InterruptedException, ExecutionException
	{
		acquireSharedInterruptibly(0);
		
		if(getState() == CANCELLED)
			throw new CancellationException();
		
		Throwable exception = getException();
		
		if(exception != null)
			throw new ExecutionException(exception);
		
		return getResult();
	}
	
	protected final Throwable getException()
	{
		return exception;
	}

	public V innerGet(long nanosTimeout) throws InterruptedException, ExecutionException, TimeoutException
	{
		if(!tryAcquireSharedNanos(0, nanosTimeout))
			throw new TimeoutException();
		
		if(getState() == CANCELLED)
			throw new CancellationException();
		
		Throwable exception = getException();
		
		if(exception != null)
			throw new ExecutionException(exception);
		
		return getResult();
	}
	
	protected final V getResult()
	{
		return result;
	}

	public boolean innerIsCancelled()
	{
		return getState() == CANCELLED;
	}

	public final boolean innerIsDone()
	{
		return ranOrCancelled(getState()) && getRunner() == null;
	}
	
	protected final void setRunner(Thread runner)
	{
		this.runner = runner;
	}

	public void innerRun(L localObjects)
	{
		if(!compareAndSetState(READY, RUNNING))
			return;

		setRunner(Thread.currentThread());
		
		LFutureTask<L, V> futureTask = getFutureTask();
		
		if(getState() == RUNNING)
		{
			V result;
			
			try
			{
				result = getCallable().call(localObjects);
			}
			catch(Throwable throwable)
			{
				futureTask.setException(throwable);
				return;
			}
			
			futureTask.setResult(result);
		}
		else
		{
			releaseShared(0);
		}
	}
	
	protected final LCallable<L, V> getCallable()
	{
		return callable;
	}

	public boolean innerRunAndReset(L localObjects)
	{
		if(!compareAndSetState(READY, RUNNING))
			return false;
		
		try
		{
			setRunner(Thread.currentThread());
			
			if(getState() == RUNNING)
				getCallable().call(localObjects);
			
			setRunner(null);
			
			return compareAndSetState(RUNNING, READY);
		}
		catch(Throwable ex)
		{
			getFutureTask().setException(ex);
			return false;
		}
	}

	public void setResult(V result)
	{
		this.result = result;
	}
	
	public void innerSet(V result)
	{
		while(true)
		{
			int state = getState();
			
			if(state == RAN)
				return;
			
			if(state == CANCELLED)
			{
				releaseShared(0);
				return;
			}
			
			if(compareAndSetState(state, RAN))
			{
				setResult(result);
				releaseShared(0);
				getFutureTask().done();
				return;
			}
		}
	}

	protected final void setException(Throwable exception)
	{
		this.exception = exception;
	}
	
	public void innerSetException(Throwable throwable)
	{
		while(true)
		{
			int state = getState();
			
			if(state == RAN)
				return;
			
			if(state == CANCELLED)
			{
				releaseShared(0);
				return;
			}
			
			if(compareAndSetState(state, RAN))
			{
				setException(throwable);
				releaseShared(0);
				getFutureTask().done();
				return;
			}
		}
	}

	protected final boolean ranOrCancelled(int state)
	{
		return (state & (RAN | CANCELLED)) != 0;
	}

	protected final int tryAcquireShared(int ignore)
	{
		return innerIsDone() ? 1 : -1;
	}

	/**
	 * Implements AQS base release to always signal after setting final done
	 * status by nulling runner thread.
	 */
	public boolean tryReleaseShared(int ignore)
	{
		setRunner(null);
		return true;
	}

	@Override
	public void finalyze()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reinit(){}
}
