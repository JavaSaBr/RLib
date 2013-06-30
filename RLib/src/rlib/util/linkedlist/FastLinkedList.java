package rlib.util.linkedlist;

import java.util.Iterator;
import java.util.NoSuchElementException;

import rlib.util.pools.Foldable;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.Pools;

/**
 * @author Ronn
 */
public class FastLinkedList<E> extends AbstractLinkedList<E>
{
	private final class IteratorImpl implements Iterator<E>
	{
		/** режим итерирования с начала в конец */
		private static final int NEXT = 1;
		/** режим итерирования с конца в начало */
		private static final int PREV = 2;

		/** последний возвращаемый элемнт */
		private Node<E> lastReturned;
		/** следующий элемент */
		private Node<E> next;

		/** режим итератора */
		private final int mode;

		/** следующий индекс */
		private int nextIndex;

		private IteratorImpl(int mode)
		{
			this.nextIndex = 0;
			this.mode = mode;
			setNext(mode == NEXT ? getFirstNode() : mode == PREV ? getLastNode() : null);
		}

		private Node<E> getLastReturned()
		{
			return lastReturned;
		}

		private Node<E> getNext()
		{
			return next;
		}

		@Override
		public boolean hasNext()
		{
			return mode == NEXT ? nextIndex < size() : mode == PREV ? nextIndex > 0 : false;
		}

		@Override
		public E next()
		{
			if(!hasNext())
				throw new NoSuchElementException();

			if(mode == NEXT)
			{
				Node<E> next = getNext();
				Node<E> lastReturned = next;

				setNext(next.getNext());
				setLastReturned(lastReturned);

				nextIndex++;

				return lastReturned.getItem();
			}
			else if(mode == PREV)
			{
				Node<E> next = getNext();
				Node<E> lastReturned = next;
				
				setNext(next.getPrev());
				setLastReturned(lastReturned);

				nextIndex--;

				return lastReturned.getItem();
			}

			return null;
		}

		@Override
		public void remove()
		{
			Node<E> lastReturned = getLastReturned();

			if(lastReturned == null)
				throw new IllegalStateException();

			unlink(lastReturned);

			if(mode == NEXT)
				nextIndex--;
			else if(mode == PREV)
				nextIndex++;
			
			setLastReturned(null);
		}

		private void setLastReturned(Node<E> lastReturned)
		{
			this.lastReturned = lastReturned;
		}

		private void setNext(Node<E> next)
		{
			this.next = next;
		}
	}

	private static final class Node<E> implements Foldable
	{
		/** содержимый итем */
		private E item;

		/** ссылка на предыдущий узел */
		private Node<E> prev;
		/** ссылка на след. узел */
		private Node<E> next;

		@Override
		public void finalyze()
		{
			item = null;
			prev = null;
			next = null;
		}

		/**
		 * @return хранимый итем.
		 */
		public E getItem()
		{
			return item;
		}

		/**
		 * @return следующий узел.
		 */
		public Node<E> getNext()
		{
			return next;
		}

		/**
		 * @return предыдущий узел.
		 */
		public Node<E> getPrev()
		{
			return prev;
		}

		@Override
		public void reinit(){}

		/**
		 * @param item хранимый итем.
		 */
		public void setItem(E item)
		{
			this.item = item;
		}

		/**
		 * @param next следующий узел.
		 */
		public void setNext(Node<E> next)
		{
			this.next = next;
		}

		/**
		 * @param prev предыдущий узел.
		 */
		public void setPrev(Node<E> prev)
		{
			this.prev = prev;
		}
	}
	
	private static final long serialVersionUID = 6627882787737291879L;

	/** пул узлов */
	private final FoldablePool<Node<E>> pool;

	/** первый элемент списка */
	private Node<E> first;
	/** последний элемент списка */
	private Node<E> last;

	/** размер списка */
	private int size;

	public FastLinkedList(Class<?> type)
	{
		super(type);
		this.pool = Pools.newFoldablePool(Node.class);
	}

