package com.ss.rlib.common.network.annotation;

import java.lang.annotation.*;

/**
 * The annotation to describe a network packet.
 *
 * @author JavaSaBR
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PacketDescription {

    /**
     * Get the packet id.
     *
     * @return the packet id.
     */
    int id();
}
