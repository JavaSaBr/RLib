package javasabr.rlib.logger.api;

import org.jspecify.annotations.Nullable;

/**
 * The interface to implement a logger.
 *
 * @author JavaSaBr
 */
public interface Logger {

  @FunctionalInterface
  interface Factory {

    String make();
  }

  @FunctionalInterface
  interface SinFactory<F> {

    String make(F first);
  }

  @FunctionalInterface
  interface IntSinFactory {

    String make(int val);
  }

  @FunctionalInterface
  interface BiFactory<F, S> {

    String make(F first, S second);
  }

  @FunctionalInterface
  interface NullableBiFactory<F, S> {

    String make(@Nullable F first, @Nullable S second);
  }

  @FunctionalInterface
  interface ObjIntFactory<F> {

    String make(F first, int second);
  }

  @FunctionalInterface
  interface IntBiFactory {

    String make(int first, int second);
  }

  @FunctionalInterface
  interface TriFactory<F, S, T> {

    String make(F first, S second, T third);
  }

  /**
   * Print the debug message.
   *
   * @param message the message.
   */
  default void debug(String message) {
    print(LoggerLevel.DEBUG, message);
  }

  /**
   * Print a build debug message.
   *
   * @param arg the arg for the message factory.
   * @param messageFactory the message factory.
   */
  default void debug(int arg, Logger.IntSinFactory messageFactory) {
    print(LoggerLevel.DEBUG, arg, messageFactory);
  }

  /**
   * Print a build debug message.
   *
   * @param arg the arg for the message factory.
   * @param messageFactory the message factory.
   * @param <T> the argument's type.
   */
  default <T> void debug(T arg, Logger.SinFactory<T> messageFactory) {
    print(LoggerLevel.DEBUG, arg, messageFactory);
  }

  /**
   * Print a build debug message.
   *
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param messageFactory the message factory.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   */
  default <F, S> void debug(F first, S second, Logger.BiFactory<F, S> messageFactory) {
    print(LoggerLevel.DEBUG, first, second, messageFactory);
  }

  /**
   * Print a build debug message.
   *
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param messageFactory the message factory.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   */
  default <F, S> void debugNullable(
      @Nullable F first,
      @Nullable S second,
      Logger.NullableBiFactory<F, S> messageFactory) {
    print(LoggerLevel.DEBUG, first, second, messageFactory);
  }

  /**
   * Print a build debug message.
   *
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param messageFactory the message factory.
   * @param <F> the first argument's type.
   */
  default <F> void debug(F first, int second, Logger.ObjIntFactory<F> messageFactory) {
    print(LoggerLevel.DEBUG, first, second, messageFactory);
  }

  /**
   * Print a build debug message.
   *
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param messageFactory the message factory.
   */
  default void debug(int first, int second, Logger.IntBiFactory messageFactory) {
    print(LoggerLevel.DEBUG, first, second, messageFactory);
  }

  /**
   * Print a build debug message.
   *
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param third the third arg for the message factory.
   * @param messageFactory the message factory.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   * @param <T> the third argument's type.
   */
  default <F, S, T> void debug(
      F first,
      S second,
      T third,
      Logger.TriFactory<F, S, T> messageFactory) {
    print(LoggerLevel.DEBUG, first, second, third, messageFactory);
  }

  /**
   * Print a build error message.
   *
   * @param message the message.
   */
  default void error(String message) {
    print(LoggerLevel.ERROR, message);
  }

  /**
   * Print a build error message.
   *
   * @param exception the exception.
   */
  default void error(Throwable exception) {
    print(LoggerLevel.ERROR, exception);
  }

  /**
   * Print a build information message.
   *
   * @param message the message.
   */
  default void info(String message) {
    print(LoggerLevel.INFO, message);
  }

  /**
   * Print a build information message.
   *
   * @param arg the arg for the message factory.
   * @param messageFactory the message factory.
   * @param <T> the argument's type.
   */
  default <T> void info(T arg, Logger.SinFactory<T> messageFactory) {
    print(LoggerLevel.INFO, arg, messageFactory);
  }

  /**
   * Print a build information message.
   *
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param messageFactory the message factory.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   */
  default <F, S> void info(F first, S second, Logger.BiFactory<F, S> messageFactory) {
    print(LoggerLevel.INFO, first, second, messageFactory);
  }

  /**
   * Print a build information message.
   *
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param third the third arg for the message factory.
   * @param messageFactory the message factory.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   * @param <T> the third argument's type.
   */
  default <F, S, T> void info(
      F first,
      S second,
      T third,
      Logger.TriFactory<F, S, T> messageFactory) {
    print(LoggerLevel.INFO, first, second, third, messageFactory);
  }

