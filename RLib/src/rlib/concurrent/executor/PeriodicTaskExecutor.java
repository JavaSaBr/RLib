package rlib.concurrent.executor;

import rlib.concurrent.task.PeriodicTask;

/**
 * Интерфейс для реализации периодического исполнителя задач.
 *
 * @author Ronn
 */
public interface PeriodicTaskExecutor<T extends PeriodicTask<L>, L> {

    /**
     * Добавить периодическую задачу на обновление.
     *
     * @param task новая периодическая задача.
     */
    public void addTask(T task);

    /**
     * Удалить периодическую задачу из обновлений.
     *
     * @param task периодическая задача.
     */
    public void removeTask(T task);
}
