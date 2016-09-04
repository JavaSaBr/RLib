package rlib.util.ref;

import org.jetbrains.annotations.NotNull;

import static rlib.util.ref.ReferenceType.SHORT;

/**
 * Ссылка на тип данных short.
 *
 * @author JavaSaBr
 */
final class ShortReference extends AbstractReference {

    /**
     * Значение по ссылке.
     */
    private short value;

    @NotNull
    @Override
    public ReferenceType getType() {
        return SHORT;
    }

    @Override
    public short getShort() {
        return value;
    }

    @Override
    public void setShort(final short value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [value=" + value + "]";
    }
}
