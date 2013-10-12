package rlib.concurrent.interfaces;

/**
 * Интерфейс для реализации фабрики работников для исполнительных потоков.
 * 
 * @author Ronn
 */
public interface WorkerFactory<L> {

	/**
	 * Создание нового работника.
	 * 
	 * @param threadPoolExecutor исполнительный пул потоков.
	 * @param firstTask стартовая задача для работника.
	 * @return новый работник.
	 */
	public Worker<L> create(ExtThreadPoolExecutor<L> threadPoolExecutor, Task<L> firstTask);

	/**
	 * Сохранение использованного работника.
	 * 
	 * @param worker использованный работник.
	 */
	public void safe(Worker<L> worker);
}
