package rlib.concurrent.interfaces;

/**
 * Интерфейс для реализации потока, что бы его можно использовать с
 * исполнительными сервисаи с участием локальных объектов.
 * 
 * @author Ronn
 */
public interface ExtThread<L> {

	/**
	 * @return контейнер локальных объектов потока.
	 */
	public L getLocalObjects();

	/**
	 * @return сам поток.
	 */
	public Thread getThread();

	/**
	 * прервать поток.
	 */
	public void interrupt();

	/**
	 * @return прерван ли поток.
	 */
	public boolean isInterrupted();

	/**
	 * @param newPriority новый приоритет потока.
	 */
	public void setPriority(int newPriority);

	/**
	 * Запуск потока.
	 */
	public void start();
}
