package rlib.concurrent.impl;

import rlib.concurrent.interfaces.ExtThread;
import rlib.concurrent.interfaces.Task;

/**
 * Реализация потока с контейнером локальных объектов.
 * 
 * @author Ronn
 */
public final class ExtThreadImpl<L> extends Thread implements ExtThread<L>, Task<L> {

	/** контейнер локальных объектов */
	private L localObjects;

	/** задача потока */
	private Task<L> task;

	public ExtThreadImpl(final Task<L> task, final L localObjects, final String name) {
		super(null, null, name);

		setLocalObjects(localObjects);
		setTask(task);
	}

	public ExtThreadImpl(final ThreadGroup group, final Task<L> task, final L localObjects, final String name) {
		super(group, null, name);

		setLocalObjects(localObjects);
		setTask(task);
	}

	@Override
	public L getLocalObjects() {
		return localObjects;
	}

	private Task<L> getTask() {
		return task;
	}

	@Override
	public Thread getThread() {
		return this;
	}

	@Override
	public void run() {
		run(getLocalObjects());
	}

	@Override
	public void run(final L localObjects) {

		final Task<L> task = getTask();

		if(task != null) {
			task.run(localObjects);
		}
	}

	/**
	 * @param localObjects контейнер локальных объектов.
	 */
	private void setLocalObjects(final L localObjects) {
		this.localObjects = localObjects;
	}

	/**
	 * @param task задача потока.
	 */
	private void setTask(final Task<L> task) {
		this.task = task;
	}
}
