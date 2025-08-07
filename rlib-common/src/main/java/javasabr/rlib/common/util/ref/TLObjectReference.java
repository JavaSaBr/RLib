package javasabr.rlib.common.util.ref;

/**
 * The reference to object.
 *
 * @author JavaSaBr
 */
final class TLObjectReference<T> extends ObjectReference<T> {

  @Override
  public void release() {
    ReferenceFactory.release(this);
  }
}
