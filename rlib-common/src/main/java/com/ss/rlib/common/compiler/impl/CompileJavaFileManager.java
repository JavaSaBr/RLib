package com.ss.rlib.common.compiler.impl;

import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;

/**
 * The manager to load byte code of classes.
 *
 * @author JavaSaBr
 */
public class CompileJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    /**
     * The list of names of loaded classes.
     */
    @NotNull
    private final Array<String> classNames;

    /**
     * The loaded of compiled classes.
     */
    @NotNull
    private final CompileClassLoader loader;

    public CompileJavaFileManager(@NotNull final StandardJavaFileManager fileManager,
                                  @NotNull final CompileClassLoader loader) {
        super(fileManager);
        this.loader = loader;
        this.classNames = ArrayFactory.newArray(String.class);
    }

    /**
     * Clear the list of names of loaded classes.
     */
    public void clear() {
        classNames.clear();
    }

    /**
     * Get class names.
     *
     * @return the list of names of loaded classes.
     */
    public @NotNull String[] getClassNames() {
        return classNames.toArray(new String[classNames.size()]);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(@NotNull final Location location, @NotNull final String name,
                                               @NotNull final Kind kind, @NotNull final FileObject sibling) {

        final CompileByteCode byteCode = new CompileByteCode(name);

        loader.addByteCode(byteCode);
        classNames.add(name);

        return byteCode;
    }
}
