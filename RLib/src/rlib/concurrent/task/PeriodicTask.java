package rlib.concurrent.task;

/**
 * Интерфейс для реализации периодической азадчи.
 * 
 * @author Ronn
 */
public interface PeriodicTask<L> extends CallableTask<Boolean, L> {

	@Override
	public default Boolean call(final L localObjects, final long currentTime) {

		if(update(localObjects, currentTime)) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	/**
	 * Уведомление задачи о том что она была финиширована для исполнителя.
	 * 
	 * @param localObjects контейнер локальных объектов.
	 */
	public default void onFinish(final L localObjets) {
	}

	/**
	 * Реализация процесса обновления задачи.
	 * 
	 * @param localObjects контейнер локальных объектов.
	 * @param currentTime текущее время.
	 * @return завершена ли работа задачи.
	 */
	public boolean update(L localObjects, long currentTime);
}
