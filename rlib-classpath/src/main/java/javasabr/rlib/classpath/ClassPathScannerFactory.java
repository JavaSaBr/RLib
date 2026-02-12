package javasabr.rlib.classpath;

import javasabr.rlib.classpath.impl.ClassPathScannerImpl;
import javasabr.rlib.classpath.impl.ManifestClassPathScannerImpl;

/**
 * Factory for creating {@link ClassPathScanner} instances.
 *
 * @since 10.0.0
 */
public final class ClassPathScannerFactory {

  /**
   * Creates a new default scanner using the factory's class loader.
   *
   * @return a new classpath scanner
   * @since 10.0.0
   */
  public static ClassPathScanner newDefaultScanner() {
    return new ClassPathScannerImpl(ClassPathScannerFactory.class.getClassLoader());
  }

  /**
   * Creates a new default scanner using the specified class loader.
   *
   * @param classLoader the class loader to use
   * @return a new classpath scanner
   * @since 10.0.0
   */
  public static ClassPathScanner newDefaultScanner(ClassLoader classLoader) {
    return new ClassPathScannerImpl(classLoader);
  }

  /**
   * Creates a new default scanner using the specified class loader and additional paths.
   *
   * @param classLoader the class loader to use
   * @param additionalPaths additional paths to scan
   * @return a new classpath scanner
   * @since 10.0.0
   */
  public static ClassPathScanner newDefaultScanner(
      ClassLoader classLoader,
      String[] additionalPaths) {
    var scanner = new ClassPathScannerImpl(classLoader);
    scanner.addAdditionalPaths(additionalPaths);
    return scanner;
  }

  /**
   * Creates a new manifest-based scanner that reads classpath entries from the JAR manifest.
   *
   * @param rootClass the class whose JAR manifest should be read
   * @return a new manifest-based scanner
   * @since 10.0.0
   */
  public static ClassPathScanner newManifestScanner(Class<?> rootClass) {
    return new ManifestClassPathScannerImpl(
        ClassPathScannerFactory.class.getClassLoader(),
        rootClass,
        "Class-Path");
  }

  /**
   * Creates a new manifest-based scanner with a custom classpath key.
   *
   * @param rootClass the class whose JAR manifest should be read
   * @param classPathKey the manifest attribute key for classpath entries
   * @return a new manifest-based scanner
   * @since 10.0.0
   */
  public static ClassPathScanner newManifestScanner(Class<?> rootClass, String classPathKey) {
    return new ManifestClassPathScannerImpl(
        ClassPathScannerFactory.class.getClassLoader(),
        rootClass,
        classPathKey);
  }

  private ClassPathScannerFactory() {
    throw new RuntimeException();
  }
}
