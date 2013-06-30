package rlib.util;

import java.lang.reflect.Field;

import rlib.util.array.Array;
import rlib.util.array.Arrays;

/**
 * Набор утильных методов по работе с рефлексией.
 *
 * @author Ronn
 */
public final class ReflectionUtils
{
	private ReflectionUtils()
	{
		throw new IllegalArgumentException();
	}

	/**
	 * Получние всех полей указанного класса.
	 *
	 * @param container контейнер полей.
	 * @param cs нужный нам класс.
	 * @param last до какого класса извлекаем.
	 * @param declared извлекать ли приватные поля.
	 * @param exceptions исключаемые поля.
	 */
	public static void addAllFields(Array<Field> container, Class<?> cs, Class<?> last, boolean declared, String... exceptions)
	{
		// клас для извлечения полей
		Class<?> next = cs;

		// если есть обрабатываемый класс
		while(next != null && next != last)
		{
			// получаем его поля
			Field[] fields = declared? next.getDeclaredFields() : next.getFields();

			// получаем его суперкласс
			next = next.getSuperclass();

			// если полей нет, пропускаем
			if(fields.length < 1)
				continue;

			// если исключаемых нет, ложим все в контейнер
			if(exceptions == null || exceptions.length < 1)
				container.addAll(fields);
			else
			{
				// перебираем поля
				for(int i = 0, length = fields.length; i < length; i++)
				{
					Field field = fields[i];

					// проверяем на исключение
					if(Arrays.contains(exceptions, field.getName()))
						continue;

					// ложим в контейнер
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
	public static Array<Field> getAllFields(Class<?> cs, Class<?> last, boolean declared, String... exceptions)
	{
		Array<Field> container = Arrays.toArray(Field.class);

		addAllFields(container, cs, last, declared, exceptions);

		return container;
	}
}
