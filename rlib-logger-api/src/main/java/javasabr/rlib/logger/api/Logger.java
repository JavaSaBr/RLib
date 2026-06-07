package javasabr.rlib.logger.api;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;

/**
 * Logger interface for outputting messages at different log levels.
 *
 * @since 10.0.0
 */
@NullUnmarked
public interface Logger {

  /**
   * Factory for creating log messages with no arguments.
   *
   * @since 10.0.0
   */
  @FunctionalInterface
  interface Factory {

    @NonNull
    String make();
  }

  /**
   * Factory for creating log messages with one argument.
   *
   * @param <A> the type of the argument
   * @since 10.0.0
   */
  @FunctionalInterface
  interface N1Factory<A> {

    @NonNull
    String make(A arg1);
  }

  /**
   * Factory for creating log messages with one int argument.
   *
   * @since 10.0.0
   */
  @FunctionalInterface
  interface IntFactory {

    @NonNull
    String make(int arg1);
  }

  /**
   * Factory for creating log messages with an int and an object argument.
   *
   * @param <B> the type of the object argument
   * @since 10.0.0
   */
  @FunctionalInterface
  interface IntN1Factory<B> {

    @NonNull
    String make(int arg1, B arg2);
  }

  /**
   * Factory for creating log messages with two arguments.
   *
   * @param <A> the type of the first argument
   * @param <B> the type of the second argument
   * @since 10.0.0
   */
  @FunctionalInterface
  interface N2Factory<A, B> {

    @NonNull
    String make(A arg1, B arg2);
  }

  /**
   * Factory for creating log messages with an object and an int argument.
   *
   * @param <A> the type of the object argument
   * @since 10.0.0
   */
  @FunctionalInterface
  interface N1IntFactory<A> {

    @NonNull
    String make(A arg1, int arg2);
  }

  /**
   * Factory for creating log messages with an object and two int arguments.
   *
   * @param <A> the type of the object argument
   * @since 10.0.0
   */
  @FunctionalInterface
  interface N1Int2Factory<A> {

    @NonNull
    String make(A arg1, int arg2, int arg3);
  }

  /**
   * Factory for creating log messages with an object, an int, and another object argument.
   *
   * @param <A> the type of the first object argument
   * @param <C> the type of the third object argument
   * @since 10.0.0
   */
  @FunctionalInterface
  interface N1IntN1Factory<A, C> {

    @NonNull
    String make(A arg1, int arg2, C arg3);
  }

  /**
   * Factory for creating log messages with two objects and one int argument.
   *
   * @param <A> the type of the first object argument
   * @param <B> the type of the second object argument
   * @since 10.0.0
   */
  @FunctionalInterface
  interface N2IntFactory<A, B> {

    @NonNull
    String make(A arg1, B arg2, int arg3);
  }

  /**
   * Factory for creating log messages with two int arguments.
   *
   * @since 10.0.0
   */
  @FunctionalInterface
  interface Int2Factory {

    @NonNull
    String make(int arg1, int arg2);
  }

  /**
   * Factory for creating log messages with three arguments.
   *
   * @param <A> the type of the first argument
   * @param <B> the type of the second argument
   * @param <C> the type of the third argument
   * @since 10.0.0
   */
  @FunctionalInterface
  interface N3Factory<A, B, C> {

    @NonNull
    String make(A arg1, B arg2, C arg3);
  }

  /**
   * Factory for creating log messages with two int and one object argument.
   *
   * @param <C> the type of the object argument
   * @since 10.0.0
   */
  @FunctionalInterface
  interface Int2N1Factory<C> {

    @NonNull
    String make(int arg1, int arg2, C arg3);
  }

  /**
   * Factory for creating log messages with an object, two ints, and another object argument.
   *
   * @param <A> the type of the first object argument
   * @param <D> the type of the second object argument
   * @since 10.0.0
   */
  @FunctionalInterface
  interface N1Int2N1Factory<A, D> {

    @NonNull
    String make(A arg1, int arg2, int arg3, D arg4);
  }

  /**
   * Factory for creating log messages with four arguments.
   *
   * @param <A> the type of the first argument
   * @param <B> the type of the second argument
   * @param <C> the type of the third argument
   * @param <D> the type of the fourth argument
   * @since 10.0.0
   */
  @FunctionalInterface
  interface N4Factory<A, B, C, D> {

