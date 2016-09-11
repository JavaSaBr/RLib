package rlib.compiler.impl;

import rlib.compiler.ByteSource;
import rlib.util.Util;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * Загрузчик откомпиленных классов в рантайме.
 *
 * @author JavaSaBr
 */
public class CompileClassLoader extends ClassLoader {

    /**
     * Байткод загруженных классов.
     */
    private final Array<ByteSource> byteCode;

    public CompileClassLoader() {
        this.byteCode = ArrayFactory.newArray(ByteSource.class);
    }

    /**
     * Добавить байткод класса.
     */
    public void addByteCode(final ByteSource byteSource) {
        byteCode.add(byteSource);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        synchronized (byteCode) {
            if (byteCode.isEmpty()) return null;

            for (final ByteSource byteSource : byteCode) {
                final byte[] bytes = byteSource.getByteSource();
                final Class<?> result = Util.safeGet(() -> defineClass(name, bytes, 0, bytes.length));
                if (result != null) return result;
            }
        }

        return null;
    }
}
