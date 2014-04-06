package rlib.util.ref;

/**
 * Ссылка на тип данных Object.
 * 
 * @author Ronn
 */
final class ObjectReference extends AbstractReference {

	/** значение по ссылке */
	private Object object;

	@Override
	public Object getObject() {
		return object;
	}

	@Override
	public ReferenceType getReferenceType() {
		return ReferenceType.OBJECT;
	}

	@Override
	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "ObjectReference [object=" + object + "]";
	}
}
