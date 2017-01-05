package rlib.network;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The interface to implement an asynchronous network.
 *
 * @author JavaSaBr
 */
public interface AsynchronousNetwork {

    /**
     * Get a config of this network.
     *
     * @return the config.
     */
    @NotNull
    NetworkConfig getConfig();

    /**
     * Get a new read buffer to use.
     *
     * @return the new buffer.
     */
    @NotNull
    ByteBuffer takeReadBuffer();

    /**
     * Get a new write buffer to use.
     *
     * @return the new buffer.
     */
    @NotNull
    ByteBuffer takeWriteBuffer();

    /**
     * Put an old read buffer.
     *
     * @param buffer the old buffer.
     */
    void putReadBuffer(@NotNull ByteBuffer buffer);

    /**
     * Put an old write buffer.
     *
     * @param buffer the old buffer.
     */
    void putWriteBuffer(@NotNull ByteBuffer buffer);
}
