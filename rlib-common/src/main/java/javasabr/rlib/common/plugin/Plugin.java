package javasabr.rlib.common.plugin;

import javasabr.rlib.common.plugin.annotation.PluginDescription;
import org.jspecify.annotations.NullMarked;

/**
 * The interface to implement a plugin.
 *
 * @author JavaSaBr
 */
@NullMarked
public interface Plugin {

  /**
   * Get a class loader of this plugin.
   *
   * @return the class loader.
   */
  ClassLoader getClassLoader();

  /**
   * Get the ID of this plugin.
   *
   * @return the plugin id.
   */
  default String getId() {
    return getClass()
        .getAnnotation(PluginDescription.class)
        .id();
  }

  /**
   * Get the version of this plugin.
   *
   * @return the plugin version.
   */
  default Version getVersion() {
    return new Version(getClass()
        .getAnnotation(PluginDescription.class)
        .version());
  }

  /**
   * Gets a name of this plugin.
   *
   * @return the name of this plugin.
   */
  default String getName() {
    return getClass()
        .getAnnotation(PluginDescription.class)
        .name();
  }

  /**
   * Gets a description of this plugin.
   *
   * @return the description of this plugin.
   */
  default String getDescription() {
    return getClass()
        .getAnnotation(PluginDescription.class)
        .description();
  }

  /**
   * Return true if this plugin is embedded.
   *
   * @return true if this plugin is embedded.
   */
  boolean isEmbedded();

  /**
   * Initialize this plugin.
   *
   * @param pluginSystem the plugin system.
   */
  void initialize(PluginSystem pluginSystem);
}
