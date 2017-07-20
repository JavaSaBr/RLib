package com.ss.rlib.plugin.impl;

import com.ss.rlib.plugin.Plugin;
import com.ss.rlib.plugin.PluginContainer;
import com.ss.rlib.plugin.PluginSystem;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of the {@link Plugin}.
 *
 * @author JavaSaBr
 */
public class BasePlugin implements Plugin {

    /**
     * The plugin container.
     */
    @NotNull
    private final PluginContainer container;

    public BasePlugin(@NotNull final PluginContainer container) {
        this.container = container;
    }

    @Override
    @NotNull
    public ClassLoader getClassLoader() {
        return container.getClassLoader();
    }

    @Override
    public boolean isEmbedded() {
        return container.isEmbedded();
    }

    @Override
    public void initialize(@NotNull final PluginSystem pluginSystem) {

    }
}
