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
public final class InitializeManager {

	private static final Logger LOGGER = Loggers.getLogger(InitializeManager.class);

	private static final String METHOD_NAME = "getInstance";

	private static final LinkedList<Class<?>> queue = LinkedLists.newLinkedList(Class.class);

	/**
	 * Инициализация зарегестрированных классов.
	 */
	public synchronized static void initialize() {

		for(final Iterator<Class<?>> iterator = queue.iterator(); iterator.hasNext();) {

			final Class<?> next = iterator.next();

			try {

				final Method method = next.getMethod(METHOD_NAME);
				final Object instance = method.invoke(null);

				if(instance == null) {
					LOGGER.warning("no initialize class " + next);
				}

				iterator.remove();

			} catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOGGER.warning(e);
			}
		}
	}

	/**
	 * Регистрация класса, имеющего статический метод getInstance().
	 */
	public synchronized static void register(Class<?> cs) {
		queue.add(cs);
	}

	/**
	 * Проверка валидности очереди инициализации класса.
	 * 
	 * @param cs проверяемый класс.
	 */
	public static void valid(Class<?> cs) {
		if(queue.getFirst() != cs) {
			Thread.dumpStack();
		}
	}

	private InitializeManager() {
		throw new RuntimeException();
	}
}
