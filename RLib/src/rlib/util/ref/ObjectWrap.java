package rlib.util.ref;

/**
 * Обертка вокруг Object.
 * 
 * @author Ronn
 */
final class ObjectWrap extends AbstractReference {

	/** значение */
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
}
