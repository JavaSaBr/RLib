package com.ss.rlib.common.classpath.impl;

import com.ss.rlib.common.classpath.ClassPathScanner;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

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

    public ManifestClassPathScannerImpl(@NotNull final ClassLoader classLoader, @NotNull final Class<?> rootClass,
                                        @NotNull final String classPathKey) {
        super(classLoader);
        this.rootClass = rootClass;
        this.classPathKey = classPathKey;
    }

    /**
     * Get manifest class path.
     *
     * @return the class paths.
     */
    protected @NotNull String[] getManifestClassPath() {

        final Path root = Utils.getRootFolderFromClass(rootClass);
        final Array<String> result = ArrayFactory.newArray(String.class);

        final Thread currentThread = Thread.currentThread();
        final ClassLoader loader = currentThread.getContextClassLoader();
        final Enumeration<URL> urls = Utils.get(loader, first -> first.getResources(JarFile.MANIFEST_NAME));

        while (urls.hasMoreElements()) {

            try {

                final URL url = urls.nextElement();
                final InputStream is = url.openStream();

                if (is == null) {
                    LOGGER.warning(this, "not found input stream for the url " + url);
                    continue;
                }

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

            } catch (final Exception e) {
                LOGGER.warning(this, e);
            }
        }

        return result.toArray(String.class);
    }

    @Override
    protected @NotNull String[] getPathsToScan() {

        final Array<String> result = ArrayFactory.newArraySet(String.class);
        result.addAll(super.getPathsToScan());
        result.addAll(getManifestClassPath());

        return result.toArray(String.class);
    }
}
