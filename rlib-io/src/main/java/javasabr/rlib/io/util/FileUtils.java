package javasabr.rlib.io.util;

import static javasabr.rlib.common.util.ObjectUtils.notNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipInputStream;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.common.util.ArrayUtils;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.common.util.Utils;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
public class FileUtils {

  private static final Logger LOGGER = LoggerManager.getLogger(FileUtils.class);

  public static final Comparator<Path> FILE_PATH_LENGTH_COMPARATOR = (first, second) -> {

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
      "# Match a valid Windows filename (unspecified file system).          \n"
          + "^                                # Anchor to start of string.        \n"
          + "(?!                              # Assert filename is not: CON, PRN, \n"
          + "  (?:                            # AUX, NUL, COM1, COM2, COM3, COM4, \n"
          + "    CON|PRN|AUX|NUL|             # COM5, COM6, COM7, COM8, COM9,     \n"
          + "    COM[1-9]|LPT[1-9]            # LPT1, LPT2, LPT3, LPT4, LPT5,     \n"
          + "  )                              # LPT6, LPT7, LPT8, and LPT9...     \n"
          + "  (?:\\.[^.]*)?                  # followed by optional extension    \n"
          + "  $                              # and end of string                 \n"
          + ")                                # End negative lookahead assertion. \n"
          + "[^<>:\"/\\\\|?*\\x00-\\x1F]*     # Zero or more valid filename chars.\n"
          + "[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]  # Last char is not a space or dot.  \n"
          + "$                                # Anchor to end of string.            ",
      Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);

  private static final Pattern NORMALIZE_FILE_NAME_PATTERN = Pattern.compile("[\\\\/:*?\"<>|]");

  private static final SimpleFileVisitor<Path> DELETE_FOLDER_VISITOR = new SimpleFileVisitor<>() {

    @Override
    public FileVisitResult postVisitDirectory(Path dir, @Nullable IOException exc) throws IOException {
      Files.delete(dir);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      Files.delete(file);
      return FileVisitResult.CONTINUE;
    }
  };

  private static final Path[] EMPTY_PATHS = new Path[0];

  public static boolean isValidFileName(@Nullable String filename) {

    if (StringUtils.isEmpty(filename)) {
      return false;
    }

    return FILE_NAME_PATTERN
        .matcher(filename)
        .matches();
  }

  /**
   * Normalize the file name to an invalid file name.
   */
  public static String normalizeName(@Nullable String filename) {

    if (StringUtils.isEmpty(filename)) {
      return "_";
    }

    return NORMALIZE_FILE_NAME_PATTERN
        .matcher(filename)
        .replaceAll("_");
  }



  public static Array<Path> getFiles(Path directory, String @Nullable ... extensions) {
    return getFiles(directory, false, extensions);
  }

  public static Array<Path> getFiles(
      Path directory,
      boolean includeDirectoriesToResult,
      String @Nullable ... extensions) {
    MutableArray<Path> result = MutableArray.ofType(Path.class);
    collectFilesTo(result, directory, includeDirectoriesToResult, extensions);
    return Array.copyOf(result);
  }

  public static Path[] getFiles(Package pckg, String @Nullable ... extensions) {

    ClassLoader classLoader = Thread
        .currentThread()
        .getContextClassLoader();

    Enumeration<URL> urls = null;
    try {
      urls = classLoader.getResources(pckg
          .getName()
          .replace('.', '/'));
    } catch (IOException exc) {
      LoggerManager
          .getDefaultLogger()
          .warning(exc);
    }

    if (urls == null) {
      return EMPTY_PATHS;
    }

    var files = MutableArray.ofType(Path.class);

    while (urls.hasMoreElements()) {

      var next = urls.nextElement();
      var path = next.getFile();

      if (path.contains("%20")) {
        path = path.replaceAll("%20", " ");
      }

      var file = Paths.get(path);

      if (Files.isDirectory(file)) {
        files.addAll(getFiles(file, extensions));
      } else if (extensions == null || extensions.length < 1 || hasExtensions(path, extensions)) {
        files.add(file);
      }
    }

    return files.toArray(Path.class);
  }

