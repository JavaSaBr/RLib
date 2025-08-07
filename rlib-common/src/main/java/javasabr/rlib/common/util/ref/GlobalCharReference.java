package javasabr.rlib.common.util.ref;

/**
 * The reference to char value.
 *
 * @author JavaSaBr
 */
final class GlobalCharReference extends CharReference {

    @Override
    public void release() {
        ReferenceFactory.release(this);
    }
}
