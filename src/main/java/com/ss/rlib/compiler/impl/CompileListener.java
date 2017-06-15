package com.ss.rlib.compiler.impl;

import com.ss.rlib.util.ClassUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

/**
 * The listener of compile events.
 *
 * @author JavaSaBr
 */
public class CompileListener implements DiagnosticListener<JavaFileObject> {

    /**
     * The list of diagnostic reports.
     */
    @NotNull
    private final Array<Diagnostic<? extends JavaFileObject>> diagnostics;

    /**
     * Instantiates a new Compile listener.
     */
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
     * Get diagnostics.
     *
     * @return the list of diagnostic reports.
     */
    @NotNull
    public Diagnostic<JavaFileObject>[] getDiagnostics() {
        final Diagnostic[] array = diagnostics.toArray(Diagnostic.class);
        return ClassUtils.unsafeCast(array);
    }

    @Override
    public void report(@NotNull final Diagnostic<? extends JavaFileObject> diagnostic) {
        diagnostics.add(diagnostic);
    }
}
