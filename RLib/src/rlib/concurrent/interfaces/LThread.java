package rlib.concurrent.interfaces;

/**
 * Интерфейс для реализации потока, что бы его можно использовать с исполнительными сервисаи с участием локальных объектов.
 * 
 * @author Ronn
 */
public interface LThread<L>
{
	/**
	 * @return контейнер локальных объектов потока.
	 */
	public L getLocalObjects();

	/**
	 * @return прерван ли поток.
	 */
	public boolean isInterrupted();

	/**
	 * прервать поток.
	 */
	public void interrupt();

	/**
	 * Запуск потока.
	 */
	public void start();
	
	/**
	 * @return сам поток.
	 */
	public Thread getThread();
	
	/**
	 * @param newPriority новый приоритет потока.
	 */
	public void setPriority(int newPriority);
}
