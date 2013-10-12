package rlib.concurrent.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rlib.concurrent.interfaces.CallableTask;
import rlib.concurrent.interfaces.ExtFutureTask;

/**
 * Реализация управляемой задачи.
 * 
 * @author Ronn
 */
public class ExtFutureTaskImpl<L, V> implements ExtFutureTask<L, V> {

	private static enum State {
		WAIT,
		RUNNING,
		DONE,
		CANCELED,
	}

	/** исполняемая задача */
	private final CallableTask<L, V> task;

	/** состояние задачи */
	private volatile State state;

	/** есть ли ожидающий исполнения поток */
	private volatile int wait;

	/** результат исполнения */
	private V result;

	public ExtFutureTaskImpl(CallableTask<L, V> task) {
		this.task = task;
	}

	@Override
	public void run(L localObjects) {

		if(getState() == State.CANCELED) {
			return;
		}

		setState(State.RUNNING);
		try {
			setResult(getTask().call(localObjects));
		} finally {
			done();
		}
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {

		if(getState() == State.WAIT) {
			synchronized(this) {
				if(getState() == State.WAIT) {
					setState(State.CANCELED);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean isCancelled() {
		return getState() == State.CANCELED;
	}

	@Override
	public boolean isDone() {
		return getState() == State.DONE;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {

		if(getState() == State.WAIT || getState() == State.RUNNING) {
			synchronized(this) {
				if(getState() == State.WAIT || getState() == State.RUNNING) {
					setWait(true);
					try {
						wait();
					} finally {
						setWait(false);
					}
				}
			}
		}

		return getResult();
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {

		if(getState() == State.WAIT || getState() == State.RUNNING) {
			synchronized(this) {
				if(getState() == State.WAIT || getState() == State.RUNNING) {
					setWait(true);
					try {
						wait(unit.toMillis(timeout));
					} finally {
						setWait(false);
					}
				}
			}
		}

		return getResult();
	}

	@Override
	public void done() {

		setState(State.DONE);

		if(isWait()) {
			synchronized(this) {
				notifyAll();
			}
		}
	}

	/**
	 * @return есть ли ожидающий исполнения поток.
	 */
	public boolean isWait() {
		return wait > 0;
	}

	/**
	 * @param isWaiting есть ли ожидающий исполнения поток.
	 */
	public void setWait(boolean isWaiting) {
		wait = isWaiting ? wait + 1 : wait - 1;
	}

	/**
	 * @return состояние задачи.
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state состояние задачи.
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @return результат исполнения.
	 */
	public V getResult() {
		return result;
	}

	/**
	 * @param result результат исполнения.
	 */
	public void setResult(V result) {
		this.result = result;
	}

	/**
	 * @return исполняемая задача.
	 */
	public CallableTask<L, V> getTask() {
		return task;
	}
}
