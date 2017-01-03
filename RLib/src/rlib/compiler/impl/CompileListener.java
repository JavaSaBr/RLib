package rlib.compiler.impl;

import static rlib.util.ClassUtils.unsafeCast;

import org.jetbrains.annotations.NotNull;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * The listener of compile evenets.
 *
 * @author JavaSaBr
 */
public class CompileListener implements DiagnosticListener<JavaFileObject> {

    /**
     * The list of diagnostic reports.
     */
    @NotNull
    private final Array<Diagnostic<? extends JavaFileObject>> diagnostics;

    public CompileListener() {
        this.diagnostics = ArrayFactory.newArray(Diagnostic.class);
    }

    /**
     * Clear reports.
     */
    public void clear() {
        diagnostics.clear();
    }

    /**
     * @return the list of diagnostic reports.
     */
    @NotNull
    public Diagnostic<JavaFileObject>[] getDiagnostics() {
        final Diagnostic[] array = diagnostics.toArray(Diagnostic.class);
        return unsafeCast(array);
    }

    @Override
    public void report(@NotNull final Diagnostic<? extends JavaFileObject> diagnostic) {
        diagnostics.add(diagnostic);
    }
}
