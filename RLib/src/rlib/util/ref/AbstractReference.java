package rlib.util.ref;

/**
 * The base implementation of the {@link Reference}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractReference implements UnsafeReference {

    protected AbstractReference() {
        super();
    }

    @Override
    public boolean isThreadLocal() {
        return false;
    }

    @Override
    public void release() {
        getType().put(this);
    }
}
