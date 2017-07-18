package com.ss.rlib.plugin;

import org.jetbrains.annotations.NotNull;

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
     * Preload all plugins.
     */
    void preLoad();

    /**
     * Initialize all plugins.
     */
    void initialize();
}
