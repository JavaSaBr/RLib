package com.ss.rlib.plugin.annotation;

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
    String id() default "unknown";

    /**
     * Don't support yet.
     */
    @NotNull
    @Deprecated
    String[] dependences() default {};

    /**
     * The plugin version.
     */
    @NotNull
    String version() default "0.1";
}
