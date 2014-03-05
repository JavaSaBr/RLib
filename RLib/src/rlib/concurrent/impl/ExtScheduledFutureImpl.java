package rlib.concurrent.impl;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import rlib.concurrent.interfaces.CallableTask;
import rlib.concurrent.interfaces.ExtScheduledFuture;

/**
 * Реализация контролерра отложенных задач.
 * 
 * @author Ronn
 */
public class ExtScheduledFutureImpl<L, V> extends ExtFutureTaskImpl<L, V> implements ExtScheduledFuture<L, V> {

	public static enum ScheduleType {
		DELAY,
		AT_FIXED_RATE,
		WITH_FIXED_DELAY,
	}

	/** сервис, который исполняет задачу */
	private final ExtScheduledThreadPoolImpl<L> executorService;

	/** тип расписания */
	private final ScheduleType scheduleType;

	/** задержка перед первым исполнением */
	private final long delay;
	/** интервал между исполнениями */
	private final long interval;

	/** первый ли запуск */
	private boolean first;

	public ExtScheduledFutureImpl(CallableTask<L, V> task, ExtScheduledThreadPoolImpl<L> executorService, ScheduleType scheduleType, long delay, long interval) {
		super(task);
		this.executorService = executorService;
		this.scheduleType = scheduleType;
		this.delay = delay;
		this.interval = interval;
	}

	@Override
	public int compareTo(Delayed o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getDelay() {
		return delay;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getInterval() {
		return interval;
	}

	public ScheduleType getScheduleType() {
		return scheduleType;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}
}
