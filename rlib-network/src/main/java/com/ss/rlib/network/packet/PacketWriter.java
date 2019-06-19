package com.ss.rlib.network.packet;

/**
 * @author JavaSaBr
 */
public interface PacketWriter {

    void writeNextPacket();

    /**
     * Close all used resources.
     */
    void close();
}
