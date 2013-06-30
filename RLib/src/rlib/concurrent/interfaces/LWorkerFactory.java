package rlib.concurrent.interfaces;

/**
 * Интерфейс для реализации фабрики работников для исполнительных потоков.
 * 
 * @author Ronn
 */
public interface LWorkerFactory<L>
{
	/**
	 * Создание нового работника.
	 * 
	 * @param threadPoolExecutor исполнительный пул потоков.
	 * @param firstTask стартовая задача для работника.
	 * @return новый работник.
	 */
	public LWorker<L> create(LThreadPoolExecutor<L> threadPoolExecutor, LRunnable<L> firstTask);
	
	/**
	 * Сохранение использованного работника.
	 * 
	 * @param worker использованный работник.
	 */
	public void safe(LWorker<L> worker);
}
