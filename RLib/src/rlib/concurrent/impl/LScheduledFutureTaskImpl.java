package rlib.concurrent.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.TimeUnit;

import rlib.concurrent.interfaces.LCallable;
import rlib.concurrent.interfaces.LScheduledFutureTask;

/**
 * @author Ronn
 */
public class LScheduledFutureTaskImpl<L, V> extends LFutureTaskImpl<L, V> implements LScheduledFutureTask<L, V>
{
	 /** Sequence number to break ties FIFO */
    private final long sequenceNumber;

    /** The time the task is enabled to execute in nanoTime units */
    private long time;

    /**
     * Period in nanoseconds for repeating tasks.  A positive
     * value indicates fixed-rate execution.  A negative value
     * indicates fixed-delay execution.  A value of 0 indicates a
     * non-repeating task.
     */
    private final long period;

    /** The actual task to be re-enqueued by reExecutePeriodic */
    LScheduledFutureTaskImpl<L, V> outerTask = this;

    /**
     * Index into delay queue, to support faster cancellation.
     */
    int heapIndex;
    
    /**
     * Creates a one-shot action with given nanoTime-based trigger time.
     */
    LScheduledFutureTaskImpl(Runnable r, V result, long ns) {
        super(r, result);
        this.time = ns;
        this.period = 0;
        this.sequenceNumber = sequencer.getAndIncrement();
    }

    /**
     * Creates a periodic action with given nano time and period.
     */
    LScheduledFutureTaskImpl(Runnable r, V result, long ns, long period) {
        super(r, result);
        this.time = ns;
        this.period = period;
        this.sequenceNumber = sequencer.getAndIncrement();
    }

    /**
     * Creates a one-shot action with given nanoTime-based trigger.
     */
    LScheduledFutureTaskImpl(Callable<V> callable, long ns) {
        super(callable);
        this.time = ns;
        this.period = 0;
        this.sequenceNumber = sequencer.getAndIncrement();
    }

	@Override
	public boolean isPeriodic()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getDelay(TimeUnit unit)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int compareTo(Delayed o)
	{
		// TODO Auto-generated method stub
		return 0;
	}	
}
