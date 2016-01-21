package rlib.concurrent.executor;

import rlib.concurrent.task.CallableTask;
import rlib.concurrent.task.SimpleTask;

import java.util.concurrent.Future;

/**
 * Интерфейс для реализации исполнителя задач.
 *
 * @author Ronn
 */
public interface TaskExecutor<L> {

    /**
     * Добавить на исполнение задачу.
     *
     * @param task добавляемая задача.
     */
    public void execute(SimpleTask<L> task);

    /**
     * Добавить на выполнение задачу с возможностью ожидания результата.
     *
     * @param task добавляемая задача.
     * @return ссылка на исполнение задачи.
     */
    public <R> Future<R> submit(CallableTask<R, L> task);
}
