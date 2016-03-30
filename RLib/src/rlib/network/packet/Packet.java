package rlib.network.packet;

/**
 * Интерфейс для реализации сетевого пакета на стороне сервера.
 *
 * @author Ronn
 */
public interface Packet<C> {

    /**
     * @return тип пакета.
     */
    public String getName();

    /**
     * @return владелец пакета.
     */
    public C getOwner();

    /**
     * Установка владельца пакета.
     *
     * @param owner владелец пакета..
     */
    public void setOwner(C owner);
}
