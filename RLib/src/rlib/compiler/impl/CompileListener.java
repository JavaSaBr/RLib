package rlib.compiler.impl;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import static rlib.util.ClassUtils.unsafeCast;

/**
 * @author JavaSaBr
 */
public class CompileListener implements DiagnosticListener<JavaFileObject> {

    /**
     * Список рапортов о компиляции классов.
     */
    private final Array<Diagnostic<? extends JavaFileObject>> diagnostics;

    public CompileListener() {
        this.diagnostics = ArrayFactory.newArray(Diagnostic.class);
    }

    /**
     * Очистка рапортов.
     */
    public void clear() {
        diagnostics.clear();
    }

    /**
     * @return список рапортов о компиляции классов.
     */
    public Diagnostic<JavaFileObject>[] getDiagnostics() {
        final Diagnostic[] array = diagnostics.toArray(Diagnostic.class);
        return unsafeCast(array);
    }

    @Override
    public void report(final Diagnostic<? extends JavaFileObject> diagnostic) {
        diagnostics.add(diagnostic);
    }
}
