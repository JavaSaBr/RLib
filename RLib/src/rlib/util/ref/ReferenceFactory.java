package rlib.util.ref;

import static rlib.util.ref.ReferenceType.BYTE;
import static rlib.util.ref.ReferenceType.CHAR;
import static rlib.util.ref.ReferenceType.DOUBLE;
import static rlib.util.ref.ReferenceType.FLOAT;
import static rlib.util.ref.ReferenceType.INTEGER;
import static rlib.util.ref.ReferenceType.LONG;
import static rlib.util.ref.ReferenceType.OBJECT;
import static rlib.util.ref.ReferenceType.SHORT;

/**
 * Фабрика ссылок на разные виды данных.
 *
 * @author JavaSaBr
 */
public final class ReferenceFactory {

    public static Reference newByteReference(final byte value, final boolean usePool) {

        final Reference reference = usePool ? BYTE.take(ByteReference::new) : new ByteReference();
        reference.setByte(value);

        return reference;
    }

    public static Reference newCharReference(final char value, final boolean usePool) {

        final Reference reference = usePool ? CHAR.take(CharReference::new) : new CharReference();
        reference.setChar(value);

        return reference;
    }

    public static Reference newDoubleReference(final double value, final boolean usePool) {

        final Reference reference = usePool ? DOUBLE.take(DoubleReference::new) : new DoubleReference();
        reference.setDouble(value);

        return reference;
    }

    public static Reference newFloatReference(final float value, final boolean usePool) {

        final Reference reference = usePool ? FLOAT.take(FloatReference::new) : new FloatReference();
        reference.setFloat(value);

        return reference;
    }

    public static Reference newIntegerReference(final int value, final boolean usePool) {

        final Reference reference = usePool ? INTEGER.take(IntegerReference::new) : new IntegerReference();
        reference.setInt(value);

        return reference;
    }

    public static Reference newLongReference(final long value, final boolean usePool) {

        final Reference reference = usePool ? LONG.take(LongReference::new) : new LongReference();
        reference.setLong(value);

        return reference;
    }

    public static Reference newObjectReference(final Object object, final boolean usePool) {

        final Reference reference = usePool ? OBJECT.take(ObjectReference::new) : new ObjectReference();
        reference.setObject(object);

        return reference;
    }

    public static Reference newShortReference(final short value, final boolean usePool) {

        final Reference reference = usePool ? SHORT.take(ShortReference::new) : new ShortReference();
        reference.setShort(value);

        return reference;
    }

    private ReferenceFactory() {
        throw new IllegalArgumentException();
    }
}
