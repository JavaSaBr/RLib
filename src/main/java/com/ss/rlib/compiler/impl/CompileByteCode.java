package com.ss.rlib.compiler.impl;

import com.ss.rlib.compiler.ByteSource;
import com.ss.rlib.compiler.Compiler;
import org.jetbrains.annotations.NotNull;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * The implementation of byte code container.
 *
 * @author JavaSaBr
 */
public class CompileByteCode extends SimpleJavaFileObject implements ByteSource {

    /**
     * The stream with byte code.
     */
    @NotNull
    private final ByteArrayOutputStream outputStream;

    /**
     * Instantiates a new Compile byte code.
     *
     * @param name the name
     */
    public CompileByteCode(final String name) {
        super(URI.create("byte:///" + name.replace('/', '.') + Compiler.CLASS_EXTENSION), Kind.CLASS);
        this.outputStream = new ByteArrayOutputStream();
    }

    @NotNull
    @Override
    public byte[] getByteSource() {
        return outputStream.toByteArray();
    }

    @NotNull
    @Override
    public OutputStream openOutputStream() {
        return outputStream;
    }
}