    @NonNull
    String make(A arg1, B arg2, C arg3, D arg4);
  }
  
  /**
   * Returns the name of this logger.
   *
   * @return the logger name
   * @since 10.0.0
   */
  @NonNull 
  String name();

  /**
   * Returns the short name of this logger.
   *
   * @return the short logger name
   * @since 10.0.0
   */
  @NonNull
  String shortName();
  
  /**
   * Prints a trace message.
   *
   * @param message the message to print
   * @since 10.0.0
   */
  default void trace(@NonNull String message) {
    print(LoggerLevel.TRACE, message);
  }

  /**
   * Prints a trace message with an exception.
   *
   * @param message   the message to print
   * @param exception the exception to print
   * @since 10.0.0
   */
  default void trace(@NonNull String message, @NonNull Throwable exception) {
    print(LoggerLevel.TRACE, message, exception);
  }
  
  /**
   * Prints a debug message.
   *
   * @param message the message to print
   * @since 10.0.0
   */
  default void debug(@NonNull String message) {
    print(LoggerLevel.DEBUG, message);
  }

  /**
   * Prints a debug message with an exception.
   *
   * @param message   the message to print
   * @param exception the exception to print
   * @since 10.0.0
   */
  default void debug(@NonNull String message, @NonNull Throwable exception) {
    print(LoggerLevel.DEBUG, message, exception);
  }

  default void debug(int arg1, @NonNull IntFactory factory) {
    print(LoggerLevel.DEBUG, arg1, factory);
  }

  default <A> void debug(A arg1, @NonNull N1Factory<A> factory) {
    print(LoggerLevel.DEBUG, arg1, factory);
  }

  default <B> void debug(int arg1, B arg2, @NonNull IntN1Factory<B> factory) {
    print(LoggerLevel.DEBUG, arg1, arg2, factory);
  }

  default <A, B> void debug(A arg1, B arg2, @NonNull N2Factory<A, B> factory) {
    print(LoggerLevel.DEBUG, arg1, arg2, factory);
  }

  default <A> void debug(A arg1, int arg2, @NonNull N1IntFactory<A> factory) {
    print(LoggerLevel.DEBUG, arg1, arg2, factory);
  }

  default void debug(int arg1, int arg2, @NonNull Int2Factory factory) {
    print(LoggerLevel.DEBUG, arg1, arg2, factory);
  }

  default <A, B, C> void debug(A arg1, B arg2, C arg3, @NonNull N3Factory<A, B, C> factory) {
    print(LoggerLevel.DEBUG, arg1, arg2, arg3, factory);
  }

  /**
   * Prints a debug message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default <A, B> void debug(A arg1, B arg2, int arg3, @NonNull N2IntFactory<A, B> factory) {
    print(LoggerLevel.DEBUG, arg1, arg2, arg3, factory);
  }

  default <A, B, C, D> void debug(A arg1, B arg2, C arg3, D arg4, @NonNull N4Factory<A, B, C, D> factory) {
    print(LoggerLevel.DEBUG, arg1, arg2, arg3, arg4, factory);
  }

  default <A, C> void debug(A arg1, int arg2, C arg3, @NonNull N1IntN1Factory<A, C> factory) {
    print(LoggerLevel.DEBUG, arg1, arg2, arg3, factory);
  }

  default <A> void debug(A arg1, int arg2, int arg3, @NonNull N1Int2Factory<A> factory) {
    print(LoggerLevel.DEBUG, arg1, arg2, arg3, factory);
  }

  default void error(@NonNull String message) {
    print(LoggerLevel.ERROR, message);
  }
  
  default void error(@NonNull Throwable exception) {
    print(LoggerLevel.ERROR, exception);
  }

  /**
   * Prints an error message with an exception.
   *
   * @param message   the message to print
   * @param exception the exception to print
   * @since 10.0.0
   */
  default void error(@NonNull String message, @NonNull Throwable exception) {
    print(LoggerLevel.ERROR, message, exception);
  }

  default <A> void error(A arg1, @NonNull N1Factory<A> factory) {
    print(LoggerLevel.ERROR, arg1, factory);
  }

  default void error(int arg1, @NonNull IntFactory factory) {
    print(LoggerLevel.ERROR, arg1, factory);
  }

  default <B> void error(int arg1, B arg2, @NonNull IntN1Factory<B> factory) {
    print(LoggerLevel.ERROR, arg1, arg2, factory);
  }