  public static void collectFilesTo(
      MutableArray<Path> container,
      Path directory,
      boolean includeDirectoriesToResult,
      String @Nullable ... extensions) {

    if (Files.isDirectory(directory) && includeDirectoriesToResult) {
      container.add(directory);
    }

    if (!Files.exists(directory)) {
      LoggerManager
          .getDefaultLogger()
          .warning(directory, "Directory:[%s] not found"::formatted);
      return;
    }

    try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
      for (Path file : stream) {
        if (Files.isDirectory(file)) {
          collectFilesTo(container, file, includeDirectoriesToResult, extensions);
          continue;
        }

        if (extensions == null || extensions.length < 1 || hasExtensions(file.getFileName(), extensions)) {
          container.add(file);
        }
      }

    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Nullable
  public static String fileName(Path file) {
    Path fileName = file.getFileName();
    return fileName == null ? null : fileName.toString();
  }

  public static boolean hasExtension(Path file, String extension) {
    String fileName = fileName(file);
    return fileName != null && fileName.endsWith(extension);
  }

  public static boolean hasExtensions(Path file, String @Nullable [] extensions) {
    return hasExtensions(file.toString(), extensions);
  }

  public static boolean hasExtensions(Path file, @Nullable Array<String> extensions) {
    return hasExtensions(file.toString(), extensions);
  }

  public static boolean hasExtensions(Path file, @Nullable Collection<String> extensions) {
    return hasExtensions(file.toString(), extensions);
  }

  public static boolean hasExtensions(String path, String @Nullable [] extensions) {
    return ArrayUtils.anyMatchR(extensions, path, String::endsWith);
  }

  public static boolean hasExtensions(String path, @Nullable Array<String> extensions) {
    return extensions != null && extensions
        .iterations()
        .reversedArgs()
        .anyMatch(path, String::endsWith);
  }

  public static boolean hasExtensions(String path, @Nullable Collection<String> extensions) {
    return extensions != null && extensions
        .stream()
        .anyMatch(path::endsWith);
  }

  public static void delete(Path file) {
    try {
      deleteImpl(file);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static void deleteImpl(Path path) throws IOException {
    if (!Files.isDirectory(path)) {
      Files.delete(path);
    } else {
      Files.walkFileTree(path, DELETE_FOLDER_VISITOR);
    }
  }

  public static boolean hasExtension(@Nullable String path) {

    if (StringUtils.isEmpty(path)) {
      return false;
    }

    int index = path.lastIndexOf('.');
    if (index == -1) {
      return false;
    }

    var separatorIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
    return separatorIndex < index;
  }

  /**
   * Get an extension of the path or empty string.
   */
  @Nullable
  public static String getExtension(@Nullable String path) {
    return getExtension(path, false);
  }

  @Nullable
  public static String getExtension(@Nullable String path, boolean toLowerCase) {

    if (StringUtils.isEmpty(path)) {
      return null;
    }

    int index = path.lastIndexOf('.');
    if (index == -1) {
      return null;
    }

    int separatorIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
    if (separatorIndex > index) {
      return null;
    }

    String result = path.substring(index + 1);
    if (toLowerCase) {
      return result.toLowerCase();
    }

    return result;
  }

  @Nullable
  public static String getExtension(Path file) {

    if (Files.isDirectory(file)) {
      return null;
    }

    return getExtension(fileName(file));
  }

  @Nullable
  public static String getExtension(Path file, boolean toLowerCase) {

    if (Files.isDirectory(file)) {
      return null;
    }

    return getExtension(fileName(file), toLowerCase);
  }

  @Nullable
  public static String getNameWithoutExtension(@Nullable String fileName) {

    if (StringUtils.isEmpty(fileName)) {
      return fileName;
    }

    int index = fileName.lastIndexOf('.');
    if (index == -1) {
      return fileName;
    }

    return fileName.substring(0, index);
  }

  @Nullable
  public static String getNameWithoutExtension(Path file) {
    return getNameWithoutExtension(fileName(file));
  }

  @Nullable
  public static String readFromClasspath(String path) {
    return readFromClasspath(FileUtils.class, path);
  }

  /**
   * Read the file as a string from classpath.
   */
  @Nullable
  public static String readFromClasspath(Class<?> cs, String path) {
    InputStream inputStream = cs.getResourceAsStream(path);
    if (inputStream != null) {
      return IoUtils.toString(inputStream);
    }
    return null;
  }

  /**
   * Find a first free file name in the directory.
   */
  public static String getFirstFreeName(Path directory, Path file) {

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
   * @param zipFile the zip file.
   * 
   * @return the count of unpacked files
   */
  public static int unzip(Path destination, Path zipFile) {
    if (!Files.exists(destination)) {
      throw new IllegalArgumentException("The folder " + destination + " doesn't exist.");
    }
    Path normalizedDestination = destination.normalize();
    int count = 0;
    try (var zin = new ZipInputStream(Files.newInputStream(zipFile))) {
      for (var entry = zin.getNextEntry(); entry != null; entry = zin.getNextEntry()) {
        String entryName = entry.getName();
        Path targetFile = destination
            .resolve(entryName)
            .normalize();
        if (!targetFile.startsWith(normalizedDestination)) {
          LOGGER.warning(entryName, "Unexpected entry name:[%s] which is outside"::formatted);
          continue;
        }
        if (entry.isDirectory()) {
          Files.createDirectories(targetFile);
        } else {
          Files.createDirectories(targetFile.getParent());
          Files.copy(zin, targetFile, StandardCopyOption.REPLACE_EXISTING);
          count++;
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return count;
  }

  /**
   * Get a name of the file by the path and the separator.
   *
   * @param path the path.
   * @param separator the separator.
   * @return the name.
   */
  public static String getName(String path, char separator) {

    if (path.length() < 2) {
      return path;
    }

    int index = path.lastIndexOf(separator);
    if (index == -1) {
      return path;
    }

    return path.substring(index + 1);
  }

  /**
   * Get a parent path of the path using the separator.
   *
   * @param path the path.
   * @param separator the separator.
   * @return the parent path.
   */
  public static String getParent(String path, char separator) {

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
   * @param attrs the directory attributes.
   * @see Files#createDirectories(Path, FileAttribute[])
   */
  public static void createDirectories(Path directory, FileAttribute<?>... attrs) {
    Utils.unchecked(directory, attrs, Files::createDirectories);
  }

  /**
   * @param file the file.
   * @param options the link options.
   * @return the last modified time.
   * @see Files#getLastModifiedTime(Path, LinkOption...)
   */
  public static FileTime getLastModifiedTime(Path file, LinkOption... options) {
    return notNull(Utils.uncheckedGet(file, options, Files::getLastModifiedTime));
  }

  /**
   * Get a {@link URI} of the file.
   *
   * @param file the file.
   * @return the {@link URI}.
   */
  public static URI getUri(Path file) {
    return Utils.uncheckedGet(file, Path::toUri);
  }

  /**
   * Get a {@link URI} of the {@link URL}.
   *
   * @param url the url.
   * @return the {@link URI}.
   */
  public static URI getUri(URL url) {
    return Utils.uncheckedGet(url, URL::toURI);
  }

  /**
   * Get a {@link URL} of the file.
   *
   * @param file the file.
   * @return the {@link URL}.
   */
  public static URL getUrl(Path file) {
    return Utils.uncheckedGet(getUri(file), URI::toURL);
  }

  /**
   * @param base the base file path.
   * @param other the other file path.
   * @return the resulting relative path, or an empty path if both paths are equal.
   * @see Path#relativize(Path)
   */
  public static Path relativize(Path base, Path other) {
    return Utils.uncheckedGet(base, other, Path::relativize);
  }

  /**
   * @param base the base file path.
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
  public static WatchService newDefaultWatchService() {
    try {
      return FileSystems
          .getDefault()
          .newWatchService();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * @param start the start folder.
   * @param visitor the visitor.
   * @return the start folder.
   * @see Files#walkFileTree(Path, FileVisitor)
   */
  public static Path walkFileTree(Path start, FileVisitor<? super Path> visitor) {
    try {
      return Files.walkFileTree(start, visitor);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * @param prefix the prefix of a temp file.
   * @param suffix the suffix of a temp file.
   * @param attrs the additional attributes.
   * @return the created temp file.
   * @see Files#createTempFile(String, String, FileAttribute[])
   */
  public static Path createTempFile(
      String prefix,
      String suffix,
      FileAttribute<?>... attrs) {
    try {
      return Files.createTempFile(prefix, suffix, attrs);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static void validateDirectory(Path directory) {
    if (!Files.isDirectory(directory)) {
      throw new IllegalArgumentException("The file " + directory + " isn't a directory.");
    } else if (!Files.exists(directory)) {
      throw new IllegalArgumentException("The file " + directory + " isn't exists.");
    }
  }

  public static Stream<Path> stream(Path directory) {
    validateDirectory(directory);

    var files = MutableArray.ofType(Path.class);

    try (var stream = Files.newDirectoryStream(directory)) {
      stream.forEach(files::add);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return files.stream();
  }
}
