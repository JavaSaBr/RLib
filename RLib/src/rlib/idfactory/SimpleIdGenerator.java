package rlib.idfactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Модель простого генератора ид.
 * 
 * @author Ronn
 */
public final class SimpleIdGenerator implements IdGenerator {

	/** промежуток генерирование ид */
	private final int start;
	private final int end;

	/** следующий ид */
	private final AtomicInteger nextId;

	/**
	 * @param start стартовый ид генератора.
	 * @param end конечный ид генератора.
	 */
	public SimpleIdGenerator(int start, int end) {
		this.start = start;
		this.end = end;
		this.nextId = new AtomicInteger(start);
	}

	@Override
	public int getNextId() {
		nextId.compareAndSet(end, start);
		return nextId.incrementAndGet();
	}

	@Override
	public void prepare() {
	}

	@Override
	public void releaseId(int id) {
	}

	@Override
	public int usedIds() {
		return nextId.get() - start;
	}
}
