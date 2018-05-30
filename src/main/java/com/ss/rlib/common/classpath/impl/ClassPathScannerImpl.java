package com.ss.rlib.common.classpath.impl;

import static java.lang.reflect.Modifier.isAbstract;
import com.ss.rlib.common.classpath.ClassPathScanner;
import com.ss.rlib.common.compiler.Compiler;
import com.ss.rlib.common.io.impl.ReuseBytesInputStream;
import com.ss.rlib.common.io.impl.ReuseBytesOutputStream;
import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.IOUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipException;

/**
 * The base implementation of the {@link ClassPathScanner}.
 *
 * @author JavaSaBr
 */
public class ClassPathScannerImpl implements ClassPathScanner {

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

    public ClassPathScannerImpl(@NotNull ClassLoader classLoader) {
        this.additionalPaths = ArrayFactory.newArray(String.class);
        this.loader = classLoader;
        this.classes = new Class[0];
        this.resources = new String[0];
    }

    @Override
    public void addClasses(@NotNull Array<Class<?>> classes) {
        this.classes = ArrayUtils.combine(this.classes,
                classes.toArray(new Class[classes.size()]), Class.class);
    }

    @Override
    public void addResources(@NotNull Array<String> resources) {
        this.resources = ArrayUtils.combine(this.resources,
                resources.toArray(new String[resources.size()]), String.class);
    }

    @Override
    public <T> void findImplements(@NotNull Array<Class<T>> container, @NotNull Class<T> interfaceClass) {

        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("The class " + interfaceClass + " is not interface.");
        }

