package rlib.classpath;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import rlib.util.array.Array;

/**
 * THe interface to implement a classpath scanner.
 *
 * @author JavaSaBr
 */
public interface ClassPathScanner {

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
     * Find all implementations of an interface class.
     *
     * @param container      the container.
     * @param interfaceClass the interface class.
     */
    <T, R extends T> void findImplements(@NotNull Array<Class<R>> container, @NotNull Class<T> interfaceClass);

    /**
     * Find all inheriting classes of a parent class.
     *
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
     * Start scanning.
     *
     * @param filter the filter.
     */
    void scanning(@NotNull Function<String, Boolean> filter);
}
