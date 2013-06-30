package rlib.util;

/**
 * Интерфейс для синхронизируемый объектов.
 * 
 * @author Ronn
 */
public interface Synchronized
{
	/**
	 * Заблокировать изминения.
	 */
	public void lock();
	
	/**
	 * Разблокировать изминения.
	 */
	public void unlock();
}
