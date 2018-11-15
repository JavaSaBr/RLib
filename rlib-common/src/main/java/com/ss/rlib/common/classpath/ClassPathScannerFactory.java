package com.ss.rlib.common.classpath;

import com.ss.rlib.common.classpath.impl.ClassPathScannerImpl;
import com.ss.rlib.common.classpath.impl.ManifestClassPathScannerImpl;
import com.ss.rlib.common.classpath.impl.ClassPathScannerImpl;
import com.ss.rlib.common.classpath.impl.ManifestClassPathScannerImpl;
import org.jetbrains.annotations.NotNull;

/**
 * The factory of classpath scanners.
 *
 * @author JavaSaBr
 */
public final class ClassPathScannerFactory {

    /**
     * Create a new default class path scanner.
     *
     * @return the new class path scanner.
     */
    public static @NotNull ClassPathScanner newDefaultScanner() {
        return new ClassPathScannerImpl(ClassPathScannerFactory.class.getClassLoader());
    }

    /**
     * Create a new default class path scanner.
     *
     * @param classLoader the class loader.
     * @return the new class path scanner.
     */
    public static @NotNull ClassPathScanner newDefaultScanner(@NotNull final ClassLoader classLoader) {
        return new ClassPathScannerImpl(classLoader);
    }

    /**
     * Create a new default class path scanner with additional class paths.
     *
     * @param classLoader     the class loader.
     * @param additionalPaths the additional class paths.
     * @return the new class path scanner.
     */
    public static @NotNull ClassPathScanner newDefaultScanner(@NotNull final ClassLoader classLoader,
                                                              @NotNull final String[] additionalPaths) {
        final ClassPathScannerImpl scanner = new ClassPathScannerImpl(classLoader);
        scanner.addAdditionalPaths(additionalPaths);
        return scanner;
    }

    /**
     * Create a new manifest class path scanner.
     *
     * @param rootClass the root class.
     * @return the new class path scanner.
     */
    public static @NotNull ClassPathScanner newManifestScanner(@NotNull final Class<?> rootClass) {
        return new ManifestClassPathScannerImpl(ClassPathScannerFactory.class.getClassLoader(), rootClass, "Class-Path");
    }

    /**
     * Create a new manifest class path scanner.
     *
     * @param rootClass    the root class.
     * @param classPathKey the class path key.
     * @return the new class path scanner.
     */
    public static @NotNull ClassPathScanner newManifestScanner(@NotNull final Class<?> rootClass,
                                                               @NotNull final String classPathKey) {
        return new ManifestClassPathScannerImpl(ClassPathScannerFactory.class.getClassLoader(), rootClass, classPathKey);
    }

    private ClassPathScannerFactory() {
        throw new RuntimeException();
    }
}
