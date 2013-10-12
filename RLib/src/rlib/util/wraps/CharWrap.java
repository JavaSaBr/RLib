package rlib.util.wraps;

/**
 * Обертка вокруг char.
 * 
 * @author Ronn
 */
final class CharWrap extends AbstractWrap {

	/** значение */
	private char value;

	@Override
	public char getChar() {
		return value;
	}

	@Override
	public WrapType getWrapType() {
		return WrapType.CHAR;
	}

	@Override
	public void setChar(char value) {
		this.value = value;
	}
}
