package rlib.util.ref;

import static rlib.util.ref.ReferenceType.INTEGER;

/**
 * Ссылка на тип данных int.
 *
 * @author Ronn
 */
final class IntegerReference extends AbstractReference {

    /**
     * Значение по ссылке.
     */
    private int value;

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public void setInt(final int value) {
        this.value = value;
    }

    @Override
    public ReferenceType getReferenceType() {
        return INTEGER;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [value=" + value + "]";
    }
}
