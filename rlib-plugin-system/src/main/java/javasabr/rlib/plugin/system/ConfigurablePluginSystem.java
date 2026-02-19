package javasabr.rlib.plugin.system;

import java.nio.file.Path;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import org.jspecify.annotations.Nullable;

/**
 * A configurable plugin system that supports loading and managing plugins.
 *
 * @since 10.0.0
 */
public interface ConfigurablePluginSystem extends PluginSystem {

  /**
   * Configures the path where installed plugins are located.
   *
   * @param installationPluginsPath the path to the plugins directory
   * @since 10.0.0
   */
  void configureInstallationPluginsPath(Path installationPluginsPath);

  /**
   * Configures the path where embedded plugins are located.
   *
   * @param embeddedPluginPath the path to embedded plugins
   * @since 10.0.0
   */
  void configureEmbeddedPluginPath(Path embeddedPluginPath);

  /**
   * Configures the application version for plugin compatibility checks.
   *
   * @param version the application version, or null to disable version checks
   * @since 10.0.0
   */
  void configureAppVersion(@Nullable Version version);

  /**
   * Pre-loads all plugins asynchronously.
   *
   * @return a completion stage that completes when pre-loading is done
   * @since 10.0.0
   */
  CompletionStage<ConfigurablePluginSystem> preLoad();

  /**
   * Pre-loads all plugins asynchronously using the specified executor.
   *
   * @param executor the executor to use for async operations
   * @return a completion stage that completes when pre-loading is done
   * @since 10.0.0
   */
  CompletionStage<ConfigurablePluginSystem> preLoad(Executor executor);

  /**
   * Initializes all pre-loaded plugins asynchronously.
   *
   * @return a completion stage that completes when initialization is done
   * @since 10.0.0
   */
  CompletionStage<ConfigurablePluginSystem> initialize();

  /**
   * Initializes all pre-loaded plugins asynchronously using the specified executor.
   *
   * @param executor the executor to use for async operations
   * @return a completion stage that completes when initialization is done
   * @since 10.0.0
   */
  CompletionStage<ConfigurablePluginSystem> initialize(Executor executor);

  /**
   * Installs a plugin from the specified file.
   *
   * @param file the plugin file to install
   * @param needInitialize true to initialize the plugin after installation
   * @return the installed plugin, or null if installation failed
   * @since 10.0.0
   */
  @Nullable
  Plugin installPlugin(Path file, boolean needInitialize);

  /**
   * Removes a plugin from the system.
   *
   * @param plugin the plugin to remove
   * @return true if the plugin was removed, false otherwise
   * @since 10.0.0
   */
  boolean removePlugin(Plugin plugin);
}
