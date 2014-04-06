package rlib.util.ref;

/**
 * Обертка вокруг float.
 * 
 * @author Ronn
 */
final class FloatWrap extends AbstractReference {

	/** значение */
	private float value;

	@Override
	public float getFloat() {
		return value;
	}

	@Override
	public ReferenceType getReferenceType() {
		return ReferenceType.FLOAT;
	}

	@Override
	public void setFloat(float value) {
		this.value = value;
	}
}
