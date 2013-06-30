package rlib.idfactory;

/**
 * Модель простого генератора ид.
 * 
 * @author Ronn
 */
public final class SimpleIdGenerator implements IdGenerator
{
	/** промежуток генерирование ид */
	private final int start;
	private final int end;
	
	/** следующий ид */
	private volatile int nextId;

	/**
	 * @param start стартовый ид генератора.
	 * @param end конечный ид генератора.
	 */
	public SimpleIdGenerator(int start, int end)
	{
		this.start = start;
		this.end = end;
		this.nextId = start;
	}
	
	@Override
	public synchronized int getNextId()
	{
		if(nextId == end)
			nextId = start;
		
		nextId += 1;
		
		return nextId;
	}

	@Override
	public void prepare(){}

	@Override
	public void releaseId(int id){}

	@Override
	public int usedIds()
	{
		return nextId - start;
	}
}