  default <A, B> void error(A arg1, B arg2, @NonNull N2Factory<A, B> factory) {
    print(LoggerLevel.ERROR, arg1, arg2, factory);
  }

  default void error(int arg1, int arg2, @NonNull Int2Factory factory) {
    print(LoggerLevel.ERROR, arg1, arg2, factory);
  }

  default <A> void error(A arg1, int arg2, @NonNull N1IntFactory<A> factory) {
    print(LoggerLevel.ERROR, arg1, arg2, factory);
  }

  default <A, B, C> void error(A arg1, B arg2, C arg3, @NonNull N3Factory<A, B, C> factory) {
    print(LoggerLevel.ERROR, arg1, arg2, arg3, factory);
  }

  default <A, C> void error(A arg1, int arg2, C arg3, @NonNull N1IntN1Factory<A, C> factory) {
    print(LoggerLevel.ERROR, arg1, arg2, arg3, factory);
  }

  default <A> void error(A arg1, int arg2, int arg3, @NonNull N1Int2Factory<A> factory) {
    print(LoggerLevel.ERROR, arg1, arg2, arg3, factory);
  }

  default <C> void error(int arg1, int arg2, C arg3, @NonNull Int2N1Factory<C> factory) {
    print(LoggerLevel.ERROR, arg1, arg2, arg3, factory);
  }

  default <A, D> void error(A arg1, int arg2, int arg3, D arg4, @NonNull N1Int2N1Factory<A, D> factory) {
    print(LoggerLevel.ERROR, arg1, arg2, arg3, arg4, factory);
  }

  default <A, B, C, D> void error(A arg1, B arg2, C arg3, D arg4, @NonNull N4Factory<A, B, C, D> factory) {
    print(LoggerLevel.ERROR, arg1, arg2, arg3, arg4, factory);
  }

  default void info(@NonNull String message) {
    print(LoggerLevel.INFO, message);
  }

  /**
   * Prints an info message with an exception.
   *
   * @param message   the message to print
   * @param exception the exception to print
   * @since 10.0.0
   */
  default void info(@NonNull String message, @NonNull Throwable exception) {
    print(LoggerLevel.INFO, message, exception);
  }

  default void info(int arg1, @NonNull IntFactory factory) {
    print(LoggerLevel.INFO, arg1, factory);
  }

  default <A> void info(A arg1, @NonNull N1Factory<A> factory) {
    print(LoggerLevel.INFO, arg1, factory);
  }

  default <A, B> void info(A arg1, B arg2, @NonNull N2Factory<A, B> factory) {
    print(LoggerLevel.INFO, arg1, arg2, factory);
  }

  default void info(int arg1, int arg2, @NonNull Int2Factory factory) {
    print(LoggerLevel.INFO, arg1, arg2, factory);
  }

  default <A> void info(A arg1, int arg2, @NonNull N1IntFactory<A> factory) {
    print(LoggerLevel.INFO, arg1, arg2, factory);
  }

  default <B> void info(int arg1, B arg2, @NonNull IntN1Factory<B> factory) {
    print(LoggerLevel.INFO, arg1, arg2, factory);
  }

  default <A, B, C> void info(A arg1, B arg2, C arg3, @NonNull N3Factory<A, B, C> factory) {
    print(LoggerLevel.INFO, arg1, arg2, arg3, factory);
  }

  default <A, C> void info(A arg1, int arg2, C arg3, @NonNull N1IntN1Factory<A, C> factory) {
    print(LoggerLevel.INFO, arg1, arg2, arg3, factory);
  }

  default <A> void info(A arg1, int arg2, int arg3, @NonNull N1Int2Factory<A> factory) {
    print(LoggerLevel.INFO, arg1, arg2, arg3, factory);
  }

  default <A, B, C, D> void info(A arg1, B arg2, C arg3, D arg4, @NonNull N4Factory<A, B, C, D> factory) {
    print(LoggerLevel.INFO, arg1, arg2, arg3, arg4, factory);
  }

  /**
   * Check of enabling the logger level.
   */
  default boolean enabled(@NonNull LoggerLevel level) {
    return level.enabled();
  }

  /**
   * Check of enabling the error level.
   */
  default boolean errorEnabled() {
    return enabled(LoggerLevel.ERROR);
  }
  
