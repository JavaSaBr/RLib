package rlib.compiler;

import rlib.compiler.impl.CompilerImpl;

import javax.tools.ToolProvider;

/**
 * Фабрика компиляторов java кода на рантайме.
 *
 * @author Ronn
 */
public class CompilerFactory {

    /**
     * @return доступен ли компилятор.
     */
    public static boolean isAvailableCompiler() {
        return ToolProvider.getSystemJavaCompiler() != null;
    }

    public static final Compiler newDefaultCompiler() {
        return new CompilerImpl(true);
    }

    private CompilerFactory() {
        throw new RuntimeException();
    }
}
