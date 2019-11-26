package com.ss.rlib.common.util;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayComparator;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.UnsafeArray;
import com.ss.rlib.logger.api.LoggerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipInputStream;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
public class FileUtils {

    public static final ArrayComparator<Path> FILE_PATH_LENGTH_COMPARATOR = (first, second) -> {

        int firstLength = first.getNameCount();
        int secondLength = second.getNameCount();

        if (firstLength == secondLength) {
            int firstLevel = Files.isDirectory(first) ? 2 : 1;
            int secondLevel = Files.isDirectory(first) ? 2 : 1;
            return firstLevel - secondLevel;
        }

        return firstLength - secondLength;
    };

    private static final Pattern FILE_NAME_PATTERN = Pattern.compile(
            "# Match a valid Windows filename (unspecified file system).          \n" +
                    "^                                # Anchor to start of string.        \n" +
                    "(?!                              # Assert filename is not: CON, PRN, \n" +
                    "  (?:                            # AUX, NUL, COM1, COM2, COM3, COM4, \n" +
                    "    CON|PRN|AUX|NUL|             # COM5, COM6, COM7, COM8, COM9,     \n" +
                    "    COM[1-9]|LPT[1-9]            # LPT1, LPT2, LPT3, LPT4, LPT5,     \n" +
                    "  )                              # LPT6, LPT7, LPT8, and LPT9...     \n" +
                    "  (?:\\.[^.]*)?                  # followed by optional extension    \n" +
                    "  $                              # and end of string                 \n" +
                    ")                                # End negative lookahead assertion. \n" +
                    "[^<>:\"/\\\\|?*\\x00-\\x1F]*     # Zero or more valid filename chars.\n" +
                    "[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]  # Last char is not a space or dot.  \n" +
                    "$                                # Anchor to end of string.            ",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);

    private static final Pattern NORMALIZE_FILE_NAME_PATTERN = Pattern.compile("[\\\\/:*?\"<>|]");

    private static final SimpleFileVisitor<Path> DELETE_FOLDER_VISITOR = new SimpleFileVisitor<>() {

        @Override
        public FileVisitResult postVisitDirectory(@NotNull Path dir, @Nullable IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }
    };

    private static final Path[] EMPTY_PATHS = new Path[0];

    /**
     * Check a string on valid file name.
     *
     * @param filename the file name.
     * @return true if the file name is valid.
     */
    public static boolean isValidName(@Nullable String filename) {

        if (StringUtils.isEmpty(filename)) {
            return false;
        }

        return FILE_NAME_PATTERN.matcher(filename)
                .matches();
    }

    /**
     * Normalize the file name to a valid file name.
     *
     * @param filename the string with file name.
     * @return normalized file name.
     */
    public static @NotNull String normalizeName(@Nullable String filename) {

        if (StringUtils.isEmpty(filename)) {
            return "_";
        }

        return NORMALIZE_FILE_NAME_PATTERN.matcher(filename)
                .replaceAll("_");
    }

