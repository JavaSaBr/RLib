package com.ss.rlib.common.compiler;

import com.ss.rlib.common.compiler.impl.CompilerImpl;
import org.jetbrains.annotations.NotNull;

import javax.tools.ToolProvider;

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