  /**
   * Check of enabling the warning level.
   */
  default boolean warnEnabled() {
    return enabled(LoggerLevel.WARNING);
  }

  /**
   * Check of enabling the info level.
   */
  default boolean infoEnabled() {
    return enabled(LoggerLevel.INFO);
  }

  /**
   * Check of enabling the debug level.
   */
  default boolean debugEnabled() {
    return enabled(LoggerLevel.DEBUG);
  }

  /**
   * Check of enabling the trace level.
   */
  default boolean traceEnabled() {
    return enabled(LoggerLevel.TRACE);
  }

  /**
   * Override the enabling status of the logger level.
   */
  default void overrideEnabled(@NonNull LoggerLevel level, boolean enabled) {}

  /**
   * Remove overriding of enabling status if the logger level.
   */
  default void resetToDefault(@NonNull LoggerLevel level) {}

  /**
   * Prints a warning message.
   *
   * @param message the message to print
   * @since 10.0.0
   */
  default void warn(@NonNull String message) {
    print(LoggerLevel.WARNING, message);
  }

  /**
   * Prints a warning message with an exception.
   *
   * @param message   the message to print
   * @param exception the exception to print
   * @since 10.0.0
   */
  default void warn(@NonNull String message, @NonNull Throwable exception) {
    print(LoggerLevel.WARNING, message, exception);
  }

  @Deprecated(forRemoval = true)
  default void warning(@NonNull String message) {
    print(LoggerLevel.WARNING, message);
  }

  /**
   * Prints a warning exception.
   *
   * @param exception the exception to print
   * @since 10.0.0
   */
  default void warn(@NonNull Throwable exception) {
    print(LoggerLevel.WARNING, exception);
  }

  @Deprecated(forRemoval = true)
  default void warning(@NonNull Throwable exception) {
    print(LoggerLevel.WARNING, exception);
  }

  /**
   * Prints a warning message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default <A> void warn(A arg1, @NonNull N1Factory<A> factory) {
    print(LoggerLevel.WARNING, arg1, factory);
  }

  @Deprecated(forRemoval = true)
  default <A> void warning(A arg1, @NonNull N1Factory<A> factory) {
    print(LoggerLevel.WARNING, arg1, factory);
  }

  /**
   * Prints a warning message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default void warn(int arg1, @NonNull IntFactory factory) {
    print(LoggerLevel.WARNING, arg1, factory);
  }

  @Deprecated(forRemoval = true)
  default void warning(int arg1, @NonNull IntFactory factory) {
    print(LoggerLevel.WARNING, arg1, factory);
  }

  /**
   * Prints a warning message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default <A, B> void warn(A arg1, B arg2, @NonNull N2Factory<A, B> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, factory);
  }

  @Deprecated(forRemoval = true)
  default <A, B> void warning(A arg1, B arg2, @NonNull N2Factory<A, B> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, factory);
  }

  /**
   * Prints a warning message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default void warn(int arg1, int arg2, @NonNull Int2Factory factory) {
    print(LoggerLevel.WARNING, arg1, arg2, factory);
  }

  @Deprecated(forRemoval = true)
  default void warning(int arg1, int arg2, @NonNull Int2Factory factory) {
    print(LoggerLevel.WARNING, arg1, arg2, factory);
  }

  /**
   * Prints a warning message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default <B> void warn(int arg1, B arg2, @NonNull IntN1Factory<B> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, factory);
  }

  @Deprecated(forRemoval = true)
  default <B> void warning(int arg1, B arg2, @NonNull IntN1Factory<B> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, factory);
  }

  /**
   * Prints a warning message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default <A> void warn(A arg1, int arg2, @NonNull N1IntFactory<A> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, factory);
  }

  @Deprecated(forRemoval = true)
  default <A> void warning(A arg1, int arg2, @NonNull N1IntFactory<A> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, factory);
  }

  /**
   * Prints a warning message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default <A, B, C> void warn(A arg1, B arg2, C arg3, @NonNull N3Factory<A, B, C> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, arg3, factory);
  }

  @Deprecated(forRemoval = true)
  default <A, B, C> void warning(A arg1, B arg2, C arg3, @NonNull N3Factory<A, B, C> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, arg3, factory);
  }

  /**
   * Prints a warning message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default <A, B> void warn(A arg1, B arg2, int arg3, @NonNull N2IntFactory<A, B> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, arg3, factory);
  }

  /**
   * Prints a warning message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default <A, C> void warn(A arg1, int arg2, C arg3, @NonNull N1IntN1Factory<A, C> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, arg3, factory);
  }

  @Deprecated(forRemoval = true)
  default <A, C> void warning(A arg1, int arg2, C arg3, @NonNull N1IntN1Factory<A, C> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, arg3, factory);
  }

  /**
   * Prints a warning message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default <A> void warn(A arg1, int arg2, int arg3, @NonNull N1Int2Factory<A> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, arg3, factory);
  }

  @Deprecated(forRemoval = true)
  default <A> void warning(A arg1, int arg2, int arg3, @NonNull N1Int2Factory<A> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, arg3, factory);
  }

  /**
   * Prints a warning message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default <A, B, C, D> void warn(
      A arg1,
      B arg2,
      C arg3,
      D arg4,
      @NonNull N4Factory<A, B, C, D> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, arg3, arg4, factory);
  }

  @Deprecated(forRemoval = true)
  default <A, B, C, D> void warning(
      A arg1,
      B arg2,
      C arg3,
      D arg4,
      @NonNull N4Factory<A, B, C, D> factory) {
    print(LoggerLevel.WARNING, arg1, arg2, arg3, arg4, factory);
  }

  void print(@NonNull LoggerLevel level, @NonNull String message);

  void print(@NonNull LoggerLevel level, @NonNull Throwable exception);

  /**
   * Prints a message with an exception at the specified level.
   *
   * @param level     the logger level
   * @param message   the message to print
   * @param exception the exception to print
   * @since 10.0.0
   */
  void print(@NonNull LoggerLevel level, @NonNull String message, @NonNull Throwable exception);
  
