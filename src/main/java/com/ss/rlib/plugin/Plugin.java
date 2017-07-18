package com.ss.rlib.plugin;

import com.ss.rlib.plugin.annotation.PluginDescription;
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
     * Initialize this plugin.
     *
     * @param pluginSystem the plugin system.
     */
    void initialize(@NotNull PluginSystem pluginSystem);
}
