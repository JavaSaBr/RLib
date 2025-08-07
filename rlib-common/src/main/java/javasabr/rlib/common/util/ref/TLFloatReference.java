package javasabr.rlib.common.util.ref;

/**
 * The reference to float value.
 *
 * @author JavaSaBr
 */
final class TLFloatReference extends FloatReference {

    @Override
    public void release() {
        ReferenceFactory.release(this);
    }
}