  /**
   * Check of enabling the logger level.
   *
   * @param level the logger level.
   * @return true if the level is enabled.
   */
  default boolean isEnabled(LoggerLevel level) {
    return level.isEnabled();
  }

  /**
   * Override the enabling status of the logger level.
   *
   * @param level the logger level.
   * @param enabled true if need to be enabled.
   * @return true if the status was changed.
   */
  default boolean setEnabled(LoggerLevel level, boolean enabled) {
    return false;
  }

  /**
   * Remove overriding of enabling status if the logger level.
   *
   * @param level the logger level.
   * @return true if the status was changed.
   */
  default boolean applyDefault(LoggerLevel level) {
    return false;
  }

  /**
   * Print the warning message.
   *
   * @param message the message.
   */
  default void warning(String message) {
    print(LoggerLevel.WARNING, message);
  }

  /**
   * Print the warning message.
   *
   * @param exception the exception.
   */
  default void warning(Throwable exception) {
    print(LoggerLevel.WARNING, exception);
  }

  /**
   * Print a warning debug message.
   *
   * @param arg the arg for the message factory.
   * @param messageFactory the message factory.
   * @param <A> the argument's type.
   */
  default <A> void warning(A arg, Logger.SinFactory<A> messageFactory) {
    print(LoggerLevel.WARNING, arg, messageFactory);
  }

  /**
   * Print a warning debug message.
   *
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param messageFactory the message factory.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   */
  default <F, S> void warning(F first, S second, Logger.BiFactory<F, S> messageFactory) {
    print(LoggerLevel.WARNING, first, second, messageFactory);
  }

  /**
   * Print the message.
   *
   * @param level the level of the message.
   * @param message the message.
   */
  void print(LoggerLevel level, String message);

  /**
   * Print the message.
   *
   * @param level the level of the message.
   * @param exception the exception.
   */
  void print(LoggerLevel level, Throwable exception);

  /**
   * Print a build message.
   *
   * @param level the level of the message.
   * @param arg the arg for the message factory.
   * @param messageFactory the message factory.
   * @param <T> the argument's type.
   */
  default <T> void print(LoggerLevel level, T arg, Logger.SinFactory<T> messageFactory) {
    if (isEnabled(level)) {
      print(level, messageFactory.make(arg));
    }
  }

  /**
   * Print a build message.
   *
   * @param level the level of the message.
   * @param arg the arg for the message factory.
   * @param messageFactory the message factory.
   */
  default void print(LoggerLevel level, int arg, Logger.IntSinFactory messageFactory) {
    if (isEnabled(level)) {
      print(level, messageFactory.make(arg));
    }
  }

  /**
   * Print a build message.
   *
   * @param level the level of the message.
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param messageFactory the message factory.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   */
  default <F, S> void print(
      LoggerLevel level,
      F first,
      S second,
      Logger.BiFactory<F, S> messageFactory) {
    if (isEnabled(level)) {
      print(level, messageFactory.make(first, second));
    }
  }

  /**
   * Print a build message.
   *
   * @param level the level of the message.
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param messageFactory the message factory.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   */
  default <F, S> void print(
      LoggerLevel level,
      @Nullable F first,
      @Nullable S second,
      Logger.NullableBiFactory<F, S> messageFactory) {
    if (isEnabled(level)) {
      print(level, messageFactory.make(first, second));
    }
  }

  /**
   * Print a build message.
   *
   * @param level the level of the message.
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param messageFactory the message factory.
   * @param <F> the first argument's type.
   */
  default <F> void print(
      LoggerLevel level,
      F first,
      int second,
      Logger.ObjIntFactory<F> messageFactory) {
    if (isEnabled(level)) {
      print(level, messageFactory.make(first, second));
    }
  }

  /**
   * Print a build message.
   *
   * @param level the level of the message.
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param messageFactory the message factory.
   */
  default void print(LoggerLevel level, int first, int second, Logger.IntBiFactory messageFactory) {
    if (isEnabled(level)) {
      print(level, messageFactory.make(first, second));
    }
  }

  /**
   * Print a build message.
   *
   * @param level the level of the message.
   * @param first the first arg for the message factory.
   * @param second the second arg for the message factory.
   * @param third the third arg for the message factory.
   * @param messageFactory the message factory.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   * @param <T> the third argument's type.
   */
  default <F, S, T> void print(
      LoggerLevel level,
      F first,
      S second,
      T third,
      Logger.TriFactory<F, S, T> messageFactory) {

    if (isEnabled(level)) {
      print(level, messageFactory.make(first, second, third));
    }
  }
}
