package com.ss.rlib.common.plugin.annotation;

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
     * Get the plugin id.
     *
     * @return the plugin id.
     */
    @NotNull String id() default "unknown";

    /**
     * Get dependencies of this plugin.
     *
     * @return dependencies of this plugin.
     */
    @NotNull String[] dependences() default {};

    /**
     * Get the plugin version.
     *
     * @return the plugin version.
     */
    @NotNull String version() default "0.1.0";

    /**
     * Get the min version of application.
     *
     * @return the min version of application.
     */
    @NotNull String minAppVersion() default "0.0.0";

    /**
     * Get the plugin name.
     *
     * @return the plugin name.
     */
    @NotNull String name() default "no name";

    /**
     * Get the plugin description.
     *
     * @return the plugin description.
     */
    @NotNull String description() default "no description";
}
