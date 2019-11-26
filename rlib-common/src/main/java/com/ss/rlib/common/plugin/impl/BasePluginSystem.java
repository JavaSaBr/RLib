package com.ss.rlib.common.plugin.impl;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import static com.ss.rlib.common.util.array.ArrayCollectors.toArray;
import static com.ss.rlib.common.util.dictionary.DictionaryCollectors.toObjectDictionary;
import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.rlib.common.classpath.ClassPathScannerFactory;
import com.ss.rlib.common.plugin.*;
import com.ss.rlib.common.plugin.annotation.PluginDescription;
import com.ss.rlib.common.plugin.exception.InitializePluginException;
import com.ss.rlib.common.plugin.exception.PluginException;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.FileUtils;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.dictionary.ObjectDictionary;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The base implementation of the {@link PluginSystem}.
 *
 * @author JavaSaBr
 */
public class BasePluginSystem implements ConfigurablePluginSystem {

    protected static final Logger LOGGER = LoggerManager.getLogger(BasePluginSystem.class);

    private static class State {

        static final State EMPTY = new State(Array.empty(), ObjectDictionary.empty(), ObjectDictionary.empty());

        /**
         * The list of preload plugin containers.
         */
        private final @NotNull Array<PluginContainer> containers;

        /**
         * The result map of plugin containers.
         */
        private final @NotNull ObjectDictionary<String, PluginContainer> idToContainer;

        /**
         * The result map of plugins.
         */
        private final @NotNull ObjectDictionary<String, Plugin> idToPlugin;

        private State(
            @NotNull Array<PluginContainer> containers,
            @NotNull ObjectDictionary<String, PluginContainer> idToContainer,
            @NotNull ObjectDictionary<String, Plugin> idToPlugin
        ) {
            this.containers = containers;
            this.idToContainer = idToContainer;
            this.idToPlugin = idToPlugin;
        }
    }

    /**
     * The base classloader.
     */
    private final @NotNull ClassLoader baseLoader;

    /**
     * The app version.
     */
    private volatile @Nullable Version appVersion;

    /**
     * The path to folder with embedded plugins.
     */
    private volatile @Nullable Path embeddedPluginsPath;

    /**
     * The path to folder with installed plugins.
     */
    private volatile @Nullable Path installationPluginsPath;

    /**
     * The flag of this plugin system was pre-loaded.
     */
    private final @NotNull AtomicBoolean preLoaded;

    /**
     * The flag of this plugin system was initialized.
     */
    private final @NotNull AtomicBoolean initialized;

    /**
     * The system's state.
     */
    private final @NotNull AtomicReference<State> state;

    public BasePluginSystem() {
        this(StackWalker.getInstance(RETAIN_CLASS_REFERENCE)
            .getCallerClass()
            .getClassLoader());
    }

    public BasePluginSystem(@NotNull ClassLoader baseLoader) {
        this.baseLoader = baseLoader;
        this.state = new AtomicReference<>(State.EMPTY);
        this.preLoaded = new AtomicBoolean(false);
        this.initialized = new AtomicBoolean(false);
    }

