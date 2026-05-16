package javasabr.rlib.common.util;

import static java.lang.Class.forName;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Utility methods for class and constructor operations.
 *
 * @since 10.0.0
 */
@NullMarked
public final class ClassUtils {

  /**
   * Finds the common super type of two objects.
   *
   * @param <E> the common type
   * @param e1 the first object
   * @param e2 the second object
   * @return the common super class
   */
  public static <E> Class<E> commonType(E e1, E e2) {
    Class<?> type = e1.getClass();
    while (!type.isInstance(e2)) {
      type = type.getSuperclass();
    }
    return (Class<E>) type;
  }

  /**
   * Finds the common super type of three objects.
   *
   * @param <E> the common type
   * @param e1 the first object
   * @param e2 the second object
   * @param e3 the third object
   * @return the common super class
   */
  public static <E> Class<E> commonType(E e1, E e2, E e3) {
    Class<?> type = e1.getClass();
    while (!type.isInstance(e2) || !type.isInstance(e3)) {
      type = type.getSuperclass();
    }
    return (Class<E>) type;
  }

  /**
   * Finds the common super type of four objects.
   *
   * @param <E> the common type
   * @param e1 the first object
   * @param e2 the second object
   * @param e3 the third object
   * @param e4 the fourth object
   * @return the common super class
   */
  public static <E> Class<E> commonType(E e1, E e2, E e3, E e4) {
    Class<?> type = e1.getClass();
    while (!type.isInstance(e2) || !type.isInstance(e3) || !type.isInstance(e4)) {
      type = type.getSuperclass();
    }
    return (Class<E>) type;
  }

  /**
   * Finds the common super type of five objects.
   *
   * @param <E> the common type
   * @param e1 the first object
   * @param e2 the second object
   * @param e3 the third object
   * @param e4 the fourth object
   * @param e5 the fifth object
   * @return the common super class
   */
  public static <E> Class<E> commonType(E e1, E e2, E e3, E e4, E e5) {
    Class<?> type = e1.getClass();
    while (!type.isInstance(e2) || !type.isInstance(e3) || !type.isInstance(e4) || !type.isInstance(e5)) {
      type = type.getSuperclass();
    }
    return (Class<E>) type;
  }

  /**
   * Finds the common super type of six objects.
   *
   * @param <E> the common type
   * @param e1 the first object
   * @param e2 the second object
   * @param e3 the third object
   * @param e4 the fourth object
   * @param e5 the fifth object
   * @param e6 the sixth object
   * @return the common super class
   */
  public static <E> Class<E> commonType(E e1, E e2, E e3, E e4, E e5, E e6) {
    Class<?> type = e1.getClass();
    while (!type.isInstance(e2) || !type.isInstance(e3) || !type.isInstance(e4) || !type.isInstance(e5) || !type.isInstance(e6)) {
      type = type.getSuperclass();
    }
    return (Class<E>) type;
  }

  /**
   * Finds the common super type of multiple objects.
   *
   * @param objects the objects to find common type for
   * @return the common super class, or Object.class if empty
   */
  public static Class<?> commonType(Object... objects) {
    if (objects.length < 1) {
      return Object.class;
    }
    Class<?> type = objects[0].getClass();
    if (objects.length < 2) {
      return type;
    }
    while (!isCommonType(type, objects, 1)) {
      type = type.getSuperclass();
    }
    return type;
  }

