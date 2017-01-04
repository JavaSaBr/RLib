package rlib.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.array.Array;
import rlib.util.array.ArrayComparator;
import rlib.util.array.ArrayFactory;
import rlib.util.array.UnsafeArray;

/**
 * The clss with utility methods.
 *
 * @author JavaSaBr
 */
public class FileUtils {

    private static final Logger LOGGER = LoggerManager.getLogger(FileUtils.class);

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

    /**
     * Check a string on valid file name.
     *
     * @param filename the string with file name.
     * @return true if the file name is valid.
     */
    public static boolean isValidName(@Nullable final String filename) {
        if (StringUtils.isEmpty(filename)) return false;
        final Matcher matcher = FILE_NAME_PATTERN.matcher(filename);
        return matcher.matches();
    }

    /**
     * Recursive add all files to a container in a folder.
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
            LOGGER.warning("not found folder " + dir);
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
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
     * Check an extension of a file.
     *
     * @param extensions the checked extensions.
     * @param path       the file.
     * @return true if the file has a checked extension.
     */
    public static boolean containsExtensions(@Nullable final String[] extensions, @NotNull final Path path) {
        return containsExtensions(extensions, path.toString());
    }

    /**
     * Check an extension of a path.
     *
     * @param extensions the checked extensions.
     * @param path       the path.
     * @return true if the path has a checked extension.
     */
    public static boolean containsExtensions(@Nullable final String[] extensions, @NotNull final String path) {
        return ArrayUtils.find(extensions, path, (extension, str) -> str.endsWith(extension)) != null;
    }

    /**
     * Delete a file.
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
     * Delete a file or a folder.
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
     * Get an extension of a path.
     *
     * @param path the path.
     * @return the extension or the path if the path doesn't have an extension.
     */
    public static String getExtension(@Nullable final String path) {
        if (StringUtils.isEmpty(path)) return path;
        final int index = path.lastIndexOf('.');
        if (index == -1) return path;
        return path.substring(index + 1, path.length());
    }

    /**
     * Get an extension of a file.
     *
     * @param file the file.
     * @return the extension or the file name if the file doesn't have an extension.
     */
    public static String getExtension(@NotNull final Path file) {
        if (Files.isDirectory(file)) return StringUtils.EMPTY;
        return getExtension(Objects.toString(file.getFileName()));
    }

    /**
     * Recursive get all files of a folder.
     *
     * @param dir        the folder.
     * @param extensions the extension filter.
     * @return the list of all files.
     */
    @NotNull
    public static Array<Path> getFiles(@NotNull final Path dir, @Nullable final String... extensions) {
        return getFiles(dir, false, extensions);
    }

    /**
     * Recursive get all files of a folder.
     *
     * @param dir         the folder.
     * @param withFolders need include folders.
     * @param extensions  the extension filter.
     * @return the list of all files.
     */
    @NotNull
    public static Array<Path> getFiles(@NotNull final Path dir, final boolean withFolders, @Nullable final String... extensions) {

        final Array<Path> result = ArrayFactory.newArray(Path.class);

        addFilesTo(result, dir, withFolders, extensions);

        final UnsafeArray<Path> unsafeArray = result.asUnsafe();
        unsafeArray.trimToSize();

        return result;
    }

    /**
     * Get all files in a package.
     *
     * @param pckg       the package.
     * @param extensions the extensions filter.
     * @return the array of files.
     */
    @NotNull
    public static Path[] getFiles(@NotNull final Package pckg, @Nullable final String... extensions) {

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Enumeration<URL> urls = null;
        try {
            urls = classLoader.getResources(pckg.getName().replace('.', '/'));
        } catch (final IOException e) {
            LOGGER.warning(e);
        }

        if (urls == null) return new Path[0];

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
    @NotNull
    public static String getNameWithoutExtension(@NotNull final String filename) {
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
    @NotNull
    public static String getNameWithoutExtension(@NotNull final Path file) {

        final String filename = file.getFileName().toString();
        if (StringUtils.isEmpty(filename)) return filename;

        final int index = filename.lastIndexOf('.');
        if (index == -1) return filename;

        return filename.substring(0, index);
    }

    /**
     * Read a file by a path.
     *
     * @param path the path to file.
     * @return the all content of the file.
     */
    @NotNull
    public static String read(@NotNull final String path) {

        final StringBuilder content = new StringBuilder();

        try (final BufferedReader in = Files.newBufferedReader(Paths.get(path))) {

            final CharBuffer buffer = CharBuffer.allocate(512);

            while (in.ready()) {

                buffer.clear();
                in.read(buffer);
                buffer.flip();

                content.append(buffer.array(), 0, buffer.limit());
            }

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }

    /**
     * Read a file.
     *
     * @param file the file.
     * @return the all content of the file.
     */
    @NotNull
    public static String read(@NotNull final Path file) {

        final StringBuilder content = new StringBuilder();

        try (final BufferedReader in = Files.newBufferedReader(file)) {

            final CharBuffer buffer = CharBuffer.allocate(512);

            while (in.ready()) {

                buffer.clear();
                in.read(buffer);
                buffer.flip();

                content.append(buffer.array(), 0, buffer.limit());
            }

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }

    /**
     * Find a first free file name in a directory.
     *
     * @param directory the directory.
     * @param file      the checked file.
     * @return the first free name.
     */
    @NotNull
    public static String getFirstFreeName(@NotNull final Path directory, @NotNull final Path file) {

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
     * Convert the file to {@link URL}.
     *
     * @param path the path for converting.
     * @return the URL of the path.
     * @throws MalformedURLException If a protocol handler for the URL could not be found, or if some other error
     *                               occurred while constructing the URL.
     */
    @NotNull
    public static URL toUrl(@NotNull final Path path) throws MalformedURLException {
        return path.toUri().toURL();
    }
}