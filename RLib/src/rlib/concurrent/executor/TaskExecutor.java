package rlib.concurrent.executor;

import java.util.concurrent.Future;

import rlib.concurrent.task.CallableTask;
import rlib.concurrent.task.SimpleTask;

/**
 * Интерфейс для реализации исполнителя задач.
 *
 * @author JavaSaBr
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
