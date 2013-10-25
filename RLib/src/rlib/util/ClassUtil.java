package rlib.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Класс с утильными методами.
 * 
 * @author Ronn
 */
public final class ClassUtil {

	private ClassUtil() {
		throw new RuntimeException();
	}

	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> cs) {

		try {
			return (T) cs.newInstance();
		} catch(InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Constructor<?> constructor, Object... objects) {

		try {
			return (T) constructor.newInstance(objects);
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Constructor<?> getConstructor(Class<?> cs, Class<?>... classes) {
		try {
			return cs.getConstructor(classes);
		} catch(NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
