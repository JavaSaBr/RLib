package rlib.compiler;

import javax.tools.ToolProvider;

/**
 * @author Ronn
 */
public class Compilers {

	/**
	 * @return доступен ли компилятор.
	 */
	public static boolean isAvailableCompiler() {
		return ToolProvider.getSystemJavaCompiler() != null;
	}

	public static final Compiler newDefaultCompiler() {
		return new CompilerImpl(true);
	}

	private Compilers() {
		throw new RuntimeException();
	}
}
