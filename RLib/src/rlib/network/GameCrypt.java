package rlib.network;

/**
 * Интерфейс для реализации криптора/декриптора.
 * 
 * @author Ronn
 */
public interface GameCrypt
{
	/**
	 * Декриптовать массив байтов.
	 * 
	 * @param data массив байтов.
	 * @param offset отступ от начала масива.
	 * @param size кол-во необходимых байтов.
	 */
	public void decrypt(byte[] data, int offset, int length);
	
	/**
	 * Закриптовать массив байтов
	 * 
	 * @param data массив байтов.
	 * @param offset отступ от начала масива.
	 * @param length кол-во необходимых байтов.
	 */
	public void encrypt(byte[] data, int offset, int length);
}
