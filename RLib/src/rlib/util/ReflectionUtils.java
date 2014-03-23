package rlib.util;

import java.lang.reflect.Field;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * Набор утильных методов по работе с рефлексией.
 * 
 * @author Ronn
 */
public final class ReflectionUtils {

	/**
	 * Получние всех полей указанного класса.
	 * 
	 * @param container контейнер полей.
	 * @param cs нужный нам класс.
	 * @param last до какого класса извлекаем.
	 * @param declared извлекать ли приватные поля.
	 * @param exceptions исключаемые поля.
	 */
	public static void addAllFields(final Array<Field> container, final Class<?> cs, final Class<?> last, final boolean declared, final String... exceptions) {

		Class<?> next = cs;

		while(next != null && next != last) {

			final Field[] fields = declared ? next.getDeclaredFields() : next.getFields();

			next = next.getSuperclass();

			if(fields.length < 1) {
				continue;
			}

			if(exceptions == null || exceptions.length < 1) {
				container.addAll(fields);
			} else {

				for(int i = 0, length = fields.length; i < length; i++) {

					final Field field = fields[i];

					if(ArrayUtils.contains(exceptions, field.getName())) {
						continue;
					}

					container.add(field);
				}
			}
		}
	}

	/**
	 * Получние всех полей указанного класса.
	 * 
	 * @param cs нужный нам класс.
	 * @param last до какого класса извлекаем.
	 * @param declared извлекать ли приватные поля.
	 * @param exceptions исключаемые поля.
	 */
	public static Array<Field> getAllFields(final Class<?> cs, final Class<?> last, final boolean declared, final String... exceptions) {
		final Array<Field> container = ArrayFactory.newArray(Field.class);
		addAllFields(container, cs, last, declared, exceptions);
		return container;
	}

	private ReflectionUtils() {
		throw new IllegalArgumentException();
	}
}
