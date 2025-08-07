package javasabr.rlib.common.compiler;

import javasabr.rlib.common.compiler.impl.CompilerImpl;
import javax.tools.ToolProvider;
import org.jetbrains.annotations.NotNull;

/**
 * The factory of java compilers.
 *
 * @author JavaSaBr
 */
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
    public static @NotNull Compiler newDefaultCompiler() {
        return new CompilerImpl(true);
    }

    private CompilerFactory() {
        throw new RuntimeException();
    }
}
