package rlib.network.client.server;

/**
 * Статус отправляющей части коннекта
 *
 * @author Ronn
 * @created 25.03.2012
 */
public enum ConnectState
{
	/** ожидание новых пакетов для отправки */
	WAITING_PACKETS,
	/** ожидание отправки новых пакетов */
	WAITING_WRITES;
}
