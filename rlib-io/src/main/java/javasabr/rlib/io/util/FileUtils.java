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
import lombok.CustomLog;
import org.jspecify.annotations.Nullable;

/**
 * Utility methods for file and path operations including file discovery, extension handling,
 * zip extraction, and path manipulation.
 *
 * @since 10.0.0
 */
@CustomLog
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

  /**
   * Checks if the filename is a valid Windows filename.
   *
   * @param filename the filename to validate
   * @return true if the filename is valid
   * @since 10.0.0
   */
  public static boolean isValidFileName(@Nullable String filename) {

    if (StringUtils.isEmpty(filename)) {
      return false;
    }

    return FILE_NAME_PATTERN
        .matcher(filename)
        .matches();
  }

  /**
   * Normalizes a filename by replacing invalid characters with underscores.
   *
   * @param filename the filename to normalize
   * @return the normalized filename
   * @since 10.0.0
   */
  public static String normalizeName(@Nullable String filename) {

    if (StringUtils.isEmpty(filename)) {
      return "_";
    }

    return NORMALIZE_FILE_NAME_PATTERN
        .matcher(filename)
        .replaceAll("_");
  }

  /**
   * Returns all files in the directory matching the specified extensions.
   *
   * @param directory the directory to search
   * @param extensions the file extensions to match, or null for all files
   * @return an array of matching paths
   * @since 10.0.0
   */
  public static Array<Path> getFiles(Path directory, String @Nullable ... extensions) {
    return getFiles(directory, false, extensions);
  }

  /**
   * Returns all files in the directory matching the specified extensions.
   *
   * @param directory the directory to search
   * @param includeDirectoriesToResult whether to include directories in the result
   * @param extensions the file extensions to match, or null for all files
   * @return an array of matching paths
   * @since 10.0.0
   */
  public static Array<Path> getFiles(
      Path directory,
      boolean includeDirectoriesToResult,
      String @Nullable ... extensions) {
    MutableArray<Path> result = MutableArray.ofType(Path.class);
    collectFilesTo(result, directory, includeDirectoriesToResult, extensions);
    return Array.copyOf(result);
  }

  /**
   * Returns all files from a package matching the specified extensions.
   *
   * @param pckg the package to search
   * @param extensions the file extensions to match, or null for all files
   * @return an array of matching paths
   * @since 10.0.0
   */
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
      Utils.printWarn(exc);
    }

    if (urls == null) {
      return EMPTY_PATHS;
    }

    var files = MutableArray.ofType(Path.class);

    while (urls.hasMoreElements()) {

      var next = urls.nextElement();
      var path = next.getFile();

      if (path.contains("%20")) {
        path = path.replace("%20", " ");
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

  /**
   * Collects files from a directory into the container.
   *
   * @param container the container to collect files into
   * @param directory the directory to search
   * @param includeDirectoriesToResult whether to include directories in the result
   * @param extensions the file extensions to match, or null for all files
   * @since 10.0.0
   */
  public static void collectFilesTo(
      MutableArray<Path> container,
      Path directory,
      boolean includeDirectoriesToResult,
      String @Nullable ... extensions) {

    if (Files.isDirectory(directory) && includeDirectoriesToResult) {
      container.add(directory);
    }

    if (!Files.exists(directory)) {
      Utils.printWarn("Directory:[%s] not found".formatted(directory));
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

  /**
   * Returns the filename of the path as a string.
   *
   * @param file the path
   * @return the filename, or null if the path has no filename
   * @since 10.0.0
   */
  @Nullable
  public static String fileName(Path file) {
    Path fileName = file.getFileName();
    return fileName == null ? null : fileName.toString();
  }

  /**
   * Checks if the file has the specified extension.
   *
   * @param file the file to check
   * @param extension the extension to match
   * @return true if the file has the extension
   * @since 10.0.0
   */
  public static boolean hasExtension(Path file, String extension) {
    String fileName = fileName(file);
    return fileName != null && fileName.endsWith(extension);
  }

  /**
   * Checks if the file has any of the specified extensions.
   *
   * @param file the file to check
   * @param extensions the extensions to match
   * @return true if the file has any of the extensions
   * @since 10.0.0
   */
  public static boolean hasExtensions(Path file, String @Nullable [] extensions) {
    return hasExtensions(file.toString(), extensions);
  }

  /**
   * Checks if the file has any of the specified extensions.
   *
   * @param file the file to check
   * @param extensions the extensions to match
   * @return true if the file has any of the extensions
   * @since 10.0.0
   */
  public static boolean hasExtensions(Path file, @Nullable Array<String> extensions) {
    return hasExtensions(file.toString(), extensions);
  }

  /**
   * Checks if the file has any of the specified extensions.
   *
   * @param file the file to check
   * @param extensions the extensions to match
   * @return true if the file has any of the extensions
   * @since 10.0.0
   */
  public static boolean hasExtensions(Path file, @Nullable Collection<String> extensions) {
    return hasExtensions(file.toString(), extensions);
  }

  /**
   * Checks if the path string ends with any of the specified extensions.
   *
   * @param path the path string to check
   * @param extensions the extensions to match
   * @return true if the path ends with any of the extensions
   * @since 10.0.0
   */
  public static boolean hasExtensions(String path, String @Nullable [] extensions) {
    return ArrayUtils.anyMatch(extensions, path, (extension, arg) -> arg.endsWith(extension));
  }

  /**
   * Checks if the path string ends with any of the specified extensions.
   *
   * @param path the path string to check
   * @param extensions the extensions to match
   * @return true if the path ends with any of the extensions
   * @since 10.0.0
   */
  public static boolean hasExtensions(String path, @Nullable Array<String> extensions) {
    return extensions != null && extensions
        .iterations()
        .reversedArgs()
        .anyMatch(path, String::endsWith);
  }

  /**
   * Checks if the path string ends with any of the specified extensions.
   *
   * @param path the path string to check
   * @param extensions the extensions to match
   * @return true if the path ends with any of the extensions
   * @since 10.0.0
   */
  public static boolean hasExtensions(String path, @Nullable Collection<String> extensions) {
    return extensions != null && extensions
        .stream()
        .anyMatch(path::endsWith);
  }

  /**
   * Deletes a file or directory recursively.
   *
   * @param file the file or directory to delete
   * @throws UncheckedIOException if an I/O error occurs
   * @since 10.0.0
   */
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

  /**
   * Checks if the path has a file extension.
   *
   * @param path the path to check
   * @return true if the path has an extension
   * @since 10.0.0
   */
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
   * Returns the extension of the path, or null if none.
   *
   * @param path the path
   * @return the extension without the dot, or null
   * @since 10.0.0
   */
  @Nullable
  public static String getExtension(@Nullable String path) {
    return getExtension(path, false);
  }

  /**
   * Returns the extension of the path, optionally in lowercase.
   *
   * @param path the path
   * @param toLowerCase whether to convert to lowercase
   * @return the extension without the dot, or null
   * @since 10.0.0
   */
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

  /**
   * Returns the extension of the file.
   *
   * @param file the file
   * @return the extension without the dot, or null if directory or no extension
   * @since 10.0.0
   */
  @Nullable
  public static String getExtension(Path file) {

    if (Files.isDirectory(file)) {
      return null;
    }

    return getExtension(fileName(file));
  }

  /**
   * Returns the extension of the file, optionally in lowercase.
   *
   * @param file the file
   * @param toLowerCase whether to convert to lowercase
   * @return the extension without the dot, or null if directory or no extension
   * @since 10.0.0
   */
  @Nullable
  public static String getExtension(Path file, boolean toLowerCase) {

    if (Files.isDirectory(file)) {
      return null;
    }

    return getExtension(fileName(file), toLowerCase);
  }

  /**
   * Returns the filename without the extension.
   *
   * @param fileName the filename
   * @return the filename without extension, or the original if no extension
   * @since 10.0.0
   */
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

  /**
   * Returns the filename without the extension.
   *
   * @param file the file
   * @return the filename without extension, or null if no filename
   * @since 10.0.0
   */
  @Nullable
  public static String getNameWithoutExtension(Path file) {
    return getNameWithoutExtension(fileName(file));
  }

  /**
   * Reads a file from the classpath as a string.
   *
   * @param path the classpath resource path
   * @return the file content as a string, or null if not found
   * @since 10.0.0
   */
  @Nullable
  public static String readFromClasspath(String path) {
    return readFromClasspath(FileUtils.class, path);
  }

  /**
   * Reads a file from the classpath as a string.
   *
   * @param cs the class to use for loading the resource
   * @param path the classpath resource path
   * @return the file content as a string, or null if not found
   * @since 10.0.0
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
   * Finds the first available filename in the directory, appending a counter if needed.
   *
   * @param directory the directory to check
   * @param file the desired file
   * @return the first available filename
   * @since 10.0.0
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
   * Extracts a zip file to the destination folder.
   *
   * @param destination the destination folder
   * @param zipFile the zip file to extract
   * @return the count of unpacked files
   * @throws IllegalArgumentException if the destination folder doesn't exist
   * @throws UncheckedIOException if an I/O error occurs
   * @since 10.0.0
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
          LOGGER.warn(entryName, "Unexpected entry name:[%s] which is outside"::formatted);
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
   * Returns the filename from the path using the specified separator.
   *
   * @param path the path
   * @param separator the path separator character
   * @return the filename
   * @since 10.0.0
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
   * Returns the parent path using the specified separator.
   *
   * @param path the path
   * @param separator the path separator character
   * @return the parent path
   * @since 10.0.0
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
   * Creates directories, wrapping checked exceptions.
   *
   * @param directory the directory to create
   * @param attrs the directory attributes
   * @see Files#createDirectories(Path, FileAttribute[])
   * @since 10.0.0
   */
  public static void createDirectories(Path directory, FileAttribute<?>... attrs) {
    Utils.unchecked(directory, attrs, Files::createDirectories);
  }

  /**
   * Returns the last modified time of the file.
   *
   * @param file the file
   * @param options the link options
   * @return the last modified time
   * @see Files#getLastModifiedTime(Path, LinkOption...)
   * @since 10.0.0
   */
  public static FileTime getLastModifiedTime(Path file, LinkOption... options) {
    return notNull(Utils.uncheckedGet(file, options, Files::getLastModifiedTime));
  }

  /**
   * Returns the URI of the file.
   *
   * @param file the file
   * @return the URI
   * @since 10.0.0
   */
  public static URI getUri(Path file) {
    return Utils.uncheckedGet(file, Path::toUri);
  }

  /**
   * Returns the URI of the URL.
   *
   * @param url the URL
   * @return the URI
   * @since 10.0.0
   */
  public static URI getUri(URL url) {
    return Utils.uncheckedGet(url, URL::toURI);
  }

  /**
   * Returns the URL of the file.
   *
   * @param file the file
   * @return the URL
   * @since 10.0.0
   */
  public static URL getUrl(Path file) {
    return Utils.uncheckedGet(getUri(file), URI::toURL);
  }

  /**
   * Returns the relative path from base to other.
   *
   * @param base the base file path
   * @param other the other file path
   * @return the resulting relative path, or an empty path if both paths are equal
   * @see Path#relativize(Path)
   * @since 10.0.0
   */
  public static Path relativize(Path base, Path other) {
    return Utils.uncheckedGet(base, other, Path::relativize);
  }

  /**
   * Returns the relative path from base to other, or null if either is null.
   *
   * @param base the base file path
   * @param other the other file path
   * @return the resulting relative path, or null if either path is null
   * @see Path#relativize(Path)
   * @since 10.0.0
   */
  public static @Nullable Path safeRelativize(@Nullable Path base, @Nullable Path other) {
    if (base == null || other == null) {
      return null;
    } else {
      return Utils.uncheckedGet(base, other, Path::relativize);
    }
  }

  /**
   * Creates a new default watch service.
   *
   * @return the new default watch service
   * @throws UncheckedIOException if an I/O error occurs
   * @see FileSystems#getDefault()
   * @since 10.0.0
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
   * Walks a file tree starting from the specified path.
   *
   * @param start the start folder
   * @param visitor the visitor
   * @return the start folder
   * @throws UncheckedIOException if an I/O error occurs
   * @see Files#walkFileTree(Path, FileVisitor)
   * @since 10.0.0
   */
  public static Path walkFileTree(Path start, FileVisitor<? super Path> visitor) {
    try {
      return Files.walkFileTree(start, visitor);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Creates a temporary file.
   *
   * @param prefix the prefix of the temp file
   * @param suffix the suffix of the temp file
   * @param attrs the additional attributes
   * @return the created temp file
   * @throws UncheckedIOException if an I/O error occurs
   * @see Files#createTempFile(String, String, FileAttribute[])
   * @since 10.0.0
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

  /**
   * Returns a stream of files in the directory.
   *
   * @param directory the directory
   * @return a stream of paths in the directory
   * @throws IllegalArgumentException if the path is not a directory or doesn't exist
   * @throws UncheckedIOException if an I/O error occurs
   * @since 10.0.0
   */
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
