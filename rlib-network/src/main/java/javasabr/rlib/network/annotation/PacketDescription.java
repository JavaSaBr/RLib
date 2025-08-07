package javasabr.rlib.network.annotation;

import java.lang.annotation.*;

/**
 * The annotation to describe a network packet.
 *
 * @author JavaSaBr
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PacketDescription {

    int id();
}
