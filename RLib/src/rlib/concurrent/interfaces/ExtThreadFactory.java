package rlib.concurrent.interfaces;

/**
 * Интерфейс для реализации фабрики потоков с участием контейнера локальных
 * объектов.
 * 
 * @author Ronn
 */
public interface ExtThreadFactory<L> {

	/**
	 * Получние нового потока для указанной задачи.
	 * 
	 * @param runnable новая задача.
	 * @return новый поток для исполнения задачи.
	 */
	public ExtThread<L> createTread(Task<L> runnable);
}
