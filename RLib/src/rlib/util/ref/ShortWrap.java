package rlib.util.ref;

/**
 * Обертка вокруг short.
 * 
 * @author Ronn
 */
final class ShortWrap extends AbstractReference {

	/** значение */
	private short value;

	@Override
	public short getShort() {
		return value;
	}

	@Override
	public ReferenceType getReferenceType() {
		return ReferenceType.SHORT;
	}

	@Override
	public void setShort(short value) {
		this.value = value;
	}
}
