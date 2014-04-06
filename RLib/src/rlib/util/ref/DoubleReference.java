package rlib.util.ref;

/**
 * Ссылка на тип данных double.
 * 
 * @author Ronn
 */
final class DoubleReference extends AbstractReference {

	/** значение по ссылке */
	private double value;

	@Override
	public double getDouble() {
		return value;
	}

	@Override
	public ReferenceType getReferenceType() {
		return ReferenceType.DOUBLE;
	}

	@Override
	public void setDouble(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "DoubleReference [value=" + value + "]";
	}
}
