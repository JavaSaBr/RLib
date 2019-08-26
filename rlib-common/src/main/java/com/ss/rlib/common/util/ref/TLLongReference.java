package com.ss.rlib.common.util.ref;

/**
 * The reference to long value.
 *
 * @author JavaSaBr
 */
final class TLLongReference extends LongReference {

    @Override
    public void release() {
        ReferenceFactory.release(this);
    }
}
