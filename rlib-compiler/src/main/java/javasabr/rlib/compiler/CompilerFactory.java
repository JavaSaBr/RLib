package javasabr.rlib.compiler;

import javasabr.rlib.compiler.impl.JdkCompiler;
import javax.tools.ToolProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Factory for creating {@link Compiler} instances.
 *
 * @since 10.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilerFactory {

  /**
   * Checks if the compiler API is available in the current runtime.
   *
   * @return true if the compiler is available
   * @since 10.0.0
   */
  public static boolean isAvailableCompiler() {
    return ToolProvider.getSystemJavaCompiler() != null;
  }

  /**
   * Creates a new default compiler instance.
   *
   * @return a new compiler
   * @since 10.0.0
   */
  public static Compiler newDefaultCompiler() {
    return new JdkCompiler(true);
  }
}
