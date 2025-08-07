package javasabr.rlib.common.compiler;

import javasabr.rlib.common.compiler.impl.CompilerImpl;
import javax.tools.ToolProvider;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * The factory of java compilers.
 *
 * @author JavaSaBr
 */
@NullMarked
public class CompilerFactory {

  /**
   * Check availability compiler API in current runtime.
   *
   * @return true if a compiler is available.
   */
  public static boolean isAvailableCompiler() {
    return ToolProvider.getSystemJavaCompiler() != null;
  }

  /**
   * Create a new default compiler.
   *
   * @return the new compiler.
   */
  public static Compiler newDefaultCompiler() {
    return new CompilerImpl(true);
  }

  private CompilerFactory() {
    throw new RuntimeException();
  }
}
