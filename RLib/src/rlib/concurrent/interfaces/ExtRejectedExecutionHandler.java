package rlib.concurrent.interfaces;

/**
 * Интерфейс для реализации обработки отказа исполнения задачи.
 * 
 * @author Ronn
 */
public interface ExtRejectedExecutionHandler {

	/**
	 * Обработка отклонения выполнения задачи указанным исполнителем.
	 * 
	 * @param runnable отклоненная задача.
	 * @param threadPoolExecutor откланяющий исполнитель.
	 */
	public void rejectedExecution(Task<?> runnable, ExtThreadPoolExecutor<?> threadPoolExecutor);
}
