package javasabr.rlib.classpath;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.function.Predicate;
import javasabr.rlib.classpath.impl.ClassPathScannerImpl;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.MutableArray;
import org.jspecify.annotations.Nullable;

/**
 * Scanner for discovering classes and resources on the classpath.
 *
 * @since 10.0.0
 */
public interface ClassPathScanner {

  /**
   * The JAR file extension.
   */
  String JAR_EXTENSION = ".jar";

  /**
   * The Java source file extension.
   */
  String SOURCE_EXTENSION = ".java";

  /**
   * The compiled class file extension.
   */
  String CLASS_EXTENSION = ".class";

  /**
   * A null scanner constant.
   */
  @Nullable ClassPathScanner NULL_SCANNER = null;

  /**
   * An empty scanner that performs no operations.
   */
  ClassPathScanner EMPTY_SCANNER = new ClassPathScannerImpl(ClassPathScanner.class.getClassLoader()) {

    @Override
    public void addClasses(Array<Class<?>> classes) {}

    @Override
    public void addAdditionalPath(String path) {}

    @Override
    public void addAdditionalPaths(String[] paths) {}

    @Override
    public void addResources(Array<String> resources) {}

    @Override
    public void scan(Predicate<String> filter) {}
  };

  /**
   * An empty class loader with no URLs.
   */
  URLClassLoader EMPTY_CLASS_LOADER = new URLClassLoader(new URL[0], ClassPathScanner.class.getClassLoader());

  /**
   * A null class loader constant.
   */
  @Nullable URLClassLoader NULL_CLASS_LOADER = null;

  /**
   * Adds discovered classes to the specified collection.
   *
   * @param classes the collection to add classes to
   * @since 10.0.0
   */
  void addClasses(Array<Class<?>> classes);

  /**
   * Adds discovered resources to the specified collection.
   *
   * @param resources the collection to add resources to
   * @since 10.0.0
   */
  void addResources(Array<String> resources);

  /**
   * Sets whether to include the system classpath in scanning.
   *
   * @param useSystemClasspath true to include system classpath
   * @since 10.0.0
   */
  void useSystemClassPath(boolean useSystemClasspath);

  /**
   * Finds all implementations of the specified interface.
   *
   * @param <T> the type of the interface
   * @param interfaceClass the interface class to find implementations for
   * @return an array of implementing classes
   * @since 10.0.0
   */
  default <T> Array<Class<T>> findImplementations(Class<T> interfaceClass) {
    MutableArray<Class<T>> result = MutableArray.ofType(Class.class);
    findImplementationsTo(result, interfaceClass);
    return result;
  }

  /**
   * Finds all implementations of the specified interface and adds them to the container.
   *
   * @param <T> the type of the interface
   * @param container the container to add implementations to
   * @param interfaceClass the interface class to find implementations for
   * @since 10.0.0
   */
  <T> void findImplementationsTo(MutableArray<Class<T>> container, Class<T> interfaceClass);

  /**
   * Finds all classes that inherit from the specified parent class.
   *
   * @param <T> the type of the parent class
   * @param parentClass the parent class to find subclasses for
   * @return an array of subclasses
   * @since 10.0.0
   */
  default <T> Array<Class<T>> findInherited(Class<T> parentClass) {
    MutableArray<Class<T>> result = MutableArray.ofType(Class.class);
    findInheritedTo(result, parentClass);
    return result;
  }

  /**
   * Finds all classes that inherit from the specified parent class and adds them to the container.
   *
   * @param <T> the type of the parent class
   * @param container the container to add subclasses to
   * @param parentClass the parent class to find subclasses for
   * @since 10.0.0
   */
  <T> void findInheritedTo(MutableArray<Class<T>> container, Class<T> parentClass);

  /**
   * Finds all classes annotated with the specified annotation.
   *
   * @param annotationClass the annotation class to search for
   * @return an array of annotated classes
   * @since 10.0.0
   */
  default Array<Class<?>> findAnnotated(Class<? extends Annotation> annotationClass) {
    MutableArray<Class<?>> result = MutableArray.ofType(Class.class);
    findAnnotatedTo(result, annotationClass);
    return result;
  }

  /**
   * Finds all classes annotated with the specified annotation and adds them to the container.
   *
   * @param container the container to add annotated classes to
   * @param annotationClass the annotation class to search for
   * @since 10.0.0
   */
  void findAnnotatedTo(MutableArray<Class<?>> container, Class<? extends Annotation> annotationClass);

  /**
   * Adds all found classes to the specified container.
   *
   * @param container the container to add classes to
   * @since 10.0.0
   */
  void foundClassesTo(MutableArray<Class<?>> container);

  /**
   * Adds all found resources to the specified container.
   *
   * @param container the container to add resources to
   * @since 10.0.0
   */
  void foundResourcesTo(MutableArray<String> container);

  /**
   * Returns all classes found during scanning.
   *
   * @return an array of found classes
   * @since 10.0.0
   */
  Array<Class<?>> foundClasses();

  /**
   * Returns all resources found during scanning.
   *
   * @return an array of found resource paths
   * @since 10.0.0
   */
  Array<String> foundResources();

  /**
   * Scans the classpath without any filter.
   *
   * @since 10.0.0
   */
  default void scan() {
    scan(null);
  }

  /**
   * Scans the classpath with the specified filter.
   *
   * @param filter the filter to apply during scanning, or null for no filter
   * @since 10.0.0
   */
  void scan(@Nullable Predicate<String> filter);

  /**
   * Adds an additional path to scan.
   *
   * @param path the path to add
   * @since 10.0.0
   */
  void addAdditionalPath(String path);

  /**
   * Adds additional paths to scan.
   *
   * @param paths the paths to add
   * @since 10.0.0
   */
  void addAdditionalPaths(String[] paths);
}
