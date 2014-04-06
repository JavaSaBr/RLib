package rlib.util.ref;

/**
 * Обертка вокруг char.
 * 
 * @author Ronn
 */
final class CharWrap extends AbstractReference {

	/** значение */
	private char value;

	@Override
	public char getChar() {
		return value;
	}

	@Override
	public ReferenceType getReferenceType() {
		return ReferenceType.CHAR;
	}

	@Override
	public void setChar(char value) {
		this.value = value;
	}
}
