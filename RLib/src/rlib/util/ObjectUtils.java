package rlib.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * Класс для работы с объектами.
 * 
 * @author Ronn
 * @created 07.04.2012
 */
public final class ObjectUtils {

	private static final Logger LOGGER = LoggerManager.getLogger(ObjectUtils.class);

	/**
	 * Клонирует объект, крайне медленная функция.
	 * 
	 * @param original оригинальный объект.
	 * @return новая копия.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(final T original) {

		if(original == null) {
			return null;
		}

		if(original instanceof Cloneable) {

			try {

				final Method method = original.getClass().getMethod("clone");
				method.setAccessible(true);

				return (T) method.invoke(original);

			} catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOGGER.warning(e);
			}

			return null;
		}

		final Object newObject = newInstance(original.getClass());
		reload(newObject, original);

		return (T) newObject;
	}

	public static boolean equals(final Object first, final Object second) {
		return first == second || first != null && first.equals(second);
	}

	/**
	 * Рассчет хэша флага.
	 * 
	 * @param value значение флага.
	 * @return хэш флага.
	 */
	public static int hash(final boolean value) {
		return value ? 1231 : 1237;
	}

	/**
	 * Рассчет хэша числа.
	 * 
	 * @param value значение числа.
	 * @return хэш числа.
	 */
	public static int hash(final long value) {
		return (int) (value ^ value >>> 32);
	}

	/**
	 * Рассчет хэша объекта.
	 * 
	 * @param object хэшеируемый объект.
	 * @return хэш объекта.
	 */
	public static int hash(final Object object) {
		return object.hashCode();
	}

	/**
	 * Создает новый объект указанного класса, крайне медленная функция.
	 * 
	 * @param cs класс объекта, который нужно создать.
	 * @return новый экземпляр объекта.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(final Class<T> cs) {

		if(cs == Boolean.class || cs == boolean.class) {
			return (T) Boolean.valueOf(false);
		} else if(cs == Character.class || cs == char.class) {
			return (T) Character.valueOf('x');
		} else if(cs == Byte.class || cs == byte.class) {
			return (T) Byte.valueOf((byte) 0);
		} else if(cs == Short.class || cs == short.class) {
			return (T) Short.valueOf((short) 0);
		} else if(cs == Integer.class || cs == int.class) {
			return (T) Integer.valueOf(0);
		} else if(cs == Long.class || cs == long.class) {
			return (T) Long.valueOf(0);
		} else if(cs == Float.class || cs == float.class) {
			return (T) Float.valueOf(0);
		} else if(cs == Double.class || cs == double.class) {
			return (T) Double.valueOf(0);
		} else if(cs == String.class) {
			return cs.cast("");
		} else if(cs == Class.class) {
			return (T) Object.class;
		}

		for(final Constructor<?> constructor : cs.getDeclaredConstructors()) {

			if(!constructor.isAccessible()) {
				constructor.setAccessible(true);
			}

			final Class<?>[] types = constructor.getParameterTypes();
			final Object[] parametrs = new Object[types.length];

			for(int i = 0, length = types.length; i < length; i++) {
				final Object object = newInstance(types[i]);
				parametrs[i] = object;
			}

			try {
				return (T) constructor.newInstance(parametrs);
			} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOGGER.warning(e);
			}
		}

		return null;
	}

	/**
	 * Обновляет полностью объект на новый вариант, крайне медленный метод.
	 * 
	 * @param original обновляемый объект.
	 * @param updated объект, с которого нужно взять значения.
	 */
	public static final <O, N extends O> void reload(final O original, final N updated) {

		if(original == null || updated == null) {
			return;
		}

		final Array<Field> array = ArrayFactory.newArray(Field.class);

		for(Class<?> cs = original.getClass(); cs != null; cs = cs.getSuperclass()) {

			final Field[] fields = cs.getDeclaredFields();

			for(final Field field : fields) {
				array.add(field);
			}
		}

		array.trimToSize();

		for(final Field field : array) {

			final String str = field.toString();

			if(str.contains("final") || str.contains("static")) {
				continue;
			}

			field.setAccessible(true);

			try {
				field.set(original, field.get(updated));
			} catch(IllegalArgumentException | IllegalAccessException e) {
				LOGGER.warning(e);
			}
		}
	}
}
