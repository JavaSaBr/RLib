package rlib.concurrent.abst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import rlib.concurrent.interfaces.CallableTask;
import rlib.concurrent.interfaces.ExtExecutorService;
import rlib.concurrent.interfaces.Task;
import rlib.util.array.Array;
import rlib.util.array.Arrays;

/**
 * Базовая реализация исполнителя задач.
 * 
 * @author Ronn
 */
public abstract class AbstractExtExecutorService<L> implements ExtExecutorService<L> {

	@Override
	public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
		throw new RuntimeException("unssuported method.");
	}

	@Override
	public void execute(final Array<Task<L>> tasks) {

		for(final Task<L> task : tasks.array()) {

			if(task == null) {
				break;
			}

			execute(task);
		}
	}

	@Override
	public void execute(final Collection<Task<L>> tasks) {
		for(final Task<L> task : tasks) {
			execute(task);
		}
	}

	@Override
	public void execute(final Task<L> task) {
		throw new RuntimeException("unssuported method.");
	}

	@Override
	public void execute(final Task<L>[] tasks) {
		for(final Task<L> task : tasks) {
			execute(task);
		}
	}

	@Override
	public <T> Array<Future<T>> invokeAll(final Array<? extends CallableTask<L, T>> tasks) throws InterruptedException {
		final Array<Future<T>> container = Arrays.toArray(Future.class, tasks.size());
		return invokeAll(tasks, container);
	}

	@Override
	public <T, C extends Array<Future<T>>> C invokeAll(final Array<? extends CallableTask<L, T>> tasks, final C container) throws InterruptedException {

		for(final CallableTask<L, T> task : tasks.array()) {

			if(task == null) {
				break;
			}

			final Future<T> future = submit(task);

			if(future != null) {
				container.add(future);
			}
		}

		return container;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Future<T>[] invokeAll(final CallableTask<L, T>[] tasks) throws InterruptedException {
		return invokeAll(tasks, new Future[tasks.length]);
	}

	@Override
	public <T> Future<T>[] invokeAll(final CallableTask<L, T>[] tasks, final Future<T>[] container) throws InterruptedException {

		for(int i = 0, length = tasks.length; i < length; i++) {
			container[i] = submit(tasks[i]);
		}

		return container;
	}

	@Override
	public <T> Collection<Future<T>> invokeAll(final Collection<? extends CallableTask<L, T>> tasks) throws InterruptedException {
		return invokeAll(tasks, new ArrayList<Future<T>>(tasks.size()));
	}

	@Override
	public <T, C extends Collection<Future<T>>> C invokeAll(final Collection<? extends CallableTask<L, T>> tasks, final C container) throws InterruptedException {

		for(final CallableTask<L, T> task : tasks) {

			final Future<T> future = submit(task);

			if(future != null) {
				container.add(future);
			}
		}

		return container;
	}

	@Override
	public boolean isShutdown() {
		throw new RuntimeException("unssuported method.");
	}

	@Override
	public boolean isTerminated() {
		throw new RuntimeException("unssuported method.");
	}

	@Override
	public void shutdown() {
		throw new RuntimeException("unssuported method.");
	}

	@Override
	public Array<Task<L>> shutdownNow() {
		throw new RuntimeException("unssuported method.");
	}

	@Override
	public <T> Future<T> submit(final CallableTask<L, T> task) {
		throw new RuntimeException("unssuported method.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Future<T> submit(final Task<L> task) {
		return (Future<T>) submit(wrap(task));
	}

	@Override
	public <T> Future<T> submit(final Task<L> task, final T result) {
		return submit(wrap(task, result));
	}

	protected CallableTask<L, Void> wrap(final Task<L> task) {
		return new CallableTask<L, Void>() {

			@Override
			public Void call(L localObjects) {
				task.run(localObjects);
				return null;
			}
		};
	}

	protected <T> CallableTask<L, T> wrap(final Task<L> task, final T result) {
		return new CallableTask<L, T>() {

			@Override
			public T call(L localObjects) {
				task.run(localObjects);
				return result;
			}
		};
	}
}
