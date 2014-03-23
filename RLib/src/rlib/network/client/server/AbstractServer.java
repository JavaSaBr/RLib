package rlib.network.client.server;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.lock.LockFactory;
import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.network.GameCrypt;
import rlib.network.packets.ReadeablePacket;
import rlib.network.packets.SendablePacket;

/**
 * Базовая модель сервера, к которому подключается клиент.
 * 
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractServer<C extends ServerConnection, T extends GameCrypt> implements Server<C> {

	protected static final Logger LOGGER = Loggers.getLogger(Server.class);

	/** блокировщик */
	protected final Lock lock;
	/** коннект к логин серверу */
	protected final C connection;
	/** криптор пакетов */
	protected final T crypt;

	/** закрыт ли клиент */
	protected boolean closed;

	protected AbstractServer(C connection, T crypt) {
		this.connection = connection;
		this.lock = LockFactory.newLock();
		this.crypt = crypt;
	}

	@Override
	public void close() {

		C connection = getConnection();

		if(connection != null) {
			connection.close();
		}
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
	 * Отправка на выполнение пакета.
	 * 
	 * @param packet выполняемый пакет.
	 */
	protected abstract void execute(ReadeablePacket packet);

	@Override
	public C getConnection() {
		return connection;
	}

	@Override
	public boolean isConnected() {
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
				execute(packet);
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

			if(connection == null) {
				LOGGER.warning(this, new Exception("not found connection"));
				return;
			}

			connection.sendPacket(packet);

		} finally {
			unlock();
		}
	}

	@Override
	public final void unlock() {
		lock.unlock();
	}
}
