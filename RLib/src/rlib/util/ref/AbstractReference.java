package rlib.util.ref;

/**
 * Базовая реализация ссылки..
 * 
 * @author Ronn
 */
public abstract class AbstractReference implements Reference {

	protected AbstractReference() {
		super();
	}

	@Override
	public final void fold() {
		getReferenceType().put(this);
	}
}
