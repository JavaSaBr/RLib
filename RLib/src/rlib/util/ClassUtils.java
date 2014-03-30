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

	private static final Logger LOGGER = LoggerManager.getLogger(ClassUtils.class);

	/**
	 * Получение конструктора по указанным параметрам указанного класса.
	 * 
	 * @param cs интересуемый класс.
	 * @param classes набор параметров конструктора.
	 * @return конструктор класса.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> getConstructor(Class<?> cs, Class<?>... classes) {
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
	public static <T> Constructor<T> getConstructor(String className, Class<?>... classes) {
		try {
			Class<?> cs = Class.forName(className);
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
	public static <T> T newInstance(Class<?> cs) {
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
	public static <T> T newInstance(Constructor<?> constructor, Object... objects) {
		try {
			return (T) constructor.newInstance(objects);
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOGGER.error(e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className) {
		try {
			return (T) Class.forName(className).newInstance();
		} catch(InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(String name) {
		try {
			return (Class<T>) Class.forName(name);
		} catch(ClassNotFoundException e) {
			LOGGER.error(e);
			throw new RuntimeException(e);
		}
	}

	private ClassUtils() {
		throw new RuntimeException();
	}
}
