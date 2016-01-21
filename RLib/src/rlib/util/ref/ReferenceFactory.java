package rlib.util.ref;

import static rlib.util.ref.ReferenceType.*;

/**
 * Фабрика ссылок на разные виды данных.
 *
 * @author Ronn
 */
public final class ReferenceFactory {

    public static Reference newByteReference(final byte value, final boolean usePool) {

        Reference reference = null;

        if (usePool) {
            reference = BYTE.take();
        }

        if (reference == null) {
            reference = new ByteReference();
        }

        reference.setByte(value);
        return reference;
    }

    public static Reference newCharReference(final char value, final boolean usePool) {

        Reference reference = null;

        if (usePool) {
            reference = CHAR.take();
        }

        if (reference == null) {
            reference = new CharReference();
        }

        reference.setChar(value);
        return reference;
    }

    public static Reference newDoubleReference(final double value, final boolean usePool) {

        Reference reference = null;

        if (usePool) {
            reference = DOUBLE.take();
        }

        if (reference == null) {
            reference = new DoubleReference();
        }

        reference.setDouble(value);
        return reference;
    }

    public static Reference newFloatReference(final float value, final boolean usePool) {

        Reference reference = null;

        if (usePool) {
            reference = FLOAT.take();
        }

        if (reference == null) {
            reference = new FloatReference();
        }

        reference.setFloat(value);
        return reference;
    }

    public static Reference newIntegerReference(final int value, final boolean usePool) {

        Reference reference = null;

        if (usePool) {
            reference = INTEGER.take();
        }

        if (reference == null) {
            reference = new IntegerReference();
        }

        reference.setInt(value);
        return reference;
    }

    public static Reference newLongReference(final long value, final boolean usePool) {

        Reference reference = null;

        if (usePool) {
            reference = LONG.take();
        }

        if (reference == null) {
            reference = new LongReference();
        }

        reference.setLong(value);
        return reference;
    }

    public static Reference newObjectReference(final Object object, final boolean usePool) {

        Reference reference = null;

        if (usePool) {
            reference = OBJECT.take();
        }

        if (reference == null) {
            reference = new ObjectReference();
        }

        reference.setObject(object);
        return reference;
    }

    public static Reference newShortReference(final short value, final boolean usePool) {

        Reference reference = null;

        if (usePool) {
            reference = SHORT.take();
        }

        if (reference == null) {
            reference = new ShortReference();
        }

        reference.setShort(value);
        return reference;
    }

    private ReferenceFactory() {
        throw new IllegalArgumentException();
    }
}
