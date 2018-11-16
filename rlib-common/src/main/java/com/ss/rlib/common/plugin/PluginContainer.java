package com.ss.rlib.common.plugin;

import com.ss.rlib.common.classpath.ClassPathScanner;
import com.ss.rlib.common.plugin.annotation.PluginDescription;
import org.jetbrains.annotations.NotNull;

import java.net.URLClassLoader;
import java.nio.file.Path;

/**
 * The implementation of a plugin container.
 *
 * @author JavaSaBr
 */
public class PluginContainer {

    /**
     * The plugin class.
     */
    @NotNull
    private final Class<Plugin> pluginClass;

    /**
     * The class loader of this class.
     */
    @NotNull
    private final URLClassLoader classLoader;

    /**
     * The classpath scanner of this plugin.
     */
    @NotNull
    private final ClassPathScanner scanner;

    /**
     * The path to a plugin folder.
     */
    @NotNull
    private final Path path;

    /**
     * The plugin id.
     */
    @NotNull
    private final String id;

    /**
     * The name.
     */
    @NotNull
    private final String name;

    /**
     * The description.
     */
    @NotNull
    private final String description;

    /**
     * The version.
     */
    @NotNull
    private final Version version;

    /**
     * The flag of that this container is of an embedded plugin.
     */
    private final boolean embedded;

    public PluginContainer(
            @NotNull Class<Plugin> pluginClass,
            @NotNull URLClassLoader classLoader,
            @NotNull ClassPathScanner scanner,
            @NotNull Path path,
            boolean embedded
    ) {
        final PluginDescription description = pluginClass.getAnnotation(PluginDescription.class);
        this.pluginClass = pluginClass;
        this.classLoader = classLoader;
        this.scanner = scanner;
        this.path = path;
        this.embedded = embedded;
        this.id = description.id();
        this.name = description.name();
        this.version = new Version(description.version());
        this.description = description.description();
    }

    public @NotNull Class<Plugin> getPluginClass() {
        return pluginClass;
    }

    /**
     * Gets the ID of this plugin.
     *
     * @return the ID.
     */
    public @NotNull String getId() {
        return id;
    }

    /**
     * Get the version of this plugin.
     *
     * @return the plugin version.
     */
    public @NotNull Version getVersion() {
        return version;
    }

    /**
     * Gets a name of this plugin.
     *
     * @return the name of this plugin.
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Gets a description of this plugin.
     *
     * @return the description of this plugin.
     */
    public @NotNull String getDescription() {
        return description;
    }

    /**
     * @return true if this container is of an embedded plugin.
     */
    public boolean isEmbedded() {
        return embedded;
    }

    /**
     * Get the scanner of this plugin.
     *
     * @return the scanner.
     */
    public @NotNull ClassPathScanner getScanner() {
        return scanner;
    }

    /**
     * Get the class loader of this plugin.
     *
     * @return the class loader.
     */
    public @NotNull URLClassLoader getClassLoader() {
        return classLoader;
    }

    public @NotNull Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "PluginContainer{" + "pluginClass=" + pluginClass + ", path=" + path + '}';
    }
}
