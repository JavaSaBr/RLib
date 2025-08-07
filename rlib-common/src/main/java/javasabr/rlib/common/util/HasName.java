package javasabr.rlib.common.util;

import org.jspecify.annotations.NullMarked;

/**
 * The interface to mark an object that it has a name.
 *
 * @author JavaSaBr
 */
@NullMarked
public interface HasName {

  /**
   * Gets name.
   *
   * @return the name of this object.
   */
  String getName();
}
