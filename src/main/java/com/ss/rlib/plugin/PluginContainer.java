package com.ss.rlib.plugin;

import com.ss.rlib.classpath.ClassPathScanner;
import com.ss.rlib.plugin.annotation.PluginDescription;
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
     * The flag of that this container is of an embedded plugin.
     */
    private final boolean embedded;

    public PluginContainer(@NotNull final Class<Plugin> pluginClass, @NotNull final URLClassLoader classLoader,
                           @NotNull final ClassPathScanner scanner, @NotNull final Path path, final boolean embedded) {
        this.pluginClass = pluginClass;
        this.classLoader = classLoader;
        this.scanner = scanner;
        this.path = path;
        this.embedded = embedded;
    }

    @NotNull
    public Class<Plugin> getPluginClass() {
        return pluginClass;
    }

    /**
     * Gets the ID of this plugin.
     *
     * @return the ID.
     */
    @NotNull
    public String getId() {
        final PluginDescription description = pluginClass.getAnnotation(PluginDescription.class);
        return description.id();
    }

    /**
     * @return true if this container is of an embedded plugin.
     */
    public boolean isEmbedded() {
        return embedded;
    }

    @NotNull
    public ClassPathScanner getScanner() {
        return scanner;
    }

    @NotNull
    public URLClassLoader getClassLoader() {
        return classLoader;
    }

    @NotNull
    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "PluginContainer{" + "pluginClass=" + pluginClass + ", path=" + path + '}';
    }
}
