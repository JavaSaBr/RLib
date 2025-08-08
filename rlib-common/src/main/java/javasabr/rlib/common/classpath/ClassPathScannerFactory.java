package javasabr.rlib.common.classpath;

import javasabr.rlib.common.classpath.impl.ClassPathScannerImpl;
import javasabr.rlib.common.classpath.impl.ManifestClassPathScannerImpl;
import org.jspecify.annotations.NullMarked;

/**
 * The factory of classpath scanners.
 *
 * @author JavaSaBr
 */
@NullMarked
public final class ClassPathScannerFactory {

  /**
   * Create a new default class path scanner.
   *
   * @return the new class path scanner.
   */
  public static ClassPathScanner newDefaultScanner() {
    return new ClassPathScannerImpl(ClassPathScannerFactory.class.getClassLoader());
  }

  /**
   * Create a new default class path scanner.
   *
   * @param classLoader the class loader.
   * @return the new class path scanner.
   */
  public static ClassPathScanner newDefaultScanner(ClassLoader classLoader) {
    return new ClassPathScannerImpl(classLoader);
  }

  /**
   * Create a new default class path scanner with additional class paths.
   *
   * @param classLoader the class loader.
   * @param additionalPaths the additional class paths.
   * @return the new class path scanner.
   */
  public static ClassPathScanner newDefaultScanner(ClassLoader classLoader, String[] additionalPaths) {
    var scanner = new ClassPathScannerImpl(classLoader);
    scanner.addAdditionalPaths(additionalPaths);
    return scanner;
  }

  /**
   * Create a new manifest class path scanner.
   *
   * @param rootClass the root class.
   * @return the new class path scanner.
   */
  public static ClassPathScanner newManifestScanner(Class<?> rootClass) {
    return new ManifestClassPathScannerImpl(ClassPathScannerFactory.class.getClassLoader(), rootClass, "Class-Path");
  }

  /**
   * Create a new manifest class path scanner.
   *
   * @param rootClass the root class.
   * @param classPathKey the class path key.
   * @return the new class path scanner.
   */
  public static ClassPathScanner newManifestScanner(Class<?> rootClass, String classPathKey) {
    return new ManifestClassPathScannerImpl(ClassPathScannerFactory.class.getClassLoader(), rootClass, classPathKey);
  }

  private ClassPathScannerFactory() {
    throw new RuntimeException();
  }
}
