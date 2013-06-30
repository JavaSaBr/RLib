package rlib.concurrent.interfaces;

/**
 * Интерфейс для создания вызываемой задачи с результатом.
 * 
 * @author Ronn
 */
public interface LCallable<L, V>
{
	/**
	 * Вызвать обработку задачи с получением результата.
	 * 
	 * @param localObjects контейнер локальных объектов.
	 * @return результат исполнения задачи.
	 */
	public V call(L localObjects);
}
