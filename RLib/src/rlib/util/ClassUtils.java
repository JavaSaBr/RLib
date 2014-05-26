package rlib.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * Класс с утильными методами по работе с классами.
 * 
 * @author Ronn
 */
public final class ClassUtils {

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(final String name) {
		try {
			return (Class<T>) Class.forName(name);
		} catch(final ClassNotFoundException e) {
			LOGGER.error(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Получение конструктора по указанным параметрам указанного класса.
	 * 
	 * @param cs интересуемый класс.
	 * @param classes набор параметров конструктора.
	 * @return конструктор класса.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> getConstructor(final Class<?> cs, final Class<?>... classes) {
		try {
			return (Constructor<T>) cs.getConstructor(classes);
		} catch(NoSuchMethodException | SecurityException e) {
			LOGGER.error(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Получение конструктора класса по его имени и набору параметров.
	 * 
	 * @param className название класса.
	 * @param classes список параметров конструктора.
	 * @return конструктор класса.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> getConstructor(final String className, final Class<?>... classes) {
		try {
			final Class<?> cs = Class.forName(className);
			return (Constructor<T>) cs.getConstructor(classes);
		} catch(NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			LOGGER.error(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Создание нового экземпляра класса через стандартный конструктор.
	 * 
	 * @param cs интересуемый класс.
	 * @return новый экземпляр класса.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(final Class<?> cs) {
		try {
			return (T) cs.newInstance();
		} catch(InstantiationException | IllegalAccessException e) {
			LOGGER.error(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Создание нового экземпляра класса через указанный конструктор.
	 * 
	 * @param constructor конструктор класса.
	 * @param objects набор параметров дял конструктора.
	 * @return новый экземпляр класса.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(final Constructor<?> constructor, final Object... objects) {
		try {
			return (T) constructor.newInstance(objects);
		} catch(final InvocationTargetException e) {
			LOGGER.error(e.getTargetException());
			throw new RuntimeException(e);
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException e) {
			LOGGER.error(e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T newInstance(final String className) {
		try {
			return (T) Class.forName(className).newInstance();
		} catch(InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			LOGGER.error(e);
			throw new RuntimeException(e);
		}
	}

	private static final Logger LOGGER = LoggerManager.getLogger(ClassUtils.class);

	private ClassUtils() {
		throw new RuntimeException();
	}
}
