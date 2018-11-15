package com.ss.rlib.common.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * The interface to implement a reusable streams.
 *
 * @author JavaSaBr
 */
public interface ReusableStream {

    /**
     * Reset this stream.
     *
     * @throws IOException the io exception
     */
    void reset() throws IOException;

    /**
     * Initialize this string using the buffer.
     *
     * @param buffer the buffer data.
     * @param offset the offset.
     * @param length the length.
     */
    void initFor(@NotNull byte[] buffer, int offset, int length);
}
