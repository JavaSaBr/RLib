package com.ss.rlib.classpath.impl;

import static com.ss.rlib.util.ClassUtils.unsafeCast;
import static java.lang.reflect.Modifier.isAbstract;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ss.rlib.classpath.ClassPathScanner;
import com.ss.rlib.compiler.Compiler;
import com.ss.rlib.io.impl.ReuseBytesInputStream;
import com.ss.rlib.io.impl.ReuseBytesOutputStream;
import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.util.ArrayUtils;
import com.ss.rlib.util.IOUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;

/**
 * The base implementation of the {@link ClassPathScanner}.
 *
 * @author JavaSaBr
 */
public class ClassPathScannerImpl implements ClassPathScanner {

    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(ClassPathScanner.class);

    private static final String CLASS_PATH = System.getProperty("java.class.path");
    private static final String PATH_SEPARATOR = File.pathSeparator;
    private static final String CLASS_EXTENSION = ".class";

    /**
     * The list of additional paths to scan.
     */
    @NotNull
    private final Array<String> additionalPaths;

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
     * The flag of using system classpath.
     */
    private boolean useSystemClassPath;

    public ClassPathScannerImpl(@NotNull final ClassLoader classLoader) {
        this.additionalPaths = ArrayFactory.newArray(String.class);
        this.loader = classLoader;
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

    @Override
    public <T> void findImplements(@NotNull final Array<Class<T>> container, @NotNull final Class<T> interfaceClass) {

        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("The class " + interfaceClass + " is not interface.");
        }

        for (final Class<?> cs : getClasses()) {

            if (cs.isInterface() || !interfaceClass.isAssignableFrom(cs) || isAbstract(cs.getModifiers())) {
                continue;
            }

            container.add(unsafeCast(cs));
        }
    }

    @Override
    public <T> void findInherited(@NotNull final Array<Class<T>> container, @NotNull final Class<T> parentClass) {

        if (Modifier.isFinal(parentClass.getModifiers())) {
            throw new IllegalArgumentException("The class " + parentClass + " is final class.");
        }

        for (final Class<?> cs : getClasses()) {

            if (cs.isInterface() || cs == parentClass || !parentClass.isAssignableFrom(cs) || isAbstract(cs.getModifiers())) {
                continue;
            }

            container.add(unsafeCast(cs));
        }
    }

