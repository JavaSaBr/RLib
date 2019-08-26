package com.ss.rlib.common.util.ref;

/**
 * The reference to integer value.
 *
 * @author JavaSaBr
 */
final class TLIntReference extends IntReference {

    @Override
    public void release() {
        ReferenceFactory.release(this);
    }
}
