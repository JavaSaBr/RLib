package com.ss.rlib.classpath;

import com.ss.rlib.util.array.Array;
import org.jetbrains.annotations.NotNull;

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
     * Find all implementations of an interface class.
     *
     * @param <T>            the type parameter
     * @param <R>            the type parameter
     * @param container      the container.
     * @param interfaceClass the interface class.
     */
    <T, R extends T> void findImplements(@NotNull Array<Class<R>> container, @NotNull Class<T> interfaceClass);

    /**
     * Find all inheriting classes of a parent class.
     *
     * @param <T>         the type parameter
     * @param <R>         the type parameter
     * @param container   the container.
     * @param parentClass the parent class.
     */
    <T, R extends T> void findInherited(@NotNull Array<Class<R>> container, @NotNull Class<T> parentClass);

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
     * Start scan.
     *
     * @param filter the filter.
     */
    void scan(@NotNull Function<String, Boolean> filter);

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
