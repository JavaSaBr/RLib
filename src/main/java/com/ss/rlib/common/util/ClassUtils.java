package com.ss.rlib.common.util;

import static java.lang.Class.forName;
import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
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

    private static final Logger LOGGER = LoggerManager.getLogger(ClassUtils.class);

    /**
     * Get a class for the name.
     *
     * @param <T>  the type parameter
     * @param name the name of a class.
     * @return the class or null.
     */
    public static <T> @Nullable Class<T> getClass(@NotNull String name) {
        try {
            return unsafeCast(forName(name));
        } catch (ClassNotFoundException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    /**
     * Get a constructor of the class.
     *
     * @param <T>     the type parameter
     * @param cs      the class.
     * @param classes the types of arguments.
     * @return the constructor or null.
     */
    public static <T> @Nullable Constructor<T> getConstructor(
            @NotNull Class<?> cs,
            @Nullable Class<?>... classes
    ) {
        try {
            return unsafeCast(cs.getConstructor(classes));
        } catch (NoSuchMethodException | SecurityException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    /**
     * Get a constructor of the class.
     *
     * @param <T>     the type parameter
     * @param cs      the class.
     * @param classes the types of arguments.
     * @return the constructor.
     */
    public static <T> @NotNull Constructor<T> requireConstructor(
            @NotNull Class<?> cs,
            @Nullable Class<?>... classes
    ) {
        try {
            return unsafeCast(cs.getConstructor(classes));
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a constructor of the class.
     *
     * @param <T>       the type parameter
     * @param className the class name.
     * @param classes   the types of arguments.
     * @return the constructor or null.
     */
    public static <T> @Nullable Constructor<T> getConstructor(@NotNull String className, @Nullable Class<?>... classes) {
        try {
            Class<?> cs = forName(className);
            return unsafeCast(cs.getConstructor(classes));
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    /**
     * Check of existing a constructor in the class.
     *
     * @param cs      the class.
     * @param classes the types of arguments.
     * @return true if this class has constructor wth the arguments.
     */
    public static boolean hasConstructor(@NotNull Class<?> cs, @Nullable Class<?>... classes) {

        for (Constructor<?> constructor : cs.getConstructors()) {
            if (Arrays.equals(constructor.getParameterTypes(), classes)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check of existing an empty constructor in the class.
     *
     * @param cs      the class.
     * @return true if this class has empty constructor.
     */
    public static boolean hasConstructor(@NotNull Class<?> cs) {

        Constructor<?>[] constructors = cs.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Create a new instance of the class.
     *
     * @param <T> the type parameter
     * @param cs  the class.
     * @return the new instance.
     */
    public static <T> @NotNull T newInstance(@NotNull Class<?> cs) {
        try {
            //noinspection ConstantConditions
            return unsafeCast(cs.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a new instance of the class using the constructor.
     *
     * @param <T>         the type parameter
     * @param constructor the constructor.
     * @param objects     the arguments.
     * @return the new instance.
     */
    public static <T> @NotNull T newInstance(@NotNull Constructor<?> constructor, @Nullable Object... objects) {
        try {
            //noinspection ConstantConditions
            return unsafeCast(constructor.newInstance(objects));
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a new instance of the class.
     *
     * @param <T>       the type parameter
     * @param className the class name.
     * @return the new instance.
     */
    public static <T> @NotNull T newInstance(@NotNull String className) {
        try {
            //noinspection ConstantConditions
            return unsafeCast(forName(className).newInstance());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unsafe cast of the object to the expected type.
     *
     * @param <T>    the expected type.
     * @param object the object.
     * @return the casted object.
     */
    public static <T> @Nullable T unsafeCast(@Nullable Object object) {
        return (T) object;
    }

    /**
     * Unsafe cast of the object to the expected type.
     *
     * @param <T>    the expected type.
     * @param object the object.
     * @return the casted object.
     */
    public static <T> @NotNull T unsafeNNCast(@NotNull Object object) {
        return (T) object;
    }

    /**
     * Unsafe cast of the object to the type.
     *
     * @param <T>    the type parameter
     * @param type   the target type.
     * @param object the object.
     * @return the casted object.
     */
    public static <T> @Nullable T unsafeCast(@NotNull Class<T> type, @Nullable Object object) {
        return type.cast(object);
    }

    /**
     * Safe cast of the object to the type.
     *
     * @param <T>    the type parameter
     * @param type   the target type.
     * @param object the object.
     * @return the casted object or null.
     */
    public static <T> @Nullable T cast(@NotNull Class<T> type, @Nullable Object object) {
        return type.isInstance(object) ? type.cast(object) : null;
    }

    private ClassUtils() {
        throw new RuntimeException();
    }
}
