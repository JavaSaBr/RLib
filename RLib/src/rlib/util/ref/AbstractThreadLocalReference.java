package rlib.util.ref;

/**
 * The base thread local implementation of the {@link Reference}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractThreadLocalReference extends AbstractReference {

    @Override
    public boolean isThreadLocal() {
        return true;
    }

    @Override
    public void release() {
        getType().putToThreadLocal(this);
    }
}
