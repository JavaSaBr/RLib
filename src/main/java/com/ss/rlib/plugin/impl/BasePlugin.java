package com.ss.rlib.plugin.impl;

import com.ss.rlib.plugin.Plugin;
import com.ss.rlib.plugin.PluginContainer;
import com.ss.rlib.plugin.PluginSystem;
import com.ss.rlib.plugin.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @NotNull
    @Override
    public String getId() {
        return container.getId();
    }

    @NotNull
    @Override
    public Version getVersion() {
        return container.getVersion();
    }

    @Nullable
    @Override
    public String getDescription() {
        return container.getDescription();
    }

    @Nullable
    @Override
    public String getName() {
        return container.getName();
    }

    @Override
    public void initialize(@NotNull final PluginSystem pluginSystem) {

    }
}
