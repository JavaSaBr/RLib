package rlib.network.packets;

import java.nio.ByteBuffer;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.Strings;
import rlib.util.Util;
import rlib.util.pools.Foldable;


/**
 * Базовая модель записываемого пакета на стороне сервера.
 *
 * @author Ronn
 */
public abstract class AbstractSendablePacket<C> extends AbstractPacket<C> implements SendablePacket<C>, Foldable
{
	protected static final Logger log = Loggers.getLogger(SendablePacket.class);

	/** счетчик добавлений на отправку экземпляра пакета */
	protected volatile int counter;

	@Override
	public abstract void complete();

	@Override
	public final void decreaseSends()
	{
		counter -= 1;
	}

	@Override
	public void decreaseSends(int count)
	{
		counter -= count;
	}

	@Override
	public void finalyze(){}

	@Override
	public final void increaseSends()
	{
		counter += 1;
	}
	@Override
	public void increaseSends(int count)
	{
		counter += count;
	}

	@Override
	public boolean isSynchronized()
	{
		return true;
	}

	@Override
	public void reinit()
	{
		counter = 0;
	}

	@Override
	public void write(ByteBuffer buffer)
	{
		// если пакет уже запулизирован
		if(counter < 0)
		{
			log.warning(this, "write pooled packet");
			return;
		}

		try
		{
			writeImpl(buffer);
		}
		catch(Exception e)
		{
			log.warning(this, e);
			log.warning(this, "Buffer " + buffer + "\n" + Util.hexdump(buffer.array(), buffer.position()));
		}
	}

	protected final void writeByte(ByteBuffer buffer, int value)
	{
		buffer.put((byte) value);
	}

	protected final void writeByte(int value)
	{
		writeByte(buffer, value);
	}

	protected final void writeChar(ByteBuffer buffer, char value)
	{
		buffer.putChar(value);
	}

	protected final void writeChar(ByteBuffer buffer, int value)
	{
		buffer.putChar((char) value);
	}

	protected final void writeChar(char value)
	{
		writeChar(buffer, value);
	}

	protected final void writeChar(int value)
	{
		writeChar(buffer, value);
	}

	protected final void writeFloat(ByteBuffer buffer, float value)
	{
		buffer.putFloat(value);
	}

	protected final void writeFloat(float value)
	{
		writeFloat(buffer, value);
	}

	/**
	 * Процесс записи пакета.
	 */
	protected void writeImpl()
	{
		log.warning(this, new Exception("unsupperted method"));
	}

	/**
	 * Процесс записи пакета в указанный буффер..
	 */
	protected void writeImpl(ByteBuffer buffer)
	{
		log.warning(this, new Exception("unsupperted method"));
	}

	protected final void writeInt(ByteBuffer buffer, int value)
	{
		buffer.putInt(value);
	}

	protected final void writeInt(int value)
	{
		writeInt(buffer, value);
	}

	@Override
	public void writeLocal()
	{
		// если пакет уже запулизирован
		if(counter < 0)
		{
			log.warning(this, "write local pooled packet");
			return;
		}

		try
		{
			writeImpl();
		}
		catch(Exception e)
		{
			log.warning(this, e);
			log.warning(this, "Buffer " + buffer + "\n" + Util.hexdump(buffer.array(), buffer.position()));
		}
	}

	protected final void writeLong(ByteBuffer buffer, long value)
	{
		buffer.putLong(value);
	}

	protected final void writeLong(long value)
	{
		writeLong(buffer, value);
	}

	protected void writeShort(ByteBuffer buffer, int value)
	{
		buffer.putShort((short) value);
	}

	protected void writeShort(int value)
	{
		writeShort(buffer, value);
	}

	protected void writeString(ByteBuffer buffer, String string)
	{
		if(string == null)
			string = Strings.EMPTY;

		for(int i = 0, length = string.length(); i < length; i++)
			buffer.putChar(string.charAt(i));
	}

	protected void writeString(String string)
	{
		writeString(buffer, string);
	}
}
