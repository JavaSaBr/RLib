package javasabr.rlib.plugin.system;

import javasabr.rlib.plugin.system.annotation.PluginDescription;

/**
 * Represents a plugin in the plugin system.
 *
 * @since 10.0.0
 */
public interface Plugin {

  /**
   * Returns the class loader used to load this plugin.
   *
   * @return the plugin's class loader
   * @since 10.0.0
   */
  ClassLoader classLoader();

  /**
   * Returns the unique identifier of this plugin.
   *
   * @return the plugin id
   * @since 10.0.0
   */
  default String id() {
    return getClass()
        .getAnnotation(PluginDescription.class)
        .id();
  }

  /**
   * Returns the version of this plugin.
   *
   * @return the plugin version
   * @since 10.0.0
   */
  default Version version() {
    return new Version(getClass()
        .getAnnotation(PluginDescription.class)
        .version());
  }

  /**
   * Returns the display name of this plugin.
   *
   * @return the plugin name
   * @since 10.0.0
   */
  default String name() {
    return getClass()
        .getAnnotation(PluginDescription.class)
        .name();
  }

  /**
   * Returns the description of this plugin.
   *
   * @return the plugin description
   * @since 10.0.0
   */
  default String description() {
    return getClass()
        .getAnnotation(PluginDescription.class)
        .description();
  }

  /**
   * Returns whether this plugin is embedded in the application.
   *
   * @return true if embedded, false if installed externally
   * @since 10.0.0
   */
  boolean isEmbedded();

  /**
   * Initializes this plugin with the given plugin system.
   *
   * @param pluginSystem the plugin system managing this plugin
   * @since 10.0.0
   */
  void initialize(PluginSystem pluginSystem);
}
