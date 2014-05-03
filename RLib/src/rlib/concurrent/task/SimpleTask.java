package rlib.concurrent.task;

/**
 * Интерфейс для реализации простой задачи.
 * 
 * @author Ronn
 */
public interface SimpleTask<L> extends CallableTask<Void, L> {

	@Override
	public default Void call(final L localObjects, final long currentTime) {
		execute(localObjects, currentTime);
		return null;
	}

	/**
	 * Выполнение задачи.
	 * 
	 * @param localObjects контейнер локальных объектов.
	 * @param currentTime примерное текущее время выполнение вызова.
	 */
	public void execute(L localObjects, long currentTime);
}
