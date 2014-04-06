package rlib.util.ref;

/**
 * Фабрика ссылок на разные виды данных.
 * 
 * @author Ronn
 */
public final class ReferenceFactory {

	public static Reference newByteReference(byte value, boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.BYTE.take();
		}

		if(reference == null) {
			reference = new ByteWrap();
		}

		reference.setByte(value);
		return reference;
	}

	public static Reference newCharReference(char value, boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.CHAR.take();
		}

		if(reference == null) {
			reference = new CharWrap();
		}

		reference.setChar(value);
		return reference;
	}

	public static Reference newDoubleReference(double value, boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.DOUBLE.take();
		}

		if(reference == null) {
			reference = new DoubleWrap();
		}

		reference.setDouble(value);
		return reference;
	}

	public static Reference newFloatReference(float value, boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.FLOAT.take();
		}

		if(reference == null) {
			reference = new FloatWrap();
		}

		reference.setFloat(value);
		return reference;
	}

	public static Reference newIntegerReference(int value, boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.INTEGER.take();
		}

		if(reference == null) {
			reference = new IntegerWrap();
		}

		reference.setInt(value);
		return reference;
	}

	public static Reference newLongReference(long value, boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.LONG.take();
		}

		if(reference == null) {
			reference = new LongWrap();
		}

		reference.setLong(value);
		return reference;
	}

	public static Reference newObjectReference(Object object, boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.OBJECT.take();
		}

		if(reference == null) {
			reference = new ObjectWrap();
		}

		reference.setObject(object);
		return reference;
	}

	public static Reference newShortReference(short value, boolean usePool) {

		Reference reference = null;

		if(usePool) {
			reference = ReferenceType.SHORT.take();
		}

		if(reference == null) {
			reference = new ShortWrap();
		}

		reference.setShort(value);
		return reference;
	}

	private ReferenceFactory() {
		throw new IllegalArgumentException();
	}
}
