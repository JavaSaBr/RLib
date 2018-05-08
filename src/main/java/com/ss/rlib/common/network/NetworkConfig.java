package com.ss.rlib.common.network;

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

    /**
     * Gets group name.
     *
     * @return thread group name.
     */
    default @NotNull String getGroupName() {
        return "NetworkThread";
    }

    /**
     * Gets group size.
     *
     * @return thread group size.
     */
    default int getGroupSize() {
        return 1;
    }

    /**
     * Gets read buffer size.
     *
     * @return the size of read buffers.
     */
    default int getReadBufferSize() {
        return 2048;
    }

    /**
     * Is direct byte buffer boolean.
     *
     * @return true if need to use direct buffers.
     */
    default boolean isDirectByteBuffer() {
        return false;
    }

    /**
     * Gets thread class.
     *
     * @return the thread class.
     */
    default @NotNull Class<? extends Thread> getThreadClass() {
        return Thread.class;
    }

    /**
     * Gets thread priority.
     *
     * @return the thread priority.
     */
    default int getThreadPriority() {
        return Thread.NORM_PRIORITY;
    }

    /**
     * Gets write buffer size.
     *
     * @return the size of write buffers.
     */
    default int getWriteBufferSize() {
        return 2048;
    }

    /**
     * Is visible read exception boolean.
     *
     * @return true if need to show read exceptions.
     */
    default boolean isVisibleReadException() {
        return false;
    }

    /**
     * Is visible write exception boolean.
     *
     * @return true if need to show write exceptions.
     */
    default boolean isVisibleWriteException() {
        return false;
    }
}