    @Override
    public void findAnnotated(@NotNull Array<Class<?>> container, @NotNull Class<? extends Annotation> annotationClass) {
        for (final Class<?> cs : getClasses()) {
            if (cs.isInterface() || isAbstract(cs.getModifiers()) || cs.isAnnotation() || !cs.isAnnotationPresent(annotationClass)) {
                continue;
            }
            
            container.add(cs);
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
    private @NotNull Class<?>[] getClasses() {
        return classes;
    }

    /**
     * @return the found resources.
     */
    private @NotNull String[] getResources() {
        return resources;
    }

    /**
     * @return the class loader.
     */
    private @NotNull ClassLoader getLoader() {
        return loader;
    }

    /**
     * Get a list of paths to scan.
     *
     * @return the list of paths.
     */
    protected @NotNull String[] getPathsToScan() {

        final String[] systemClasspath = useSystemClasspath() ? getClasspathPaths() : ArrayUtils.EMPTY_STRING_ARRAY;
        final Array<String> result = ArrayFactory.newArray(String.class,
                additionalPaths.size() + systemClasspath.length);

        result.addAll(systemClasspath);
        result.addAll(additionalPaths);

        return result.toArray(String.class);
    }

    /**
     * @return true if need to scan the system classpath.
     */
    protected boolean useSystemClasspath() {
        return useSystemClassPath;
    }

    @Override
    public void setUseSystemClasspath(final boolean useSystemClasspath) {
        this.useSystemClassPath = useSystemClasspath;
    }

    /**
     * @return the list of paths of system classpath.
     */
    protected @NotNull String[] getClasspathPaths() {
        return CLASS_PATH.split(PATH_SEPARATOR);
    }

    /**
     * Load a class by its name to container.
     *
     * @param rootPath  the root folder.
     * @param file      the class file.
     * @param name      the name.
     * @param container the container.
     */
    private void loadClass(@Nullable final Path rootPath, @Nullable final Path file, @NotNull final String name,
                           @NotNull final Array<Class<?>> container) {

        if (!name.endsWith(CLASS_EXTENSION)) {
            return;
        }

        String className;
        try {

            final StringBuilder result = new StringBuilder(name.length() - CLASS_EXTENSION.length());

            for (int i = 0, length = name.length() - CLASS_EXTENSION.length(); i < length; i++) {

                char ch = name.charAt(i);
                if (ch == '/' || ch == '\\') {
                    ch = '.';
                }

                result.append(ch);
            }

            className = result.toString();

        } catch (final Exception e) {
            LOGGER.warning(this, "Incorrect replaced " + name +
                    " to java path, used separator " + File.separator);
            return;
        }

        try {
            container.add(getLoader().loadClass(className));
        } catch (final NoClassDefFoundError error) {

            LOGGER.warning(this, "Can't load class: " + className + "\n" +
                    "Original name:" + name +"\n" +
                    "Root folder: " + rootPath + "\n" +
                    "Class file: " + file);

            LOGGER.warning(this, error);

        } catch (final Throwable e) {
            LOGGER.warning(this, e);
        }
    }

    /**
     * Scan a directory and find here classes, resources or jars.
     *
     * @param classes   the classes container.
     * @param resources the resources container.
     * @param directory the directory.
     */
    private void scanDirectory(@NotNull final Path rootPath, @NotNull final Array<Class<?>> classes,
                               @NotNull final Array<String> resources, @NotNull final Path directory) {

        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {

            for (final Path file : stream) {

                if (Files.isDirectory(file)) {
                    scanDirectory(rootPath, classes, resources, file);
                    continue;
                }

                final String filename = file.getFileName().toString();

                if (filename.endsWith(JAR_EXTENSION)) {
                    scanJar(classes, resources, file);
                } else if (filename.endsWith(CLASS_EXTENSION)) {

                    String path = file.subpath(rootPath.getNameCount(), file.getNameCount()).toString();

                    if (path.startsWith(File.separator)) {
                        path = path.substring(1, path.length());
                    }

                    loadClass(rootPath, file, path, classes);

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
    private void scanJar(@NotNull final Array<Class<?>> classes, @NotNull final Array<String> resources,
                         @NotNull final Path jarFile) {

        if (!Files.exists(jarFile)) {
            LOGGER.warning(this, "not exists " + jarFile);
            return;
        }

        final ReuseBytesOutputStream rout = new ReuseBytesOutputStream();
        final ReuseBytesInputStream rin = new ReuseBytesInputStream();

        final byte[] buffer = new byte[128];

        try (final JarInputStream jin = new JarInputStream(Files.newInputStream(jarFile))) {

            scanJarInputStream(classes, resources, rout, rin, buffer, jin);

        } catch (final ZipException e) {
            LOGGER.warning(this, "Can't open zip file " + jarFile);
            LOGGER.warning(this, e);
        } catch (final IOException e) {
            LOGGER.warning(this, e);
        }
    }

    private void scanJarInputStream(@NotNull final Array<Class<?>> classes, @NotNull final Array<String> resources,
                                    @NotNull final ReuseBytesOutputStream rout,
                                    @NotNull final ReuseBytesInputStream rin, @NotNull final byte[] buffer,
                                    @NotNull final JarInputStream jin) throws IOException {

        for (JarEntry entry = jin.getNextJarEntry(); entry != null; entry = jin.getNextJarEntry()) {

            if (entry.isDirectory()) {
                continue;
            }

            final String name = entry.getName();

            if (name.endsWith(JAR_EXTENSION)) {
                rout.reset();
                IOUtils.copy(jin, rout, buffer, false);
                rin.initFor(rout.getData(), 0, rout.size());
                scanJar(classes, resources, rin);
            } else if (name.endsWith(CLASS_EXTENSION)) {
                loadClass(null, null, name, classes);
            } else if (!name.endsWith(Compiler.SOURCE_EXTENSION)) {
                resources.add(name);
            }
        }
    }

    /**
     * Scan a .jar to load classes.
     *
     * @param classes   the classes container.
     * @param resources the resources container.
     * @param jarFile   the input stream of a .jar.
     */
    private void scanJar(@NotNull final Array<Class<?>> classes, @NotNull final Array<String> resources,
                         @NotNull final InputStream jarFile) {

        final ReuseBytesOutputStream rout = new ReuseBytesOutputStream();
        final ReuseBytesInputStream rin = new ReuseBytesInputStream();

        final byte[] buffer = new byte[128];

        try (final JarInputStream jin = new JarInputStream(jarFile)) {
            scanJarInputStream(classes, resources, rout, rin, buffer, jin);
        } catch (final ZipException e) {
            LOGGER.warning(this, "can't open zip file " + jarFile);
            LOGGER.warning(this, e);
        } catch (final IOException e) {
            LOGGER.warning(this, e);
        }
    }

    @Override
    public void scan(@Nullable final Function<String, Boolean> filter) {

        final String[] paths = getPathsToScan();

        final Array<Class<?>> classes = ArrayFactory.newArray(Class.class);
        final Array<String> resources = ArrayFactory.newArray(String.class);

        for (final String path : paths) {

            final Path file = Paths.get(path);

            if (!Files.exists(file) || (filter != null && !filter.apply(path))) {
                continue;
            }

            LOGGER.debug(this, file, toScan -> "scan " + toScan);

            final String filename = file.getFileName().toString();

            if (Files.isDirectory(file)) {
                scanDirectory(file, classes, resources, file);
            } else if (filename.endsWith(JAR_EXTENSION)) {
                scanJar(classes, resources, file);
            }
        }

        this.classes = classes.toArray(new Class[classes.size()]);
        this.resources = resources.toArray(new String[resources.size()]);

        LOGGER.debug(this, getClasses(), getResources(),
                (cses, rses) -> "scanned for " + cses.length + " classes and " + rses.length + " resources.");
    }

    @Override
    public void addAdditionalPath(@NotNull final String path) {
        this.additionalPaths.add(path);
    }

    @Override
    public void addAdditionalPaths(@NotNull final String[] paths) {
        this.additionalPaths.addAll(paths);
    }
}
