package javasabr.rlib.plugin.system.exception;

import java.nio.file.Path;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Exception thrown when plugin initialization fails.
 *
 * @since 10.0.0
 */
@Getter
@Accessors(fluent = true)
public class InitializePluginException extends PluginException {

  private final Path path;

  /**
   * Creates a new initialization exception.
   *
   * @param message the error message
   * @param path the path to the plugin that failed to initialize
   * @since 10.0.0
   */
  public InitializePluginException(String message, Path path) {
    super(message);
    this.path = path;
  }

  /**
   * Creates a new initialization exception with a cause.
   *
   * @param message the error message
   * @param path the path to the plugin that failed to initialize
   * @param e the underlying cause
   * @since 10.0.0
   */
  public InitializePluginException(String message, Path path, Throwable e) {
    super(message, e);
    this.path = path;
  }
}
