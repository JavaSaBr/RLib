package com.ss.rlib.common.classpath.impl;

import com.ss.rlib.common.classpath.ClassPathScanner;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
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
    private final @NotNull Class<?> rootClass;

    /**
     * The classpath key.
     */
    private final @NotNull String classPathKey;

    public ManifestClassPathScannerImpl(
        @NotNull ClassLoader classLoader,
        @NotNull Class<?> rootClass,
        @NotNull String classPathKey
    ) {
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

        var root = Utils.getRootFolderFromClass(rootClass);
        var result = Array.ofType(String.class);

        var currentThread = Thread.currentThread();
        var loader = currentThread.getContextClassLoader();
        var urls = Utils.uncheckedGet(loader, arg -> arg.getResources(JarFile.MANIFEST_NAME));

        while (urls.hasMoreElements()) {

            try {

                var url = urls.nextElement();
                var is = url.openStream();

                if (is == null) {
                    LOGGER.warning(url, arg -> "not found input stream for the url " + arg);
                    continue;
                }

                var manifest = new Manifest(is);
                var attributes = manifest.getMainAttributes();

                var value = attributes.getValue(classPathKey);

                if (value == null) {
                    continue;
                }

                var classpath = value.split(" ");

                for (var path : classpath) {

                    var file = root.resolve(path);

                    if (Files.exists(file)) {
                        result.add(file.toString());
                    }
                }

            } catch (Exception exc) {
                LOGGER.warning(exc);
            }
        }

        return result.toArray(String.class);
    }

    @Override
    protected @NotNull String[] getPathsToScan() {

        var result = ArrayFactory.newArraySet(String.class);
        result.addAll(super.getPathsToScan());
        result.addAll(getManifestClassPath());

        return result.toArray(String.class);
    }
}
