package rlib;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.SafeTask;
import rlib.util.array.Array;
import rlib.util.array.ArrayUtils;

/**
 * Модель поиска и обнаружения делоков.
 * 
 * @author Ronn
 */
public class DeadLockDetector extends SafeTask {

	private static final Logger LOGGER = Loggers.getLogger(DeadLockDetector.class);

	/** набор слушателей дедлоков */
	private final Array<DeadLockListener> listeners;

	/** информация об состоянии потоков */
	private final ThreadMXBean mxThread;
	/** сервис по запуску детектора */
	private final ScheduledExecutorService executor;

	/** ссылка на исполение детектора */
	private volatile ScheduledFuture<?> schedule;

	/** интервал детектора */
	private final int interval;

	public DeadLockDetector(final int interval) {

		if(interval < 1) {
			throw new IllegalArgumentException("negative interval.");
		}

		this.listeners = ArrayUtils.toConcurrentArray(DeadLockListener.class);
		this.mxThread = ManagementFactory.getThreadMXBean();
		this.executor = Executors.newSingleThreadScheduledExecutor();
		this.interval = interval;
	}

	/**
	 * Добавление слушателя мертвых блокировок.
	 * 
	 * @param listener новый слушатель.
	 */
	public void addListener(final DeadLockListener listener) {
		listeners.add(listener);
	}

	/**
	 * @return список слушателей.
	 */
	public Array<DeadLockListener> getListeners() {
		return listeners;
	}

	@Override
	protected void runImpl() {

		final long[] threadIds = mxThread.findDeadlockedThreads();

		if(threadIds.length < 1) {
			return;
		}

		Array<DeadLockListener> listeners = getListeners();

		for(int i = 0, length = threadIds.length; i < length; i++) {

			final long id = threadIds[i];

			final ThreadInfo info = mxThread.getThreadInfo(id);

			if(listeners.isEmpty()) {
				continue;
			}

			listeners.readLock();
			try {

				for(DeadLockListener listener : listeners.array()) {

					if(listener == null) {
						break;
					}

					listener.onDetected(info);
				}

			} finally {
				listeners.readUnlock();
			}

			LOGGER.warning("DeadLock detected! : " + info);
		}
	}

	/**
	 * Запуск детектора.
	 */
	public synchronized void start() {

		if(schedule != null) {
			return;
		}

		schedule = executor.scheduleAtFixedRate(this, interval, interval, TimeUnit.MILLISECONDS);
	}

	/**
	 * Остановка детектора.
	 */
	public synchronized void stop() {

		if(schedule == null) {
			return;
		}

		schedule.cancel(false);
		schedule = null;
	}
}
