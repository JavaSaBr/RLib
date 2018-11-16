package com.ss.rlib.common.plugin;

import com.ss.rlib.common.plugin.annotation.PluginDescription;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a plugin.
 *
 * @author JavaSaBr
 */
public interface Plugin {

    /**
     * Get a class loader of this plugin.
     *
     * @return the class loader.
     */
    @NotNull ClassLoader getClassLoader();

    /**
     * Get the ID of this plugin.
     *
     * @return the plugin id.
     */
    default @NotNull String getId() {
        return getClass()
                .getAnnotation(PluginDescription.class)
                .id();
    }

    /**
     * Get the version of this plugin.
     *
     * @return the plugin version.
     */
    default @NotNull Version getVersion() {
        return new Version(getClass()
                .getAnnotation(PluginDescription.class)
                .version());
    }

    /**
     * Gets a name of this plugin.
     *
     * @return the name of this plugin.
     */
    default @NotNull String getName() {
        return getClass()
                .getAnnotation(PluginDescription.class)
                .name();
    }

    /**
     * Gets a description of this plugin.
     *
     * @return the description of this plugin.
     */
    default @NotNull String getDescription() {
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
    void initialize(@NotNull PluginSystem pluginSystem);
}
