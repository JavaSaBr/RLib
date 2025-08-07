package javasabr.rlib.common.data;

import org.jspecify.annotations.NullMarked;

/**
 * The interface to implement a parser of xml documents.
 *
 * @param <C> the result type.
 * @author JavaSaBr
 */
@NullMarked
public interface DocumentXML<C> {

  /**
   * Parse this document and get the result.
   *
   * @return the result.
   */
  C parse();
}
