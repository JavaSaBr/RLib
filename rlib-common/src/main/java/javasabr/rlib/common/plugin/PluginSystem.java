package javasabr.rlib.common.plugin;

import java.util.Optional;
import javasabr.rlib.common.util.array.Array;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The interface to implement a plugin system.
 *
 * @author JavaSaBr
 */
@NullMarked
public interface PluginSystem {

  /**
   * Get all available plugin containers.
   *
   * @return the list of all available plugin containers.
   */
  Array<PluginContainer> getPluginContainers();

  /**
   * Get a plugin container by the plugin id.
   *
   * @param id the plugin id.
   * @return the container or null.
   */
  @Nullable
  PluginContainer getPluginContainer(String id);

  /**
   * Get a plugin container by the plugin id.
   *
   * @param id the plugin id.
   * @return the optional value of container.
   */
  default Optional<PluginContainer> getPluginContainerOpt(String id) {
    return Optional.ofNullable(getPluginContainer(id));
  }

  /**
   * Get all available plugins.
   *
   * @return the list of all available plugins.
   */
  Array<Plugin> getPlugins();

  /**
   * Get a plugin by the plugin id.
   *
   * @param id the plugin id.
   * @return the plugin or null.
   */
  @Nullable Plugin getPlugin(String id);

  /**
   * Get a plugin by the plugin id.
   *
   * @param id the plugin id.
   * @return the optional value of a plugin.
   */
  default Optional<Plugin> getPluginOpt(String id) {
    return Optional.ofNullable(getPlugin(id));
  }
}
