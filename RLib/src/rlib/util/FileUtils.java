package rlib.util;

import java.io.BufferedReader;
import java.io.IOException;
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
 * Класс для работы с файлами.
 *
 * @author JavaSaBr
 */
public class FileUtils {

    private static final Logger LOGGER = LoggerManager.getLogger(FileUtils.class);

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
     * Проверка валидности названия файла.
     *
     * @param filename название файла.
     * @return валидное ли название файла.
     */
    public static boolean isValidName(final String filename) {
        final Matcher matcher = FILE_NAME_PATTERN.matcher(filename);
        return matcher.matches();
    }

    /**
     * Рекурсивное получение всех файлов в папке с учетом расширения.
     *
     * @param container   контейнер файлов.
     * @param dir         папка.
     * @param withFolders добавлять ли папки в результат.
     * @param extensions  набор нужных расширений.
     */
    public static void addFilesTo(final Array<Path> container, final Path dir, final boolean withFolders, final String... extensions) {

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
            LOGGER.warning(e);
        }
    }

    /**
     * Определят, подходит ли по расширению фаил.
     *
     * @param extensions набор расширений.
     * @param path       проверяемый фаил.
     * @return подходит ли.
     */
    public static boolean containsExtensions(final String[] extensions, final Path path) {
        return path != null && containsExtensions(extensions, path.toString());
    }

    /**
     * Определят, подходит ли по расширению фаил.
     *
     * @param extensions набор расширений.
     * @param path       проверяемый фаил.
     * @return подходит ли.
     */
    public static boolean containsExtensions(final String[] extensions, final String path) {
        return ArrayUtils.find(extensions, path, (extension, p) -> p.endsWith(extension)) != null;
    }

    /**
     * Удаление файла.
     */
    public static void delete(final Path path) {
        try {
            deleteImpl(path);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteImpl(final Path path) throws IOException {
        if (!Files.isDirectory(path)) {
            Files.delete(path);
        } else {
            Files.walkFileTree(path, DELETE_FOLDER_VISITOR);
        }
    }

    /**
     * Получение расширения файла.
     *
     * @param path путь файла чье расширение надо получить.
     * @return расширение этого файла.
     */
    public static String getExtension(final String path) {
        if (StringUtils.isEmpty(path)) return path;

        final int index = path.lastIndexOf('.');
        if (index == -1) return path;

        return path.substring(index + 1, path.length());
    }

    /**
     * Получение расширения файла.
     *
     * @param file файл чье расширение надо получить.
     * @return расширение этого файла.
     */
    public static String getExtension(final Path file) {
        if (Files.isDirectory(file)) return StringUtils.EMPTY;
        return getExtension(Objects.toString(file.getFileName()));
    }

    /**
     * Рекурсивное получение всех файлов в папке с учетом расширения.
     *
     * @param dir        папка.
     * @param extensions набор нужных расширений.
     * @return список всех найденных файлов.
     */
    public static Array<Path> getFiles(final Path dir, final String... extensions) {
        return getFiles(dir, false, extensions);
    }

    /**
     * Рекурсивное получение всех файлов в папке с учетом расширения.
     *
     * @param dir         папка.
     * @param withFolders вместе с папками.
     * @param extensions  набор нужных расширений.
     * @return список всех найденных файлов.
     */
    public static Array<Path> getFiles(final Path dir, final boolean withFolders, final String... extensions) {

        final Array<Path> result = ArrayFactory.newArray(Path.class);

        addFilesTo(result, dir, withFolders, extensions);

        final UnsafeArray<Path> unsafeArray = result.asUnsafe();
        unsafeArray.trimToSize();

        return result;
    }

    /**
     * Получаем все файлы в пакете нужных форматов.
     *
     * @param pckg    пакет.
     * @param formats набор нужных форматов.
     * @return все файлы.
     */
    public static Path[] getFiles(final Package pckg, final String... formats) {

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
                files.addAll(getFiles(file, formats));
            } else if (formats == null || formats.length < 1 || containsExtensions(formats, path)) {
                files.add(file);
            }
        }

        return files.toArray(Path.class);
    }

    /**
     * Получение имени файла без расширения.
     *
     * @param filename имя файла.
     * @return имя файла без расширения.
     */
    public static String getNameWithoutExtension(final String filename) {
        if (StringUtils.isEmpty(filename)) return filename;

        final int index = filename.lastIndexOf('.');
        if (index == -1) return filename;

        return filename.substring(0, index);
    }

    /**
     * Получение имени файла без расширения.
     *
     * @param file файл для получения имени.
     * @return имя файла бе расширения.
     */
    public static String getNameWithoutExtension(final Path file) {

        final String filename = file.getFileName().toString();

        if (StringUtils.isEmpty(filename)) {
            return filename;
        }

        final int index = filename.lastIndexOf('.');

        if (index == -1) {
            return filename;
        }

        return filename.substring(0, index);
    }

    /**
     * Чтение текста из файла по указанному пути.
     *
     * @param path путь к файлу.
     */
    public static String read(final String path) {

        if (path == null) {
            return null;
        }

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
            LOGGER.warning(e);
        }

        return content.toString();
    }

    /**
     * Чтение текста из файла.
     *
     * @param file читаемый файл.
     */
    public static String read(final Path file) {

        if (file == null) {
            return null;
        }

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
            LOGGER.warning(e);
        }

        return content.toString();
    }

    /**
     * Получение свободного имени для указанного файла в указанной директории.
     *
     * @param directory проверяемая директория.
     * @param file      проверяемый файл.
     * @return свободное имя.
     */
    public static String getFirstFreeName(final Path directory, final Path file) {

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
}