package rlib.compiler;

/**
 * Интерфейс для реализации хранилища байткода скомпиленного класса.
 * 
 * @author Ronn
 */
public interface ByteSource {

	/**
	 * @return байт код скомпиленного класса.
	 */
	public byte[] getByteSource();
}
