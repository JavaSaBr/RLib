package com.ss.rlib.plugin.annotation;

import com.ss.rlib.plugin.Version;
import org.jetbrains.annotations.NotNull;

/**
 * The annotation to describe a plugin.
 *
 * @author JavaSaBr
 */
public @interface PluginDescription {

    /**
     * The plugin id.
     */
    @NotNull
    String id = "unknown";

    /**
     * Don't support yet.
     */
    @NotNull
    @Deprecated
    String[] dependences = {};

    /**
     * The plugin version.
     */
    @NotNull
    Version version = new Version("0.1");
}
