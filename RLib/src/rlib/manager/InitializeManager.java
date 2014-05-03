package rlib.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.linkedlist.LinkedList;
import rlib.util.linkedlist.LinkedListFactory;

/**
 * Инициализатор синглетонов в указанном порядке.
 * 
 * @author Ronn
 */
public final class InitializeManager {

	private static final Logger LOGGER = LoggerManager.getLogger(InitializeManager.class);

	private static final String METHOD_NAME = "getInstance";

	private static final LinkedList<Class<?>> QUEUE = LinkedListFactory.newLinkedList(Class.class);

	/**
	 * Инициализация зарегестрированных классов.
	 */
	public synchronized static void initialize() {

		for(final Iterator<Class<?>> iterator = QUEUE.iterator(); iterator.hasNext();) {

			final Class<?> next = iterator.next();

			try {

				final Method method = next.getMethod(METHOD_NAME);
				final Object instance = method.invoke(null);

				if(instance == null) {
					LOGGER.warning("no initialize class " + next);
				}

				iterator.remove();

			} catch(final InvocationTargetException e) {
				LOGGER.warning(e.getTargetException());
			} catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
				LOGGER.warning(e);
			}
		}
	}

	/**
	 * Регистрация класса, имеющего статический метод getInstance().
	 */
	public synchronized static void register(final Class<?> cs) {
		QUEUE.add(cs);
	}

	/**
	 * Проверка валидности очереди инициализации класса.
	 * 
	 * @param cs проверяемый класс.
	 */
	public static void valid(final Class<?> cs) {
		if(QUEUE.getFirst() != cs) {
			Thread.dumpStack();
		}
	}

	private InitializeManager() {
		throw new RuntimeException();
	}
}
