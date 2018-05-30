package com.ss.rlib.common.util;

import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayComparator;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.UnsafeArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
public class FileUtils {

    /**
     * The constant FILE_PATH_LENGTH_COMPARATOR.
     */
    @NotNull
    public static final ArrayComparator<Path> FILE_PATH_LENGTH_COMPARATOR = (first, second) -> {

        final int firstLength = first.getNameCount();
        final int secondLength = second.getNameCount();

        if (firstLength == secondLength) {
            final int firstLevel = Files.isDirectory(first) ? 2 : 1;
            final int secondLevel = Files.isDirectory(first) ? 2 : 1;
            return firstLevel - secondLevel;
        }

        return firstLength - secondLength;
    };

    @NotNull
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

    @NotNull
    private static final Pattern NORMALIZE_FILE_NAME_PATTERN = Pattern.compile("[\\\\/:*?\"<>|]");

    @NotNull
    private static final SimpleFileVisitor<Path> DELETE_FOLDER_VISITOR = new SimpleFileVisitor<Path>() {

        @Override
        public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }
    };

    @NotNull
    private static final Path[] EMPTY_PATHS = new Path[0];

    /**
     * Check a string on valid file name.
     *
     * @param filename the file name.
     * @return true if the file name is valid.
     */
    public static boolean isValidName(@Nullable final String filename) {
        if (StringUtils.isEmpty(filename)) return false;
        final Matcher matcher = FILE_NAME_PATTERN.matcher(filename);
        return matcher.matches();
    }

    /**
     * Normalize the file name to a valid file name.
     *
     * @param filename the string with file name.
     * @return normalized file name.
     */
    public static @NotNull String normalizeName(@Nullable final String filename) {
        if (StringUtils.isEmpty(filename)) return "_";
        final Matcher matcher = NORMALIZE_FILE_NAME_PATTERN.matcher(filename);
        return matcher.replaceAll("_");
    }

    /**
     * Add recursive all files to the container from the folder.
     *
     * @param container   the file container.
     * @param dir         the folder.
     * @param withFolders need to add folders.
     * @param extensions  extensions filter.
     */
    public static void addFilesTo(@NotNull final Array<Path> container, @NotNull final Path dir, final boolean withFolders,
                                  @Nullable final String... extensions) {

        if (Files.isDirectory(dir) && withFolders) {
            container.add(dir);
        }

        if (!Files.exists(dir)) {
            Utils.print(FileUtils.class, "not found folder " + dir);
            return;
        }

        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (final Path path : stream) {
                if (Files.isDirectory(path)) {
                    addFilesTo(container, path, withFolders, extensions);
                } else if (extensions == null || extensions.length < 1 || containsExtensions(extensions, path.getFileName())) {
                    container.add(path);
                }
            }

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check the extensions of the file.
     *
     * @param extensions the checked extensions.
     * @param path       the file.
     * @return true if the file has a checked extension.
     */
    public static boolean containsExtensions(@Nullable final String[] extensions, @NotNull final Path path) {
        return containsExtensions(extensions, path.toString());
    }

    /**
     * Check the extensions of the path.
     *
     * @param extensions the checked extensions.
     * @param path       the path.
     * @return true if the path has a checked extension.
     */
    public static boolean containsExtensions(@Nullable final String[] extensions, @NotNull final String path) {
        return ArrayUtils.find(extensions, path, (extension, str) -> str.endsWith(extension)) != null;
    }

    /**
     * Check the extensions of the file.
     *
     * @param extensions the checked extensions.
     * @param path       the file.
     * @return true if the file has a checked extension.
     */
    public static boolean containsExtensions(@Nullable final Array<String> extensions, @NotNull final Path path) {
        return containsExtensions(extensions, path.toString());
    }

    /**
     * Check the extensions of the path.
     *
     * @param extensions the checked extensions.
     * @param path       the path.
     * @return true if the path has a checked extension.
     */
    public static boolean containsExtensions(@Nullable final Array<String> extensions, @NotNull final String path) {
        return extensions != null && extensions.search(path, (extension, str) -> str.endsWith(extension)) != null;
    }

    /**
     * Check the extensions of the file.
     *
     * @param extensions the checked extensions.
     * @param path       the file.
     * @return true if the file has a checked extension.
     */
    public static boolean containsExtensions(@Nullable final Collection<String> extensions, @NotNull final Path path) {
        return containsExtensions(extensions, path.toString());
    }

    /**
     * Check the extensions of the path.
     *
     * @param extensions the checked extensions.
     * @param path       the path.
     * @return true if the path has a checked extension.
     */
    public static boolean containsExtensions(@Nullable final Collection<String> extensions,
                                             @NotNull final String path) {
        return extensions != null && extensions.stream().anyMatch(path::endsWith);
    }

    /**
     * Delete the file.
     *
     * @param path the file to delete.
     */
    public static void delete(@NotNull final Path path) {
        try {
            deleteImpl(path);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the file or the folder.
     *
     * @param path the file or folder.
     */
    private static void deleteImpl(@NotNull final Path path) throws IOException {
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
    public static @NotNull String getExtension(@Nullable final String path) {
        return getExtension(path, false);
    }

    /**
     * Get an extension of the path.
     *
     * @param path        the path.
     * @param toLowerCase true if need that extension was only in lower case.
     * @return the extension or the path if the path doesn't have an extension.
     */
    public static @NotNull String getExtension(@Nullable final String path, final boolean toLowerCase) {
        if (StringUtils.isEmpty(path)) return StringUtils.EMPTY;
        final int index = path.lastIndexOf('.');
        if (index == -1) return StringUtils.EMPTY;
        final String result = path.substring(index + 1, path.length());
        if (toLowerCase) return result.toLowerCase();
        return result;
    }

    /**
     * Get an extension of the file.
     *
     * @param file the file.
     * @return the extension or the file name if the file doesn't have an extension.
     */
    public static @NotNull String getExtension(@NotNull final Path file) {
        if (Files.isDirectory(file)) return StringUtils.EMPTY;
        return getExtension(Objects.toString(file.getFileName()));
    }

    /**
     * Get an extension of the file.
     *
     * @param file        the file.
     * @param toLowerCase true if need that extension was only in lower case.
     * @return the extension or the file name if the file doesn't have an extension.
     */
    public static @NotNull String getExtension(@NotNull final Path file, final boolean toLowerCase) {
        if (Files.isDirectory(file)) return StringUtils.EMPTY;
        return getExtension(Objects.toString(file.getFileName()), toLowerCase);
    }

    /**
     * Recursive get all files of the folder.
     *
     * @param dir        the folder.
     * @param extensions the extension filter.
     * @return the list of all files.
     */
    public static @NotNull Array<Path> getFiles(@NotNull final Path dir, @Nullable final String... extensions) {
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
    public static @NotNull Array<Path> getFiles(@NotNull final Path dir, final boolean withFolders, @Nullable final String... extensions) {

        final Array<Path> result = ArrayFactory.newArray(Path.class);

        addFilesTo(result, dir, withFolders, extensions);

        final UnsafeArray<Path> unsafeArray = result.asUnsafe();
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
    public static @NotNull Path[] getFiles(@NotNull final Package pckg, @Nullable final String... extensions) {

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Enumeration<URL> urls = null;
        try {
            urls = classLoader.getResources(pckg.getName().replace('.', '/'));
        } catch (final IOException e) {
            Utils.print(FileUtils.class, e);
        }

        if (urls == null) {
            return EMPTY_PATHS;
        }

        final Array<Path> files = ArrayFactory.newArray(Path.class);

        while (urls.hasMoreElements()) {

            final URL next = urls.nextElement();

            String path = next.getFile();

            if (path.contains("%20")) {
                path = path.replaceAll("%20", " ");
            }

            final Path file = Paths.get(path);

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
    public static @NotNull String getNameWithoutExtension(@NotNull final String filename) {
        if (StringUtils.isEmpty(filename)) return filename;

        final int index = filename.lastIndexOf('.');
        if (index == -1) return filename;

        return filename.substring(0, index);
    }

    /**
     * Get a file name without extension.
     *
     * @param file the file.
     * @return the file name without extension.
     */
    public static @NotNull String getNameWithoutExtension(@NotNull final Path file) {

        final String filename = file.getFileName().toString();
        if (StringUtils.isEmpty(filename)) return filename;

        final int index = filename.lastIndexOf('.');
        if (index == -1) return filename;

        return filename.substring(0, index);
    }

    /**
     * Read the file as a string.
     *
     * @param path the path to file.
     * @return the string content of the file.
     */
    public static @NotNull String read(@NotNull final String path) {
        try {
            return read(Files.newInputStream(Paths.get(path)));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read the input stream as a string.
     *
     * @param in the input stream.
     * @return the string content of the input stream.
     */
    public static @NotNull String read(@NotNull final InputStream in) {

        final StringBuilder content = new StringBuilder();

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            final CharBuffer buffer = CharBuffer.allocate(512);

            while (reader.ready()) {

                buffer.clear();
                reader.read(buffer);
                buffer.flip();

                content.append(buffer.array(), 0, buffer.limit());
            }

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }

    /**
     * Read the file as a string.
     *
     * @param file the file.
     * @return the string content of the file.
     */
    public static @NotNull String read(@NotNull final Path file) {
        try {
            return read(Files.newInputStream(file));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Find a first free file name in the directory.
     *
     * @param directory the directory.
     * @param file      the checked file.
     * @return the first free name.
     */
    public static @NotNull String getFirstFreeName(@NotNull final Path directory, @NotNull final Path file) {

        String initFileName = file.getFileName().toString();

        if (!Files.exists(directory.resolve(initFileName))) {
            return initFileName;
        }

        final String extension = getExtension(initFileName);
        final String nameWithoutExtension = getNameWithoutExtension(initFileName);

        String result = nameWithoutExtension + "_1." + extension;

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
    public static void unzip(@NotNull final Path destination, @NotNull final Path zipFile) {

        if (!Files.exists(destination)) {
            throw new IllegalArgumentException("The folder " + destination + " doesn't exist.");
        }

        try (ZipInputStream zin = new ZipInputStream(Files.newInputStream(zipFile))) {
            for (ZipEntry entry = zin.getNextEntry(); entry != null; entry = zin.getNextEntry()) {

                final Path file = destination.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(file);
                } else {
                    Files.copy(zin, file, StandardCopyOption.REPLACE_EXISTING);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
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
        Utils.run(directory, attrs, Files::createDirectories);
    }

    /**
     * @param file    the file.
     * @param options the link options.
     * @see Files#getLastModifiedTime(Path, LinkOption...)
     */
    public static FileTime getLastModifiedTime(@NotNull Path file, @NotNull LinkOption... options) {
        return Utils.safeGet(file, options, Files::getLastModifiedTime);
    }

    /**
     * Get a {@link URI} of the file.
     *
     * @param file the file.
     * @return the {@link URI}.
     */
    public static @NotNull URI getUri(@NotNull Path file) {
        return Utils.get(file, Path::toUri);
    }

    /**
     * Get a {@link URL} of the file.
     *
     * @param file the file.
     * @return the {@link URL}.
     */
    public static @NotNull URL getUrl(@NotNull Path file) {
        return Utils.get(getUri(file), URI::toURL);
    }
}