  private static boolean isCommonType(Class<?> type, Object[] objects, int offset) {
    for (int i = offset; i < objects.length; i++) {
      if (!type.isInstance(objects[i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * Try to find a class by name.
   *
   * @param name the name of a class.
   * @param <T> the type of a class.
   * @return the class or null.
   * @since 9.8.0
   */
  @Nullable
  public static <T> Class<T> tryGetClass(String name) {
    try {
      return unsafeCast(forName(name));
    } catch (ClassNotFoundException e) {
      Utils.printWarn(e);
      return null;
    }
  }

  /**
   * Find a class by name.
   *
   * @param name the name of a class.
   * @param <T> the type of a class.
   * @return the class.
   * @throws RuntimeException if the class is not found.
   */
  public static <T> Class<T> getClass(String name) {
    try {
      return unsafeNNCast(forName(name));
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Try to find a constructor of a class by types of arguments.
   *
   * @param cs the class.
   * @param classes the types of arguments.
   * @param <T> the type of a class.
   * @return the constructor or null.
   * @since 9.8.0
   */
  @Nullable
  public static <T> Constructor<T> tryGetConstructor(Class<?> cs, @Nullable Class<?>... classes) {
    try {
      return unsafeCast(cs.getConstructor(classes));
    } catch (NoSuchMethodException | SecurityException e) {
      Utils.printWarn(e);
      return null;
    }
  }

  /**
   * Try to find a constructor of a class by types of arguments.
   *
   * @param className the class name.
   * @param classes the types of arguments.
   * @param <T> the type of a class.
   * @return the constructor or null.
   * @since 9.8.0
   */
  @Nullable
  public static <T> Constructor<T> tryGetConstructor(String className, Class<?>... classes) {
    try {
      return unsafeCast(forName(className).getConstructor(classes));
    } catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
      Utils.printWarn(e);
      return null;
    }
  }

  /**
   * Get a constructor of the class by types of arguments.
   *
   * @param cs the class.
   * @param classes the types of arguments.
   * @param <T> the type of a class.
   * @return the constructor.
   * @throws RuntimeException if the constructor is not available.
   */
  public static <T> Constructor<T> getConstructor(Class<?> cs, @Nullable Class<?>... classes) {
    try {
      return unsafeNNCast(cs.getConstructor(classes));
    } catch (NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Check of existing a constructor in a class.
   *
   * @param cs the class.
   * @param classes the types of arguments.
   * @return true if this class has constructor wth the arguments.
   */
  public static boolean hasConstructor(Class<?> cs, Class<?>... classes) {
    for (var constructor : cs.getConstructors()) {
      if (Arrays.equals(constructor.getParameterTypes(), classes)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check of existing an empty constructor in the class.
   *
   * @param cs the class.
   * @return true if this class has empty constructor.
   */
  public static boolean hasConstructor(Class<?> cs) {
    for (var constructor : cs.getConstructors()) {
      if (constructor.getParameterCount() == 0) {
        return true;
      }
    }
    return false;
  }

  /**
   * Create a new instance of a class.
   *
   * @param cs the class.
   * @param <T> the type of a class.
   * @return the new instance.
   */
  public static <T> T newInstance(Class<?> cs) {
    try {
      return unsafeNNCast(cs.getDeclaredConstructor().newInstance());
    } catch (InstantiationException | IllegalAccessException | 
             NoSuchMethodException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Create a new instance of a class using a constructor.
   *
   * @param constructor the constructor.
   * @param objects the arguments.
   * @param <T> the type of a class.
   * @return the new instance.
   */
  public static <T> T newInstance(Constructor<? super T> constructor, Object... objects) {
    try {
      return unsafeNNCast(constructor.newInstance(objects));
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e.getTargetException());
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Create a new instance of a class.
   *
   * @param className the class name.
   * @param <T> the type of a class.
   * @return the new instance.
   */
  public static <T> T newInstance(String className) {
    try {
      return unsafeNNCast(forName(className).getDeclaredConstructor().newInstance());
    } catch (InstantiationException | IllegalAccessException | 
             ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Unsafe cast of an object to expected type.
   *
   * @param object the object.
   * @param <T> the expected type.
   * @return the casted object.
   */
  @Nullable
  public static <T> T unsafeCast(@Nullable Object object) {
    return (T) object;
  }

  /**
   * Unsafe cast of an object to expected type.
   *
   * @param object the object.
   * @param <T> the expected type.
   * @return the casted object.
   */
  public static <T> T unsafeNNCast(Object object) {
    return (T) object;
  }

  /**
   * Unsafe cast of an object to some type.
   *
   * @param type the target type.
   * @param object the object.
   * @param <T> the target type.
   * @return the casted object.
   */
  @Nullable
  public static <T> T unsafeCast(Class<T> type, @Nullable Object object) {
    return type.cast(object);
  }

  /**
   * Safe cast of an object to a type.
   *
   * @param type the target type.
   * @param object the object.
   * @param <T> the target type.
   * @return the casted object or null.
   */
  public static <T> @Nullable T cast(Class<T> type, @Nullable Object object) {
    return type.isInstance(object) ? type.cast(object) : null;
  }

  /**
   * Safe cast of a not null object to some type.
   *
   * @param type the target type.
   * @param object the object.
   * @param <T> the target type.
   * @return the casted object.
   */
  public static <T> T nnCast(Class<T> type, Object object) {
    return type.cast(object);
  }

  private ClassUtils() {
    throw new RuntimeException();
  }
}
