package rlib.util.ref;

/**
 * Обертка вокруг byte.
 * 
 * @author Ronn
 */
final class ByteWrap extends AbstractReference {

	/** значение */
	private byte value;

	@Override
	public byte getByte() {
		return value;
	}

	@Override
	public ReferenceType getReferenceType() {
		return ReferenceType.BYTE;
	}

	@Override
	public void setByte(byte value) {
		this.value = value;
	}
}
