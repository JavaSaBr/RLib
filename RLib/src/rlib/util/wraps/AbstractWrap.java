package rlib.util.wraps;

/**
 * Базовая модель обертки.
 * 
 * @author Ronn
 */
public abstract class AbstractWrap implements Wrap {

	protected AbstractWrap() {
		super();
	}

	@Override
	public final void fold() {
		getWrapType().put(this);
	}
}
