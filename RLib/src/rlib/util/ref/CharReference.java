package rlib.util.ref;

import static rlib.util.ref.ReferenceType.CHAR;

/**
 * Ссылка на тип данных char.
 *
 * @author JavaSaBr
 */
final class CharReference extends AbstractReference {

    /**
     * Значение по ссылке.
     */
    private char value;

    @Override
    public char getChar() {
        return value;
    }

    @Override
    public void setChar(final char value) {
        this.value = value;
    }

    @Override
    public ReferenceType getReferenceType() {
        return CHAR;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [value=" + value + "]";
    }
}
