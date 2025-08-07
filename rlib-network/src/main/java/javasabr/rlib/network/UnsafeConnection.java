package javasabr.rlib.network;

import javasabr.rlib.network.packet.ReadablePacket;
import javasabr.rlib.network.packet.WritablePacket;

public interface UnsafeConnection<R extends ReadablePacket, W extends WritablePacket> extends Connection<R, W> {

    void onConnected();

}
