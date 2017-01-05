package rlib.network;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a network config.
 *
 * @author JavaSaBr
 */
public interface NetworkConfig {

    /**
     * @return thread group name.
     */
    @NotNull
    default String getGroupName() {
        return "NetworkThread";
    }

    /**
     * @return thread group size.
     */
    default int getGroupSize() {
        return 1;
    }

    /**
     * @return the size of read buffers.
     */
    default int getReadBufferSize() {
        return 2048;
    }

    /**
     * @return true if need to use direct buffers.
     */
    default boolean isDirectByteBuffer() {
        return false;
    }

    /**
     * @return the thread class.
     */
    @NotNull
    default Class<? extends Thread> getThreadClass() {
        return Thread.class;
    }

    /**
     * @return the thread priority.
     */
    default int getThreadPriority() {
        return Thread.NORM_PRIORITY;
    }

    /**
     * @return the size of write buffers.
     */
    default int getWriteBufferSize() {
        return 2048;
    }

    /**
     * @return true if need to show read exceptions.
     */
    default boolean isVisibleReadException() {
        return false;
    }

    /**
     * @return true if need to show write exceptions.
     */
    default boolean isVisibleWriteException() {
        return false;
    }
}
