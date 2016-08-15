package rlib.classpath;

import rlib.classpath.impl.ClassPathScannerImpl;
import rlib.classpath.impl.ManifestClassPathScannerImpl;

/**
 * Фабрика сканеров classpath.
 *
 * @author Ronn
 */
public final class ClassPathScannerFactory {

    public static ClassPathScanner newDefaultScanner() {
        return new ClassPathScannerImpl();
    }

    public static ClassPathScanner newManifestScanner(final Class<?> rootClass, final String classPathKey) {
        return new ManifestClassPathScannerImpl(rootClass, classPathKey);
    }

    private ClassPathScannerFactory() {
        throw new RuntimeException();
    }
}
