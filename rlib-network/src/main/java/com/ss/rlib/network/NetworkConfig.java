package com.ss.rlib.network;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a network config.
 *
 * @author JavaSaBr
 */
public interface NetworkConfig {

    @NotNull NetworkConfig DEFAULT_SERVER = new NetworkConfig() {

        @Override
        public int getGroupSize() {
            return 2;
        }

        @Override
        public @NotNull String getGroupName() {
            return "ServerNetworkThread";
        }
    };

    @NotNull NetworkConfig DEFAULT_CLIENT = new NetworkConfig() {

        @Override
        public int getGroupSize() {
            return 1;
        }

        @Override
        public @NotNull String getGroupName() {
            return "ClientNetworkThread";
        }
    };

    default @NotNull String getGroupName() {
        return "NetworkThread";
    }

    default int getGroupSize() {
        return 1;
    }

    default int getReadBufferSize() {
        return 2048;
    }

    default boolean isDirectByteBuffer() {
        return false;
    }

    default @NotNull Class<? extends Thread> getThreadClass() {
        return Thread.class;
    }

    default int getThreadPriority() {
        return Thread.NORM_PRIORITY;
    }

    default int getWriteBufferSize() {
        return 2048;
    }

    default boolean isVisibleReadException() {
        return false;
    }

    default boolean isVisibleWriteException() {
        return false;
    }
}
