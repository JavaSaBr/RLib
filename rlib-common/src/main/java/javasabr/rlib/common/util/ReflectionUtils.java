package javasabr.rlib.common.util;

import static javasabr.rlib.common.util.ArrayUtils.contains;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javasabr.rlib.common.util.array.Array;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The class with utility reflection methods.
 *
 * @author JavaSaBr
 */
@NullMarked
public final class ReflectionUtils {

  /**
   * Get all fields of the class.
   *
   * @param container the field container.
   * @param startClass the class.
   * @param lastClass the last class.
   * @param declared the flag to get private fields as well.
   * @param exceptions exception fields.
   */
  public static void addAllFields(
      Array<Field> container,
      Class<?> startClass,
      Class<?> lastClass,
      boolean declared,
      String... exceptions) {

    var next = startClass;

    while (next != null && next != lastClass) {

      var fields = declared ? next.getDeclaredFields() : next.getFields();

      next = next.getSuperclass();

      if (fields.length < 1) {
        continue;
      }

      if (exceptions.length < 1) {
        container.addAll(fields);
      } else {
        ArrayUtils.forEach(fields, toCheck -> !contains(exceptions, toCheck.getName()), container::add);
      }
    }
  }

  /**
   * Get all fields of a class.
   *
   * @param cs the class.
   * @param exceptions exception fields.
   * @return the all declared fields.
   * @since 9.9.0
   */
  public static Array<Field> getAllDeclaredFields(Class<?> cs, String... exceptions) {
    var container = Array.ofType(Field.class);
    addAllFields(container, cs, Object.class, true, exceptions);
    return container;
  }

  /**
   * Get all fields of the class.
   *
   * @param cs the class.
   * @param last the last class.
   * @param declared the flag of getting private fields.
   * @param exceptions exception fields.
   * @return the all fields
   */
  public static Array<Field> getAllFields(
      Class<?> cs,
      Class<?> last,
      boolean declared,
      String... exceptions) {
    var container = Array.ofType(Field.class);
    addAllFields(container, cs, last, declared, exceptions);
    return container;
  }

