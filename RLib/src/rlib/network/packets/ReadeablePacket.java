package rlib.network.packets;

/**
 * Интерфейс для реализации читаемого пакета.
 *
 * @author Ronn
 */
public interface ReadeablePacket<C> extends Packet<C>, Runnable
{
	/**
	 * @return кол-во свободных байтов.
	 */
	public int getAvaliableBytes();
	
	/**
	 * @return выполнять ли синхронно пакет.
	 */
	public boolean isSynchronized();
	
	/**
	 * @return новый экземпляр пакета.
	 */
	public ReadeablePacket<C> newInstance();
	
	/**
	 * Прочитать присланную информацию.
	 * 
	 * @return успешно ли прочитано.
	 */
	public boolean read();
}