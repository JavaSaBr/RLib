package com.ss.rlib.classpath.impl;

import com.ss.rlib.classpath.ClassPathScanner;
import com.ss.rlib.util.Utils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * The implementation of the {@link ClassPathScanner} with parsing manifest file.
 *
 * @author JavaSaBr
 */
public class ManifestClassPathScannerImpl extends ClassPathScannerImpl {

    /**
     * The roo class.
     */
    @NotNull
    private final Class<?> rootClass;

    /**
     * The classpath key.
     */
    @NotNull
    private final String classPathKey;

    /**
     * Instantiates a new Manifest class path scanner.
     *
     * @param rootClass    the root class
     * @param classPathKey the class path key
     */
    public ManifestClassPathScannerImpl(@NotNull final Class<?> rootClass, @NotNull final String classPathKey) {
        this.rootClass = rootClass;
        this.classPathKey = classPathKey;
    }

    /**
     * Get manifest class path.
     *
     * @return the class paths.
     */
    protected String[] getManifestClassPath() {

        final Path root = Utils.getRootFolderFromClass(rootClass);
        final Array<String> result = ArrayFactory.newArray(String.class);

        final Thread currentThread = Thread.currentThread();
        final ClassLoader loader = currentThread.getContextClassLoader();

        Enumeration<URL> urls;

        try {

            urls = loader.getResources(JarFile.MANIFEST_NAME);

            while (urls.hasMoreElements()) {

                try {

                    final URL url = urls.nextElement();
                    final InputStream is = url.openStream();

                    if (is != null) {

                        final Manifest manifest = new Manifest(is);
                        final Attributes attributes = manifest.getMainAttributes();

                        final String value = attributes.getValue(classPathKey);
                        if (value == null) continue;

                        final String[] classpath = value.split(" ");

                        for (final String path : classpath) {

                            final Path file = root.resolve(path);

                            if (Files.exists(file)) {
                                result.add(file.toString());
                            }
                        }
                    }

                } catch (final Exception e) {
                    LOGGER.warning(e);
                }
            }

        } catch (final IOException e1) {
            LOGGER.warning(e1);
        }

        return result.toArray(String.class);
    }

    @NotNull
    @Override
    protected String[] getPaths() {

        final Array<String> result = ArrayFactory.newArraySet(String.class);
        result.addAll(super.getPaths());
        result.addAll(getManifestClassPath());

        return result.toArray(String.class);
    }
}
