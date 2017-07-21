package com.ss.rlib.plugin;

import com.ss.rlib.plugin.annotation.PluginDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @NotNull
    ClassLoader getClassLoader();

    /**
     * Get the ID of this plugin.
     *
     * @return the plugin id.
     */
    @NotNull
    default String getId() {
        final Class<? extends Plugin> cs = getClass();
        final PluginDescription description = cs.getAnnotation(PluginDescription.class);
        return description.id();
    }

    /**
     * Get the version of this plugin.
     *
     * @return the plugin version.
     */
    @NotNull
    default Version getVersion() {
        final Class<? extends Plugin> cs = getClass();
        final PluginDescription description = cs.getAnnotation(PluginDescription.class);
        return new Version(description.version());
    }

    /**
     * Gets a name of this plugin.
     *
     * @return the name of this plugin.
     */
    @Nullable
    default String getName() {
        final Class<? extends Plugin> cs = getClass();
        final PluginDescription description = cs.getAnnotation(PluginDescription.class);
        return description.name();
    }

    /**
     * Gets a description of this plugin.
     *
     * @return the description of this plugin.
     */
    @Nullable
    default String getDescription() {
        final Class<? extends Plugin> cs = getClass();
        final PluginDescription description = cs.getAnnotation(PluginDescription.class);
        return description.description();
    }

    /**
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
