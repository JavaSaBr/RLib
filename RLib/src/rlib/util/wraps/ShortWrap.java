package rlib.util.wraps;

/**
 * Обертка вокруг short.
 * 
 * @author Ronn
 */
final class ShortWrap extends AbstractWrap
{
	/** значение */
	private short value;
	
	@Override
	public short getShort()
	{
		return value;
	}
	
	@Override
	public WrapType getWrapType()
	{
		return WrapType.SHORT;
	}
	
	@Override
	public void setShort(short value)
	{
		this.value = value;
	}
}
