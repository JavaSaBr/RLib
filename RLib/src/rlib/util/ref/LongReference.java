package rlib.util.ref;

import static rlib.util.ref.ReferenceType.LONG;

/**
 * Ссылка на тип данных long.
 *
 * @author JavaSaBr
 */
final class LongReference extends AbstractReference {

    /**
     * Значение по ссылке.
     */
    private long value;

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public void setLong(final long value) {
        this.value = value;
    }

    @Override
    public ReferenceType getReferenceType() {
        return LONG;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [value=" + value + "]";
    }
}
