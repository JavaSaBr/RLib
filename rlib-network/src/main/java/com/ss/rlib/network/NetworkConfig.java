package com.ss.rlib.network;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

/**
 * The interface to implement a network config.
 *
 * @author JavaSaBr
 */
public interface NetworkConfig {

    @Builder
    @Getter
    class SimpleNetworkConfig implements NetworkConfig {

        @Builder.Default
        private String groupName = "NetworkThread";
        @Builder.Default
        private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

        @Builder.Default
        private int readBufferSize = 2048;
        @Builder.Default
        private int pendingBufferSize = 4096;
        @Builder.Default
        private int writeBufferSize = 2048;
    }

    @NotNull NetworkConfig DEFAULT_CLIENT = new NetworkConfig() {

        @Override
        public @NotNull String getThreadGroupName() {
            return "ClientNetworkThread";
        }
    };

    /**
     * Get a group name of network threads.
     *
     * @return the group name.
     */
    default @NotNull String getThreadGroupName() {
        return "NetworkThread";
    }

    /**
     * Get size of buffer with used to collect received data from network.
     *
     * @return the read buffer's size.
     */
    default int getReadBufferSize() {
        return 2048;
    }

    /**
     * Get size of buffer with pending data. Pending buffer allows to construct a packet with
     * bigger data than {@link #getReadBufferSize()}. It should be at least 2x of {@link #getReadBufferSize()}
     *
     * @return the pending buffer's size.
     */
    default int getPendingBufferSize() {
        return getReadBufferSize() * 2;
    }

    /**
     * Get size of buffer which used to serialize packets to bytes.
     *
     * @return the write buffer's size.
     */
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
