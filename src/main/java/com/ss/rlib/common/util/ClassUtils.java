package com.ss.rlib.common.util;

import static java.lang.Class.forName;
import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
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

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(ClassUtils.class);

    /**
     * Get a class for the name.
     *
     * @param <T>  the type parameter
     * @param name the name of a class.
     * @return the class or null.
     */
    public static <T> @Nullable Class<T> getClass(@NotNull final String name) {
        try {
            return unsafeCast(forName(name));
        } catch (final ClassNotFoundException e) {
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
    public static <T> @Nullable Constructor<T> getConstructor(@NotNull final Class<?> cs, @Nullable final Class<?>... classes) {
        try {
            return unsafeCast(cs.getConstructor(classes));
        } catch (final NoSuchMethodException | SecurityException e) {
            LOGGER.warning(e);
            return null;
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
    public static <T> @Nullable Constructor<T> getConstructor(@NotNull final String className, @Nullable final Class<?>... classes) {
        try {
            final Class<?> cs = forName(className);
            return unsafeCast(cs.getConstructor(classes));
        } catch (final NoSuchMethodException | SecurityException | ClassNotFoundException e) {
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
    public static boolean hasConstructor(@NotNull final Class<?> cs, @Nullable final Class<?>... classes) {

        final Constructor<?>[] constructors = cs.getConstructors();
        for (final Constructor<?> constructor : constructors) {
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
    public static boolean hasConstructor(@NotNull final Class<?> cs) {

        final Constructor<?>[] constructors = cs.getConstructors();
        for (final Constructor<?> constructor : constructors) {
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
    public static <T> @NotNull T newInstance(@NotNull final Class<?> cs) {
        try {
            //noinspection ConstantConditions
            return unsafeCast(cs.newInstance());
        } catch (final InstantiationException | IllegalAccessException e) {
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
    public static <T> @NotNull T newInstance(@NotNull final Constructor<?> constructor, @Nullable final Object... objects) {
        try {
            //noinspection ConstantConditions
            return unsafeCast(constructor.newInstance(objects));
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException e) {
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
    public static <T> @NotNull T newInstance(@NotNull final String className) {
        try {
            //noinspection ConstantConditions
            return unsafeCast(forName(className).newInstance());
        } catch (final InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unsafe cast of the object.
     *
     * @param <T>    the type parameter
     * @param object the object.
     * @return the casted object.
     */
    public static <T> @Nullable T unsafeCast(@Nullable final Object object) {
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
    public static <T> @Nullable T unsafeCast(@NotNull final Class<T> type, @Nullable final Object object) {
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
    public static <T> @Nullable T cast(@NotNull final Class<T> type, @Nullable final Object object) {
        return type.isInstance(object) ? type.cast(object) : null;
    }

    private ClassUtils() {
        throw new RuntimeException();
    }
}
