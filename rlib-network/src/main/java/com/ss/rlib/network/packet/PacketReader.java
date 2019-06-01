package com.ss.rlib.network.packet;

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
