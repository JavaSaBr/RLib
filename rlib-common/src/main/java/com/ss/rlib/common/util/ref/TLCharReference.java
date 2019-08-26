package com.ss.rlib.common.util.ref;

/**
 * The reference to char value.
 *
 * @author JavaSaBr
 */
final class TLCharReference extends CharReference {

    @Override
    public void release() {
        ReferenceFactory.release(this);
    }
}
