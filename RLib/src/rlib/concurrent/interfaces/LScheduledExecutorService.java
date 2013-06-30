package rlib.concurrent.interfaces;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Интерфейс для реализации пула исполнителей отложенных задач.
 * 
 * @author Ronn
 */
public interface LScheduledExecutorService<L> extends LExecutorService<L>
{
	/**
     * Выполнить отложенную задачу через указанное время.
     *
     * @param command выполняемая задача.
     * @param delay время ожидания исполнения.
     * @param unit формат времени.
     * @return ссылка на состояние задачи.
     * @throws RejectedExecutionException 
     * @throws NullPointerException
     */
	public <T extends LRunnable<L>> ScheduledFuture<T> schedule(T command, long delay, TimeUnit unit);

    /**
     * Выполнить отложенную задачу через указанное время.
     *
     * @param command выполняемая задача.
     * @param delay время ожидания исполнения.
     * @param unit формат времени.
     * @return ссылка на состояние задачи.
     * @throws RejectedExecutionException 
     * @throws NullPointerException
     */
    public <V, T extends LCallable<L, V>> ScheduledFuture<V> schedule(LCallable<L, V> callable, long delay, TimeUnit unit);

    /**
     * Создаие отложенной задачи с жестким периодичным исполнением.
     *
     * @param command отложенная задача.
     * @param initialDelay время до первого исполнения.
     * @param period интервал исполнений.
     * @param unit формат времени.
     * @return ссылка на состояние задачи.
     * @throws RejectedExecutionException
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public <T extends LRunnable<L>> ScheduledFuture<T> scheduleAtFixedRate(LRunnable<L> command, long initialDelay, long period, TimeUnit unit);

    /**
     * Создаие отложенной задачи с периодичным исполнением, 
     * где интервал смотрится по завершени предыдущго исполнения.
     *
     * @param command отложенная задача.
     * @param initialDelay время до первого исполнения.
     * @param period интервал исполнений.
     * @param unit формат времени.
     * @return ссылка на состояние задачи.
     * @throws RejectedExecutionException
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public <T extends LRunnable<L>> ScheduledFuture<T> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit);
}
