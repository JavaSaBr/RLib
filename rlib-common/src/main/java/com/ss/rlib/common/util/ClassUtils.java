package com.ss.rlib.common.util;

import static java.lang.Class.forName;
import com.ss.rlib.logger.api.LoggerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * The class with utility methods.
 *
 * @author JavaSaBr
 */
public final class ClassUtils {

    /**
     * Try to find a class by name.
     *
     * @param name the name of a class.
     * @param <T>  the type of a class.
     * @return the class or null.
     * @since 9.8.0
     */
    public static <T> @Nullable Class<T> tryGetClass(@NotNull String name) {
        try {
            return unsafeCast(forName(name));
        } catch (ClassNotFoundException e) {
            LoggerManager.getDefaultLogger().warning(e);
            return null;
        }
    }

    /**
     * Find a class by name.
     *
     * @param name the name of a class.
     * @param <T>  the type of a class.
     * @return the class.
     * @throws RuntimeException if the class is not found.
     */
    public static <T> @NotNull Class<T> getClass(@NotNull String name) {
        try {
            return unsafeNNCast(forName(name));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Try to find a constructor of a class by types of arguments.
     *
     * @param cs      the class.
     * @param classes the types of arguments.
     * @param <T>     the type of a class.
     * @return the constructor or null.
     * @since 9.8.0
     */
    public static <T> @Nullable Constructor<T> tryGetConstructor(@NotNull Class<?> cs, @Nullable Class<?>... classes) {
        try {
            return unsafeCast(cs.getConstructor(classes));
        } catch (NoSuchMethodException | SecurityException e) {
            LoggerManager.getDefaultLogger().warning(e);
            return null;
        }
    }

    /**
     * Try to find a constructor of a class by types of arguments.
     *
     * @param className the class name.
     * @param classes   the types of arguments.
     * @param <T>       the type of a class.
     * @return the constructor or null.
     * @since 9.8.0
     */
    public static <T> @Nullable Constructor<T> tryGetConstructor(
        @NotNull String className,
        @NotNull Class<?>... classes
    ) {
        try {
            return unsafeCast(forName(className).getConstructor(classes));
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            LoggerManager.getDefaultLogger().warning(e);
            return null;
        }
    }

    /**
     * Get a constructor of the class by types of arguments.
     *
     * @param cs      the class.
     * @param classes the types of arguments.
     * @param <T>     the type of a class.
     * @return the constructor.
     * @throws RuntimeException if the constructor is not available.
     */
    public static <T> @NotNull Constructor<T> getConstructor(@NotNull Class<?> cs, @Nullable Class<?>... classes) {
        try {
            return unsafeNNCast(cs.getConstructor(classes));
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check of existing a constructor in a class.
     *
     * @param cs      the class.
     * @param classes the types of arguments.
     * @return true if this class has constructor wth the arguments.
     */
    public static boolean hasConstructor(@NotNull Class<?> cs, @NotNull Class<?>... classes) {

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
    public static boolean hasConstructor(@NotNull Class<?> cs) {

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
     * @param cs  the class.
     * @param <T> the type of a class.
     * @return the new instance.
     */
    public static <T> @NotNull T newInstance(@NotNull Class<?> cs) {
        try {
            return unsafeNNCast(cs.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a new instance of a class using a constructor.
     *
     * @param constructor the constructor.
     * @param objects     the arguments.
     * @param <T>         the type of a class.
     * @return the new instance.
     */
    public static <T> @NotNull T newInstance(@NotNull Constructor<? super T> constructor, @NotNull Object... objects) {
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
     * @param <T>       the type of a class.
     * @return the new instance.
     */
    public static <T> @NotNull T newInstance(@NotNull String className) {
        try {
            return unsafeNNCast(forName(className).newInstance());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unsafe cast of an object to expected type.
     *
     * @param object the object.
     * @param <T>    the expected type.
     * @return the casted object.
     */
    public static <T> @Nullable T unsafeCast(@Nullable Object object) {
        return (T) object;
    }

    /**
     * Unsafe cast of an object to expected type.
     *
     * @param object the object.
     * @param <T>    the expected type.
     * @return the casted object.
     */
    public static <T> @NotNull T unsafeNNCast(@NotNull Object object) {
        return (T) object;
    }

    /**
     * Unsafe cast of an object to some type.
     *
     * @param type   the target type.
     * @param object the object.
     * @param <T>    the target type.
     * @return the casted object.
     */
    public static <T> @Nullable T unsafeCast(@NotNull Class<T> type, @Nullable Object object) {
        return type.cast(object);
    }

    /**
     * Safe cast of an object to a type.
     *
     * @param type   the target type.
     * @param object the object.
     * @param <T>    the target type.
     * @return the casted object or null.
     */
    public static <T> @Nullable T cast(@NotNull Class<T> type, @Nullable Object object) {
        return type.isInstance(object) ? type.cast(object) : null;
    }

    /**
     * Safe cast of a not null object to some type.
     *
     * @param type   the target type.
     * @param object the object.
     * @param <T>    the target type.
     * @return the casted object.
     */
    public static <T> @NotNull T nnCast(@NotNull Class<T> type, @NotNull Object object) {
        return type.cast(object);
    }

    private ClassUtils() {
        throw new RuntimeException();
    }
}
