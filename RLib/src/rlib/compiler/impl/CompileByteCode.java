package rlib.compiler.impl;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

import rlib.compiler.ByteSource;
import rlib.compiler.Compiler;

/**
 * Контейнер байткода класса в памяти.
 *
 * @author JavaSaBr
 */
public class CompileByteCode extends SimpleJavaFileObject implements ByteSource {

    /**
     * Контейнер байткода класса в памяти.
     */
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

    @Override
    public OutputStream openOutputStream() {
        return outputStream;
    }
}
