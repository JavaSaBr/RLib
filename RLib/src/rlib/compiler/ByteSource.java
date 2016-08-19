package rlib.compiler;

/**
 * Интерфейс для реализации хранилища байткода скомпиленного класса.
 *
 * @author JavaSaBr
 */
public interface ByteSource {

    /**
     * @return байт код скомпиленного класса.
     */
    public byte[] getByteSource();
}
