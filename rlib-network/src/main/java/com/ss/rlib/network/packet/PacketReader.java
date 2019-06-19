package com.ss.rlib.network.packet;

/**
 * @author JavaSaBr
 */
public interface PacketReader {

    /**
     * Activate a process of receiving packets.
     */
    void startRead();

    /**
     * Close all used resources.
     */
    void close();
}
