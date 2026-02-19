package javasabr.rlib.plugin.system.exception;

/**
 * Base exception for plugin-related errors.
 *
 * @since 10.0.0
 */
public class PluginException extends RuntimeException {

  /**
   * Creates a new plugin exception with the specified message.
   *
   * @param message the error message
   * @since 10.0.0
   */
  public PluginException(String message) {
    super(message);
  }

  /**
   * Creates a new plugin exception with the specified message and cause.
   *
   * @param message the error message
   * @param cause the underlying cause
   * @since 10.0.0
   */
  public PluginException(String message, Throwable cause) {
    super(message, cause);
  }
}
