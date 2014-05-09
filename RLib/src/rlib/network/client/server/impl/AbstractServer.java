package rlib.network.client.server.impl;

import java.nio.ByteBuffer;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.GameCrypt;
import rlib.network.client.server.Server;
import rlib.network.client.server.ServerConnection;
import rlib.network.packet.ReadeablePacket;
import rlib.network.packet.SendablePacket;

/**
 * Базовая реализация сервера, к которому подключается клиент.
 * 
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractServer<C extends ServerConnection, T extends GameCrypt> implements Server<C> {

	protected static final Logger LOGGER = LoggerManager.getLogger(Server.class);

	/** коннект к логин серверу */
	protected final C connection;
	/** криптор пакетов */
	protected final T crypt;

	/** закрыт ли клиент */
	protected boolean closed;

	protected AbstractServer(final C connection, final T crypt) {
		this.connection = connection;
		this.crypt = crypt;
	}

	@Override
	public void close() {

		final C connection = getConnection();

		if(connection != null) {
			connection.close();
		}
	}

	@Override
	public void decrypt(final ByteBuffer data, final int offset, final int length) {
		crypt.decrypt(data.array(), offset, length);
	}

	@Override
	public void encrypt(final ByteBuffer data, final int offset, final int length) {
		crypt.encrypt(data.array(), offset, length);
	}

	/**
	 * Отправка на выполнение пакета.
	 * 
	 * @param packet выполняемый пакет.
	 */
	protected abstract void execute(ReadeablePacket packet);

	@Override
	public C getConnection() {
		return connection;
	}

	/**
	 * @return закрыт ли клиент.
	 */
	public boolean isClosed() {
		return closed;
	}

	@Override
	public boolean isConnected() {
		return !isClosed() && connection != null && !connection.isClosed();
	}

	@Override
	@SuppressWarnings("unchecked")
	public final void readPacket(final ReadeablePacket packet, final ByteBuffer buffer) {

		if(packet != null) {

			packet.setBuffer(buffer);
			packet.setOwner(this);

			if(packet.read()) {
				packet.setBuffer(null);
				execute(packet);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public final void sendPacket(final SendablePacket packet) {

		if(isClosed()) {
			return;
		}

		final C connection = getConnection();

		if(connection == null || connection.isClosed()) {
			LOGGER.warning(this, new Exception("not found connection"));
			return;
		}

		connection.sendPacket(packet);
	}
}
