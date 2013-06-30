package rlib.concurrent.impl;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import rlib.concurrent.interfaces.LCallable;
import rlib.concurrent.interfaces.LRunnable;
import rlib.concurrent.interfaces.LScheduledFutureTask;
import rlib.concurrent.interfaces.LThreadPoolExecutor;

/**
 * @author Ronn
 */
public class LScheduledFutureTaskImpl<L, V> extends LFutureTaskImpl<L, V> implements LScheduledFutureTask<L, V>
{
    protected static final long now() 
    {
        return System.nanoTime();
    }
    
	private LThreadPoolExecutor<L> threadPoolExecutor;
	
	/** The actual task to be re-enqueued by reExecutePeriodic */
	private LScheduledFutureTaskImpl<L, V> outerTask = this;

    private long sequenceNumber;

    private long time;

    private long period;

    private int heapIndex;
    
    /**
     * Creates a one-shot action with given nanoTime-based trigger time.
     */
    public LScheduledFutureTaskImpl(LRunnable<L> r, V result, long sequenceNumber, long time) {
        super();
        this.time = time;
        this.period = 0;
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Creates a periodic action with given nano time and period.
     */
    public LScheduledFutureTaskImpl(LRunnable<L> r, V result, long sequenceNumber, long time, long period) {
        this.time = time;
        this.period = period;
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Creates a one-shot action with given nanoTime-based trigger.
     */
    public LScheduledFutureTaskImpl(LCallable<L, V> callable, long sequenceNumber, long time) {
        this.time = time;
        this.period = 0;
        this.sequenceNumber = sequenceNumber;
    }

	@Override
    public boolean isPeriodic() {
        return getPeriod() != 0;
    }
	
	protected final long getPeriod()
	{
		return period;
	}

	@Override
	public long getDelay(TimeUnit unit) {
        return unit.convert(getTime() - now(), TimeUnit.NANOSECONDS);
    }
	
    protected final long getTime()
	{
		return time;
	}
    
    protected final long getSequenceNumber()
	{
		return sequenceNumber;
	}

    @Override
	public int compareTo(Delayed delayed) 
	{
        if (delayed == this)
            return 0;
        
        if (delayed instanceof LScheduledFutureTaskImpl<?, ?>) 
        {
        	LScheduledFutureTaskImpl<?, ?> target = (LScheduledFutureTaskImpl<?, ?>)delayed;
        	
            long diff = getTime() - target.getTime();
            
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else if (getSequenceNumber() < target.getSequenceNumber())
                return -1;
            
            return 1;
        }
        
        long diff = (getDelay(TimeUnit.NANOSECONDS) -
                  delayed.getDelay(TimeUnit.NANOSECONDS));
        
        return (diff == 0) ? 0 : ((diff < 0) ? -1 : 1);
    }
	
	

    /**
     * Sets the next time to run for a periodic task.
     */
    private void setNextRunTime() {
        long p = period;
        if (p > 0)
            time += p;
        else
            time = triggerTime(-p);
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean cancelled = super.cancel(mayInterruptIfRunning);
       // if (cancelled && removeOnCancel && heapIndex >= 0)
      //      remove(this);
        return cancelled;
    }

    /**
     * Overrides FutureTask version so as to reset/requeue if periodic.
     */
    public void run() {
        boolean periodic = isPeriodic();
//        if (!canRunInCurrentRunState(periodic))
//            cancel(false);
//        else if (!periodic)
//            ScheduledFutureTask.super.run();
//        else if (ScheduledFutureTask.super.runAndReset()) {
//            setNextRunTime();
//            reExecutePeriodic(outerTask);
//        }
    }
	
    /**
     * Returns the trigger time of a delayed action.
     */
    long triggerTime(long delay) {
        return now() +
            ((delay < (Long.MAX_VALUE >> 1)) ? delay : overflowFree(delay));
    }
    
    /**
     * Constrains the values of all delays in the queue to be within
     * Long.MAX_VALUE of each other, to avoid overflow in compareTo.
     * This may occur if a task is eligible to be dequeued, but has
     * not yet been, while some other task is added with a delay of
     * Long.MAX_VALUE.
     */
    private long overflowFree(long delay) {
//        Delayed head = (Delayed) super.getQueue().peek();
//        if (head != null) {
//            long headDelay = head.getDelay(TimeUnit.NANOSECONDS);
//            if (headDelay < 0 && (delay - headDelay < 0))
//                delay = Long.MAX_VALUE + headDelay;
//        }
        return delay;
    }
}
