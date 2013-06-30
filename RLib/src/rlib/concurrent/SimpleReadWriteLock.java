package rlib.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author Ronn
 */
public final class SimpleReadWriteLock implements AsynReadSynWriteLock
{
	/** блокировщик записи */
	private Lock readLock;
	/** блокировщик чтения */
	private Lock writeLock;
	
	public SimpleReadWriteLock()
	{
		ReadWriteLock readWriteLock = Locks.newRWLock();
		
		readLock = readWriteLock.readLock();
		writeLock = readWriteLock.writeLock();
	}
	
	@Override
	public void readLock()
	{
		readLock.lock();
	}
	
	@Override
	public void readUnlock()
	{
		readLock.unlock();
	}
	
	@Override
	public void writeLock()
	{
		writeLock.lock();
	}
	
	@Override
	public void writeUnlock()
	{
		writeLock.unlock();
	}
}
