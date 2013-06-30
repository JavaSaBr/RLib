package rlib.util.wraps;

/**
 * Обертка вокруг long.
 *
 * @author Ronn
 */
final class LongWrap extends AbstractWrap
{
	/** обернутое значение */
	private long value;

    @Override
    public long getLong()
    {
    	return value;
    }

	@Override
	public WrapType getWrapType()
	{
		return WrapType.LONG;
	}

	@Override
	public void setLong(long value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return String.valueOf(value);
	}
}