  /**
   * Get a field by the name from the type.
   *
   * @param type the type.
   * @param fieldName the field name.
   * @return the field.
   */
  public static Field getField(Class<?> type, String fieldName) {
    try {
      return type.getDeclaredField(fieldName);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get a field by the name from the type of the object.
   *
   * @param object the object.
   * @param fieldName the field name.
   * @return the field.
   */
  public static Field getField(Object object, String fieldName) {
    return getField(object.getClass(), fieldName);
  }

  /**
   * Get a field by the name with full access from the type.
   *
   * @param type the type.
   * @param fieldName the field name.
   * @return the field.
   */
  public static Field getUnsafeField(Class<?> type, String fieldName) {
    try {
      Field field = getField(type, fieldName);
      field.setAccessible(true);
      return field;
    } catch (SecurityException | IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get a field by the name with full access.
   *
   * @param object the object.
   * @param fieldName the field name.
   * @return the field.
   */
  public static Field getUnsafeField(Object object, String fieldName) {
    return getUnsafeField(object.getClass(), fieldName);
  }

  /**
   * Get a field value by the field name.
   *
   * @param <T> the result value's type.
   * @param object the object.
   * @param fieldName the field name.
   * @return the value.
   */
  public static <T> @Nullable T getFiledValue(Object object, String fieldName) {
    try {
      Field field = getField(object, fieldName);
      return ClassUtils.unsafeCast(field.get(object));
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get a field value by the field name with full access.
   *
   * @param <T> the result value's type.
   * @param object the object.
   * @param fieldName the field name.
   * @return the value.
   */
  public static <T> @Nullable T getUnsafeFieldValue(final Object object, final String fieldName) {
    try {
      final Field field = getUnsafeField(object, fieldName);
      return ClassUtils.unsafeCast(field.get(object));
    } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get a field value.
   *
   * @param <T> the result value's type.
   * @param object the object.
   * @param field the field.
   * @return the value.
   */
  public static <T> @Nullable T getFieldValue(Object object, Field field) {
    try {
      return ClassUtils.unsafeCast(field.get(object));
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Set a field value.
   *
   * @param object the object.
   * @param fieldName the field name.
   * @param value the value.
   */
  public static void setFieldValue(Object object, String fieldName, Object value) {
    try {
      Field field = getField(object, fieldName);
      field.set(object, value);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Set a field value using full access.
   *
   * @param object the object.
   * @param fieldName the field name.
   * @param value the value.
   */
  public static void setUnsafeFieldValue(Object object, String fieldName, Object value) {
    try {
      Field field = getUnsafeField(object, fieldName);
      field.set(object, value);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Set a field value.
   *
   * @param object the object.
   * @param field the field.
   * @param value the value.
   */
  public static void setFieldValue(Object object, Field field, Object value) {
    try {
      field.set(object, value);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get a static field.
   *
   * @param type the class.
   * @param fieldName the field name.
   * @return the static field.
   */
  public static Field getStaticField(Class<?> type, String fieldName) {
    try {
      return type.getDeclaredField(fieldName);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get a static field using full access.
   *
   * @param type the class.
   * @param fieldName the field name.
   * @return the static field.
   */
  public static Field getUnsafeStaticField(Class<?> type, String fieldName) {
    try {
      Field field = getStaticField(type, fieldName);
      field.setAccessible(true);
      return field;
    } catch (SecurityException | IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get a static field value.
   *
   * @param <T> the type parameter
   * @param type the class.
   * @param fieldName the field name.
   * @return the value.
   */
  public static <T> @Nullable T getStaticFieldValue(Class<?> type, String fieldName) {
    try {
      Field field = getStaticField(type, fieldName);
      return ClassUtils.unsafeCast(field.get(null));
    } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get a static field value using full access.
   *
   * @param <T> the type parameter
   * @param type the class.
   * @param fieldName the field name.
   * @return the value.
   */
  public static <T> @Nullable T getUnsafeStaticFieldValue(Class<?> type, String fieldName) {
    try {
      Field field = getUnsafeStaticField(type, fieldName);
      return ClassUtils.unsafeCast(field.get(null));
    } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get a static field value.
   *
   * @param <T> the type parameter
   * @param field the field.
   * @return the value.
   */
  public static <T> @Nullable T getStaticFieldValue(Field field) {
    try {
      return ClassUtils.unsafeCast(field.get(null));
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Set a static field value.
   *
   * @param type the class.
   * @param fieldName the field name.
   * @param value the new value.
   */
  public static void setStaticFieldValue(Class<?> type, String fieldName, Object value) {
    try {
      Field field = getStaticField(type, fieldName);
      field.set(null, value);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Set a static field value.
   *
   * @param type the class.
   * @param fieldName thr field name.
   * @param value the new value.
   */
  public static void setUnsafeStaticFieldValue(
      Class<?> type,
      String fieldName,
      Object value) {
    try {
      Field field = getUnsafeStaticField(type, fieldName);
      field.set(null, value);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Change a static field value.
   *
   * @param field the field.
   * @param value the new value.
   */
  public static void setStaticFieldValue(Field field, Object value) {
    try {
      field.set(null, value);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get a method of the type by the method name and arg types.
   *
   * @param type the type.
   * @param methodName the method name.
   * @param argTypes the arg types.
   * @return the found method.
   */
  public static Method getMethod(Class<?> type, String methodName, Class<?>... argTypes) {
    try {
      return type.getDeclaredMethod(methodName, argTypes);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Call a void method of the object by the name.
   *
   * @param object the object.
   * @param methodName the method name.
   */
  public static void callVoidMethod(Object object, String methodName) {
    try {
      getMethod(object.getClass(), methodName).invoke(object);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Call a void method of the object by the name using full access.
   *
   * @param object the object.
   * @param methodName the method name.
   */
  public static void callUnsafeVoidMethod(Object object, String methodName) {
    try {
      Method method = getMethod(object.getClass(), methodName);
      method.setAccessible(true);
      method.invoke(object);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private ReflectionUtils() {
    throw new IllegalArgumentException();
  }
}
