package com.ss.rlib.network;

import com.ss.rlib.common.concurrent.GroupThreadFactory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

/**
 * The interface to implement a server network config.
 *
 * @author JavaSaBr
 */
public interface ServerNetworkConfig extends NetworkConfig {

    @NotNull ServerNetworkConfig DEFAULT_SERVER = new ServerNetworkConfig() {

        @Override
        public int getGroupSize() {
            return 1;
        }

        @Override
        public @NotNull String getGroupName() {
            return "ServerNetworkThread";
        }
    };

    /**
     * Get a minimal size of network thread executor.
     *
     * @return the minimal executor size.
     */
    default int getGroupSize() {
        return 1;
    }

    /**
     * Get a maximum size of network thread executor.
     *
     * @return the maximum executor size.
     */
    default int getGroupMaxSize() {
        return getGroupSize();
    }

    /**
     * Get a thread constructor which should be used to create network threads.
     *
     * @return the thread constructor.
     */
    default @NotNull GroupThreadFactory.ThreadConstructor getThreadConstructor() {
        return Thread::new;
    }

    /**
     * Get a priority of network threads.
     *
     * @return the priority of network threads.
     */
    default int getThreadPriority() {
        return Thread.NORM_PRIORITY;
    }
}
