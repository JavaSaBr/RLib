package com.ss.rlib.common.plugin.impl;

import com.ss.rlib.common.plugin.Plugin;
import com.ss.rlib.common.plugin.PluginContainer;
import com.ss.rlib.common.plugin.PluginSystem;
import com.ss.rlib.common.plugin.Version;
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
    public @NotNull ClassLoader getClassLoader() {
        return container.getClassLoader();
    }

    @Override
    public boolean isEmbedded() {
        return container.isEmbedded();
    }

    @Override
    public @NotNull String getId() {
        return container.getId();
    }

    @Override
    public @NotNull Version getVersion() {
        return container.getVersion();
    }

    @Override
    public @NotNull String getDescription() {
        return container.getDescription();
    }

    @Override
    public @NotNull String getName() {
        return container.getName();
    }

    /**
     * The container of this plugin.
     *
     * @return the container.
     */
    protected @NotNull PluginContainer getContainer() {
        return container;
    }

    @Override
    public void initialize(@NotNull final PluginSystem pluginSystem) {
    }
}
