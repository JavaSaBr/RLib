package javasabr.rlib.common.util.ref;

/**
 * The reference to float value.
 *
 * @author JavaSaBr
 */
final class GlobalFloatReference extends FloatReference {

  @Override
  public void release() {
    ReferenceFactory.release(this);
  }
}
