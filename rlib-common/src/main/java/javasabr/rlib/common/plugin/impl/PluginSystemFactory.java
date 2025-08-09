package javasabr.rlib.common.plugin.impl;

import javasabr.rlib.common.plugin.ConfigurablePluginSystem;
import org.jspecify.annotations.NullMarked;

/**
 * The factory of different plugin systems.
 *
 * @author JavaSaBr
 */
@NullMarked
public class PluginSystemFactory {

  public static ConfigurablePluginSystem newBasePluginSystem() {
    return new BasePluginSystem();
  }

  public static ConfigurablePluginSystem newBasePluginSystem(ClassLoader classLoader) {
    return new BasePluginSystem(classLoader);
  }
}
