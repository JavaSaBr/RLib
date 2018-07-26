package com.ss.rlib.common.plugin.impl;

import com.ss.rlib.common.plugin.ConfigurablePluginSystem;
import org.jetbrains.annotations.NotNull;

/**
 * The factory of different plugin systems.
 *
 * @author JavaSaBr
 */
public class PluginSystemFactory {

    public static  @NotNull ConfigurablePluginSystem newBasePluginSystem() {
        return new BasePluginSystem();
    }

    public static  @NotNull ConfigurablePluginSystem newBasePluginSystem(@NotNull ClassLoader classLoader) {
        return new BasePluginSystem(classLoader);
    }
}
