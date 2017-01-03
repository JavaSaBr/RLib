package rlib.compiler;

import org.jetbrains.annotations.NotNull;

import javax.tools.ToolProvider;

import rlib.compiler.impl.CompilerImpl;

/**
 * The factory of java compilers.
 *
 * @author JavaSaBr
 */
public class CompilerFactory {

    /**
     * @return true if a compiler is available.
     */
    public static boolean isAvailableCompiler() {
        return ToolProvider.getSystemJavaCompiler() != null;
    }

    @NotNull
    public static Compiler newDefaultCompiler() {
        return new CompilerImpl(true);
    }

    private CompilerFactory() {
        throw new RuntimeException();
    }
}
