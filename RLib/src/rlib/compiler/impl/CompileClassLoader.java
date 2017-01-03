package rlib.compiler.impl;

import org.jetbrains.annotations.NotNull;

import rlib.compiler.ByteSource;
import rlib.util.Util;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

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

    public CompileClassLoader() {
        this.byteCode = ArrayFactory.newArray(ByteSource.class);
    }

    /**
     * Add a new compiled class.
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
                return Util.safeGet(() -> defineClass(name, bytes, 0, bytes.length));
            }
        }

        return null;
    }
}
