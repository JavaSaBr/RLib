package javasabr.rlib.plugin.system;

import java.util.Optional;
import javasabr.rlib.collections.array.Array;
import org.jspecify.annotations.Nullable;

/**
 * Manages plugins and their containers.
 *
 * @since 10.0.0
 */
public interface PluginSystem {

  /**
   * Returns all plugin containers managed by this system.
   *
   * @return an array of plugin containers
   * @since 10.0.0
   */
  Array<PluginContainer> pluginContainers();

  /**
   * Returns the plugin container with the specified id.
   *
   * @param id the plugin id
   * @return the plugin container, or null if not found
   * @since 10.0.0
   */
  @Nullable
  PluginContainer getPluginContainer(String id);

  /**
   * Returns the plugin container with the specified id as an Optional.
   *
   * @param id the plugin id
   * @return an Optional containing the plugin container, or empty if not found
   * @since 10.0.0
   */
  default Optional<PluginContainer> getPluginContainerOptional(String id) {
    return Optional.ofNullable(getPluginContainer(id));
  }

  /**
   * Returns all plugins managed by this system.
   *
   * @return an array of plugins
   * @since 10.0.0
   */
  Array<Plugin> plugins();

  /**
   * Returns the plugin with the specified id.
   *
   * @param id the plugin id
   * @return the plugin, or null if not found
   * @since 10.0.0
   */
  @Nullable
  Plugin getPlugin(String id);

  /**
   * Returns the plugin with the specified id as an Optional.
   *
   * @param id the plugin id
   * @return an Optional containing the plugin, or empty if not found
   * @since 10.0.0
   */
  default Optional<Plugin> getPluginOptional(String id) {
    return Optional.ofNullable(getPlugin(id));
  }
}
