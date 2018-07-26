package com.ss.rlib.common.plugin;

import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
     * Get a plugin container by the plugin id.
     *
     * @param id the plugin id.
     * @return the container or null.
     */
    @Nullable PluginContainer getPluginContainer(@NotNull String id);

    /**
     * Get a plugin container by the plugin id.
     *
     * @param id the plugin id.
     * @return the optional value of container.
     */
    default @NotNull Optional<PluginContainer> getPluginContainerOpt(@NotNull String id) {
        return Optional.ofNullable(getPluginContainer(id));
    }

    /**
     * Get all available plugins.
     *
     * @return the list of all available plugins.
     */
    @NotNull Array<Plugin> getPlugins();

    /**
     * Get a plugin by the plugin id.
     *
     * @param id the plugin id.
     * @return the plugin or null.
     */
    @Nullable Plugin getPlugin(@NotNull String id);

    /**
     * Get a plugin by the plugin id.
     *
     * @param id the plugin id.
     * @return the optional value of a plugin.
     */
    default @NotNull Optional<Plugin> getPluginOpt(@NotNull String id) {
        return Optional.ofNullable(getPlugin(id));
    }
}
