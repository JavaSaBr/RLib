package rlib.concurrent.task;

/**
 * Интерфейс для реализации задача с возвращаемся результатом.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface CallableTask<R, L> {

    /**
     * Вызов выполнение задачи.
     *
     * @param local       контейнер локальных объектов.
     * @param currentTime примерное текущее время выполнение вызова.
     * @return результат выполнение задачи.
     */
    public R call(L local, long currentTime);
}
