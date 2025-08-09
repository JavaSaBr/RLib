package javasabr.rlib.common.util;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javasabr.rlib.common.function.NotNullSafeBiConsumer;
import javasabr.rlib.common.function.NotNullSafeBiFunction;
import javasabr.rlib.common.function.NotNullSafeConsumer;
import javasabr.rlib.common.function.NotNullSafeFactory;
import javasabr.rlib.common.function.NotNullSafeFunction;
import javasabr.rlib.common.function.NotNullSafeSupplier;
import javasabr.rlib.common.function.NotNullSafeTriFunction;
import javasabr.rlib.common.function.SafeRunnable;
import javasabr.rlib.logger.api.LoggerManager;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
@NullMarked
public final class Utils {

  /**
   * Get a folder of a class.
   *
   * @param cs the class.
   * @return the path to the folder.
   */
  public static Path getRootFolderFromClass(Class<?> cs) {

    var className = cs.getName();
    var builder = new StringBuilder(className.length()).append('/');

    for (int i = 0, length = className.length(); i < length; i++) {

      var ch = className.charAt(i);

      if (ch == '.') {
        ch = '/';
      }

      builder.append(ch);
    }

    builder.append(".class");

    className = builder.toString();

    try {

      var url = Utils.class.getResource(className);

      var path = url.getPath();
      path = path.substring(0, path.length() - className.length());
      path = path.substring(0, path.lastIndexOf('/'));

      var uri = new URI(path);

      path = uri.getPath();
      path = path.replaceAll("%20", " ");

      if (File.separatorChar != '/') {

        var pathBuilder = new StringBuilder();

        for (int i = 0, length = path.length(); i < length; i++) {

          var ch = path.charAt(i);

          if (ch == '/' && i == 0) {
            continue;
          }

          if (ch == '/') {
            ch = File.separatorChar;
          }

          pathBuilder.append(ch);
        }

        path = pathBuilder.toString();
      }

      var file = Paths.get(path);

      while (path.lastIndexOf(File.separatorChar) != -1 && !Files.exists(file)) {
        path = path.substring(0, path.lastIndexOf(File.separatorChar));
        file = Paths.get(uri);
      }

      return file;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get a username of a computer user.
   *
   * @return the username.
   */
  public static String getUserName() {
    return System.getProperty("user.name");
  }

  /**
   * Execute the function with auto-converting a checked exception to an unchecked.
   *
   * @param function the function.
   */
  public static void unchecked(SafeRunnable function) {
    try {
      function.run();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Try to execute a function with some result.
   *
   * @param function the function.
   * @param <R> the result's type.
   * @return the result or null.
   * @since 9.5.0
   */
  public static <R> @Nullable R tryGet(NotNullSafeSupplier<R> function) {
    try {
      return function.get();
    } catch (Exception e) {
      // can be ignored
      return null;
    }
  }

  /**
   * Execute the function with auto-converting a checked exception to an unchecked.
   *
   * @param first the argument.
   * @param function the function.
   * @param <F> the argument's type.
   */
  public static <F> void unchecked(F first, NotNullSafeConsumer<F> function) {
    try {
      function.accept(first);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Execute the function with auto-converting a checked exception to an unchecked.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param consumer the function.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   */
  public static <F, S> void unchecked(
      F first,
      S second,
      NotNullSafeBiConsumer<F, S> consumer) {
    try {
      consumer.accept(first, second);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Execute the function with auto-converting a checked exception to an unchecked.
   *
   * @param function the function.
   * @param <R> the result type.
   * @return the result.
   */
  public static <R> R uncheckedGet(NotNullSafeFactory<R> function) {
    try {
      return function.get();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Execute the function with auto-converting a checked exception to an unchecked.
   *
   * @param argument the argument.
   * @param function the function.
   * @param <F> the argument's type.
   * @param <R> the result's type.
   * @return the result.
   */
  public static <F, R> R uncheckedGet(F argument, NotNullSafeFunction<F, R> function) {
    try {
      return function.apply(argument);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Execute the function with returning default value if got an exception.
   *
   * @param argument the argument.
   * @param function the function.
   * @param def the default value.
   * @param <F> the argument's type.
   * @param <R> the result's type.
   * @return the result.
   */
  public static <F, R> R uncheckedGet(
      F argument,
      NotNullSafeFunction<F, R> function,
      R def) {
    try {
      return function.apply(argument);
    } catch (Exception e) {
      return def;
    }
  }

  /**
   * Execute the function with auto-converting a checked exception to an unchecked.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param function the function.
   * @param <F> the first's type.
   * @param <S> the second's type.
   * @param <R> the result's type.
   * @return the result.
   * @since 9.2.1
   */
  public static <F, S, R> R uncheckedGet(
      F first,
      S second,
      NotNullSafeBiFunction<F, S, R> function) {
    try {
      return function.apply(first, second);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Execute the function with auto-converting a checked exception to an unchecked.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param function the function.
   * @param <F> the first's type.
   * @param <S> the second's type.
   * @param <T> the third's type.
   * @param <R> the result's type.
   * @return the result.
   * @since 9.9.0
   */
  public static <F, S, T, R> R uncheckedGet(
      F first,
      S second,
      T third,
      NotNullSafeTriFunction<F, S, T, R> function) {
    try {
      return function.apply(first, second, third);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Try to execute a function with some result.
   *
   * @param argument the argument.
   * @param function the function.
   * @param <F> the argument's type.
   * @param <R> the result's type.
   * @return the result or null.
   * @since 9.3.0
   */
  public static <F, R> @Nullable R tryGet(F argument, NotNullSafeFunction<F, R> function) {
    try {
      return function.apply(argument);
    } catch (Exception e) {
      // can be ignored
      return null;
    }
  }

  /**
   * Try to execute a function with some result.
   *
   * @param argument the argument.
   * @param function the function.
   * @param def the default value.
   * @param <F> the argument's type.
   * @param <R> the result's type.
   * @return the result or null.
   * @since 9.3.0
   */
  public static <F, R> R tryGet(
      F argument,
      NotNullSafeFunction<F, R> function,
      R def) {
    try {
      return function.apply(argument);
    } catch (Exception e) {
      // can be ignored
      return def;
    }
  }

  /**
   * Try to execute a function with some result and convert this result to another.
   *
   * @param argument the argument.
   * @param function the function.
   * @param resultConverter the result converter.
   * @param <F> the argument's type.
   * @param <R> the function result's type.
   * @param <FR> the converter result's type.
   * @return the final result or null.
   * @since 9.3.0
   */
  public static <F, R, FR> @Nullable FR tryGetAndConvert(
      F argument,
      NotNullSafeFunction<F, R> function,
      NotNullSafeFunction<R, FR> resultConverter) {
    try {
      return resultConverter.apply(function.apply(argument));
    } catch (Exception e) {
      // can be ignored
      return null;
    }
  }

  /**
   * Print an exception to log.
   *
   * @param exception the exception.
   * @since 9.8.0
   */
  public static void print(Exception exception) {
    LoggerManager
        .getDefaultLogger()
        .warning(exception);
  }

  /**
   * Print a message to log.
   *
   * @param message the message.
   * @since 9.8.0
   */
  public static void print(String message) {
    LoggerManager
        .getDefaultLogger()
        .warning(message);
  }

  private Utils() {
    throw new RuntimeException();
  }
}
