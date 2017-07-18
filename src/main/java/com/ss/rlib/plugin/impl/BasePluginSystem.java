package com.ss.rlib.plugin.impl;

import com.ss.rlib.classpath.ClassPathScanner;
import com.ss.rlib.classpath.ClassPathScannerFactory;
import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.plugin.ConfigurablePluginSystem;
import com.ss.rlib.plugin.Plugin;
import com.ss.rlib.plugin.PluginContainer;
import com.ss.rlib.plugin.PluginSystem;
import com.ss.rlib.plugin.exception.PluginException;
import com.ss.rlib.util.ClassUtils;
import com.ss.rlib.util.FileUtils;
import com.ss.rlib.util.Utils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import com.ss.rlib.util.dictionary.DictionaryFactory;
import com.ss.rlib.util.dictionary.ObjectDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The base implementation of the {@link PluginSystem}.
 *
 * @author JavaSaBr
 */
public class BasePluginSystem implements ConfigurablePluginSystem {

    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(BasePluginSystem.class);

    /**
     * The list of preload plugin containers.
     */
    @NotNull
    private final Array<PluginContainer> preloadPluginContainers;

    /**
     * The result map of plugin containers.
     */
    @NotNull
    private final ObjectDictionary<String, PluginContainer> pluginContainers;

    /**
     * The result map of plugins.
     */
    @NotNull
    private final ObjectDictionary<String, Plugin> plugins;

    /**
     * The base classloader.
     */
    @NotNull
    private final ClassLoader baseLoader;

    /**
     * The path to folder with embedded plugins.
     */
    @Nullable
    private Path embeddedPluginsPath;

    /**
     * The path to folder with installed plugins.
     */
    @Nullable
    private Path installationPluginsPath;

    /**
     * The flag of this plugin system was pre-loaded.
     */
    private boolean preLoaded;
    /**
     * The flag of this plugin system was initialized.
     */
    private boolean initialized;

    public BasePluginSystem(@NotNull final ClassLoader baseLoader) {
        this.baseLoader = baseLoader;
        this.preloadPluginContainers = ArrayFactory.newArray(PluginContainer.class);
        this.pluginContainers = DictionaryFactory.newObjectDictionary();
        this.plugins = DictionaryFactory.newObjectDictionary();
    }

    @Override
    public void preLoad() {
        if (isPreLoaded()) throw new PluginException("This system was already pre-loaded.");

        if (LOGGER.isEnabledDebug()) {
            LOGGER.debug(this, "start to pre-load all plugins.");
        }

        final Path embeddedPluginsPath = getEmbeddedPluginsPath();
        final Path installationPluginsPath = getInstallationPluginsPath();

        if (embeddedPluginsPath != null) {

            if (LOGGER.isEnabledDebug()) {
                LOGGER.debug(this, "try to pre-load embedded plugins from the folder " + embeddedPluginsPath + ".");
            }

            if (!Files.exists(embeddedPluginsPath)) {
                throw new PluginException("Can't read the path " + embeddedPluginsPath);
            }

            loadPlugins(embeddedPluginsPath, true);
        }

        if (installationPluginsPath != null) {

            if (LOGGER.isEnabledDebug()) {
                LOGGER.debug(this, "try to pre-load installed plugins from the folder " + installationPluginsPath + ".");
            }

            if (!Files.exists(installationPluginsPath)) {
                throw new PluginException("Can't read the path " + installationPluginsPath);
            }

            loadPlugins(installationPluginsPath, false);
        }

        for (final PluginContainer pluginContainer : preloadPluginContainers) {
            pluginContainers.put(pluginContainer.getId(), pluginContainer);
        }

        if (LOGGER.isEnabledDebug()) {
            LOGGER.debug(this, "Pre-loaded: " + pluginContainers);
        }

        if (LOGGER.isEnabledDebug()) {
            LOGGER.debug(this, "all plugins were pre-loaded.");
        }

        setPreLoaded(true);
    }

    @Override
    public void initialize() {
        if (isInitialized()) throw new PluginException("This system was already initialized.");

        if (LOGGER.isEnabledDebug()) {
            LOGGER.debug(this, "start to load all plugins.");
        }

        for (final PluginContainer container : preloadPluginContainers) {

            final Class<Plugin> pluginClass = container.getPluginClass();

            if (LOGGER.isEnabledDebug()) {
                LOGGER.debug(this, "start to create a plugin " + pluginClass);
            }

            final Constructor<Plugin> constructor = ClassUtils.getConstructor(pluginClass, PluginContainer.class);

            if (constructor == null) {
                throw new PluginException("Not found base constructor in the class " + constructor);
            }

            final Plugin plugin = ClassUtils.newInstance(constructor, container);

            plugins.put(plugin.getId(), plugin);
        }

        if (LOGGER.isEnabledDebug()) {
            LOGGER.debug(this, "start to initialize all plugins.");
        }

        plugins.forEach((s, plugin) -> plugin.initialize(this));

        if (LOGGER.isEnabledDebug()) {
            LOGGER.debug(this, "all plugins were initialized.");
        }


        setInitialized(true);
    }

