package javasabr.rlib.plugin.system.exception;

import java.nio.file.Path;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Exception thrown when plugin pre-loading fails.
 *
 * @since 10.0.0
 */
@Getter
@Accessors(fluent = true)
public class PreloadPluginException extends PluginException {

  private final Path path;

  /**
   * Creates a new preload exception.
   *
   * @param message the error message
   * @param path the path to the plugin that failed to preload
   * @since 10.0.0
   */
  public PreloadPluginException(String message, Path path) {
    super(message);
    this.path = path;
  }
}
