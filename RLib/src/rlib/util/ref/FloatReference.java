package rlib.util.ref;

/**
 * Ссылка на тип данных float.
 * 
 * @author Ronn
 */
final class FloatReference extends AbstractReference {

	/** значение по ссылке */
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

	@Override
	public String toString() {
		return "FloatReference [value=" + value + "]";
	}
}
