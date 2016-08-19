package rlib.util.ref;

/**
 * Базовая реализация ссылки.
 *
 * @author JavaSaBr
 */
public abstract class AbstractReference implements Reference {

    protected AbstractReference() {
        super();
    }

    @Override
    public void release() {
        final ReferenceType referenceType = getReferenceType();
        referenceType.put(this);
    }
}
