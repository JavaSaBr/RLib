package javasabr.rlib.common.util.ref;

/**
 * The reference to byte value.
 *
 * @author JavaSaBr
 */
final class TLByteReference extends ByteReference {

    @Override
    public void release() {
        ReferenceFactory.release(this);
    }
}
