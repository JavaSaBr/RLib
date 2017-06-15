package com.ss.rlib.classpath;

import com.ss.rlib.classpath.impl.ManifestClassPathScannerImpl;
import org.jetbrains.annotations.NotNull;

import com.ss.rlib.classpath.impl.ClassPathScannerImpl;

/**
 * The factory of classpath scanners.
 *
 * @author JavaSaBr
 */
public final class ClassPathScannerFactory {

    /**
     * New default scanner class path scanner.
     *
     * @return the class path scanner
     */
    @NotNull
    public static ClassPathScanner newDefaultScanner() {
        return new ClassPathScannerImpl();
    }

    /**
     * New manifest scanner class path scanner.
     *
     * @param rootClass    the root class
     * @param classPathKey the class path key
     * @return the class path scanner
     */
    @NotNull
    public static ClassPathScanner newManifestScanner(final Class<?> rootClass, final String classPathKey) {
        return new ManifestClassPathScannerImpl(rootClass, classPathKey);
    }

    private ClassPathScannerFactory() {
        throw new RuntimeException();
    }
}
