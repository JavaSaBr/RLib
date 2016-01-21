package rlib.concurrent.task;

/**
 * Интерфейс для реализации периодической азадчи.
 *
 * @author Ronn
 */
public interface PeriodicTask<L> extends CallableTask<Boolean, L> {

    @Override
    public default Boolean call(final L local, final long currentTime) {

        if (update(local, currentTime)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    /**
     * Уведомление задачи о том что она была финиширована для исполнителя.
     *
     * @param local контейнер локальных объектов.
     */
    public default void onFinish(final L local) {
    }

    /**
     * Реализация процесса обновления задачи.
     *
     * @param local       контейнер локальных объектов.
     * @param currentTime текущее время.
     * @return завершена ли работа задачи.
     */
    public boolean update(L local, long currentTime);
}
