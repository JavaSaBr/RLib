package rlib.concurrent.interfaces;

import java.util.Collection;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import rlib.util.array.Array;

/**
 * Интерфейс для создания исполнительного сервиса с участием контейнеров
 * локальных объектов.
 * 
 * @author Ronn
 */
public interface ExtExecutorService<L> {

	/**
	 * Блокировка исполнителя на указанное время, для завершения активных задач
	 * после выключения его.
	 * 
	 * @param timeout максимальное время ожидания.
	 * @param unit тип времени.
	 * @return успел ли за отведенное время исполнитель завершить свою работу.
	 * @throws InterruptedException был ли прерван во время ожидания.
	 */
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

	/**
	 * Исполнить указанные задачи.
	 * 
	 * @param tasks исполняемые задачи.
	 */
	public void execute(Array<Task<L>> tasks);

	/**
	 * Исполнить указанные задачи.
	 * 
	 * @param tasks исполняемые задачи.
	 */
	public void execute(Collection<Task<L>> tasks);

	/**
	 * Исполнить указанную задачу.
	 * 
	 * @param task исполняемая задача.
	 */
	public void execute(Task<L> task);

	/**
	 * Исполнить указанные задачи.
	 * 
	 * @param tasks исполняемые задачи.
	 */
	public void execute(Task<L>[] tasks);

	/**
	 * Отправка набора задач с получением набора фьючерсов.
	 * 
	 * @param tasks список исполняемых задач.
	 * @return список фьючерсов под список указанных задач.
	 * @throws InterruptedException
	 * @throws NullPointerException
	 */
	public <T> Array<Future<T>> invokeAll(Array<? extends CallableTask<L, T>> tasks) throws InterruptedException;

	/**
	 * Отправка набора задач с получением набора фьючерсов.
	 * 
	 * @param tasks список исполняемых задач.
	 * @param container контейнер для фьючерсов.
	 * @return список фьючерсов под список указанных задач.
	 * @throws InterruptedException
	 * @throws NullPointerException
	 */
	public <T, C extends Array<Future<T>>> C invokeAll(Array<? extends CallableTask<L, T>> tasks, C container) throws InterruptedException;

	/**
	 * Отправка набора задач с получением набора фьючерсов.
	 * 
	 * @param tasks список исполняемых задач.
	 * @return список фьючерсов под список указанных задач.
	 * @throws InterruptedException
	 * @throws NullPointerException
	 */
	public <T> Collection<Future<T>> invokeAll(Collection<? extends CallableTask<L, T>> tasks) throws InterruptedException;

	/**
	 * Отправка набора задач с получением набора фьючерсов.
	 * 
	 * @param tasks список исполняемых задач.
	 * @param container контейнер для фьючерсов.
	 * @return список фьючерсов под список указанных задач.
	 * @throws InterruptedException
	 * @throws NullPointerException
	 */
	public <T, C extends Collection<Future<T>>> C invokeAll(Collection<? extends CallableTask<L, T>> tasks, C container) throws InterruptedException;

	/**
	 * Отправка набора задач с получением набора фьючерсов.
	 * 
	 * @param tasks список исполняемых задач.
	 * @return список фьючерсов под список указанных задач.
	 * @throws InterruptedException
	 * @throws NullPointerException
	 */
	public <T> Future<T>[] invokeAll(CallableTask<L, T>[] tasks) throws InterruptedException;

	/**
	 * Отправка набора задач с получением набора фьючерсов.
	 * 
	 * @param tasks список исполняемых задач.
	 * @param container контейнер для фьючерсов.
	 * @return список фьючерсов под список указанных задач.
	 * @throws InterruptedException
	 * @throws NullPointerException
	 */
	public <T> Future<T>[] invokeAll(CallableTask<L, T>[] tasks, Future<T>[] container) throws InterruptedException;

	/**
	 * @return остановлен ли исполнитель.
	 */
	public boolean isShutdown();

	/**
	 * @return завершены ли все задачи после выключения.
	 */
	public boolean isTerminated();

	/**
	 * Запустить процесс обычного завершения работы пула, текущие задачи будут
	 * довыполнены, новые приниматься не будут.
	 */
	public void shutdown();

	/**
	 * Произвести остановку всех активных задач, при этом вернуть список
	 * ожидающих задач.
	 */
	public Array<Task<L>> shutdownNow();

	/**
	 * Отправка на исполнение задачи с получением ссылки на фьючерс.
	 * 
	 * @param task исполняемая задача.
	 * @return ссылка на фьючерс с возможностью его ожидания результата.
	 * @throws RejectedExecutionException
	 * @throws NullPointerException
	 */
	public <T> Future<T> submit(CallableTask<L, T> task);

	/**
	 * Отправка на исполнение задачи с получением ссылки на фьючерс.
	 * 
	 * @param task задача для исполнения.
	 * @return ссылка на фьючерс с возможностью его ожидания результата.
	 * @throws RejectedExecutionException
	 * @throws NullPointerException
	 */
	public <T> Future<T> submit(Task<L> task);

	/**
	 * Отправка на исполнение задачи с получением ссылки на фьючерс.
	 * 
	 * @param task задача для исполнения.
	 * @param result ожидаемый результат.
	 * @return ссылка на фьючерс с возможностью его ожидания результата.
	 * @throws RejectedExecutionException
	 * @throws NullPointerException
	 */
	public <T> Future<T> submit(Task<L> task, T result);
}