package rlib.compiler;

import javax.tools.ToolProvider;

import rlib.compiler.impl.CompilerImpl;

/**
 * Фабрика компиляторов java кода в на рантайме.
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
