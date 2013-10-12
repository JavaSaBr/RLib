package rlib.util.wraps;

/**
 * Обертка вокруг byte.
 * 
 * @author Ronn
 */
final class ByteWrap extends AbstractWrap {

	/** значение */
	private byte value;

	@Override
	public byte getByte() {
		return value;
	}

	@Override
	public WrapType getWrapType() {
		return WrapType.BYTE;
	}

	@Override
	public void setByte(byte value) {
		this.value = value;
	}
}
