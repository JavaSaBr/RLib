package rlib.compiler;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

import rlib.util.array.Array;
import rlib.util.array.Arrays;

public class CompileListener implements DiagnosticListener<JavaFileObject>
{
	/** список рапортов о компиляции классов */
	private final Array<Diagnostic<? extends JavaFileObject>> diagnostics;

	public CompileListener()
	{
		this.diagnostics = Arrays.toArray(Diagnostic.class);
	}

	@Override
	public void report(Diagnostic<? extends JavaFileObject> diagnostic)
	{
		diagnostics.add(diagnostic);
	}

	/**
	 * @return список рапортов о компиляции классов.
	 */
	@SuppressWarnings("unchecked")
	public Diagnostic<JavaFileObject>[] getDiagnostics()
	{
		return diagnostics.toArray(new Diagnostic[diagnostics.size()]);
	}

	/**
	 * Очистка рапортов.
	 */
	public void clear()
	{
		diagnostics.clear();
	}
}
