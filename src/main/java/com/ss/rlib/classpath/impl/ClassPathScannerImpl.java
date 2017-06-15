package com.ss.rlib.classpath.impl;

import static java.lang.reflect.Modifier.isAbstract;
import com.ss.rlib.classpath.ClassPathScanner;
import com.ss.rlib.compiler.Compiler;
import com.ss.rlib.io.impl.ReuseBytesInputStream;
import com.ss.rlib.io.impl.ReuseBytesOutputStream;
import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.util.ArrayUtils;
import com.ss.rlib.util.IOUtils;
import com.ss.rlib.util.StringUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipException;

/**
 * The base implementation of the {@link ClassPathScanner}.
 *
 * @author JavaSaBr
 */
public class ClassPathScannerImpl implements ClassPathScanner {

    /**
     * The constant LOGGER.
     */
    protected static final Logger LOGGER = LoggerManager.getLogger(ClassPathScanner.class);

    private static final String CLASS_PATH = System.getProperty("java.class.path");
    private static final String PATH_SEPARATOR = File.pathSeparator;
    private static final String CLASS_EXTENSION = ".class";

    /**
     * The class loader.
     */
    @NotNull
    private final ClassLoader loader;

    /**
     * The found classes.
     */
    @NotNull
    private Class<?>[] classes;

    /**
     * The found resources.
     */
    @NotNull
    private String[] resources;

    /**
     * Instantiates a new Class path scanner.
     */
    public ClassPathScannerImpl() {
        this.loader = getClass().getClassLoader();
        this.classes = new Class[0];
        this.resources = new String[0];
    }

    @Override
    public void addClasses(@NotNull final Array<Class<?>> classes) {
        this.classes = ArrayUtils.combine(this.classes, classes.toArray(new Class[classes.size()]), Class.class);
    }

