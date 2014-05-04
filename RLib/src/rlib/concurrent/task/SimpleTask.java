package rlib.concurrent.task;

/**
 * Интерфейс для реализации простой задачи.
 * 
 * @author Ronn
 */
public interface SimpleTask<L> extends CallableTask<Void, L> {

	@Override
	public default Void call(final L local, final long currentTime) {
		execute(local, currentTime);
		return null;
	}

	/**
	 * Выполнение задачи.
	 * 
	 * @param local контейнер локальных объектов.
	 * @param currentTime примерное текущее время выполнение вызова.
	 */
	public void execute(L local, long currentTime);
}
