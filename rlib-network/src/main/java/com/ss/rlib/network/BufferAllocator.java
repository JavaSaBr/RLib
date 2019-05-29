package com.ss.rlib.network;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The interface to implement a buffer allocator for network things.
 *
 * @author JavaSaBr
 */
public interface BufferAllocator {

    /**
     * Get a new read buffer to use.
     *
     * @return the new buffer.
     */
    @NotNull ByteBuffer takeReadBuffer();

    /**
     * Get a new pending buffer to use.
     *
     * @return the new pending buffer.
     */
    @NotNull ByteBuffer takePendingBuffer();

    /**
     * Get a new write buffer to use.
     *
     * @return the new buffer.
     */
    @NotNull ByteBuffer takeWriteBuffer();

    /**
     * Store the old read buffer.
     *
     * @param buffer the old buffer.
     */
    @NotNull BufferAllocator putReadBuffer(@NotNull ByteBuffer buffer);

    /**
     * Store the old pending buffer.
     *
     * @param buffer the old pending buffer.
     */
    @NotNull BufferAllocator putPendingBuffer(@NotNull ByteBuffer buffer);

    /**
     * Store the old write buffer.
     *
     * @param buffer the old buffer.
     */
    @NotNull BufferAllocator putWriteBuffer(@NotNull ByteBuffer buffer);
}