	@Override
	public boolean add(E element)
	{
		if(element == null)
			throw new NullPointerException("element is null.");

		linkLast(element);
		return true;
	}

	@Override
	public void addFirst(E element)
	{
		linkFirst(element);
	}

	@Override
	public void addLast(E element)
	{
		linkLast(element);
	}

	@Override
	public void clear()
	{
		FoldablePool<Node<E>> pool = getPool();

		for(Node<E> node = getFirstNode(); node != null; node = node.getNext())
			pool.put(node);

		setFirstNode(null);
		setLastNode(null);

		size = 0;
	}

	@Override
	public boolean contains(Object object)
	{
		return indexOf(object) != -1;
	}

	@Override
	public Iterator<E> descendingIterator()
	{
		return new IteratorImpl(IteratorImpl.PREV);
	}

	@Override
	public E element()
	{
		return getFirst();
	}

	@Override
	public E get(int index)
	{
		return index < (size() >> 1) ? getFirst(index) : getLast(index);
	}

	@Override
	public E getFirst()
	{
		Node<E> first = getFirstNode();

		if(first == null)
			throw new NoSuchElementException();

		return first.getItem();
	}

	private final E getFirst(int index)
	{
		int i = 0;

		for(Node<E> node = getFirstNode(); node != null; node = node.getNext())
		{
			if(i == index)
				return node.getItem();

			i++;
		}

		return null;
	}

	/**
	 * @return первый узел.
	 */
	private final Node<E> getFirstNode()
	{
		return first;
	}
	
	@Override
	public E getLast()
	{
		Node<E> last = getLastNode();

		if(last == null)
			throw new NoSuchElementException();

		return last.getItem();
	}

	private final E getLast(int index)
	{
		int i = size() - 1;

		for(Node<E> node = getLastNode(); node != null; node = node.getPrev())
		{
			if(i == index)
				return node.getItem();

			i--;
		}

		return null;
	}

	/**
	 * @return последний узел.
	 */
	private final Node<E> getLastNode()
	{
		return last;
	}

	/**
	 * Получение нового узла по указанным параметрам.
	 * 
	 * @param prev предыдущий узел.
	 * @param item хранимый итем.
	 * @param next следующий узел.
	 * @return новый узел.
	 */
	private Node<E> getNewNode(Node<E> prev, E item, Node<E> next)
	{
		Node<E> node = getPool().take();

		if(node == null)
			node = new Node<E>();

		node.setItem(item);
		node.setNext(next);
		node.setPrev(prev);

		return node;
	}

	/**
	 * @return пул узлов.
	 */
	public FoldablePool<Node<E>> getPool()
	{
		return pool;
	}

	@Override
	public int indexOf(Object object)
	{
		int index = 0;

		for(Node<E> node = getFirstNode(); node != null; node = node.getNext())
		{
			E item = node.getItem();

			if(item.equals(object))
				return index;

			index++;
		}

		return -1;
	}

	@Override
	public Iterator<E> iterator()
	{
		return new IteratorImpl(IteratorImpl.NEXT);
	}

	private final void linkFirst(E eitem)
	{
		Node<E> first = getFirstNode();
		Node<E> newNode = getNewNode(null, eitem, first);

		setFirstNode(newNode);

		if(first == null)
			setLastNode(newNode);
		else
			first.setPrev(newNode);

		size++;
	}

	private final void linkLast(E item)
	{
		Node<E> last = getLastNode();
		Node<E> newNode = getNewNode(last, item, null);

		setLastNode(newNode);

		if(last == null)
			setFirstNode(newNode);
		else
			last.setNext(newNode);

		size++;
	}

	@Override
	public boolean offer(E element)
	{
		return add(element);
	}

	@Override
	public boolean offerFirst(E element)
	{
		addFirst(element);
		return true;
	}

	@Override
	public boolean offerLast(E element)
	{
		addLast(element);
		return true;
	}

	@Override
	public E peek()
	{
		Node<E> first = getFirstNode();
		
        return (first == null) ? null : first.getItem();
	}

	@Override
	public E peekFirst()
	{
		Node<E> first = getFirstNode();

		return (first == null) ? null : first.getItem();
	}

