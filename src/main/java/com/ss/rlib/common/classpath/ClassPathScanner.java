package com.ss.rlib.common.classpath;

import com.ss.rlib.common.classpath.impl.ClassPathScannerImpl;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.function.Predicate;

/**
 * THe interface to implement a classpath scanner.
 *
 * @author JavaSaBr
 */
public interface ClassPathScanner {

    String JAR_EXTENSION = ".jar";

    ClassPathScanner NULL_SCANNER = null;

    ClassPathScanner EMPTY_SCANNER = new ClassPathScannerImpl(ClassPathScanner.class.getClassLoader()) {

        @Override
        public void addClasses(@NotNull Array<Class<?>> classes) {
        }

        @Override
        public void addAdditionalPath(@NotNull String path) {
        }

        @Override
        public void addAdditionalPaths(@NotNull String[] paths) {
        }

        @Override
        public void addResources(@NotNull Array<String> resources) {
        }

        @Override
        public void scan(@Nullable Predicate<String> filter) {
        }
    };

    URLClassLoader EMPTY_CLASS_LOADER =
            new URLClassLoader(new URL[0], ClassPathScanner.class.getClassLoader());

    URLClassLoader NULL_CLASS_LOADER = null;

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
    void setUseSystemClasspath(boolean useSystemClasspath);

    /**
     * Find all implementations of the interface class.
     *
     * @param interfaceClass the interface class.
     * @param <T>            the first argument's type.
     * @return the list of found implementations.
     */
    default <T> @NotNull Array<Class<T>> findImplements(@NotNull Class<T> interfaceClass) {
        Array<Class<T>> result = ArrayFactory.newArray(Class.class);
        findImplements(result, interfaceClass);
        return result;
    }

    /**
     * Find all implementations of the interface class.
     *
     * @param container      the container.
     * @param interfaceClass the interface class.
     * @param <T> the interface's type.
     */
    <T> void findImplements(@NotNull Array<Class<T>> container, @NotNull Class<T> interfaceClass);

    /**
     * Find all inheriting classes of the parent class.
     *
     * @param parentClass the parent class.
     * @param <T> the classes type.
     * @return the list of found inherited classes.
     */
    default <T> @NotNull Array<Class<T>> findInherited(@NotNull Class<T> parentClass) {
        Array<Class<T>> result = ArrayFactory.newArray(Class.class);
        findInherited(result, parentClass);
        return result;
    }

    /**
     * Find all inheriting classes of the parent class.
     *
     * @param container   the container.
     * @param parentClass the parent class.
     * @param <T> the parent classes type.
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
    default @NotNull Array<Class<?>> findAnnotated(@NotNull Class<? extends Annotation> annotationClass) {
        Array<Class<?>> result = ArrayFactory.newArray(Class.class);
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
    void getFoundClasses(@NotNull Array<Class<?>> container);

    /**
     * Get all found resources.
     *
     * @param container the container.
     */
    void getFoundResources(@NotNull Array<String> container);

    /**
     * Get all found classes.
     *
     * @return the array of all found classes.
     */
    @NotNull Array<Class<?>> getFoundClasses();

    /**
     * Get all found resources.
     *
     * @return the array of all found resources.
     */
    @NotNull Array<String> getFoundResources();

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
    void scan(@Nullable Predicate<String> filter);

    /**
     * Add the additional path to scan.
     *
     * @param path the additional path.
     */
    void addAdditionalPath(@NotNull String path);

    /**
     * Add the additional paths to scan.
     *
     * @param paths the additional paths.
     */
    void addAdditionalPaths(@NotNull String[] paths);
}
