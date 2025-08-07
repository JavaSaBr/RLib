package javasabr.rlib.common.util.ref;

/**
 * The reference to double value.
 *
 * @author JavaSaBr
 */
final class GlobalDoubleReference extends DoubleReference {

    @Override
    public void release() {
        ReferenceFactory.release(this);
    }
}
