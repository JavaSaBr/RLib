package com.ss.rlib.common.util.ref;

/**
 * The reference to short value.
 *
 * @author JavaSaBr
 */
final class TLShortReference extends ShortReference {

    @Override
    public void release() {
        ReferenceFactory.release(this);
    }
}
