package javasabr.rlib.common.util.ref;

/**
 * The reference to short value.
 *
 * @author JavaSaBr
 */
final class GlobalShortReference extends ShortReference {

    @Override
    public void release() {
        ReferenceFactory.release(this);
    }
}
