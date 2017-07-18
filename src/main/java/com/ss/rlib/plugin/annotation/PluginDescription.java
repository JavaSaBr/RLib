package com.ss.rlib.plugin.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * The annotation to describe a plugin.
 *
 * @author JavaSaBr
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
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
