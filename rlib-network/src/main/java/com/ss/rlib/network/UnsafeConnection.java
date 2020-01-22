package com.ss.rlib.network;

import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.WritablePacket;

public interface UnsafeConnection<R extends ReadablePacket, W extends WritablePacket> extends Connection<R, W> {

    void onConnected();

}
