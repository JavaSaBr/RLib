package rlib.network.server.client.impl;

import java.nio.ByteBuffer;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.AsynConnection;
import rlib.network.GameCrypt;
import rlib.network.packets.ReadeablePacket;
import rlib.network.packets.SendablePacket;
import rlib.network.server.client.Client;

/**
 * Базовая модель серверного клиента.
 * 
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractClient<A, O, C extends AsynConnection, T extends GameCrypt> implements Client<A, O, C> {

	protected static final Logger LOGGER = LoggerManager.getLogger(Client.class);

	/** владелец коннекта */
	protected O owner;
	/** аккаунт клиента */
	protected A account;
	/** коннект клиента к серверу */
	protected C connection;
	/** криптор клиента */
	protected T crypt;

	/** закрыт ли клиент */
	protected boolean closed;

	public AbstractClient(C connection, T crypt) {
		this.connection = connection;
		this.crypt = crypt;
	}

	@Override
	public void close() {

		C connection = getConnection();

		if(connection != null) {
			connection.close();
		}

		setClosed(true);
	}

	@Override
	public void decrypt(ByteBuffer data, int offset, int length) {
		crypt.decrypt(data.array(), offset, length);
	}

	@Override
	public void encrypt(ByteBuffer data, int offset, int length) {
		crypt.encrypt(data.array(), offset, length);
	}

	/**
	 * @param packet обрабатываемый пакет.
	 */
	protected abstract void executePacket(ReadeablePacket packet);

	@Override
	public final A getAccount() {
		return account;
	}

	@Override
	public final C getConnection() {
		return connection;
	}

	@Override
	public final O getOwner() {
		return owner;
	}

	@Override
	public final boolean isConnected() {
		return !isClosed() && connection != null && !connection.isClosed();
	}

	/**
	 * @return закрыт ли клиент.
	 */
	public boolean isClosed() {
		return closed;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final void readPacket(ReadeablePacket packet, ByteBuffer buffer) {

		if(packet != null) {

			packet.setBuffer(buffer);
			packet.setOwner(this);

			if(packet.read()) {
				packet.setBuffer(null);
				executePacket(packet);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public final void sendPacket(SendablePacket packet) {

		if(isClosed()) {
			return;
		}

		C connection = getConnection();

		if(connection != null) {
			connection.sendPacket(packet);
		}
	}

	@Override
	public final void setAccount(A account) {
		this.account = account;
	}

	/**
	 * @param closed закрыт ли клиент.
	 */
	private void setClosed(boolean closed) {
		this.closed = closed;
	}

	@Override
	public final void setOwner(O owner) {
		this.owner = owner;
	}

	@Override
	public void successfulConnection() {
		LOGGER.info(this, getHostAddress() + " successful connection.");
	}
}
