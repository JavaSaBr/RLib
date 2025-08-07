package javasabr.rlib.common.plugin.exception;

import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * The exception about problems with pre-loading plugins.
 *
 * @author JavaSaBr
 */
@NullMarked
public class PreloadPluginException extends PluginException {

  private final Path path;

  public PreloadPluginException(String message, Path path) {
    super(message);
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
