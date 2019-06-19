package com.ss.rlib.network.packet;

import com.ss.rlib.network.annotation.PacketDescription;

/**
 * @author JavaSaBr
 */
public interface IdBasedPacket extends Packet {

    /**
     * Get id of this packet.
     *
     * @return the packet type's id.
     */
    default int getPacketId() {
        return getClass()
            .getAnnotation(PacketDescription.class)
            .id();
    }
}
