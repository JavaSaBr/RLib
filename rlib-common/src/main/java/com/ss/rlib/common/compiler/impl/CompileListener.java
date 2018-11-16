package com.ss.rlib.common.compiler.impl;

import static com.ss.rlib.common.util.ClassUtils.unsafeCast;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
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
    public @NotNull Diagnostic<JavaFileObject>[] getDiagnostics() {
        final Diagnostic[] array = diagnostics.toArray(Diagnostic.class);
        return notNull(unsafeCast(array));
    }

    @Override
    public void report(@NotNull final Diagnostic<? extends JavaFileObject> diagnostic) {
        diagnostics.add(diagnostic);
    }
}
