package com.ss.rlib.classpath;

import com.ss.rlib.classpath.impl.ClassPathScannerImpl;
import com.ss.rlib.classpath.impl.ManifestClassPathScannerImpl;
import org.jetbrains.annotations.NotNull;

/**
 * The factory of classpath scanners.
 *
 * @author JavaSaBr
 */
public final class ClassPathScannerFactory {

    /**
     * New default class path scanner.
     *
     * @return the class path scanner
     */
    @NotNull
    public static ClassPathScanner newDefaultScanner() {
        return new ClassPathScannerImpl(ClassPathScannerFactory.class.getClassLoader());
    }

    /**
     * New default class path scanner.
     *
     * @param classLoader the classloader.
     * @return the class path scanner
     */
    @NotNull
    public static ClassPathScanner newDefaultScanner(@NotNull final ClassLoader classLoader) {
        return new ClassPathScannerImpl(classLoader);
    }

    /**
     * New default class path scanner.
     *
     * @param classLoader     the classloader.
     * @param additionalPaths the additional paths.
     * @return the class path scanner
     */
    @NotNull
    public static ClassPathScanner newDefaultScanner(@NotNull final ClassLoader classLoader,
                                                     @NotNull final String[] additionalPaths) {
        final ClassPathScannerImpl scanner = new ClassPathScannerImpl(classLoader);
        scanner.addAdditionalPaths(additionalPaths);
        return scanner;
    }

    /**
     * New manifest class path scanner.
     *
     * @param rootClass    the root class
     * @param classPathKey the class path key
     * @return the class path scanner
     */
    @NotNull
    public static ClassPathScanner newManifestScanner(final Class<?> rootClass, final String classPathKey) {
        return new ManifestClassPathScannerImpl(ClassPathScannerFactory.class.getClassLoader(), rootClass, classPathKey);
    }

    private ClassPathScannerFactory() {
        throw new RuntimeException();
    }
}
