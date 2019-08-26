package com.ss.rlib.common.util.ref;

/**
 * The reference to byte value.
 *
 * @author JavaSaBr
 */
final class GlobalByteReference extends ByteReference {

    @Override
    public void release() {
        ReferenceFactory.release(this);
    }
}
