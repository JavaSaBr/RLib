package javasabr.rlib.plugin.system;

import java.net.URLClassLoader;
import java.nio.file.Path;
import javasabr.rlib.classpath.ClassPathScanner;
import javasabr.rlib.plugin.system.annotation.PluginDescription;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * Container for a plugin that holds plugin metadata and resources.
 *
 * @since 10.0.0
 */
@Getter
@Accessors(fluent = true)
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class PluginContainer {

  Class<Plugin> pluginClass;
  URLClassLoader classLoader;
  ClassPathScanner scanner;
  Path path;
  String id;
  String name;
  String description;
  Version version;
  boolean embedded;

  /**
   * Creates a new plugin container.
   *
   * @param pluginClass the plugin class
   * @param classLoader the class loader for the plugin
   * @param scanner the classpath scanner for the plugin
   * @param path the path to the plugin file
   * @param embedded true if the plugin is embedded
   * @since 10.0.0
   */
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

  @Override
  public String toString() {
    return "PluginContainer{" + "pluginClass=" + pluginClass + ", path=" + path + '}';
  }
}
