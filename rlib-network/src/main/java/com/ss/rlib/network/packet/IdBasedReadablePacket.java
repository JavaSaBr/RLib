package com.ss.rlib.network.packet;

import com.ss.rlib.common.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
public interface IdBasedReadablePacket<S extends IdBasedReadablePacket<S>> extends ReadablePacket, IdBasedPacket {

    /**
     * Create a new instance of this type.
     *
     * @return the new instance of this type.
     */
    default @NotNull S newInstance() {
        return ClassUtils.newInstance(getClass());
    }
}
