package rlib.network.server.client;

import java.nio.ByteBuffer;

import rlib.network.AsynConnection;
import rlib.network.packets.ReadeablePacket;
import rlib.network.packets.SendablePacket;
import rlib.util.Synchronized;

/**
 * Интерфейс для реализации игрового клиента в ММО играх.
 * 
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public interface Client<A, P, C extends AsynConnection> extends Synchronized {

	/**
	 * Закрыть клиент.
	 */
	public void close();

	/**
	 * Декриптовать массив байтов.
	 * 
	 * @param data массив байтов.
	 * @param offset отступ от начала массива.
	 * @param length размер массива, который нужно декриптовать.
	 */
	public void decrypt(ByteBuffer data, int offset, int length);

	/**
	 * Закриптовать массив байтов.
	 * 
	 * @param data массив байтов.
	 * @param offset отступ от начала массива.
	 * @param length размер массива, который нужно закриптовать.
	 */
	public void encrypt(ByteBuffer data, int offset, int length);

	/**
	 * @return аккаунт этого клиента.
	 */
	public A getAccount();

	/**
	 * @return коннект клиента.
	 */
	public C getConnection();

	/**
	 * @return адресс клиента.
	 */
	public String getHostAddress();

	/**
	 * @return владелец этого клиента.
	 */
	public P getOwner();

	/**
	 * @return на связи ли еще клиент.
	 */
	public boolean isConnected();

	/**
	 * Читаем пакет и добавляем в очередь на обработку.
	 * 
	 * @param packet читаемый пакет.
	 * @param buffer читаемый буффер данных.
	 */
	public void readPacket(ReadeablePacket packet, ByteBuffer buffer);

	/**
	 * Отправка пакета клиенту.
	 * 
	 * @param packet отпраляемый пакет.
	 */
	public void sendPacket(SendablePacket packet);

	/**
	 * @param account аккаунт клиента.
	 */
	public void setAccount(A account);

	/**
	 * @param owner владелец этого клиента.
	 */
	public void setOwner(P owner);

	/**
	 * Обработка успешного подключения.
	 */
	public void successfulConnection();
}
