package com.ss.rlib.plugin.impl;

import com.ss.rlib.plugin.ConfigurablePluginSystem;
import org.jetbrains.annotations.NotNull;

/**
 * The factory of different plugin systems.
 *
 * @author JavaSaBr
 */
public class PluginSystemFactory {

    public static  @NotNull ConfigurablePluginSystem newBasePluginSystem(@NotNull final ClassLoader classLoader) {
        return new BasePluginSystem(classLoader);
    }
}
