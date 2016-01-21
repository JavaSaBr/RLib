package rlib.compiler.impl;

import rlib.compiler.ByteSource;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * Загрузчик откомпиленных классов в рантайме.
 *
 * @author Ronn
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

        final Array<ByteSource> byteCode = getByteCode();

        if (byteCode.isEmpty()) {
            return null;
        }

        synchronized (byteCode) {

            for (final ByteSource byteSource : byteCode) {

                final byte[] bytes = byteSource.getByteSource();

                Class<?> result = null;

                try {
                    result = defineClass(name, bytes, 0, bytes.length);
                } catch (ClassFormatError | NoClassDefFoundError e) {
                    continue;
                }

                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    /**
     * @return байт код загруженных классов.
     */
    private Array<ByteSource> getByteCode() {
        return byteCode;
    }
}
