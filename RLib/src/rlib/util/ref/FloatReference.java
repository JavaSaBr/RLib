package rlib.util.ref;

import static rlib.util.ref.ReferenceType.FLOAT;

/**
 * Ссылка на тип данных float.
 *
 * @author Ronn
 */
final class FloatReference extends AbstractReference {

    /**
     * Значение по ссылке.
     */
    private float value;

    @Override
    public float getFloat() {
        return value;
    }

    @Override
    public void setFloat(final float value) {
        this.value = value;
    }

    @Override
    public ReferenceType getReferenceType() {
        return FLOAT;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [value=" + value + "]";
    }
}
