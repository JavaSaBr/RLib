package com.ss.rlib.network.packet;

import com.ss.rlib.network.annotation.PacketDescription;

public interface IdBasedWritablePacket extends WritablePacket {

    /**
     * Get id of this packet type.
     *
     * @return the packet type's id.
     */
    default int getPacketId() {
        return getClass()
            .getAnnotation(PacketDescription.class)
            .id();
    }
}