    /**
     * Load plugins from the directory.
     *
     * @param path     the path to the directory.
     * @param embedded true if this directory with embedded plugins.
     */
    protected void loadPlugins(@NotNull final Path path, final boolean embedded) {

        try (final DirectoryStream<Path> directories = Files.newDirectoryStream(path)) {

            for (final Path directory : directories) {

                if (!Files.isDirectory(directory)) {
                    LOGGER.warning(this, "The path " + directory + " isn't a directory.");
                    continue;
                }

                preloadPluginContainers.add(loadPlugin(directory, baseLoader, null));
            }

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load a plugin from the directory.
     *
     * @param directory    the plugin directory.
     * @param parentLoader the parent class loader.
     * @param parents      the parent plugins.
     */
    protected PluginContainer loadPlugin(@NotNull final Path directory, @NotNull final ClassLoader parentLoader,
                                         @Nullable final PluginContainer[] parents) {

        final Array<Path> files = FileUtils.getFiles(directory, ".jar");

        final URL[] urls = files.stream()
                .map(path -> Utils.get(path, Path::toUri))
                .map(uri -> Utils.get(uri, URI::toURL))
                .toArray(URL[]::new);

        final String[] additionalPaths = files.stream()
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .toArray(String[]::new);

        final URLClassLoader classLoader = new URLClassLoader(urls);
        final ClassPathScanner scanner = ClassPathScannerFactory.newDefaultScanner(classLoader, additionalPaths);
        scanner.setUseSystemClasspath(false);
        scanner.scan(file -> true);

        final Array<Class<Plugin>> pluginImplementations = ArrayFactory.newArray(Class.class);
        scanner.findImplements(pluginImplementations, Plugin.class);

        if (pluginImplementations.isEmpty()) {
            throw new RuntimeException(
                    "Not found an implementation of the Plugin interface in the plugin " + directory);
        } else if (pluginImplementations.size() > 1) {
            throw new RuntimeException(
                    "Found more than 1 implementations of the Plugin interface in the plugin " + directory);
        }

        final Class<Plugin> pluginClass = pluginImplementations.first();
        return new PluginContainer(pluginClass, classLoader, scanner, directory);
    }

    /**
     * @return the path to folder with embedded plugins.
     */
    @Nullable
    protected Path getEmbeddedPluginsPath() {
        return embeddedPluginsPath;
    }

    /**
     * @return the path to folder with installed plugins.
     */
    @Nullable
    protected Path getInstallationPluginsPath() {
        return installationPluginsPath;
    }

    @Override
    public void configureEmbeddedPluginPath(@NotNull final Path embeddedPluginPath) {
        if (isInitialized()) throw new RuntimeException("The plugin system is already initialized.");
        this.embeddedPluginsPath = embeddedPluginPath;
    }

    @Override
    public void configureInstallationPluginsPath(@NotNull final Path installationPluginsPath) {
        if (isInitialized()) throw new RuntimeException("The plugin system is already initialized.");
        this.installationPluginsPath = installationPluginsPath;
    }

    /**
     * @return true if this system was pre-loaded.
     */
    protected boolean isPreLoaded() {
        return preLoaded;
    }

    /**
     * @param preLoaded true if this system was pre-loaded.
     */
    protected void setPreLoaded(final boolean preLoaded) {
        this.preLoaded = preLoaded;
    }

    /**
     * @return true if this system was initialized.
     */
    protected boolean isInitialized() {
        return initialized;
    }

    /**
     * @param initialized true if this system was initialized.
     */
    protected void setInitialized(final boolean initialized) {
        this.initialized = initialized;
    }

    @NotNull
    @Override
    public PluginContainer[] getPluginContainers() {
        final Array<PluginContainer> containers = ArrayFactory.newArray(PluginContainer.class, pluginContainers.size());
        pluginContainers.values(containers);
        return containers.array();
    }

    @Override
    @Nullable
    public PluginContainer getPluginContainer(@NotNull final String id) {
        return pluginContainers.get(id);
    }

    @NotNull
    @Override
    public Plugin[] getPlugins() {
        final Array<Plugin> plugins = ArrayFactory.newArray(Plugin.class, this.plugins.size());
        this.plugins.values(plugins);
        return plugins.array();
    }

    @Override
    @Nullable
    public Plugin getPlugin(final @NotNull String id) {
        return plugins.get(id);
    }
}
