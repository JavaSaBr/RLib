package rlib.util.wraps;

/**
 * Обертка вокруг float.
 * 
 * @author Ronn
 */
final class FloatWrap extends AbstractWrap
{
	/** значение */
	private float value;
	
	@Override
	public float getFloat()
	{
		return value;
	}
	
	@Override
	public WrapType getWrapType()
	{
		return WrapType.FLOAT;
	}
	
	@Override
	public void setFloat(float value)
	{
		this.value = value;
	}
}
