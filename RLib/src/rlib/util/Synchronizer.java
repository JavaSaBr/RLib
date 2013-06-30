package rlib.util;

import java.util.concurrent.locks.Lock;

import rlib.concurrent.Locks;


/**
 * @author Ronn
 */
public final class Synchronizer implements Synchronized
{
	/** блокировщик */
	private final Lock lock;
	
	/** флаг блокировки */
	public volatile boolean locked;
	
	public Synchronizer()
	{
		this.lock =Locks.newLock();
	}
	
	/**
	 * @return the locked
	 */
	public final boolean isLocked()
	{
		return locked;
	}

	@Override
	public void lock()
	{
		lock.lock();
	}

	/**
	 * @param locked the locked to set
	 */
	public final void setLocked(boolean locked)
	{
		this.locked = locked;
	}

	@Override
	public void unlock()
	{
		lock.unlock();
	}
}
