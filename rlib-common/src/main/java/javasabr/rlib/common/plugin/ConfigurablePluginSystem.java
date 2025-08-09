package javasabr.rlib.common.plugin;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The interface to implement configurable plugin system.
 *
 * @author JavaSaBr
 */
@NullMarked
public interface ConfigurablePluginSystem extends PluginSystem {

  /**
   * Configure the path to installed plugins.
   *
   * @param installationPluginsPath the path.
   */
  void configureInstallationPluginsPath(Path installationPluginsPath);

  /**
   * Configure the path to embedded plugins.
   *
   * @param embeddedPluginPath the path.
   */
  void configureEmbeddedPluginPath(Path embeddedPluginPath);

  /**
   * Set the app version.
   *
   * @param version the app version.
   */
  void setAppVersion(@Nullable Version version);

  /**
   * Preload all plugins.
   *
   * @return the async result of pre-loaded plugin system.
   */
  CompletableFuture<ConfigurablePluginSystem> preLoad();

  /**
   * Preload all plugins.
   *
   * @param executor the executor.
   * @return the async result of pre-loaded plugin system.
   */
  CompletableFuture<ConfigurablePluginSystem> preLoad(Executor executor);

  /**
   * Initialize all plugins.
   *
   * @return the async result of initialized plugin system.
   */
  CompletableFuture<ConfigurablePluginSystem> initialize();

  /**
   * Initialize all plugins.
   *
   * @param executor the executor.
   * @return the async result of initialized plugin system.
   */
  CompletableFuture<ConfigurablePluginSystem> initialize(Executor executor);

  /**
   * Install a new plugin.
   *
   * @param file the path to the plugin.
   * @param needInitialize true if need to initialize the plugin.
   * @return the installed plugin or null.
   */
  @Nullable Plugin installPlugin(Path file, boolean needInitialize);

  /**
   * Remove the plugin.
   *
   * @param plugin the plugin.
   */
  void removePlugin(Plugin plugin);
}
