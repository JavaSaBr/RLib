package rlib.concurrent.interfaces;

/**
 * Интерфейс для реализации фабрики потоков с участием контейнера локальных объектов.
 * 
 * @author Ronn
 */
public interface LThreadFactory<L>
{
	/**
	 * Получние нового потока для указанной задачи.
	 * 
	 * @param runnable новая задача.
	 * @return новый поток для исполнения задачи.
	 */
	public LThread<L> createTread(LRunnable<L> runnable);
}
