package rlib.util;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.ObjectDictionary;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;

/**
 * Класс для работы с файлами.
 *
 * @author Ronn
 * @created 01.03.2012
 */
public class FileUtils {

    private static final Logger LOGGER = LoggerManager.getLogger(FileUtils.class);

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
     * Кеш текста файлов.
     */
    private static final ObjectDictionary<String, String> CACHE = DictionaryFactory.newObjectDictionary();

    /**
     * Кэш файлов.
     */
    private static final ObjectDictionary<String, Path> CACHE_FILES = DictionaryFactory.newObjectDictionary();

    /**
     * Рекурсивное получение всех файлов в папке с учетом расширения.
     *
     * @param dir        папка.
     * @param container  контейнер файлов.
     * @param extensions набор нужных расширений.
     * @return список всех найденных файлов.
     */
    public static void addFilesTo(final Path dir, final Array<Path> container, final String... extensions) {

        if (!Files.exists(dir)) {
            LOGGER.warning("not found folder " + dir);
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {

            for (final Path path : stream) {
                if (Files.isDirectory(path)) {
                    addFilesTo(path, container, extensions);
                } else if (extensions == StringUtils.EMPTY_ARRAY || containsFormat(extensions, path.getFileName())) {
                    container.add(path);
                }
            }

        } catch (final IOException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * Очистка кэша файлов.
     */
    public static void clean() {
        CACHE.clear();
    }

    /**
     * Определят, подходит ли по формату фаил.
     *
     * @param formats набор форматов.
     * @param path    проверяемый фаил.
     * @return подходит ли.
     */
    public static boolean containsFormat(final String[] formats, final Path path) {

        if (path == null) {
            return false;
        }

        return containsFormat(formats, path.toString());
    }

    /**
     * Определят, подходит ли по формату фаил.
     *
     * @param formats набор форматов.
     * @param path    проверяемый фаил.
     * @return подходит ли.
     */
    public static boolean containsFormat(final String[] formats, final String path) {

        for (int i = 0, length = formats.length; i < length; i++) {
            if (path.endsWith(formats[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Копирует информацию с одного файла в другой.
     *
     * @param pathSource адресс исходного файла.
     * @param pathDest   адресс конечного файла.
     * @return скопирован ли фаил.
     */
    public static boolean copyFile(final String pathSource, final String pathDest) {

        try (FileInputStream source = new FileInputStream(pathSource)) {
            try (FileOutputStream destination = new FileOutputStream(pathDest)) {

                final FileChannel sourceChannel = source.getChannel();
                final FileChannel destinationChannel = destination.getChannel();

                destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            }

            return true;
        } catch (final IOException e) {
            LOGGER.warning(e);
        }

        return false;
    }

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
     * Чтение контента файла.
     *
     * @param file читаемый фаил.
     * @return массив байтов фаила.
     */
    public static byte[] getContent(final Path file) {

        try (SeekableByteChannel channel = Files.newByteChannel(file)) {

            final byte[] content = new byte[(int) channel.size()];
            final ByteBuffer byteBuffer = ByteBuffer.wrap(content);

            channel.read(byteBuffer);

            return content;
        } catch (final IOException e) {
            LOGGER.warning(e);
        }

        return null;
    }

    public static final String getExtension(final String path) {

        if (StringUtils.isEmpty(path)) {
            return path;
        }

        final int index = path.lastIndexOf('.');

        if (index == -1) {
            return path;
        }

        return path.substring(index + 1, path.length());
    }

    /**
     * Рекурсивное получение всех файлов в папке с учетом расширения.
     *
     * @param dir        папка.
     * @param extensions набор нужных расширений.
     * @return список всех найденных файлов.
     */
    public static Array<Path> getFiles(final Path dir, final String... extensions) {
        final Array<Path> result = ArrayFactory.newArray(Path.class);
        addFilesTo(dir, result, extensions);
        result.trimToSize();
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

        if (urls == null) {
            return new Path[0];
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
                files.addAll(getFiles(file, formats));
            } else if (formats == StringUtils.EMPTY_ARRAY || containsFormat(formats, path)) {
                files.add(file);
            }
        }

        files.trimToSize();

        return files.array();
    }

    public static final String getNameWithoutExtension(final String path) {

        if (StringUtils.isEmpty(path)) {
            return path;
        }

        final int index = path.lastIndexOf('.');

        if (index == -1) {
            return path;
        }

        return path.substring(0, index);
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

        if (CACHE.containsKey(path)) {
            return CACHE.get(path);
        }

        Path file = CACHE_FILES.get(path);

        if (file == null) {
            file = Paths.get(path);
            CACHE_FILES.put(path, file);
        }

        if (!Files.exists(file)) {
            return null;
        }

        final StringBuilder content = new StringBuilder();

        try (BufferedReader in = Files.newBufferedReader(file)) {

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

        CACHE.put(path, content.toString());
        return content.toString();
    }
}