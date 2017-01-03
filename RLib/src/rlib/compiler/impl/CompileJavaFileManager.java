package rlib.compiler.impl;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * The manager to load byte source of classes.
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

    public CompileJavaFileManager(@NotNull final StandardJavaFileManager fileManager, @NotNull final CompileClassLoader loader) {
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
     * @return the list of names of loaded classes.
     */
    @NotNull
    public String[] getClassNames() {
        return classNames.toArray(new String[classNames.size()]);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(@NotNull final Location location, @NotNull final String name,
                                               @NotNull final Kind kind, @NotNull final FileObject sibling) throws IOException {

        final CompileByteCode byteCode = new CompileByteCode(name);

        loader.addByteCode(byteCode);
        classNames.add(name);

        return byteCode;
    }
}
