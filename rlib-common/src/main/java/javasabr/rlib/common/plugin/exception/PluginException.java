package javasabr.rlib.common.plugin.exception;

import org.jspecify.annotations.NullMarked;

/**
 * The base implementation of a plugin exception.
 *
 * @author JavaSaBr
 */
@NullMarked
public class PluginException extends RuntimeException {

  public PluginException(String message) {
    super(message);
  }

  public PluginException(String message, Throwable cause) {
    super(message, cause);
  }
}
