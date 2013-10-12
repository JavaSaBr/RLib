package rlib.util.wraps;

/**
 * Обертка вокруг Object.
 * 
 * @author Ronn
 */
final class ObjectWrap extends AbstractWrap {

	/** значение */
	private Object object;

	@Override
	public Object getObject() {
		return object;
	}

	@Override
	public WrapType getWrapType() {
		return WrapType.OBJECT;
	}

	@Override
	public void setObject(Object object) {
		this.object = object;
	}
}
