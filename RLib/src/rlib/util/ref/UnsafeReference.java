package rlib.util.ref;

/**
 * The interface for the {@link Reference} with unsafe methods.
 *
 * @author JavaSaBr
 */
public interface UnsafeReference extends Reference {

    boolean isThreadLocal();
}
