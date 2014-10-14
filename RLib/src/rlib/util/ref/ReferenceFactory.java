package rlib.util.ref;

/**
 * Фабрика ссылок на разные виды данных.
 * 
 * @author Ronn
 */
public final class ReferenceFactory {

	public static Reference newByteReference(final byte value, final boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.BYTE.take();
		}

		if(reference == null) {
			reference = new ByteReference();
		}

		reference.setByte(value);
		return reference;
	}

	public static Reference newCharReference(final char value, final boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.CHAR.take();
		}

		if(reference == null) {
			reference = new CharReference();
		}

		reference.setChar(value);
		return reference;
	}

	public static Reference newDoubleReference(final double value, final boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.DOUBLE.take();
		}

		if(reference == null) {
			reference = new DoubleReference();
		}

		reference.setDouble(value);
		return reference;
	}

	public static Reference newFloatReference(final float value, final boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.FLOAT.take();
		}

		if(reference == null) {
			reference = new FloatReference();
		}

		reference.setFloat(value);
		return reference;
	}

	public static Reference newIntegerReference(final int value, final boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.INTEGER.take();
		}

		if(reference == null) {
			reference = new IntegerReference();
		}

		reference.setInt(value);
		return reference;
	}

	public static Reference newLongReference(final long value, final boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.LONG.take();
		}

		if(reference == null) {
			reference = new LongReference();
		}

		reference.setLong(value);
		return reference;
	}

	public static Reference newObjectReference(final Object object, final boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.OBJECT.take();
		}

		if(reference == null) {
			reference = new ObjectReference();
		}

		reference.setObject(object);
		return reference;
	}

	public static Reference newShortReference(final short value, final boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.SHORT.take();
		}

		if(reference == null) {
			reference = new ShortReference();
		}

		reference.setShort(value);
		return reference;
	}

	private ReferenceFactory() {
		throw new IllegalArgumentException();
	}
}
