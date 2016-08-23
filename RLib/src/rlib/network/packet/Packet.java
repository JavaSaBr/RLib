package rlib.network.packet;

/**
 * The interface for implementing a network packet.
 *
 * @author JavaSaBr
 */
public interface Packet {

    /**
     * @return the name of this packet.
     */
    public String getName();

    /**
     * @return the owner of this packet.
     */
    public Object getOwner();

    /**
     * Sets the new owner of this packet.
     *
     * @param owner the new owner of this packet.
     */
    public void setOwner(Object owner);
}