    @Override
    public void addResources(@NotNull final Array<String> resources) {
        this.resources = ArrayUtils.combine(this.resources, resources.toArray(new String[resources.size()]), String.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, R extends T> void findImplements(@NotNull final Array<Class<R>> container, @NotNull final Class<T> interfaceClass) {

        if (!interfaceClass.isInterface()) {
            throw new RuntimeException("class " + interfaceClass + " is not interface.");
        }

        for (final Class<?> cs : getClasses()) {

            if (cs.isInterface() || !interfaceClass.isAssignableFrom(cs) || isAbstract(cs.getModifiers())) {
                continue;
            }

            container.add((Class<R>) cs);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, R extends T> void findInherited(@NotNull final Array<Class<R>> container, @NotNull final Class<T> parentClass) {

        if (Modifier.isFinal(parentClass.getModifiers())) {
            throw new RuntimeException("class " + parentClass + " is final class.");
        }

        for (final Class<?> cs : getClasses()) {

            if (cs.isInterface() || cs == parentClass || !parentClass.isAssignableFrom(cs) || isAbstract(cs.getModifiers())) {
                continue;
            }

            container.add((Class<R>) cs);
        }
    }

    @Override
    public void getAll(@NotNull final Array<Class<?>> container) {
        container.addAll(getClasses());
    }

    @Override
    public void getAllResources(@NotNull Array<String> container) {
        container.addAll(getResources());
    }

    /**
     * @return the found classes.
     */
    @NotNull
    private Class<?>[] getClasses() {
        return classes;
    }

    /**
     * @return the found resources.
     */
    @NotNull
    private String[] getResources() {
        return resources;
    }

    /**
     * @return the class loader.
     */
    @NotNull
    private ClassLoader getLoader() {
        return loader;
    }

    /**
     * Get paths string [ ].
     *
     * @return the list of classpathes.
     */
    @NotNull
    protected String[] getPaths() {
        return CLASS_PATH.split(PATH_SEPARATOR);
    }

    /**
     * Load a class by its name to container.
     *
     * @param name      the name.
     * @param container the container.
     */
    private void loadClass(@NotNull final String name, @NotNull final Array<Class<?>> container) {
        if (!name.endsWith(CLASS_EXTENSION)) return;

        String className;
        try {

            className = name.replace(CLASS_EXTENSION, StringUtils.EMPTY);

            final StringBuilder result = new StringBuilder(className.length());

            for (int i = 0, length = className.length(); i < length; i++) {
                char ch = className.charAt(i);
                if (ch == '/') ch = '.';
                result.append(ch);
            }

            className = result.toString();

        } catch (final Exception e) {
            LOGGER.info("incorrect replace " + name + " to java path, separator " + File.separator);
            return;
        }

        try {
            container.add(getLoader().loadClass(className));
        } catch (NoClassDefFoundError error) {
            LOGGER.warning("can't load class: " + error.getMessage());
        } catch (final Throwable e) {
            LOGGER.warning(e);
        }
    }

    /**
     * Scan a directory and find here classes, resources or jars.
     *
     * @param classes   the classes container.
     * @param resources the resources container.
     * @param directory the directory.
     */
    private void scanningDirectory(@NotNull final Path rootPath, @NotNull final Array<Class<?>> classes,
                                   @NotNull final Array<String> resources, @NotNull final Path directory) {

        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {

            for (final Path file : stream) {

                if (Files.isDirectory(file)) {
                    scanningDirectory(rootPath, classes, resources, file);
                    continue;
                }

                final String filename = file.getFileName().toString();

                if (filename.endsWith(JAR_EXTENSION)) {
                    scanningJar(classes, resources, file);
                } else if (filename.endsWith(CLASS_EXTENSION)) {
                    try {

                        String path = file.subpath(rootPath.getNameCount(), file.getNameCount()).toString();

                        if (path.startsWith(File.separator)) {
                            path = path.substring(1, path.length());
                        }

                        loadClass(path, classes);

                    } catch (final Exception e) {
                        LOGGER.info("incorrect replace " + file + " from root " + rootPath);
                    }

                } else if (!filename.endsWith(Compiler.SOURCE_EXTENSION)) {

                    String path = file.subpath(rootPath.getNameCount(), file.getNameCount()).toString();

                    if (path.startsWith(File.separator)) {
                        path = path.substring(1, path.length());
                    }

                    resources.add(path);
                }
            }

        } catch (final IOException ex) {
            LOGGER.warning(ex);
        }
    }

    /**
     * Scan a .jar to load classes.
     *
     * @param classes   the classes container.
     * @param resources the resources container.
     * @param jarFile   the .jar file.
     */
    private void scanningJar(@NotNull final Array<Class<?>> classes, @NotNull Array<String> resources, @NotNull final Path jarFile) {

        if (!Files.exists(jarFile)) {
            LOGGER.warning("not exists " + jarFile);
            return;
        }

        final ReuseBytesOutputStream rout = new ReuseBytesOutputStream();
        final ReuseBytesInputStream rin = new ReuseBytesInputStream();

        final byte[] buffer = new byte[128];

        try (final JarInputStream jin = new JarInputStream(Files.newInputStream(jarFile))) {

            for (JarEntry entry = jin.getNextJarEntry(); entry != null; entry = jin.getNextJarEntry()) {
                if (entry.isDirectory()) continue;

                final String name = entry.getName();

                if (name.endsWith(JAR_EXTENSION)) {
                    rout.reset();
                    IOUtils.copy(jin, rout, buffer, false);
                    rin.initFor(rout.getData(), 0, rout.size());
                    scanningJar(classes, resources, rin);
                } else if (name.endsWith(CLASS_EXTENSION)) {
                    loadClass(name, classes);
                } else if (!name.endsWith(Compiler.SOURCE_EXTENSION)) {
                    resources.add(name);
                }
            }

        } catch (final ZipException e) {
            LOGGER.warning("can't open zip file " + jarFile);
        } catch (final IOException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * Scan a .jar to load classes.
     *
     * @param classes   the classes container.
     * @param resources the resources container.
     * @param jarFile   the input stream of a .jar.
     */
    private void scanningJar(@NotNull final Array<Class<?>> classes, @NotNull Array<String> resources, @NotNull final InputStream jarFile) {

        final ReuseBytesOutputStream rout = new ReuseBytesOutputStream();
        final ReuseBytesInputStream rin = new ReuseBytesInputStream();

        final byte[] buffer = new byte[128];

        try (final JarInputStream jin = new JarInputStream(jarFile)) {

            for (JarEntry entry = jin.getNextJarEntry(); entry != null; entry = jin.getNextJarEntry()) {
                if (entry.isDirectory()) continue;

                final String name = entry.getName();

                if (name.endsWith(JAR_EXTENSION)) {
                    rout.reset();
                    IOUtils.copy(jin, rout, buffer, false);
                    rin.initFor(rout.getData(), 0, rout.size());
                    scanningJar(classes, resources, rin);
                } else if (name.endsWith(CLASS_EXTENSION)) {
                    loadClass(name, classes);
                } else if (!name.endsWith(Compiler.SOURCE_EXTENSION)) {
                    resources.add(name);
                }
            }

        } catch (final ZipException e) {
            LOGGER.warning("can't open zip file " + jarFile);
        } catch (final IOException e) {
            LOGGER.warning(e);
        }
    }

    @Override
    public void scanning(@NotNull final Function<String, Boolean> filter) {

        final String[] paths = getPaths();

        final Array<Class<?>> classes = ArrayFactory.newArray(Class.class);
        final Array<String> resources = ArrayFactory.newArray(String.class);

        for (final String path : paths) {

            final Path file = Paths.get(path);

            if (!Files.exists(file) || (!filter.apply(path))) {
                continue;
            }

            if (LOGGER.isEnabledInfo()) {
                LOGGER.info("scanning " + file);
            }

            final String filename = file.getFileName().toString();

            if (Files.isDirectory(file)) {
                scanningDirectory(file, classes, resources, file);
            } else if (filename.endsWith(JAR_EXTENSION)) {
                scanningJar(classes, resources, file);
            }
        }

        this.classes = classes.toArray(new Class[classes.size()]);
        this.resources = resources.toArray(new String[resources.size()]);

        LOGGER.info("scanned for " + this.classes.length + " classes and " + this.resources.length + " resources.");
    }
}