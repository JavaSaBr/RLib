package com.ss.rlib.compiler;

import com.ss.rlib.compiler.impl.CompilerImpl;
import org.jetbrains.annotations.NotNull;

import javax.tools.ToolProvider;

/**
 * The factory of java compilers.
 *
 * @author JavaSaBr
 */
public class CompilerFactory {

    /**
     * Is available compiler.
     *
     * @return true if a compiler is available.
     */
    public static boolean isAvailableCompiler() {
        return ToolProvider.getSystemJavaCompiler() != null;
    }

    /**
     * New default compiler compiler.
     *
     * @return the compiler.
     */
    @NotNull
    public static Compiler newDefaultCompiler() {
        return new CompilerImpl(true);
    }

    private CompilerFactory() {
        throw new RuntimeException();
    }
}