	@Override
	public E peekLast()
	{
		Node<E> last = getLastNode();

		return (last == null) ? null : last.getItem();
	}

	@Override
	public E poll()
	{
		Node<E> first = getFirstNode();
		
        return (first == null) ? null : unlinkFirst(first);
	}

	@Override
	public E pollFirst()
	{
		Node<E> first = getFirstNode();

		return (first == null) ? null : unlinkFirst(first);
	}

	@Override
	public E pollLast()
	{
		Node<E> last = getLastNode();
		
		return (last == null) ? null : unlinkLast(last);
	}

	@Override
	public E pop()
	{
		return removeFirst();
	}

	@Override
	public void push(E element)
	{
		addFirst(element);
	}

	@Override
	public E remove()
	{
		return removeFirst();
	}

	@Override
	public boolean remove(Object object)
	{
		if(object == null)
			throw new NullPointerException("object is null.");

		for(Node<E> node = getFirstNode(); node != null; node = node.getNext())
			if(object.equals(node.getItem()))
			{
				unlink(node);
				return true;
			}

		return false;
	}

	@Override
	public E removeFirst()
	{
		Node<E> first = getFirstNode();

		if(first == null)
			throw new NoSuchElementException();

		return unlinkFirst(first);
	}

	@Override
	public boolean removeFirstOccurrence(Object object)
	{
		if(object == null)
			throw new NullPointerException("not fond object.");

		for(Node<E> node = getFirstNode(); node != null; node = node.getNext())
			if(object.equals(node.getItem()))
			{
				unlink(node);
				return true;
			}

		return false;
	}

	@Override
	public E removeLast()
	{
		Node<E> last = getLastNode();

		if(last == null)
			throw new NoSuchElementException();

		return unlinkLast(last);
	}

	@Override
	public boolean removeLastOccurrence(Object object)
	{
		if(object == null)
			throw new NullPointerException("not fond object.");

		for(Node<E> node = getLastNode(); node != null; node = node.getPrev())
			if(object.equals(node.getItem()))
			{
				unlink(node);
				return true;
			}

		return false;
	}

	/**
	 * @param first первый узел.
	 */
	private void setFirstNode(Node<E> first)
	{
		this.first = first;
	}

	/**
	 * @param last последний узел.
	 */
	private void setLastNode(Node<E> last)
	{
		this.last = last;
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public E take()
	{
		return removeFirst();
	}

	@Override
	public Object[] toArray()
	{
		Object[] array = (Object[]) java.lang.reflect.Array.newInstance(getType(), size());

		int index = 0;

		for(Node<E> node = getFirstNode(); node != null; node = node.getNext())
			array[index++] = node.getItem();

		return array;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] array)
	{
		int size = size();

		if(array.length < size)
			array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);

		int i = 0;

		Object[] result = array;

		for(Node<E> node = getFirstNode(); node != null; node = node.getNext())
			result[i++] = node.getItem();

		return array;
	}

	private final E unlink(Node<E> node)
	{
		E element = node.getItem();

		Node<E> next = node.getNext();
		Node<E> prev = node.getPrev();

		if(prev == null)
			setFirstNode(next);
		else
			prev.setNext(next);

		if(next == null)
			setLastNode(prev);
		else
			next.setPrev(prev);

		size--;

		getPool().put(node);

		return element;
	}

	private final E unlinkFirst(Node<E> node)
	{
		E element = node.getItem();

		Node<E> next = node.getNext();

		setFirstNode(next);

		if(next == null)
			setLastNode(null);
		else
			next.setPrev(null);

		size--;

		getPool().put(node);

		return element;
	}

	private final E unlinkLast(Node<E> node)
	{
		E element = node.getItem();

		Node<E> prev = node.getPrev();

		setLastNode(prev);

		if(prev == null)
			setFirstNode(null);
		else
			prev.setNext(null);

		size--;

		getPool().put(node);

		return element;
	}
	
	@Override
	public String toString()
	{
		return super.toString() + "\n " + pool;
	}
}
