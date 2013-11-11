package rlib.concurrent.interfaces;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Интерфейс для реализации исполнительного пула потоков.
 * 
 * @author Ronn
 */
public interface ExtThreadPoolExecutor<L> extends ExtExecutorService<L> {

	/**
	 * @return кол-во выполненых задач.
	 */
	public long getCompletedTasks();

	/**
	 * @return фабрика потоков.
	 */
	public ExtThreadFactory<L> getThreadFactory();

	/**
	 * Удаление из ожидания исполнение указанной задачи.
	 * 
	 * @param task удаляемая задача.
	 * @return была ли она удалена.
	 */
	public boolean remove(Task<L> task);

	/**
	 * Запуск обработки работника.
	 * 
	 * @param worker запускаемый работник.
	 */
	public void runWorker(Worker<L> worker, L localObjects);

	/**
	 * @return состояние пула.
	 */
	public AtomicInteger getState();
}
