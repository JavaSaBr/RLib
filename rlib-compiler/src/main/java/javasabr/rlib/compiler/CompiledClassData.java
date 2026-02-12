package javasabr.rlib.compiler;

/**
 * Represents compiled class bytecode data.
 *
 * @since 10.0.0
 */
public interface CompiledClassData {

  /**
   * Returns the compiled bytecode of the class.
   *
   * @return the bytecode array
   * @since 10.0.0
   */
  byte[] byteCode();

  /**
   * Returns the size of the bytecode in bytes.
   *
   * @return the bytecode size
   * @since 10.0.0
   */
  int size();

  /**
   * Returns the fully qualified name of the compiled class.
   *
   * @return the class name
   * @since 10.0.0
   */
  String name();
}
