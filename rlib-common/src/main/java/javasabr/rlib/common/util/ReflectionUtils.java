package javasabr.rlib.common.util;

import static javasabr.rlib.common.util.ArrayUtils.contains;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.Nullable;

/**
 * Utility methods for Java reflection operations.
 *
 * @since 10.0.0
 */
@UtilityClass
public final class ReflectionUtils {

  public static void addAllFields(
      Collection<Field> container,
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
        container.addAll(Arrays.asList(fields));
      } else {
        ArrayUtils.forEach(fields, toCheck -> !contains(exceptions, toCheck.getName()), container::add);
      }
    }
  }

  public static Collection<Field> getAllDeclaredFields(Class<?> cs, String... exceptions) {
    var container = new ArrayList<Field>();
    addAllFields(container, cs, Object.class, true, exceptions);
    return container;
  }

  public static Collection<Field> getAllFields(
      Class<?> cs,
      Class<?> last,
      boolean declared,
      String... exceptions) {
    var container = new ArrayList<Field>();
    addAllFields(container, cs, last, declared, exceptions);
    return container;
  }

  public static Field getField(Class<?> type, String fieldName) {
    try {
      return type.getDeclaredField(fieldName);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public static Field getField(Object object, String fieldName) {
    return getField(object.getClass(), fieldName);
  }


  public static Field getUnsafeField(Class<?> type, String fieldName) {
    try {
      Field field = getField(type, fieldName);
      field.setAccessible(true);
      return field;
    } catch (SecurityException | IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public static Field getUnsafeField(Object object, String fieldName) {
    return getUnsafeField(object.getClass(), fieldName);
  }

  @Nullable
  public static <T> T getFiledValue(Object object, String fieldName) {
    try {
      Field field = getField(object, fieldName);
      return ClassUtils.unsafeCast(field.get(object));
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static <T> T getUnsafeFieldValue(final Object object, final String fieldName) {
    try {
      final Field field = getUnsafeField(object, fieldName);
      return ClassUtils.unsafeCast(field.get(object));
    } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static <T> T getFieldValue(Object object, Field field) {
    try {
      return ClassUtils.unsafeCast(field.get(object));
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static void setFieldValue(Object object, String fieldName, Object value) {
    try {
      Field field = getField(object, fieldName);
      field.set(object, value);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static void setUnsafeFieldValue(Object object, String fieldName, Object value) {
    try {
      Field field = getUnsafeField(object, fieldName);
      field.set(object, value);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static void setFieldValue(Object object, Field field, Object value) {
    try {
      field.set(object, value);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static Field getStaticField(Class<?> type, String fieldName) {
    try {
      return type.getDeclaredField(fieldName);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public static Field getUnsafeStaticField(Class<?> type, String fieldName) {
    try {
      Field field = getStaticField(type, fieldName);
      field.setAccessible(true);
      return field;
    } catch (SecurityException | IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static <T> T getStaticFieldValue(Class<?> type, String fieldName) {
    try {
      Field field = getStaticField(type, fieldName);
      return ClassUtils.unsafeCast(field.get(null));
    } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static <T> T getUnsafeStaticFieldValue(Class<?> type, String fieldName) {
    try {
      Field field = getUnsafeStaticField(type, fieldName);
      return ClassUtils.unsafeCast(field.get(null));
    } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static <T> T getStaticFieldValue(Field field) {
    try {
      return ClassUtils.unsafeCast(field.get(null));
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static void setStaticFieldValue(Class<?> type, String fieldName, Object value) {
    try {
      Field field = getStaticField(type, fieldName);
      field.set(null, value);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static void setUnsafeStaticFieldValue(Class<?> type, String fieldName, Object value) {
    try {
      Field field = getUnsafeStaticField(type, fieldName);
      field.set(null, value);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static void setStaticFieldValue(Field field, Object value) {
    try {
      field.set(null, value);
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static Method getMethod(Class<?> type, String methodName, Class<?>... argTypes) {
    try {
      return type.getDeclaredMethod(methodName, argTypes);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public static void callVoidMethod(Object object, String methodName) {
    try {
      getMethod(object.getClass(), methodName).invoke(object);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  public static void callUnsafeVoidMethod(Object object, String methodName) {
    try {
      Method method = getMethod(object.getClass(), methodName);
      method.setAccessible(true);
      method.invoke(object);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
