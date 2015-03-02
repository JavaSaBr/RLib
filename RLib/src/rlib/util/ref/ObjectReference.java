package rlib.util.ref;

import static rlib.util.ref.ReferenceType.OBJECT;

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
		return OBJECT;
	}

	@Override
	public void setObject(final Object object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [object=" + object + "]";
	}
}
