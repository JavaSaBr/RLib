package com.ss.rlib.common.plugin;

import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement a plugin system.
 *
 * @author JavaSaBr
 */
public interface PluginSystem {

    /**
     * Get all available plugin containers.
     *
     * @return the list of all available plugin containers.
     */
    @NotNull Array<PluginContainer> getPluginContainers();

    /**
     * Gets a plugin container by the plugin id.
     *
     * @param id the plugin id.
     * @return the container or null.
     */
    @Nullable PluginContainer getPluginContainer(@NotNull String id);

    /**
     * Get all available plugins.
     *
     * @return the list of all available plugins.
     */
    @NotNull Array<Plugin> getPlugins();

    /**
     * Gets a plugin by the plugin id.
     *
     * @param id the plugin id.
     * @return the plugin or null.
     */
    @Nullable Plugin getPlugin(@NotNull final String id);
}
