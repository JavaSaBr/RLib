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

    @NotNull
    private final Class<Plugin> pluginClass;

    @NotNull
    private final URLClassLoader classLoader;

    @NotNull
    private final ClassPathScanner scanner;

    @NotNull
    private final Path path;

    public PluginContainer(@NotNull final Class<Plugin> pluginClass, @NotNull final URLClassLoader classLoader,
                           @NotNull final ClassPathScanner scanner, @NotNull final Path path) {
        this.pluginClass = pluginClass;
        this.classLoader = classLoader;
        this.scanner = scanner;
        this.path = path;
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
        return description.id;
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