    @Override
    public void setAppVersion(@Nullable Version appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public @NotNull CompletableFuture<ConfigurablePluginSystem> preLoad() {
        return preLoad(ForkJoinPool.commonPool());
    }

    @Override
    public @NotNull CompletableFuture<ConfigurablePluginSystem> preLoad(@NotNull Executor executor) {

        if (!preLoaded.compareAndSet(false, true)) {
            throw new PluginException("This system was already pre-loaded.");
        }

        return supplyAsync(() -> {

            var current = state.get();

            LOGGER.debug("Start to pre-load all plugins.");

            var futures = Array.optionals(
                CompletableFuture.class,
                getEmbeddedPluginsPath().map(path -> loadPlugins(path, executor, true)),
                getInstallationPluginsPath().map(path -> loadPlugins(path, executor, false))
            );

            var containers = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(Array::stream)
                .collect(toArray(PluginContainer.class));

            var idToContainer = containers.stream()
                .collect(toObjectDictionary(PluginContainer::getId, container -> container));

            if (state.compareAndSet(current, new State(containers, idToContainer, ObjectDictionary.empty()))) {
                LOGGER.debug(containers, arg -> "Pre-loaded: " + arg);
                LOGGER.debug("All plugins were pre-loaded.");
                return this;
            }

            return null;

        }, executor);
    }

    @Override
    public @NotNull CompletableFuture<ConfigurablePluginSystem> initialize() {
        return initialize(ForkJoinPool.commonPool());
    }

    @Override
    public @NotNull CompletableFuture<ConfigurablePluginSystem> initialize(@NotNull Executor executor) {

        if (!initialized.compareAndSet(false, true)) {
            throw new PluginException("This system was already initialized.");
        }

        LOGGER.debug("Start to load all plugins.");

        return supplyAsync(() -> {

            var current = state.get();
            var plugins = current.containers
                .stream()
                .map(pluginContainer -> createPluginClass(pluginContainer, executor))
                .map(future -> future.thenApply(this::initializePlugin))
                .map(CompletableFuture::join)
                .collect(toObjectDictionary(Plugin::getId, plugin -> plugin));

            if (state.compareAndSet(current, new State(current.containers, current.idToContainer, plugins))) {
                LOGGER.debug("All plugins were initialized.");
                return this;
            }

            return null;

        }, executor);
    }

    /**
     * Create a plugin's class.
     *
     * @param container the plugin's container.
     * @param executor the executor.
     * @return the plugin's class.
     */
    private @NotNull CompletableFuture<Plugin> createPluginClass(
        @NotNull PluginContainer container,
        @NotNull Executor executor
    ) {

        return supplyAsync(() -> {

            var pluginClass = container.getPluginClass();

            LOGGER.debug(pluginClass, arg -> "Start to create a plugin " + arg);

            var constructor = ClassUtils.<Plugin>tryGetConstructor(pluginClass, PluginContainer.class);

            if (constructor == null) {
                throw new InitializePluginException(
                    "Not found base constructor in the class " + pluginClass,
                    container.getPath()
                );
            }

            Plugin plugin;
            try {
                plugin = ClassUtils.newInstance(constructor, container);
            } catch (Throwable exc) {
                throw new InitializePluginException(
                    "Found a problem with creating a plugin " + pluginClass,
                    container.getPath(),
                    exc
                );
            }

            return plugin;

        }, executor);
    }

    /**
     * Initialize the plugin.
     *
     * @param plugin the plugin.
     * @return the initialized plugin.
     */
    private @NotNull Plugin initializePlugin(@NotNull Plugin plugin) {

        try {
            plugin.initialize(this);
        } catch (Throwable exc) {
            var container = notNull(getPluginContainer(plugin.getId()));
            throw new InitializePluginException(
                "Found a problem with initializing a plugin " + plugin,
                container.getPath(),
                exc
            );
        }

        return plugin;
    }

    /**
     * Load plugins from the directory.
     *
     * @param path     the path to the directory.
     * @param executor the executor.
     * @param embedded true if this directory with embedded plugins.
     * @return the future of the loading plugins.
     */
    protected @NotNull CompletableFuture<@NotNull Array<PluginContainer>> loadPlugins(
        @NotNull Path path,
        @NotNull Executor executor,
        boolean embedded
    ) {

        LOGGER.debug(path, arg -> "Try to pre-load plugins from the folder " + arg);

        return supplyAsync(() -> FileUtils.stream(path)
            .filter(Files::isDirectory)
            .map(directory -> loadPlugin(directory, baseLoader, executor, embedded))
            .map(CompletableFuture::join)
            .filter(Objects::nonNull)
            .collect(toArray(PluginContainer.class)), executor);
    }

    /**
     * Load a plugin from the directory.
     *
     * @param directory    the plugin directory.
     * @param parentLoader the parent class loader.
     * @param executor     the executor.
     * @param embedded     the embedded flag.
     * @return the future of loaded plugin container.
     */
    protected @NotNull CompletableFuture<@Nullable PluginContainer> loadPlugin(
        @NotNull Path directory,
        @NotNull ClassLoader parentLoader,
        @NotNull Executor executor,
        boolean embedded
    ) {
        return loadPlugin(directory, parentLoader, null, executor, embedded);
    }

    /**
     * Load a plugin from the directory.
     *
     * @param directory    the plugin directory.
     * @param parentLoader the parent class loader.
     * @param parents      the parent plugins.
     * @param executor     the executor.
     * @param embedded     the embedded flag.
     * @return the future of loading plugin container.
     */
    protected @NotNull CompletableFuture<@Nullable PluginContainer> loadPlugin(
        @NotNull Path directory,
        @NotNull ClassLoader parentLoader,
        @Nullable PluginContainer[] parents,
        @NotNull Executor executor,
        boolean embedded
    ) {
        return supplyAsync(() -> {

            var files = FileUtils.getFiles(directory, ".jar");

            var urls = files.stream()
                .map(path -> Utils.uncheckedGet(path, Path::toUri))
                .map(uri -> Utils.uncheckedGet(uri, URI::toURL))
                .toArray(URL[]::new);

            var additionalPaths = files.stream()
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .toArray(String[]::new);

            var classLoader = new URLClassLoader(urls);

            var scanner = ClassPathScannerFactory.newDefaultScanner(classLoader, additionalPaths);
            scanner.setUseSystemClasspath(false);

            try {
                scanner.scan();
            } catch (Throwable exc) {
                LOGGER.warning(exc);
                return null;
            }

            var pluginImplementations = ArrayFactory.<Class<Plugin>>newArray(Class.class);
            scanner.findImplements(pluginImplementations, Plugin.class);

            if (pluginImplementations.isEmpty()) {
                LOGGER.warning(directory, arg -> "Can't load the plugin from the directory" + arg +
                    " because can't find any implementation of the plugin interface.");
                return null;
            } else if (pluginImplementations.size() > 1) {
                LOGGER.warning(directory, arg -> "Can't load the plugin from the directory" + arg +
                    " because found more than 1 implementation of the plugin interfaces.");
                return null;
            }

            var pluginClass = notNull(pluginImplementations.first());
            var description = pluginClass.getAnnotation(PluginDescription.class);

            if (description == null) {
                LOGGER.warning(directory, arg -> "Can't load the plugin from the directory" + arg +
                    " because can't find any plugin class with description.");
                return null;
            }

            var appVersion = getAppVersion();

            if (appVersion != null) {

                var minVersion = new Version(description.minAppVersion());

                if (minVersion.compareTo(appVersion) > 0) {
                    LOGGER.warning(description, arg -> "Can't load the plugin " + arg.id() +
                        ", it requires the app version " + arg.minAppVersion() + " or higher.");
                    return null;
                }
            }

            return new PluginContainer(pluginClass, classLoader, scanner, directory, embedded);

        }, executor);
    }

    /**
     * Get the application version.
     *
     * @return the application version or null.
     */
    private @Nullable Version getAppVersion() {
        return appVersion;
    }

    /**
     * Get the path to folder with embedded plugins.
     *
     * @return the path to folder with embedded plugins.
     */
    protected @NotNull Optional<Path> getEmbeddedPluginsPath() {
        return Optional.ofNullable(embeddedPluginsPath);
    }

    /**
     * Get the path to folder with installed plugins.
     *
     * @return the path to folder with installed plugins.
     */
    protected @NotNull Optional<Path> getInstallationPluginsPath() {
        return Optional.ofNullable(installationPluginsPath);
    }

    @Override
    public void configureEmbeddedPluginPath(@NotNull Path embeddedPluginPath) {
        if (isInitialized()) throw new RuntimeException("The plugin system is already initialized.");
        this.embeddedPluginsPath = embeddedPluginPath;
    }

    @Override
    public void configureInstallationPluginsPath(@NotNull Path installationPluginsPath) {
        if (isInitialized()) throw new RuntimeException("The plugin system is already initialized.");
        this.installationPluginsPath = installationPluginsPath;
    }

    /**
     * Return true if this system was pre-loaded.
     *
     * @return true if this system was pre-loaded.
     */
    protected boolean isPreLoaded() {
        return preLoaded.get();
    }

    /**
     * Return true if this system was initialized.
     *
     * @return true if this system was initialized.
     */
    protected boolean isInitialized() {
        return initialized.get();
    }

    @Override
    public @NotNull Array<PluginContainer> getPluginContainers() {
        return state.get()
            .idToContainer
            .values(PluginContainer.class);
    }

    @Override
    public @Nullable PluginContainer getPluginContainer(@NotNull String id) {
        return state.get()
            .idToContainer
            .get(id);
    }

    @Override
    public @NotNull Array<Plugin> getPlugins() {
        return state.get()
            .idToPlugin
            .values(Plugin.class);
    }

    @Override
    public @Nullable Plugin getPlugin(@NotNull String id) {
        return state.get()
            .idToPlugin
            .get(id);
    }

    @Override
    public @Nullable Plugin installPlugin(@NotNull Path file, boolean needInitialize) {

        var installPath = getInstallationPluginsPath()
            .filter(path -> Files.exists(path))
            .orElse(null);

        if (installPath == null) {
            throw new PluginException("The installation folder " + getInstallationPluginsPath() + " doesn't exists.");
        }

        var current = state.get();
        var folderName = FileUtils.getNameWithoutExtension(file);
        var pluginFolder = installPath.resolve(folderName);

        if (Files.exists(pluginFolder)) {
            FileUtils.delete(pluginFolder);
        }

        FileUtils.createDirectories(pluginFolder);
        FileUtils.unzip(pluginFolder, file);

        var container = loadPlugin(pluginFolder, baseLoader, ForkJoinPool.commonPool(), false)
            .join();

        if (container == null) {
            return null;
        }

        var existsContainer = getPluginContainer(container.getId());

        if (existsContainer != null && !pluginFolder.equals(existsContainer.getPath())) {
            FileUtils.delete(existsContainer.getPath());
        }

        var pluginClass = container.getPluginClass();
        var constructor = ClassUtils.<Plugin>tryGetConstructor(pluginClass, PluginContainer.class);

        if (constructor == null) {
            throw new InitializePluginException(
                "Not found base constructor in the class " + pluginClass,
                container.getPath()
            );
        }

        Plugin plugin;
        try {
            plugin = ClassUtils.newInstance(constructor, container);
        } catch (Exception exc) {
            throw new InitializePluginException(
                "Found a problem with creating a plugin " + pluginClass,
                container.getPath(),
                exc
            );
        }

        if (needInitialize) {
            try {
                plugin.initialize(this);
            } catch (Exception exc) {
                throw new InitializePluginException(
                    "Found a problem with initializing a plugin " + plugin,
                    container.getPath(),
                    exc
                );
            }
        }

        var idToContainer = ObjectDictionary.ofType(String.class, PluginContainer.class);
        idToContainer.put(current.idToContainer);
        idToContainer.put(existsContainer.getId(), existsContainer);

        var idToPlugin = ObjectDictionary.ofType(String.class, Plugin.class);
        idToPlugin.put(current.idToPlugin);
        idToPlugin.put(plugin.getId(), plugin);

        var containers = Array.ofType(PluginContainer.class);
        containers.addAll(current.containers);
        containers.add(existsContainer);

        if (state.compareAndSet(current, new State(containers, idToContainer, idToPlugin))) {
            return plugin;
        }

        return installPlugin(file, needInitialize);
    }

    @Override
    public void removePlugin(@NotNull Plugin plugin) {

        var current = state.get();

        var pluginId = plugin.getId();
        var pluginContainer = notNull(current.idToContainer.get(pluginId));

        var containers = Array.ofType(PluginContainer.class);
        containers.addAll(current.containers);
        containers.fastRemove(pluginContainer);

        var idToContainer = ObjectDictionary.ofType(String.class, PluginContainer.class);
        idToContainer.put(current.idToContainer);
        idToContainer.remove(pluginId);

        var idToPlugin = ObjectDictionary.ofType(String.class, Plugin.class);
        idToPlugin.put(current.idToPlugin);
        idToPlugin.remove(pluginId);

        containers.addAll(current.containers);
        containers.fastRemove(pluginContainer);

        if (state.compareAndSet(current, new State(containers, idToContainer, idToPlugin))) {
            FileUtils.delete(pluginContainer.getPath());
        }
    }
}
