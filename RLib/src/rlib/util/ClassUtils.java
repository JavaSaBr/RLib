package rlib.util;

import static java.lang.Class.forName;
import static rlib.util.Utils.print;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * The class with utility methods.
 *
 * @author JavaSaBr
 */
public final class ClassUtils {

    /**
     * Get a class for a name.
     *
     * @param name the name of a class.
     * @return the class or null.
     */
    @Nullable
    public static <T> Class<T> getClass(@NotNull final String name) {
        try {
            return unsafeCast(forName(name));
        } catch (final ClassNotFoundException e) {
            print(ClassUtils.class, e);
            return null;
        }
    }

    /**
     * Get a constructor of a class.
     *
     * @param cs      the class.
     * @param classes the types of arguments.
     * @return the constructor or null.
     */
    @Nullable
    public static <T> Constructor<T> getConstructor(@NotNull final Class<?> cs, @Nullable final Class<?>... classes) {
        try {
            return unsafeCast(cs.getConstructor(classes));
        } catch (final NoSuchMethodException | SecurityException e) {
            print(ClassUtils.class, e);
            return null;
        }
    }

    /**
     * Get a constructor of a class.
     *
     * @param className      the class name.
     * @param classes the types of arguments.
     * @return the constructor or null.
     */
    @Nullable
    public static <T> Constructor<T> getConstructor(@NotNull final String className, @Nullable final Class<?>... classes) {
        try {
            final Class<?> cs = forName(className);
            return unsafeCast(cs.getConstructor(classes));
        } catch (final NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            print(ClassUtils.class, e);
            return null;
        }
    }

    /**
     * Create a new instance of a class.
     *
     * @param cs the class.
     * @return the new instance.
     */
    @NotNull
    public static <T> T newInstance(@NotNull final Class<?> cs) {
        try {
            //noinspection ConstantConditions
            return unsafeCast(cs.newInstance());
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a new instance of a class using a constructor.
     *
     * @param constructor the constructor.
     * @param objects     the arguments.
     * @return the new instance.
     */
    @NotNull
    public static <T> T newInstance(@NotNull final Constructor<?> constructor, @Nullable final Object... objects) {
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
     * Create a new instance of a class.
     *
     * @param className the class name.
     * @return the new instance.
     */
    @NotNull
    public static <T> T newInstance(@NotNull final String className) {
        try {
            //noinspection ConstantConditions
            return unsafeCast(forName(className).newInstance());
        } catch (final InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unsafe cast an object.
     *
     * @param object the object.
     * @return the casted object.
     */
    @Nullable
    public static <T> T unsafeCast(@Nullable final Object object) {
        return (T) object;
    }

    /**
     * Unsafe cast an object to a type.
     *
     * @param object the object.
     * @param type the target type.
     * @return the casted object.
     */
    @Nullable
    public static <T> T unsafeCast(@NotNull final Class<T> type, @Nullable final Object object) {
        return type.cast(object);
    }

    /**
     * Safe cast an object to a type.
     *
     * @param object the object.
     * @param type the target type.
     * @return the casted object or null.
     */
    @Nullable
    public static <T> T cast(@NotNull final Class<T> type, @Nullable final Object object) {
        return type.isInstance(object) ? type.cast(object) : null;
    }

    private ClassUtils() {
        throw new RuntimeException();
    }
}
