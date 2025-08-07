package javasabr.rlib.common.plugin.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * The annotation to describe a plugin.
 *
 * @author JavaSaBr
 */
@NullMarked
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
public @interface PluginDescription {

  /**
   * Get the plugin id.
   *
   * @return the plugin id.
   */
  String id() default "unknown";

  /**
   * Get dependencies of this plugin.
   *
   * @return dependencies of this plugin.
   */
  String[] dependences() default {};

  /**
   * Get the plugin version.
   *
   * @return the plugin version.
   */
  String version() default "0.1.0";

  /**
   * Get the min version of application.
   *
   * @return the min version of application.
   */
  String minAppVersion() default "0.0.0";

  /**
   * Get the plugin name.
   *
   * @return the plugin name.
   */
  String name() default "no name";

  /**
   * Get the plugin description.
   *
   * @return the plugin description.
   */
  String description() default "no description";
}
