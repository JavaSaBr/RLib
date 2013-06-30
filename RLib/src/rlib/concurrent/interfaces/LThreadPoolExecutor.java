package rlib.concurrent.interfaces;

/**
 * Интерфейс для реализации  исполнительного пула потоков.
 * 
 * @author Ronn
 */
public interface LThreadPoolExecutor<L> extends LExecutorService<L>
{
	/**
	 * @return фабрика потоков.
	 */
	public LThreadFactory<L> getThreadFactory();
	
	/**
	 * Запуск обработки воркера.
	 * 
	 * @param worker запускаемый воркер.
	 */
	public void runWorker(LWorker<L> worker, L localObjects);
	
	/**
	 * @return кол-во выполненых задач.
	 */
	public long getCompletedTasks();
	
	public boolean remove(LRunnable<L> task);
}
