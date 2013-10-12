package rlib.concurrent.impl;

import rlib.concurrent.interfaces.CallableTask;
import rlib.concurrent.interfaces.Task;
import rlib.util.pools.Foldable;

/**
 * Обертка поверх Runnable для реализации интерфейса Callable.
 * 
 * @author Ronn
 */
public final class TaskWrapper<L, V> implements CallableTask<L, V>, Foldable {

	/** обернутая задача */
	private Task<L> task;

	/** результат его работы */
	private V result;

	/**
	 * @param result результат его работы.
	 */
	public void setResult(V result) {
		this.result = result;
	}

	/**
	 * @param tasl обернутая задача.
	 */
	public void setTask(Task<L> tasl) {
		this.task = tasl;
	}

	@Override
	public V call(L localObjects) {
		task.run(localObjects);
		return result;
	}

	@Override
	public void finalyze() {
		setResult(null);
		setTask(null);
	}

	@Override
	public void reinit() {
	}
}
