package javasabr.rlib.common.plugin.exception;

import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

/**
 * The exception about problems with initializing plugins.
 *
 * @author JavaSaBr
 */
@NullMarked
public class InitializePluginException extends PluginException {

  private final Path path;

  public InitializePluginException(String message, Path path) {
    super(message);
    this.path = path;
  }

  public InitializePluginException(String message, Path path, Throwable e) {
    super(message, e);
    this.path = path;
  }

  /**
   * Get the path of the plugin.
   *
   * @return the path of the plugin.
   */
  public Path getPath() {
    return path;
  }
}
