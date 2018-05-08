package com.ss.rlib.common.compiler.impl;

import com.ss.rlib.common.compiler.ByteCode;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

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
    private final Array<ByteCode> byteCode;

    public CompileClassLoader() {
        this.byteCode = ArrayFactory.newArray(ByteCode.class);
    }

    /**
     * Add a new compiled class.
     *
     * @param byteCode the byte code
     */
    public void addByteCode(@NotNull final ByteCode byteCode) {
        this.byteCode.add(byteCode);
    }

    @Override
    protected Class<?> findClass(@NotNull final String name) {

        synchronized (byteCode) {
            if (byteCode.isEmpty()) return null;
            for (final ByteCode byteCode : this.byteCode) {
                final byte[] content = byteCode.getByteCode();
                return Utils.get(() -> defineClass(name, content, 0, content.length));
            }
        }

        return null;
    }
}
