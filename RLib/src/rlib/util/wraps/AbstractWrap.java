package rlib.util.wraps;

/**
 * Базовая модель обертки.
 *
 * @author Ronn
 */
public abstract class AbstractWrap implements Wrap
{
	protected AbstractWrap()
	{
		super();
	}

	@Override
	public void finalyze(){}

	@Override
	public final void fold()
	{
		getWrapType().put(this);
	}

	@Override
	public byte getByte()
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public char getChar()
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public double getDouble()
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public float getFloat()
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public int getInt()
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public long getLong()
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public Object getObject()
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public short getShort()
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public void reinit(){}

	@Override
	public void setByte(byte value)
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public void setChar(char value)
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public void setDouble(double value)
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public void setFloat(float value)
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public void setInt(int value)
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public void setLong(long value)
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public void setObject(Object object)
	{
		throw new IllegalArgumentException("not supported method.");
	}

	@Override
	public void setShort(short value)
	{
		throw new IllegalArgumentException("not supported method.");
	}
}
