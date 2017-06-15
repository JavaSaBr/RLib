package com.ss.rlib.compiler.impl;

import org.jetbrains.annotations.NotNull;

import com.ss.rlib.compiler.ByteSource;
import com.ss.rlib.util.Utils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;

/**
 * The implementation of a class loader of compiled classes.
 *
 * @author JavaSaBr
 */
public class CompileClassLoader extends ClassLoader {

    /**
     * The list of byte codes of loaded classes.
     */
    @NotNull
    private final Array<ByteSource> byteCode;

    /**
     * Instantiates a new Compile class loader.
     */
    public CompileClassLoader() {
        this.byteCode = ArrayFactory.newArray(ByteSource.class);
    }

    /**
     * Add a new compiled class.
     *
     * @param byteSource the byte source
     */
    public void addByteCode(@NotNull final ByteSource byteSource) {
        byteCode.add(byteSource);
    }

    @Override
    protected Class<?> findClass(@NotNull final String name) throws ClassNotFoundException {

        synchronized (byteCode) {
            if (byteCode.isEmpty()) return null;
            for (final ByteSource byteSource : byteCode) {
                final byte[] bytes = byteSource.getByteSource();
                return Utils.get(() -> defineClass(name, bytes, 0, bytes.length));
            }
        }

        return null;
    }
}
