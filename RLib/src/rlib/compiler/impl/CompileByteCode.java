package rlib.compiler.impl;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

import rlib.compiler.ByteSource;
import rlib.compiler.Compiler;

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
