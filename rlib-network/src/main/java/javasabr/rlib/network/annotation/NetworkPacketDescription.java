package javasabr.rlib.network.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to describe a network packet with its unique identifier.
 *
 * @author JavaSaBr
 * @since 10.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface NetworkPacketDescription {

  /**
   * The unique identifier for this packet type.
   *
   * @return the packet ID
   * @since 10.0.0
   */
  int id();
}
