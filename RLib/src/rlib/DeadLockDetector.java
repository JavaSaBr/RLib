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
import rlib.util.array.Arrays;


/**
 * Модель поиска и обнаружения делоков.
 *
 * @author Ronn
 */
public class DeadLockDetector extends SafeTask
{
	private static final Logger log = Loggers.getLogger(DeadLockDetector.class);

	/** набор слушателей дедлоков */
	private final Array<DeadLockListener> listeners;

	/** информация об состоянии потоков */
	private final ThreadMXBean mxThread;

	/** сервис по запуску детектора */
	private final ScheduledExecutorService executor;

	/** ссылка на исполение детектора */
	private volatile ScheduledFuture<?> schedule;

	/** интервал детектора */
	private int interval;

	public DeadLockDetector(int interval)
	{
		if(interval < 1)
			throw new IllegalArgumentException("negative interval.");

		this.listeners = Arrays.toConcurrentArray(DeadLockListener.class);
		this.mxThread = ManagementFactory.getThreadMXBean();
		this.executor = Executors.newSingleThreadScheduledExecutor();
		this.interval = interval;
	}

	@Override
	protected void runImpl()
	{
		// ищем дедлоки
		long[] threadIds = mxThread.findDeadlockedThreads();

		// если не нашли, пропускаем цикл
		if(threadIds.length < 1)
			return;

		// перебираем ид найденных деллоков
		for(int i = 0, length = threadIds.length; i < length; i++)
		{
			// ид потока
			long id = threadIds[i];

			// получаем инфу о потоке
			ThreadInfo info = mxThread.getThreadInfo(id);

			// если есть заинтересованные слушатели
			if(!listeners.isEmpty())
			{
				listeners.readLock();
				try
				{
					// получаем список слушателей
					DeadLockListener[] array = listeners.array();

					// уведомляем слушателей об этом
					for(int g = 0, size = listeners.size(); g < size; g++)
						array[g].onDetected(info);
				}
				finally
				{
					listeners.readUnlock();
				}
			}

			// пишем в консоль инфу о дедлоке
			log.warning("DeadLock detected! : " + info);
		}
	}

	/**
	 * Запуск детектора.
	 */
	public synchronized void start()
	{
		if(schedule != null)
			return;

		schedule = executor.scheduleAtFixedRate(this, interval, interval, TimeUnit.MILLISECONDS);
	}

	/**
	 * Остановка детектора.
	 */
	public synchronized void stop()
	{
		if(schedule == null)
			return;

		schedule.cancel(false);

		schedule = null;
	}

	/**
	 * Добавление слушателя мертвых блокировок.
	 *
	 * @param listener новый слушатель.
	 */
	public void addListener(DeadLockListener listener)
	{
		listeners.add(listener);
	}
}
