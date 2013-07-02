package rlib.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.linkedlist.LinkedList;
import rlib.util.linkedlist.LinkedLists;

/**
 * Инициализатор друих менеджеров в указанном порядке.
 * 
 * @author Ronn
 */
public final class InitializeManager
{
	private static final Logger log = Loggers.getLogger(InitializeManager.class);
	
	private static final LinkedList<Class<?>> queue = LinkedLists.newLinkedList(Class.class);
	
	/**
	 * Инициализация зарегестрированных классов.
	 */
	public synchronized static void initialize()
	{
		for(final Iterator<Class<?>> iterator = queue.iterator(); iterator.hasNext();)
		{
			final Class<?> next = iterator.next();
			
			try
			{
				final Method method = next.getMethod("getInstance");
				
				final Object instance = method.invoke(null);
				
				if(instance == null)
					log.warning("no initialize class " + next);
				
				iterator.remove();
			}
			catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Регистрация класса, имеющего статический метод getInstance().
	 * 
	 * @param cs
	 */
	public synchronized static void register(Class<?> cs)
	{
		queue.add(cs);
	}
	
	/**
	 * Проверка валидности очереди инициализации класса.
	 * 
	 * @param cs проверяемый класс.
	 */
	public static void valid(Class<?> cs)
	{
		if(queue.getFirst() != cs)
			Thread.dumpStack();
	}
	
	private InitializeManager()
	{
		throw new RuntimeException();
	}
}
