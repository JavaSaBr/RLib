package rlib.util.linkedlist;

import java.util.Collection;

import rlib.util.array.FuncElement;

/**
 * @author Ronn
 */
public abstract class AbstractLinkedList<E> implements LinkedList<E>
{
	private static final long serialVersionUID = 8034712584065781997L;

	/** тип элементов в коллекции */
	protected final Class<E> type;

	@SuppressWarnings("unchecked")
	public AbstractLinkedList(Class<?> type)
	{
		this.type = (Class<E>) type;
	}
	
	/**
	 * @return тип элементов в коллекции.
	 */
	protected Class<E> getType()
	{
		return type;
	}

	@Override
	public boolean isEmpty()
	{
		return size() < 1;
	}

	@Override
	public boolean containsAll(Collection<?> collection)
	{
		for(Object object : collection)
			if(!contains(object))
				return false;
		
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection)
	{
		for(E object : collection)
			if(!add(object))
				return false;
		
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> collection)
	{
		for(Object object : collection)
			if(!remove(object))
				return false;
		
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> collection)
	{
		for(E object : this)
			if(!collection.contains(object) && !remove(object))
				return false;
		
		return true;
	}
	
	@Override
	public void finalyze()
	{
		clear();
	}

	@Override
	public void reinit(){}

	@Override
	public void writeLock(){}

	@Override
	public void writeUnlock(){}

	@Override
	public void apply(FuncElement<? super E> func)
	{
		for(E element : this)
			func.apply(element);
	}

	@Override
	public void readLock(){}

	@Override
	public void readUnlock(){}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		
		builder.append(" size = ").append(size()).append(" : [");
		
		if(!isEmpty())
		{
			for(E element : this)
				builder.append(element).append(", ");
			
			if(builder.indexOf(",") != -1)
				builder.delete(builder.length() - 2, builder.length());
		}
		
		builder.append("]");
		
		return builder.toString();
	}
}
