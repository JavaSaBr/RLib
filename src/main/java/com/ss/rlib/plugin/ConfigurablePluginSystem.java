package com.ss.rlib.plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * The interface to implement configurable plugin system.
 *
 * @author JavaSaBr
 */
public interface ConfigurablePluginSystem extends PluginSystem {

    /**
     * Configure the path to installed plugins.
     *
     * @param installationPluginsPath the path.
     */
    void configureInstallationPluginsPath(@NotNull Path installationPluginsPath);

    /**
     * Configure the path to embedded plugins.
     *
     * @param embeddedPluginPath the path.
     */
    void configureEmbeddedPluginPath(@NotNull Path embeddedPluginPath);

    /**
     * Set the app version.
     *
     * @param version the app version.
     */
    void setAppVersion(@Nullable Version version);

    /**
     * Preload all plugins.
     */
    void preLoad();

    /**
     * Initialize all plugins.
     */
    void initialize();

    /**
     * Install a new plugin.
     *
     * @param file           the path to the plugin.
     * @param needInitialize true if need to initialize the plugin.
     * @return the installed plugin or null.
     */
    @Nullable Plugin installPlugin(@NotNull Path file, boolean needInitialize);

    /**
     * Remove the plugin.
     *
     * @param plugin the plugin.
     */
    void removePlugin(@NotNull Plugin plugin);
}
