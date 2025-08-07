package javasabr.rlib.common.plugin.impl;

import javasabr.rlib.common.plugin.Plugin;
import javasabr.rlib.common.plugin.PluginContainer;
import javasabr.rlib.common.plugin.PluginSystem;
import javasabr.rlib.common.plugin.Version;
import org.jspecify.annotations.NullMarked;

/**
 * The base implementation of the {@link Plugin}.
 *
 * @author JavaSaBr
 */
@NullMarked
public class BasePlugin implements Plugin {

  /**
   * The plugin container.
   */
  private final PluginContainer container;

  public BasePlugin(PluginContainer container) {
    this.container = container;
  }

  @Override
  public ClassLoader getClassLoader() {
    return container.getClassLoader();
  }

  @Override
  public boolean isEmbedded() {
    return container.isEmbedded();
  }

  @Override
  public String getId() {
    return container.getId();
  }

  @Override
  public Version getVersion() {
    return container.getVersion();
  }

  @Override
  public String getDescription() {
    return container.getDescription();
  }

  @Override
  public String getName() {
    return container.getName();
  }

  /**
   * The container of this plugin.
   *
   * @return the container.
   */
  protected PluginContainer getContainer() {
    return container;
  }

  @Override
  public void initialize(PluginSystem pluginSystem) {
  }
}
