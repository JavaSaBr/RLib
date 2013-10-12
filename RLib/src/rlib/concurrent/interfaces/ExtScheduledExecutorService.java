package rlib.concurrent.interfaces;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Интерфейс для реализации пула исполнителей отложенных задач.
 * 
 * @author Ronn
 */
public interface ExtScheduledExecutorService<L> extends ExtExecutorService<L> {

	/**
	 * Выполнить отложенную задачу через указанное время.
	 * 
	 * @param callable выполняемая задача.
	 * @param delay время ожидания исполнения.
	 * @param unit формат времени.
	 * @return ссылка на состояние задачи.
	 * @throws RejectedExecutionException
	 * @throws NullPointerException
	 */
	public <V, T extends CallableTask<L, V>> ScheduledFuture<V> schedule(CallableTask<L, V> callable, long delay, TimeUnit unit);

	/**
	 * Выполнить отложенную задачу через указанное время.
	 * 
	 * @param task выполняемая задача.
	 * @param delay время ожидания исполнения.
	 * @param unit формат времени.
	 * @return ссылка на состояние задачи.
	 * @throws RejectedExecutionException
	 * @throws NullPointerException
	 */
	public <T extends Task<L>> ScheduledFuture<T> schedule(T task, long delay, TimeUnit unit);

	/**
	 * Создание отложенной задачи с жестким периодичным исполнением.
	 * 
	 * @param task отложенная задача.
	 * @param initialDelay время до первого исполнения.
	 * @param period интервал исполнений.
	 * @param unit формат времени.
	 * @return ссылка на состояние задачи.
	 * @throws RejectedExecutionException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public <T extends Task<L>> ScheduledFuture<T> scheduleAtFixedRate(T task, long initialDelay, long period, TimeUnit unit);

	/**
	 * Создаие отложенной задачи с периодичным исполнением, где интервал
	 * смотрится по завершени предыдущго исполнения.
	 * 
	 * @param task отложенная задача.
	 * @param initialDelay время до первого исполнения.
	 * @param period интервал исполнений.
	 * @param unit формат времени.
	 * @return ссылка на состояние задачи.
	 * @throws RejectedExecutionException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public <T extends Task<L>> ScheduledFuture<T> scheduleWithFixedDelay(T task, long initialDelay, long delay, TimeUnit unit);
}
