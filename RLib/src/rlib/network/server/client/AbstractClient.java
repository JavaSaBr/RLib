package rlib.network.server.client;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.sync.LockFactory;
import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.network.AsynConnection;
import rlib.network.GameCrypt;
import rlib.network.packets.ReadeablePacket;
import rlib.network.packets.SendablePacket;

/**
 * Базовая модель серверного клиента.
 * 
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractClient<A, O, C extends AsynConnection, T extends GameCrypt> implements Client<A, O, C> {

	protected static final Logger LOGGER = Loggers.getLogger(Client.class);

	/** блокировщик */
	private final Lock lock;

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
		this.lock = LockFactory.newLock();
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
		return !closed && connection != null && !connection.isClosed();
	}

	@Override
	public final void lock() {
		lock.lock();
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

		if(closed) {
			return;
		}

		lock();
		try {

			C connection = getConnection();

			if(connection != null) {
				connection.sendPacket(packet);
			}

		} finally {
			unlock();
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

	@Override
	public final void unlock() {
		lock.unlock();
	}
}