        for (Class<?> cs : getClasses()) {

            if (cs.isInterface() || !interfaceClass.isAssignableFrom(cs) || isAbstract(cs.getModifiers())) {
                continue;
            }

            container.add(ClassUtils.unsafeCast(cs));
        }
    }

    @Override
    public <T> void findInherited(@NotNull Array<Class<T>> container, @NotNull Class<T> parentClass) {

        if (Modifier.isFinal(parentClass.getModifiers())) {
            throw new IllegalArgumentException("The class " + parentClass + " is final class.");
        }

        for (Class<?> cs : getClasses()) {

            if (cs.isInterface() || cs == parentClass || !parentClass.isAssignableFrom(cs) ||
                    isAbstract(cs.getModifiers())) {
                continue;
            }

            container.add(ClassUtils.unsafeCast(cs));
        }
    }

    @Override
    public void findAnnotated(@NotNull Array<Class<?>> container, @NotNull Class<? extends Annotation> annotationClass) {
        for (Class<?> cs : getClasses()) {

            if (cs.isInterface() || isAbstract(cs.getModifiers()) || cs.isAnnotation() ||
                    !cs.isAnnotationPresent(annotationClass)) {
                continue;
            }
            
            container.add(cs);
        }
    }

    @Override
    public void getFoundClasses(@NotNull Array<Class<?>> container) {
        container.addAll(getClasses());
    }

    @Override
    public void getFoundResources(@NotNull Array<String> container) {
        container.addAll(getResources());
    }

    @Override
    public @NotNull Array<Class<?>> getFoundClasses() {
        return Array.of(getClasses());
    }

    @Override
    public @NotNull Array<String> getFoundResources() {
        return Array.of(getResources());
    }

    /**
     * Get the found classes.
     *
     * @return the found classes.
     */
    private @NotNull Class<?>[] getClasses() {
        return classes;
    }

    /**
     * Get the found resources.
     *
     * @return the found resources.
     */
    private @NotNull String[] getResources() {
        return resources;
    }

    /**
     * Get the class loader.
     *
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

        String[] systemClasspath = useSystemClasspath() ? getClasspathPaths() : ArrayUtils.EMPTY_STRING_ARRAY;
        int capacity = additionalPaths.size() + systemClasspath.length;

        Array<String> result = ArrayFactory.newArray(String.class, capacity);
        result.addAll(systemClasspath);
        result.addAll(additionalPaths);

        return result.toArray(String.class);
    }

    /**
     * Return true if need to scan the system classpath.
     *
     * @return true if need to scan the system classpath.
     */
    protected boolean useSystemClasspath() {
        return useSystemClassPath;
    }

    @Override
    public void setUseSystemClasspath(boolean useSystemClasspath) {
        this.useSystemClassPath = useSystemClasspath;
    }

    /**
     * Get the list of paths of system classpath.
     *
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
    private void loadClass(
            @Nullable Path rootPath,
            @Nullable Path file,
            @NotNull String name,
            @NotNull Array<Class<?>> container
    ) {

        if (!name.endsWith(CLASS_EXTENSION)) {
            return;
        }

        String className;
        try {

            StringBuilder result = new StringBuilder(name.length() - CLASS_EXTENSION.length());

            for (int i = 0, length = name.length() - CLASS_EXTENSION.length(); i < length; i++) {

                char ch = name.charAt(i);
                if (ch == '/' || ch == '\\') {
                    ch = '.';
                }

                result.append(ch);
            }

            className = result.toString();

        } catch (Exception e) {
            LOGGER.warning(this, "Incorrect replaced " + name +
                    " to java path, used separator " + File.separator);
            return;
        }

        try {
            container.add(getLoader().loadClass(className));
        } catch (NoClassDefFoundError error) {

            LOGGER.warning(this, "Can't load class: " + className + "\n" +
                    "Original name:" + name +"\n" +
                    "Root folder: " + rootPath + "\n" +
                    "Class file: " + file);

            LOGGER.warning(this, error);

        } catch (Throwable e) {
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
    private void scanDirectory(
            @NotNull Path rootPath,
            @NotNull Array<Class<?>> classes,
            @NotNull Array<String> resources,
            @NotNull Path directory
    ) {

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {

            for (Path file : stream) {

                if (Files.isDirectory(file)) {
                    scanDirectory(rootPath, classes, resources, file);
                    continue;
                }

                String filename = file.getFileName().toString();

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

        } catch (IOException ex) {
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
    private void scanJar(@NotNull Array<Class<?>> classes, @NotNull Array<String> resources, @NotNull Path jarFile) {

        if (!Files.exists(jarFile)) {
            LOGGER.warning(this, "not exists " + jarFile);
            return;
        }

        ReuseBytesOutputStream rout = new ReuseBytesOutputStream();
        ReuseBytesInputStream rin = new ReuseBytesInputStream();

        byte[] buffer = new byte[128];

        try (JarInputStream jin = new JarInputStream(Files.newInputStream(jarFile))) {
            scanJarInputStream(classes, resources, rout, rin, buffer, jin);
        } catch (ZipException e) {
            LOGGER.warning(this, "Can't open zip file " + jarFile);
            LOGGER.warning(this, e);
        } catch (IOException e) {
            LOGGER.warning(this, e);
        }
    }

    private void scanJarInputStream(
            @NotNull Array<Class<?>> classes,
            @NotNull Array<String> resources,
            @NotNull ReuseBytesOutputStream rout,
            @NotNull ReuseBytesInputStream rin, @NotNull byte[] buffer,
            @NotNull JarInputStream jin) throws IOException {

        for (JarEntry entry = jin.getNextJarEntry(); entry != null; entry = jin.getNextJarEntry()) {

            if (entry.isDirectory()) {
                continue;
            }

            String name = entry.getName();

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
    private void scanJar(
            @NotNull Array<Class<?>> classes,
            @NotNull Array<String> resources,
            @NotNull InputStream jarFile
    ) {

        ReuseBytesOutputStream rout = new ReuseBytesOutputStream();
        ReuseBytesInputStream rin = new ReuseBytesInputStream();

        byte[] buffer = new byte[128];

        try (JarInputStream jin = new JarInputStream(jarFile)) {
            scanJarInputStream(classes, resources, rout, rin, buffer, jin);
        } catch (final ZipException e) {
            LOGGER.warning(this, "can't open zip file " + jarFile);
            LOGGER.warning(this, e);
        } catch (final IOException e) {
            LOGGER.warning(this, e);
        }
    }

    @Override
    public void scan(@Nullable Predicate<String> filter) {

        String[] paths = getPathsToScan();

        Array<Class<?>> classes = ArrayFactory.newArray(Class.class);
        Array<String> resources = ArrayFactory.newArray(String.class);

        for (String path : paths) {

            Path file = Paths.get(path);

            if (!Files.exists(file) || (filter != null && !filter.test(path))) {
                continue;
            }

            LOGGER.debug(this, file, toScan -> "scan " + toScan);

            String filename = file.getFileName().toString();

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
    public void addAdditionalPath(@NotNull String path) {
        this.additionalPaths.add(path);
    }

    @Override
    public void addAdditionalPaths(@NotNull String[] paths) {
        this.additionalPaths.addAll(paths);
    }
}
