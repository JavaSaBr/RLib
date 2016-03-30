package rlib.classpath.impl;

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

import rlib.classpath.ClassPathScanner;
import rlib.io.impl.ReuseBytesInputStream;
import rlib.io.impl.ReuseBytesOutputStream;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ArrayUtils;
import rlib.util.StringUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import static rlib.compiler.Compiler.SOURCE_EXTENSION;
import static rlib.util.IOUtils.copy;

/**
 * Реализация обычного сканера classpath.
 *
 * @author Ronn
 */
public class ClassPathScannerImpl implements ClassPathScanner {

    protected static final Logger LOGGER = LoggerManager.getLogger(ClassPathScanner.class);

    private static final String CLASS_PATH = System.getProperty("java.class.path");
    private static final String PATH_SEPARATOR = File.pathSeparator;
    private static final String CLASS_EXTENSION = ".class";

    /**
     * Загрузчик классов.
     */
    private final ClassLoader loader;

    /**
     * Найденные классы.
     */
    private Class<?>[] classes;

    /**
     * Найденные ресурсы.
     */
    private String[] resources;

    public ClassPathScannerImpl() {
        this.loader = getClass().getClassLoader();
    }

    @Override
    public void addClasses(final Array<Class<?>> added) {
        this.classes = ArrayUtils.combine(classes, added.toArray(new Class[added.size()]), Class.class);
    }

    @Override
    public void addResources(final Array<String> added) {
        this.resources = ArrayUtils.combine(resources, added.toArray(new String[added.size()]), String.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, R extends T> void findImplements(final Array<Class<R>> container, final Class<T> interfaceClass) {

        if (!interfaceClass.isInterface()) {
            throw new RuntimeException("class " + interfaceClass + " is not interface.");
        }

        for (final Class<?> cs : getClasses()) {

            if (cs.isInterface() || !interfaceClass.isAssignableFrom(cs) || Modifier.isAbstract(cs.getModifiers())) {
                continue;
            }

            container.add((Class<R>) cs);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, R extends T> void findInherited(final Array<Class<R>> container, final Class<T> parentClass) {

        if (Modifier.isFinal(parentClass.getModifiers())) {
            throw new RuntimeException("class " + parentClass + " is final class.");
        }

        for (final Class<?> cs : getClasses()) {

            if (cs.isInterface() || cs == parentClass || !parentClass.isAssignableFrom(cs) || Modifier.isAbstract(cs.getModifiers())) {
                continue;
            }

            container.add((Class<R>) cs);
        }
    }

    @Override
    public void getAll(final Array<Class<?>> container) {
        container.addAll(getClasses());
    }

    @Override
    public void getAllResources(Array<String> container) {
        container.addAll(getResources());
    }

    /**
     * @return найденные классы.
     */
    private Class<?>[] getClasses() {
        return classes;
    }

    /**
     * @return найденные ресурсы.
     */
    private String[] getResources() {
        return resources;
    }

    /**
     * @return загрузчик классов.
     */
    private ClassLoader getLoader() {
        return loader;
    }

    /**
     * @return список путей к классам.
     */
    protected String[] getPaths() {
        return CLASS_PATH.split(PATH_SEPARATOR);
    }

    /**
     * Загрузка класса по его имени в контейнер.
     *
     * @param name      название класса.
     * @param container контейнер загруженных классов.
     */
    private void loadClass(final String name, final Array<Class<?>> container) {

        if (!name.endsWith(CLASS_EXTENSION)) {
            return;
        }

        String className = null;

        try {

            className = name.replace(CLASS_EXTENSION, StringUtils.EMPTY);

            final StringBuilder result = new StringBuilder(className.length());

            for (int i = 0, length = className.length(); i < length; i++) {

                char ch = className.charAt(i);

                if (ch == '/') {
                    ch = '.';
                }

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
     * Сканирование папки на наличие в ней классов, ресурсов или jar.
     *
     * @param classes   контейнер загружаемых классов.
     * @param resources контейнер загружаемых ресурсов.
     * @param directory сканируемая папка.
     */
    private void scanningDirectory(final Path rootPath, final Array<Class<?>> classes, final Array<String> resources, final Path directory) {

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

                } else if (!filename.endsWith(SOURCE_EXTENSION)) {

                    String path = file.subpath(rootPath.getNameCount(), file.getNameCount()).toString();

                    if (path.startsWith(File.separator)) {
                        path = path.substring(1, path.length());
                    }

                    resources.add(path);
                }
            }

        } catch (IOException e1) {
            LOGGER.warning(e1);
        }
    }

    /**
     * Сканирование .jar для подгрузки классов.
     *
     * @param classes   контейнер подгруженных классов.
     * @param resources контейнер ресурсов.
     * @param jarFile   ссылка на .jar фаил.
     */
    private void scanningJar(final Array<Class<?>> classes, Array<String> resources, final Path jarFile) {

        if (!Files.exists(jarFile)) {
            LOGGER.warning("not exists " + jarFile);
            return;
        }

        final ReuseBytesOutputStream rout = new ReuseBytesOutputStream();
        final ReuseBytesInputStream rin = new ReuseBytesInputStream();

        final byte[] buffer = new byte[128];

        try (final JarInputStream jin = new JarInputStream(Files.newInputStream(jarFile))) {

            for (JarEntry entry = jin.getNextJarEntry(); entry != null; entry = jin.getNextJarEntry()) {

                if (entry.isDirectory()) {
                    continue;
                }

                final String name = entry.getName();

                if (name.endsWith(JAR_EXTENSION)) {
                    rout.reset();
                    copy(jin, rout, buffer, false);
                    rin.initFor(rout.getData(), 0, rout.size());
                    scanningJar(classes, resources, rin);
                } else if (name.endsWith(CLASS_EXTENSION)) {
                    loadClass(name, classes);
                } else if (!name.endsWith(SOURCE_EXTENSION)) {
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
     * Сканирование .jar для подгрузки классов.
     *
     * @param classes   контейнер подгруженных классов.
     * @param resources контейнер ресурсов.
     * @param jarFile   содержимоей jar файла.
     */
    private void scanningJar(final Array<Class<?>> classes, Array<String> resources, final InputStream jarFile) {

        final ReuseBytesOutputStream rout = new ReuseBytesOutputStream();
        final ReuseBytesInputStream rin = new ReuseBytesInputStream();

        final byte[] buffer = new byte[128];

        try (final JarInputStream jin = new JarInputStream(jarFile)) {

            for (JarEntry entry = jin.getNextJarEntry(); entry != null; entry = jin.getNextJarEntry()) {

                if (entry.isDirectory()) {
                    continue;
                }

                final String name = entry.getName();

                if (name.endsWith(JAR_EXTENSION)) {
                    rout.reset();
                    copy(jin, rout, buffer, false);
                    rin.initFor(rout.getData(), 0, rout.size());
                    scanningJar(classes, resources, rin);
                } else if (name.endsWith(CLASS_EXTENSION)) {
                    loadClass(name, classes);
                } else if (!name.endsWith(SOURCE_EXTENSION)) {
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
    public void scanning(final Function<String, Boolean> filter) {

        if (classes != null || resources != null) {
            throw new RuntimeException("scanning is already.");
        }

        final String[] paths = getPaths();

        final Array<Class<?>> classes = ArrayFactory.newArray(Class.class);
        final Array<String> resources = ArrayFactory.newArray(String.class);

        for (final String path : paths) {

            final Path file = Paths.get(path);

            if (!Files.exists(file) || (filter != null && !filter.apply(path))) {
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
