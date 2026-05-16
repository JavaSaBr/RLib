package javasabr.rlib.plugin.system.impl;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static javasabr.rlib.common.util.ObjectUtils.notNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javasabr.rlib.classpath.ClassPathScannerFactory;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayCollectors;
import javasabr.rlib.collections.dictionary.DictionaryCollectors;
import javasabr.rlib.collections.dictionary.MutableRefToRefDictionary;
import javasabr.rlib.collections.dictionary.RefToRefDictionary;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.common.util.Utils;
import javasabr.rlib.io.util.FileUtils;
import javasabr.rlib.plugin.system.ConfigurablePluginSystem;
import javasabr.rlib.plugin.system.Plugin;
import javasabr.rlib.plugin.system.PluginContainer;
import javasabr.rlib.plugin.system.Version;
import javasabr.rlib.plugin.system.annotation.PluginDescription;
import javasabr.rlib.plugin.system.exception.InitializePluginException;
import javasabr.rlib.plugin.system.exception.PluginException;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@CustomLog
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BasePluginSystem implements ConfigurablePluginSystem {

    protected record State(
      Array<PluginContainer> containers,
      Array<Plugin> plugins,
      RefToRefDictionary<String, PluginContainer> idToContainer,
      RefToRefDictionary<String, Plugin> idToPlugin) {

      static final State EMPTY = new State(
          Array.empty(PluginContainer.class),
          Array.empty(Plugin.class),
          RefToRefDictionary.empty(),
          RefToRefDictionary.empty());
  }

  final ClassLoader baseLoader;
  final AtomicBoolean preLoaded;
  final AtomicBoolean initialized;
  final AtomicReference<State> state;

  @Getter
  volatile @Nullable Version appVersion;
  volatile @Nullable Path embeddedPluginsPath;
  volatile @Nullable Path installationPluginsPath;

  public BasePluginSystem() {
    this(StackWalker
        .getInstance(RETAIN_CLASS_REFERENCE)
        .getCallerClass()
        .getClassLoader());
  }

  public BasePluginSystem(ClassLoader baseLoader) {
    this.baseLoader = baseLoader;
    this.state = new AtomicReference<>(State.EMPTY);
    this.preLoaded = new AtomicBoolean(false);
    this.initialized = new AtomicBoolean(false);
  }

  @Override
  public void configureAppVersion(@Nullable Version appVersion) {
    this.appVersion = appVersion;
  }

  @Override
  public CompletionStage<ConfigurablePluginSystem> preLoad() {
    return preLoad(ForkJoinPool.commonPool());
  }

  @Override
  public CompletionStage<ConfigurablePluginSystem> preLoad(Executor executor) {

    if (!preLoaded.compareAndSet(false, true)) {
      throw new PluginException("This system was already pre-loaded");
    }

    return supplyAsync(() -> preLoadImpl(executor), executor);
  }

  protected BasePluginSystem preLoadImpl(Executor executor) {
    log.debug("Start pre-loading all plugins...");

    State current = state.get();
    Array<CompletionStage<Array<PluginContainer>>> futures = Array.optionals(
        CompletionStage.class,
        embeddedPluginsPathOptional().map(path -> loadPlugins(path, executor, true)),
        installationPluginsPathOptional().map(path -> loadPlugins(path, executor, false)));

    Array<PluginContainer> containers = futures
        .stream()
        .map(CompletionStage::toCompletableFuture)
        .map(CompletableFuture::join)
        .flatMap(Array::stream)
        .collect(ArrayCollectors.toArray(PluginContainer.class));

    var idToContainer = containers
        .stream()
        .collect(DictionaryCollectors.toRefToRefDictionary(PluginContainer::id));

    State newState = new State(
        containers,
        Array.empty(Plugin.class),
        idToContainer,
        RefToRefDictionary.empty());

    if (state.compareAndSet(current, newState)) {
      log.debug(containers, "Pre-loaded:%s"::formatted);
      log.debug("All plugins were pre-loaded");
      return this;
    }

    throw new PluginException("This system was already pre-loaded.");
  }

  @Override
  public CompletionStage<ConfigurablePluginSystem> initialize() {
    return initialize(ForkJoinPool.commonPool());
  }

  @Override
  public CompletionStage<ConfigurablePluginSystem> initialize(Executor executor) {

    if (!initialized.compareAndSet(false, true)) {
      throw new PluginException("This system was already initialized.");
    }

    return supplyAsync(() -> initializeImpl(executor), executor);
  }

  protected BasePluginSystem initializeImpl(Executor executor) {
    log.debug("Start loading all plugins...");

    var current = state.get();
    var plugins = current.containers
        .stream()
        .map(pluginContainer -> createPluginClass(pluginContainer, executor))
        .map(future -> future.thenApply(this::initializePlugin))
        .map(CompletionStage::toCompletableFuture)
        .map(CompletableFuture::join)
        .collect(DictionaryCollectors.toRefToRefDictionary(Plugin::id));

    State newState = new State(
        current.containers,
        Array.typed(Plugin.class, plugins.values(Plugin.class).toArray()),
        current.idToContainer,
        plugins);

    if (state.compareAndSet(current, newState)) {
      log.debug("All plugins were initialized");
      return this;
    }

    throw new PluginException("This system was already initialized.");
  }

  protected CompletionStage<Plugin> createPluginClass(PluginContainer container, Executor executor) {
    return supplyAsync(() -> createPluginClassImpl(container), executor);
  }

  protected Plugin createPluginClassImpl(PluginContainer container) {
    var pluginClass = container.pluginClass();

    log.debug(pluginClass, "Start creating plugin:[%s]"::formatted);

    Constructor<Plugin> constructor = ClassUtils.tryGetConstructor(pluginClass, PluginContainer.class);
    if (constructor == null) {
      throw new InitializePluginException(
          "Not found base constructor in class:[%s]".formatted(pluginClass),
          container.path());
    }

    Plugin plugin;
    try {
      plugin = ClassUtils.newInstance(constructor, container);
    } catch (Throwable throwable) {
      throw new InitializePluginException(
          "Found problem with creating plugin:[%s]".formatted(pluginClass),
          container.path(),
          throwable);
    }

    return plugin;
  }

  protected Plugin initializePlugin(Plugin plugin) {

    try {
      plugin.initialize(this);
    } catch (Throwable throwable) {
      var container = notNull(getPluginContainer(plugin.id()));
      throw new InitializePluginException(
          "Found problem with initializing plugin:[%s]".formatted(plugin.id()),
          container.path(),
          throwable);
    }

    return plugin;
  }

  protected CompletionStage<Array<PluginContainer>> loadPlugins(
      Path path,
      Executor executor,
      boolean embedded) {

    Path realPath;
    try {
      realPath = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    log.debug(realPath, "Try to pre-load plugins from the folder:[%s]"::formatted);
    return supplyAsync(
        () -> FileUtils
            .stream(realPath)
            .filter(Files::isDirectory)
            .map(directory -> loadPlugin(directory, baseLoader, executor, embedded))
            .map(CompletionStage::toCompletableFuture)
            .map(CompletableFuture::join)
            .filter(Objects::nonNull)
            .collect(ArrayCollectors.toArray(PluginContainer.class)), executor);
  }

  protected CompletionStage<@Nullable PluginContainer> loadPlugin(
      Path directory,
      ClassLoader parentLoader,
      Executor executor,
      boolean embedded) {
    return loadPlugin(directory, parentLoader, null, executor, embedded);
  }

  protected CompletionStage<@Nullable PluginContainer> loadPlugin(
      Path directory,
      ClassLoader parentLoader,
      PluginContainer @Nullable [] parents,
      Executor executor,
      boolean embedded) {
    return supplyAsync(() -> loadPluginImpl(directory, parentLoader, parents, embedded), executor);
  }

  @Nullable
  protected PluginContainer loadPluginImpl(
      Path directory,
      ClassLoader parentLoader,
      PluginContainer @Nullable [] parents,
      boolean embedded) {

    Array<Path> files = FileUtils.getFiles(directory, ".jar");
    URL[] urls = files
        .stream()
        .map(path -> Utils.uncheckedGet(path, Path::toUri))
        .map(uri -> Utils.uncheckedGet(uri, URI::toURL))
        .toArray(URL[]::new);
    String[] additionalPaths = files
        .stream()
        .map(Path::toAbsolutePath)
        .map(Path::toString)
        .toArray(String[]::new);

    var classLoader = new URLClassLoader(urls);
    var scanner = ClassPathScannerFactory.newDefaultScanner(classLoader, additionalPaths);
    scanner.useSystemClassPath(false);
    try {
      scanner.scan();
    } catch (Throwable throwable) {
      log.warn(throwable);
      return null;
    }

    Array<Class<Plugin>> pluginImplementations = scanner.findImplementations(Plugin.class);
    if (pluginImplementations.isEmpty()) {
      log.warn(
          directory,
          "Can't load plugin from directory:[%s] because can't find any implementation of plugin interface"::formatted);
      return null;
    } else if (pluginImplementations.size() > 1) {
      log.warn(
          directory,
          "Can't load plugin from directory:[%s] because found more than 1 implementation of plugin interface"::formatted);
      return null;
    }

    Class<Plugin> pluginClass = notNull(pluginImplementations.first());
    PluginDescription description = pluginClass.getAnnotation(PluginDescription.class);
    if (description == null) {
      log.warn(
          directory,
          pluginClass,
          "Can't load plugin from directory:[%s] because can't find description on class:[%s]"::formatted);
      return null;
    }

    Version appVersion = appVersion();
    if (appVersion != null) {
      var minVersion = new Version(description.minAppVersion());
      if (minVersion.compareTo(appVersion) > 0) {
        log.warn(
            description.id(),
            description.minAppVersion(),
            "Can't load plugin:[%s] because it requires minimum app version:[%s]"::formatted);
        return null;
      }
    }

    return new PluginContainer(pluginClass, classLoader, scanner, directory, embedded);
  }

  protected Optional<Path> embeddedPluginsPathOptional() {
    return Optional.ofNullable(embeddedPluginsPath);
  }

  protected Optional<Path> installationPluginsPathOptional() {
    return Optional.ofNullable(installationPluginsPath);
  }

  @Override
  public void configureEmbeddedPluginPath(Path embeddedPluginPath) {
    if (isInitialized()) {
      throw new RuntimeException("The plugin system is already initialized.");
    }
    this.embeddedPluginsPath = embeddedPluginPath;
  }

  @Override
  public void configureInstallationPluginsPath(Path installationPluginsPath) {
    if (isInitialized()) {
      throw new RuntimeException("The plugin system is already initialized.");
    }
    this.installationPluginsPath = installationPluginsPath;
  }

  protected boolean isPreLoaded() {
    return preLoaded.get();
  }

  protected boolean isInitialized() {
    return initialized.get();
  }

  @Override
  public Array<PluginContainer> pluginContainers() {
    return state.get().containers;
  }
  @Nullable
  @Override
  public PluginContainer getPluginContainer(String id) {
    return state.get().idToContainer.get(id);
  }

  @Override
  public Array<Plugin> plugins() {
    return state.get().plugins;
  }

  @Nullable
  @Override
  public Plugin getPlugin(String id) {
    return state.get().idToPlugin.get(id);
  }

  @Nullable
  @Override
  public Plugin installPlugin(Path file, boolean needInitialize) {

    var installPath = installationPluginsPathOptional()
        .filter(Files::exists)
        .orElse(null);

    if (installPath == null) {
      throw new PluginException("Installation folder:[%s] doesn't exists".formatted(installationPluginsPathOptional()));
    }

    State current = state.get();
    String folderName = FileUtils.getNameWithoutExtension(file);
    Path pluginFolder = installPath.resolve(folderName);

    if (Files.exists(pluginFolder)) {
      FileUtils.delete(pluginFolder);
    }

    FileUtils.createDirectories(pluginFolder);
    FileUtils.unzip(pluginFolder, file);

    var container = loadPlugin(pluginFolder, baseLoader, ForkJoinPool.commonPool(), false)
        .toCompletableFuture()
        .join();

    if (container == null) {
      return null;
    }

    var existsContainer = getPluginContainer(container.id());
    if (existsContainer != null && !pluginFolder.equals(existsContainer.path())) {
      FileUtils.delete(existsContainer.path());
    }

    Class<Plugin> pluginClass = container.pluginClass();
    Constructor<Plugin> constructor = ClassUtils.tryGetConstructor(pluginClass, PluginContainer.class);
    if (constructor == null) {
      throw new InitializePluginException(
          "Not found base constructor in class:[%s]".formatted(pluginClass),
          container.path());
    }

    Plugin plugin;
    try {
      plugin = ClassUtils.newInstance(constructor, container);
    } catch (Exception exception) {
      throw new InitializePluginException(
          "Found problem with creating plugin:[%s]".formatted(pluginClass),
          container.path(),
          exception);
    }

    if (needInitialize) {
      try {
        plugin.initialize(this);
      } catch (Exception exception) {
        throw new InitializePluginException(
            "Found problem with initializing plugin:[%s]".formatted(plugin),
            container.path(),
            exception);
      }
    }

    var idToContainer = MutableRefToRefDictionary.ofTypes(String.class, PluginContainer.class);
    idToContainer.putAll(current.idToContainer);
    idToContainer.put(container.id(), container);

    var idToPlugin = MutableRefToRefDictionary.ofTypes(String.class, Plugin.class);
    idToPlugin.putAll(current.idToPlugin);
    idToPlugin.put(plugin.id(), plugin);

    State newState = new State(
        idToContainer.values(PluginContainer.class),
        idToPlugin.values(Plugin.class),
        idToContainer,
        idToPlugin);

    if (state.compareAndSet(current, newState)) {
      return plugin;
    }

    log.warn("Detected concurrent attempt to install plugin:[%s], trying again...".formatted(pluginClass));
    return installPlugin(file, needInitialize);
  }

  @Override
  public boolean removePlugin(Plugin plugin) {

    State current = state.get();
    String pluginId = plugin.id();
    PluginContainer pluginContainer = current.idToContainer.get(pluginId);
    if (pluginContainer == null) {
      log.warn("Plugin:[%s] is already removed".formatted(plugin.name()));
      return false;
    }

    var idToContainer = MutableRefToRefDictionary.ofTypes(String.class, PluginContainer.class);
    idToContainer.putAll(current.idToContainer);
    idToContainer.remove(pluginId);

    var idToPlugin = MutableRefToRefDictionary.ofTypes(String.class, Plugin.class);
    idToPlugin.putAll(current.idToPlugin);
    idToPlugin.remove(pluginId);

    State newState = new State(
        idToContainer.values(PluginContainer.class),
        idToPlugin.values(Plugin.class),
        idToContainer,
        idToPlugin);

    if (state.compareAndSet(current, newState)) {
      FileUtils.delete(pluginContainer.path());
      return true;
    }

    log.warn("Detected concurrent attempt to remove plugin:[%s], trying again...".formatted(plugin.name()));
    return removePlugin(plugin);
  }
}
