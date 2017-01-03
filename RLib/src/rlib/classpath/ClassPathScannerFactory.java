package rlib.classpath;

import org.jetbrains.annotations.NotNull;

import rlib.classpath.impl.ClassPathScannerImpl;
import rlib.classpath.impl.ManifestClassPathScannerImpl;

/**
 * The factory of classpath scanners.
 *
 * @author JavaSaBr
 */
public final class ClassPathScannerFactory {

    @NotNull
    public static ClassPathScanner newDefaultScanner() {
        return new ClassPathScannerImpl();
    }

    @NotNull
    public static ClassPathScanner newManifestScanner(final Class<?> rootClass, final String classPathKey) {
        return new ManifestClassPathScannerImpl(rootClass, classPathKey);
    }

    private ClassPathScannerFactory() {
        throw new RuntimeException();
    }
}
