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
            return 2;
        }

        @Override
        public @NotNull String getGroupName() {
            return "ServerNetworkThread";
        }
    };

    default int getGroupSize() {
        return 1;
    }

    default int getGroupMaxSize() {
        return getGroupSize();
    }

    default @NotNull GroupThreadFactory.ThreadConstructor getThreadConstructor() {
        return Thread::new;
    }

    default int getThreadPriority() {
        return Thread.NORM_PRIORITY;
    }
}
