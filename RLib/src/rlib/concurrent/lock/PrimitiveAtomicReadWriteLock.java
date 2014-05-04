package rlib.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.atomic.AtomicInteger;

/**
 * Реализация блокировщика с возможность асинхронного чтения и синхронной записи
 * на основе атомиков, без возможности использования вложенных вызовов с
 * приоритетом на запись.
 * 
 * Выгодно использовать посравнению с ReentrantReadWriteLock в случае, когда
 * записи намного реже чтения и в случае если необходимо уменьшить нагрузку на
 * GC.
 * 
 * Смысл этой реализации в создании легковесного блокировщика, который для
 * синхронизации не создает временных объектов.
 * 
 * @author Ronn
 */
public class PrimitiveAtomicReadWriteLock implements AsynReadSynWriteLock, Lock {

	public static final int STATUS_WRITE_COUNT_LOCKED = -1;
	public static final int STATUS_NOT_WRITE = 0;
	public static final int STATUS_WRITE_LOCKED = 1;
	public static final int STATUS_WRITE_UNLOCKED = 0;

	public static final int STATUS_READ_UNLOCKED = 0;
	public static final int STATUS_READ_LOCKED = -2;
	public static final int STATUS_READ_POST_INCREMENT_LOCKED = STATUS_READ_LOCKED + 1;

	/** статус блокировки на запись */
	private final AtomicInteger writeStatus;

	/** кол-во ожидающих потоков на запись */
	private final AtomicInteger writeCount;
	/** кол-во читающих потоков */
	private final AtomicInteger readCount;

	public PrimitiveAtomicReadWriteLock() {
		this.writeCount = new AtomicInteger(0);
		this.writeStatus = new AtomicInteger(0);
		this.readCount = new AtomicInteger(0);
	}

	@Override
	public void asynLock() {

		// ожидаем отсутствия желающих записать потоков
		final AtomicInteger writeCount = getWriteCount();
		while(writeCount.get() != 0);

		final AtomicInteger readCount = getReadCount();

		// добавляемся к читающим потокам и если после добавления оказалось что
		// мы добавились
		// после начало записи, возвращаем в исходную позицию счетчик и пробуем
		// еще раз.
		if(readCount.incrementAndGet() == STATUS_READ_POST_INCREMENT_LOCKED) {
			while(!readCount.compareAndSet(STATUS_READ_POST_INCREMENT_LOCKED, STATUS_READ_LOCKED));
			asynLock();
		}
	}

	@Override
	public void asynUnlock() {
		readCount.decrementAndGet();
	}

	/**
	 * @return кол-во читающих потоков.
	 */
	protected AtomicInteger getReadCount() {
		return readCount;
	}

	/**
	 * @return кол-во ожидающих потоков на запись.
	 */
	protected AtomicInteger getWriteCount() {
		return writeCount;
	}

	/**
	 * @return статус блокировки на запись.
	 */
	protected AtomicInteger getWriteStatus() {
		return writeStatus;
	}

	@Override
	public void lock() {
		synLock();
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		throw new RuntimeException("not supported.");
	}

	@Override
	public Condition newCondition() {
		throw new RuntimeException("not supported.");
	}

	@Override
	public void synLock() {

		// добавляемся в счетчик ожидающих записи потоков
		final AtomicInteger writeCount = getWriteCount();
		writeCount.incrementAndGet();

		// ждем завершения чтения другими потоками потоков и блокируем чтение
		final AtomicInteger readCount = getReadCount();
		while(!readCount.compareAndSet(STATUS_READ_UNLOCKED, STATUS_READ_LOCKED));

		// помечаем блокировку на процесс записи этим потоком
		final AtomicInteger writeStatus = getWriteStatus();
		while(!writeStatus.compareAndSet(STATUS_WRITE_UNLOCKED, STATUS_WRITE_LOCKED));
	}

	@Override
	public void synUnlock() {

		// помечаем завершение записи этим потоком
		final AtomicInteger writeStatus = getWriteStatus();
		while(!writeStatus.compareAndSet(STATUS_WRITE_LOCKED, STATUS_WRITE_UNLOCKED));

		// разблокируем возможность читать другими потоками
		final AtomicInteger readCount = getReadCount();
		while(!readCount.compareAndSet(STATUS_READ_LOCKED, STATUS_READ_UNLOCKED));

		// отмечаемся из счетчика ожидающих запись потоков
		final AtomicInteger writeCount = getWriteCount();
		writeCount.decrementAndGet();
	}

	@Override
	public boolean tryLock() {
		throw new RuntimeException("not supported.");
	}

	@Override
	public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {
		throw new RuntimeException("not supported.");
	}

	@Override
	public void unlock() {
		synUnlock();
	}
}
