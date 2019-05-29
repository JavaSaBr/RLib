package com.ss.rlib.network;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

/**
 * The interface to implement a network config.
 *
 * @author JavaSaBr
 */
public interface NetworkConfig {

    @NotNull NetworkConfig DEFAULT_CLIENT = new NetworkConfig() {

        @Override
        public @NotNull String getGroupName() {
            return "ClientNetworkThread";
        }
    };

    default @NotNull String getGroupName() {
        return "NetworkThread";
    }

    default int getReadBufferSize() {
        return 2048;
    }

    default int getPendingBufferSize() {
        return getReadBufferSize() * 2;
    }

    default int getWriteBufferSize() {
        return 2048;
    }

    default @NotNull ByteOrder getByteOrder() {
        return ByteOrder.BIG_ENDIAN;
    }

    default boolean isDirectByteBuffer() {
        return false;
    }
}