  default <A> void print(@NonNull LoggerLevel level, A arg1, @NonNull N1Factory<A> factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1));
    }
  }

  default void print(@NonNull LoggerLevel level, int arg1, @NonNull IntFactory factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1));
    }
  }

  default <B> void print(
      @NonNull LoggerLevel level,
      int arg1,
      B arg2,
      @NonNull IntN1Factory<B> factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1, arg2));
    }
  }

  default <A, B> void print(
      @NonNull LoggerLevel level,
      A arg1,
      B arg2,
      @NonNull N2Factory<A, B> factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1, arg2));
    }
  }

  default <A> void print(
      @NonNull LoggerLevel level,
      A arg1,
      int arg2,
      @NonNull N1IntFactory<A> factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1, arg2));
    }
  }

  default void print(
      @NonNull LoggerLevel level,
      int arg1,
      int arg2,
      @NonNull Int2Factory factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1, arg2));
    }
  }

  default <A, B, C> void print(
      @NonNull LoggerLevel level,
      A arg1,
      B arg2,
      C arg3,
      @NonNull N3Factory<A, B, C> factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1, arg2, arg3));
    }
  }

  /**
   * Prints a level-specific message produced by the provided factory.
   *
   * @since 10.0.0
   */
  default <A, B> void print(
      @NonNull LoggerLevel level,
      A arg1,
      B arg2,
      int arg3,
      @NonNull N2IntFactory<A, B> factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1, arg2, arg3));
    }
  }

  default <A, C> void print(
      @NonNull LoggerLevel level,
      A arg1,
      int arg2,
      C arg3,
      @NonNull N1IntN1Factory<A, C> factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1, arg2, arg3));
    }
  }

  default <A> void print(
      @NonNull LoggerLevel level,
      A arg1,
      int arg2,
      int arg3,
      @NonNull N1Int2Factory<A> factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1, arg2, arg3));
    }
  }

  default <C>  void print(
      @NonNull LoggerLevel level,
      int arg1,
      int arg2,
      C arg3,
      @NonNull Int2N1Factory<C> factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1, arg2, arg3));
    }
  }

  default <A, D> void print(
      @NonNull LoggerLevel level,
      A arg1,
      int arg2,
      int arg3,
      D arg4,
      @NonNull N1Int2N1Factory<A, D> factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1, arg2, arg3, arg4));
    }
  }

  default <A, B, C, D> void print(
      @NonNull LoggerLevel level,
      A arg1,
      B arg2,
      C arg3,
      D arg4,
      @NonNull N4Factory<A, B, C, D> factory) {
    if (enabled(level)) {
      print(level, factory.make(arg1, arg2, arg3, arg4));
    }
  }
}
