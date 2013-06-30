package rlib.concurrent.interfaces;

import java.util.concurrent.ScheduledFuture;

/**
 * @author Ronn
 */
public interface LRunnableScheduledFuture<L, V> extends LRunnableFuture<L, V>, ScheduledFuture<V>
{
	/**
	 * @return ялвяется ли задача периодичной.
	 */ 
	public boolean isPeriodic();
}
