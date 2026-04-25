package javasabr.rlib.plugin.system.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to describe a plugin's metadata.
 *
 * @since 10.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
public @interface PluginDescription {

  /**
   * Returns the unique plugin identifier.
   *
   * @return the plugin id
   * @since 10.0.0
   */
  String id() default "unknown";

  /**
   * Returns the dependencies of this plugin.
   *
   * @return the array of dependency plugin ids
   * @since 10.0.0
   */
  String[] dependences() default {};

  /**
   * Returns the plugin version.
   *
   * @return the version string
   * @since 10.0.0
   */
  String version() default "0.1.0";

  /**
   * Returns the minimum application version required by this plugin.
   *
   * @return the minimum app version string
   * @since 10.0.0
   */
  String minAppVersion() default "0.0.0";

  /**
   * Returns the display name of this plugin.
   *
   * @return the plugin name
   * @since 10.0.0
   */
  String name() default "no name";

  /**
   * Returns the description of this plugin.
   *
   * @return the plugin description
   * @since 10.0.0
   */
  String description() default "no description";
}
