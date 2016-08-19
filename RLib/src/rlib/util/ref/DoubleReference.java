package rlib.util.ref;

import static rlib.util.ref.ReferenceType.DOUBLE;

/**
 * Ссылка на тип данных double.
 *
 * @author JavaSaBr
 */
final class DoubleReference extends AbstractReference {

    /**
     * Значение по ссылке.
     */
    private double value;

    @Override
    public double getDouble() {
        return value;
    }

    @Override
    public void setDouble(final double value) {
        this.value = value;
    }

    @Override
    public ReferenceType getReferenceType() {
        return DOUBLE;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [value=" + value + "]";
    }
}
