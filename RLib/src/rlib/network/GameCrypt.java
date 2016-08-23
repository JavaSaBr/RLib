package rlib.network;

/**
 * Интерфейс для реализации криптора/декриптора.
 *
 * @author JavaSaBr
 */
public interface GameCrypt {

    /**
     * Декриптовать массив байтов.
     *
     * @param data   массив байтов.
     * @param offset отступ от начала масива.
     * @param length кол-во необходимых байтов.
     */
    void decrypt(byte[] data, int offset, int length);

    /**
     * Закриптовать массив байтов
     *
     * @param data   массив байтов.
     * @param offset отступ от начала масива.
     * @param length кол-во необходимых байтов.
     */
    void encrypt(byte[] data, int offset, int length);
}