    /**
     * Add recursive all files to the container from the folder.
     *
     * @param container   the file container.
     * @param dir         the folder.
     * @param withFolders need to add folders.
     * @param extensions  extensions filter.
     */
    public static void addFilesTo(
        @NotNull Array<Path> container,
        @NotNull Path dir,
        boolean withFolders,
        @Nullable String... extensions
    ) {

        if (Files.isDirectory(dir) && withFolders) {
            container.add(dir);
        }

        if (!Files.exists(dir)) {
            LoggerManager.getDefaultLogger().warning(dir, arg -> "Folder " + arg + " not found");
            return;
        }

        try (var stream = Files.newDirectoryStream(dir)) {
            for (var path : stream) {
                if (Files.isDirectory(path)) {
                    addFilesTo(container, path, withFolders, extensions);
                } else if (extensions == null || extensions.length < 1 || containsExtensions(extensions, path.getFileName())) {
                    container.add(path);
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Check the extensions of the file.
     *
     * @param extensions the checked extensions.
     * @param path       the file.
     * @return true if the file has a checked extension.
     */
    public static boolean containsExtensions(@Nullable String[] extensions, @NotNull Path path) {
        return containsExtensions(extensions, path.toString());
    }

    /**
     * Check the extensions of the path.
     *
     * @param extensions the checked extensions.
     * @param path       the path.
     * @return true if the path has a checked extension.
     */
    public static boolean containsExtensions(@Nullable String[] extensions, @NotNull String path) {
        return ArrayUtils.anyMatchR(extensions, path, String::endsWith);
    }

    /**
     * Check the extensions of the file.
     *
     * @param extensions the checked extensions.
     * @param path       the file.
     * @return true if the file has a checked extension.
     */
    public static boolean containsExtensions(@Nullable Array<String> extensions, @NotNull Path path) {
        return containsExtensions(extensions, path.toString());
    }

    /**
     * Check the extensions of the path.
     *
     * @param extensions the checked extensions.
     * @param path       the path.
     * @return true if the path has a checked extension.
     */
    public static boolean containsExtensions(@Nullable Array<String> extensions, @NotNull String path) {
        return extensions != null && extensions.anyMatchR(path, String::endsWith);
    }

    /**
     * Check the extensions of the file.
     *
     * @param extensions the checked extensions.
     * @param path       the file.
     * @return true if the file has a checked extension.
     */
    public static boolean containsExtensions(@Nullable Collection<String> extensions, @NotNull Path path) {
        return containsExtensions(extensions, path.toString());
    }

    /**
     * Check the extensions of the path.
     *
     * @param extensions the checked extensions.
     * @param path       the path.
     * @return true if the path has a checked extension.
     */
    public static boolean containsExtensions(@Nullable Collection<String> extensions, @NotNull String path) {
        return extensions != null && extensions.stream().anyMatch(path::endsWith);
    }

    /**
     * Delete the file.
     *
     * @param path the file to delete.
     */
    public static void delete(@NotNull Path path) {
        try {
            deleteImpl(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Delete the file or the folder.
     *
     * @param path the file or folder.
     */
    private static void deleteImpl(@NotNull Path path) throws IOException {
        if (!Files.isDirectory(path)) {
            Files.delete(path);
        } else {
            Files.walkFileTree(path, DELETE_FOLDER_VISITOR);
        }
    }

    /**
     * Get an extension of the path.
     *
     * @param path the path.
     * @return the extension or the path if the path doesn't have an extension.
     */
    public static @NotNull String getExtension(@Nullable String path) {
        return getExtension(path, false);
    }

    /**
     * Check the path about existing an extension.
     *
     * @param path the path.
     * @return true if the path contains any extension.
     */
    public static boolean hasExtension(@Nullable String path) {

        if (StringUtils.isEmpty(path)) {
            return false;
        }

        var index = path.lastIndexOf('.');

        if (index == -1) {
            return false;
        }

        var separatorIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));

        return separatorIndex < index;
    }

    /**
     * Get an extension of the path.
     *
     * @param path        the path.
     * @param toLowerCase true if need that extension was only in lower case.
     * @return the extension or empty string.
     */
    public static @NotNull String getExtension(@Nullable String path, boolean toLowerCase) {

        if (StringUtils.isEmpty(path)) {
            return StringUtils.EMPTY;
        }

        var index = path.lastIndexOf('.');

        if (index == -1) {
            return StringUtils.EMPTY;
        }

        var separatorIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));

        if (separatorIndex > index) {
            return StringUtils.EMPTY;
        }

        var result = path.substring(index + 1);

        if (toLowerCase) {
            return result.toLowerCase();
        }

        return result;
    }

    /**
     * Get an extension of the file.
     *
     * @param file the file.
     * @return the extension or empty string.
     */
    public static @NotNull String getExtension(@NotNull Path file) {

        if (Files.isDirectory(file)) {
            return StringUtils.EMPTY;
        }

        return getExtension(Objects.toString(file.getFileName()));
    }

    /**
     * Get an extension of the file.
     *
     * @param file        the file.
     * @param toLowerCase true if need that extension was only in lower case.
     * @return the extension or empty string.
     */
    public static @NotNull String getExtension(@NotNull Path file, boolean toLowerCase) {

        if (Files.isDirectory(file)) {
            return StringUtils.EMPTY;
        }

        return getExtension(Objects.toString(file.getFileName()), toLowerCase);
    }

    /**
     * Recursive get all files of the folder.
     *
     * @param dir        the folder.
     * @param extensions the extension filter.
     * @return the list of all files.
     */
    public static @NotNull Array<Path> getFiles(@NotNull Path dir, @Nullable String... extensions) {
        return getFiles(dir, false, extensions);
    }

    /**
     * Recursive get all files of the folder.
     *
     * @param dir         the folder.
     * @param withFolders need include folders.
     * @param extensions  the extension filter.
     * @return the list of all files.
     */
    public static @NotNull Array<Path> getFiles(@NotNull Path dir, boolean withFolders, @Nullable String... extensions) {

        Array<Path> result = ArrayFactory.newArray(Path.class);

        addFilesTo(result, dir, withFolders, extensions);

        UnsafeArray<Path> unsafeArray = result.asUnsafe();
        unsafeArray.trimToSize();

        return result;
    }

    /**
     * Get all files in the package.
     *
     * @param pckg       the package.
     * @param extensions the extensions filter.
     * @return the array of files.
     */
    public static @NotNull Path[] getFiles(@NotNull Package pckg, @Nullable String... extensions) {

        ClassLoader classLoader = Thread
            .currentThread()
            .getContextClassLoader();

        Enumeration<URL> urls = null;
        try {
            urls = classLoader.getResources(pckg.getName().replace('.', '/'));
        } catch (IOException exc) {
            LoggerManager.getDefaultLogger().warning(exc);
        }

        if (urls == null) {
            return EMPTY_PATHS;
        }

        var files = Array.ofType(Path.class);

        while (urls.hasMoreElements()) {

            var next = urls.nextElement();
            var path = next.getFile();

            if (path.contains("%20")) {
                path = path.replaceAll("%20", " ");
            }

            var file = Paths.get(path);

            if (Files.isDirectory(file)) {
                files.addAll(getFiles(file, extensions));
            } else if (extensions == null || extensions.length < 1 || containsExtensions(extensions, path)) {
                files.add(file);
            }
        }

        return files.toArray(Path.class);
    }

    /**
     * Get a file name without extension.
     *
     * @param filename the file name.
     * @return the file name without extension.
     */
    public static @NotNull String getNameWithoutExtension(@NotNull String filename) {

        if (StringUtils.isEmpty(filename)) {
            return filename;
        }

        int index = filename.lastIndexOf('.');

        if (index == -1) {
            return filename;
        }

        return filename.substring(0, index);
    }

    /**
     * Get a file name without extension.
     *
     * @param file the file.
     * @return the file name without extension.
     */
    public static @NotNull String getNameWithoutExtension(@NotNull Path file) {

        String filename = file.getFileName().toString();

        if (StringUtils.isEmpty(filename)) {
            return filename;
        }

        int index = filename.lastIndexOf('.');

        if (index == -1) {
            return filename;
        }

        return filename.substring(0, index);
    }

    /**
     * Read the file as a string from classpath.
     *
     * @param path the path to file.
     * @return the string content of the file.
     */
    public static @NotNull String readFromClasspath(@NotNull String path) {
        return read(FileUtils.class.getResourceAsStream(path));
    }

    /**
     * Read the file as a string from classpath.
     *
     * @param cs the class to get a classloader.
     * @param path the path to file.
     * @return the string content of the file.
     */
    public static @NotNull String readFromClasspath(@NotNull Class<?> cs, @NotNull String path) {
        return read(cs.getResourceAsStream(path));
    }

    /**
     * Read the file as a string.
     *
     * @param path the path to file.
     * @return the string content of the file.
     */
    public static @NotNull String read(@NotNull String path) {
        try {
            return read(Files.newInputStream(Paths.get(path)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Read the input stream as a string.
     *
     * @param in the input stream.
     * @return the string content of the input stream.
     */
    public static @NotNull String read(@NotNull InputStream in) {

        var content = new StringBuilder();

        try (var reader = new BufferedReader(new InputStreamReader(in))) {

            var buffer = CharBuffer.allocate(512);

            while (reader.ready()) {

                buffer.clear();
                reader.read(buffer);
                buffer.flip();

                content.append(buffer.array(), 0, buffer.limit());
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return content.toString();
    }

    /**
     * Read the file as a string.
     *
     * @param file the file.
     * @return the string content of the file.
     */
    public static @NotNull String read(@NotNull Path file) {
        try {
            return read(Files.newInputStream(file));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Find a first free file name in the directory.
     *
     * @param directory the directory.
     * @param file      the checked file.
     * @return the first free name.
     */
    public static @NotNull String getFirstFreeName(@NotNull Path directory, @NotNull Path file) {

        var initFileName = file
            .getFileName()
            .toString();

        if (!Files.exists(directory.resolve(initFileName))) {
            return initFileName;
        }

        var extension = getExtension(initFileName);
        var nameWithoutExtension = getNameWithoutExtension(initFileName);
        var result = nameWithoutExtension + "_1." + extension;

        for (int i = 2; Files.exists(directory.resolve(result)); i++) {
            result = nameWithoutExtension + "_" + i + "." + extension;
        }

        return result;
    }

    /**
     * Unzip the zip file to the destination folder.
     *
     * @param destination the destination folder.
     * @param zipFile     the zip file.
     */
    public static void unzip(@NotNull Path destination, @NotNull Path zipFile) {

        if (!Files.exists(destination)) {
            throw new IllegalArgumentException("The folder " + destination + " doesn't exist.");
        }

        try (var zin = new ZipInputStream(Files.newInputStream(zipFile))) {
            for (var entry = zin.getNextEntry(); entry != null; entry = zin.getNextEntry()) {

                var file = destination.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(file);
                } else {
                    Files.copy(zin, file, StandardCopyOption.REPLACE_EXISTING);
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Get a name of the file by the path and the separator.
     *
     * @param path      the path.
     * @param separator the separator.
     * @return the name.
     */
    public static @NotNull String getName(@NotNull String path, char separator) {

        if (path.length() < 2) {
            return path;
        }

        int index = path.lastIndexOf(separator);

        if (index == -1) {
            return path;
        }

        return path.substring(index + 1, path.length());
    }

    /**
     * Get a parent path of the path using the separator.
     *
     * @param path      the path.
     * @param separator the separator.
     * @return the parent path.
     */
    public static @NotNull String getParent(@NotNull String path, char separator) {

        if (path.length() < 2) {
            return path;
        }

        int index = path.lastIndexOf(separator);

        if (index == -1) {
            return path;
        }

        return path.substring(0, index);
    }

    /**
     * @param directory the directory.
     * @param attrs     the directory attributes.
     * @see Files#createDirectories(Path, FileAttribute[])
     */
    public static void createDirectories(@NotNull Path directory, @NotNull FileAttribute<?>... attrs) {
        Utils.unchecked(directory, attrs, Files::createDirectories);
    }

    /**
     * @param file    the file.
     * @param options the link options.
     * @return the last modified time.
     * @see Files#getLastModifiedTime(Path, LinkOption...)
     */
    public static @NotNull FileTime getLastModifiedTime(@NotNull Path file, @NotNull LinkOption... options) {
        return notNull(Utils.uncheckedGet(file, options, Files::getLastModifiedTime));
    }

    /**
     * Get a {@link URI} of the file.
     *
     * @param file the file.
     * @return the {@link URI}.
     */
    public static @NotNull URI getUri(@NotNull Path file) {
        return Utils.uncheckedGet(file, Path::toUri);
    }

    /**
     * Get a {@link URI} of the {@link URL}.
     *
     * @param url the url.
     * @return the {@link URI}.
     */
    public static @NotNull URI getUri(@NotNull URL url) {
        return Utils.uncheckedGet(url, URL::toURI);
    }

    /**
     * Get a {@link URL} of the file.
     *
     * @param file the file.
     * @return the {@link URL}.
     */
    public static @NotNull URL getUrl(@NotNull Path file) {
        return Utils.uncheckedGet(getUri(file), URI::toURL);
    }

    /**
     * @param base  the base file path.
     * @param other the other file path.
     * @return the resulting relative path, or an empty path if both paths are equal.
     * @see Path#relativize(Path)
     */
    public static @NotNull Path relativize(@NotNull Path base, @NotNull Path other) {
        return Utils.uncheckedGet(base, other, Path::relativize);
    }

    /**
     * @param base  the base file path.
     * @param other the other filepath.
     * @return the resulting relative path, or an empty path if both paths are equal or null.
     * @see Path#relativize(Path)
     */
    public static @Nullable Path safeRelativize(@Nullable Path base, @Nullable Path other) {
        if (base == null || other == null) {
            return null;
        } else {
            return Utils.uncheckedGet(base, other, Path::relativize);
        }
    }

    /**
     * Create a new default watch service.
     *
     * @return the new default watch service.
     * @see FileSystems#getDefault()
     */
    public static @NotNull WatchService newDefaultWatchService() {
        try {
            return FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * @param start   the start folder.
     * @param visitor the visitor.
     * @return the start folder.
     * @see Files#walkFileTree(Path, FileVisitor)
     */
    public static @NotNull Path walkFileTree(@NotNull Path start, @NotNull FileVisitor<? super Path> visitor) {
        try {
            return Files.walkFileTree(start, visitor);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * @param prefix the prefix of a temp file.
     * @param suffix the suffix of a temp file.
     * @param attrs  the additional attributes.
     * @return the created temp file.
     * @see Files#createTempFile(String, String, FileAttribute[])
     */
    public static @NotNull Path createTempFile(
        @NotNull String prefix,
        @NotNull String suffix,
        @NotNull FileAttribute<?>... attrs
    ) {
        try {
            return Files.createTempFile(prefix, suffix, attrs);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Apply each sub-file of the directory to the consumer.
     *
     * @param directory the directory.
     * @param consumer  the consumer.
     */
    public static void forEach(@NotNull Path directory, @NotNull Consumer<@NotNull Path> consumer) {
        validateDirectory(directory);

        try (var stream = Files.newDirectoryStream(directory)) {
            for (var path : stream) {
                consumer.accept(path);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void validateDirectory(@NotNull Path directory) {
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("The file " + directory + " isn't a directory.");
        } else if (!Files.exists(directory)) {
            throw new IllegalArgumentException("The file " + directory + " isn't exists.");
        }
    }

    /**
     * Apply each sub-file of the directory to the consumer.
     *
     * @param directory the directory.
     * @param argument  the argument.
     * @param consumer  the consumer.
     * @param <T>       the argument's type.
     */
    public static <T> void forEach(
        @NotNull Path directory,
        @NotNull T argument,
        @NotNull BiConsumer<@NotNull Path, @NotNull T> consumer
    ) {
        validateDirectory(directory);

        try (var stream = Files.newDirectoryStream(directory)) {
            for (var path : stream) {
                consumer.accept(path, argument);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Get children's stream of the directory.
     *
     * @param directory the directory.
     * @return children's stream of the directory.
     */
    public static @NotNull Stream<Path> stream(@NotNull Path directory) {
        validateDirectory(directory);

        var files = Array.ofType(Path.class);

        try (var stream = Files.newDirectoryStream(directory)) {
            stream.forEach(files::add);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return files.stream();
    }

    /**
     * Apply each sub-file of the directory to the consumer.
     *
     * @param directory the directory.
     * @param argument  the argument.
     * @param consumer  the consumer.
     * @param <T>       the argument's type.
     */
    public static <T> void forEachR(
        @NotNull Path directory,
        @NotNull T argument,
        @NotNull BiConsumer<@NotNull T, @NotNull Path> consumer
    ) {
        validateDirectory(directory);

        try (var stream = Files.newDirectoryStream(directory)) {
            for (var path : stream) {
                consumer.accept(argument, path);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Apply each sub-file of the directory to the consumer.
     *
     * @param directory the directory.
     * @param argument  the argument.
     * @param condition the condition.
     * @param consumer  the consumer.
     * @param <T>       the argument's type.
     */
    public static <T> void forEachR(
        @NotNull Path directory,
        @NotNull T argument,
        @NotNull Predicate<@NotNull Path> condition,
        @NotNull BiConsumer<@NotNull T, @NotNull Path> consumer
    ) {
        validateDirectory(directory);

        try (var stream = Files.newDirectoryStream(directory)) {
            for (var path : stream) {
                if (condition.test(path)) {
                    consumer.accept(argument, path);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
