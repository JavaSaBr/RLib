package javasabr.rlib.common.plugin;

import java.net.URLClassLoader;
import java.nio.file.Path;
import javasabr.rlib.common.classpath.ClassPathScanner;
import javasabr.rlib.common.plugin.annotation.PluginDescription;
import org.jspecify.annotations.NullMarked;

/**
 * The implementation of a plugin container.
 *
 * @author JavaSaBr
 */
@NullMarked
public class PluginContainer {

  /**
   * The plugin class.
   */
  private final Class<Plugin> pluginClass;

  /**
   * The class loader of this class.
   */
  private final URLClassLoader classLoader;

  /**
   * The classpath scanner of this plugin.
   */
  private final ClassPathScanner scanner;

  /**
   * The path to a plugin folder.
   */
  private final Path path;

  /**
   * The plugin id.
   */
  private final String id;

  /**
   * The name.
   */
  private final String name;

  /**
   * The description.
   */
  private final String description;

  /**
   * The version.
   */
  private final Version version;

  /**
   * The flag of that this container is of an embedded plugin.
   */
  private final boolean embedded;

  public PluginContainer(
      Class<Plugin> pluginClass,
      URLClassLoader classLoader,
      ClassPathScanner scanner,
      Path path,
      boolean embedded) {
    PluginDescription description = pluginClass.getAnnotation(PluginDescription.class);
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

  public Class<Plugin> getPluginClass() {
    return pluginClass;
  }

  /**
   * Gets the ID of this plugin.
   *
   * @return the ID.
   */
  public String getId() {
    return id;
  }

  /**
   * Get the version of this plugin.
   *
   * @return the plugin version.
   */
  public Version getVersion() {
    return version;
  }

  /**
   * Gets a name of this plugin.
   *
   * @return the name of this plugin.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets a description of this plugin.
   *
   * @return the description of this plugin.
   */
  public String getDescription() {
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
  public ClassPathScanner getScanner() {
    return scanner;
  }

  /**
   * Get the class loader of this plugin.
   *
   * @return the class loader.
   */
  public URLClassLoader getClassLoader() {
    return classLoader;
  }

  public Path getPath() {
    return path;
  }

  @Override
  public String toString() {
    return "PluginContainer{" + "pluginClass=" + pluginClass + ", path=" + path + '}';
  }
}
