package rlib.concurrent.interfaces;

/**
 * Интерфейс для реализации обработки отказа исполнения задачи.
 * 
 * @author Ronn
 */
public interface LRejectedExecutionHandler
{
	/**
	 * Обработка отклонения выполнения задачи указанным исполнителем.
	 * 
	 * @param runnable отклоненная задача.
	 * @param threadPoolExecutor откланяющий исполнитель.
	 */
    public void rejectedExecution(LRunnable<?> runnable, LThreadPoolExecutor<?> threadPoolExecutor);
}
