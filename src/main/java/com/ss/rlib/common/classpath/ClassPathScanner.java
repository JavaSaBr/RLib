package com.ss.rlib.common.classpath;

import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.function.Function;

/**
 * THe interface to implement a classpath scanner.
 *
 * @author JavaSaBr
 */
public interface ClassPathScanner {

    /**
     * The constant JAR_EXTENSION.
     */
    String JAR_EXTENSION = ".jar";

    /**
     * Add some classes to this scanner.
     *
     * @param classes the classes.
     */
    void addClasses(@NotNull Array<Class<?>> classes);

    /**
     * Add some resources to this scanner.
     *
     * @param resources the resources.
     */
    void addResources(@NotNull Array<String> resources);

    /**
     * Sets the flat of using system classpath.
     *
     * @param useSystemClasspath true if need to use system classpath.
     */
    void setUseSystemClasspath(final boolean useSystemClasspath);

    /**
     * Find all implementations of the interface class.
     *
     * @param interfaceClass the interface class.
     * @return the list of found implementations.
     */
    default <T> @NotNull Array<Class<T>> findImplements(@NotNull final Class<T> interfaceClass) {
        final Array<Class<T>> result = ArrayFactory.newArray(Class.class);
        findImplements(result, interfaceClass);
        return result;
    }

    /**
     * Find all implementations of the interface class.
     *
     * @param container      the container.
     * @param interfaceClass the interface class.
     */
    <T> void findImplements(@NotNull Array<Class<T>> container, @NotNull Class<T> interfaceClass);

    /**
     * Find all inheriting classes of the parent class.
     *
     * @param parentClass the parent class.
     * @return the list of found inherited classes.
     */
    default <T> @NotNull Array<Class<T>> findInherited(@NotNull final Class<T> parentClass) {
        final Array<Class<T>> result = ArrayFactory.newArray(Class.class);
        findInherited(result, parentClass);
        return result;
    }

    /**
     * Find all inheriting classes of the parent class.
     *
     * @param container   the container.
     * @param parentClass the parent class.
     */
    <T> void findInherited(@NotNull Array<Class<T>> container, @NotNull Class<T> parentClass);

    /**
     * Find all classes annotated via specified annotation.<br>
     * Exclude class types:<ul>
     * <li>Interfaces</li>
     * <li>Abstract classes</li>
     * <li>Annotations</li>
     * </ul>
     *
     * @param annotationClass the annotation class.
     * @return the list of found annotated classes.
     */
    default @NotNull Array<Class<?>> findAnnotated(@NotNull final Class<? extends Annotation> annotationClass) {
        final Array<Class<?>> result = ArrayFactory.newArray(Class.class);
        findAnnotated(result, annotationClass);
        return result;
    }

    /**
     * Find all classes annotated via specified annotation.<br>
     * Exclude class types:<ul>
     * <li>Interfaces</li>
     * <li>Abstract classes</li>
     * <li>Annotations</li>
     * </ul>
     *
     * @param container       the container.
     * @param annotationClass the annotation class.
     */
    void findAnnotated(@NotNull Array<Class<?>> container, @NotNull Class<? extends Annotation> annotationClass);

    /**
     * Get all found classes.
     *
     * @param container the container.
     */
    void getAll(@NotNull Array<Class<?>> container);

    /**
     * Get all found resources.
     *
     * @param container the container.
     */
    void getAllResources(@NotNull Array<String> container);

    /**
     * Start scanning classpath.
     */
    default void scan() {
        scan(null);
    }

    /**
     * Start scanning classpath.
     *
     * @param filter the filter.
     */
    void scan(@Nullable Function<String, Boolean> filter);

    /**
     * Adds an additional path to scan.
     *
     * @param path the additional path.
     */
    void addAdditionalPath(@NotNull String path);

    /**
     * Adds additional paths to scan.
     *
     * @param paths the additional paths.
     */
    void addAdditionalPaths(@NotNull String[] paths);
}
