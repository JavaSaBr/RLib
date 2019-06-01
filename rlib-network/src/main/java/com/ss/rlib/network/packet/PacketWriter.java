package com.ss.rlib.network.packet;

public interface PacketWriter {

    void writeNextPacket();

    /**
     * Close all used resources.
     */
    void close();
}
