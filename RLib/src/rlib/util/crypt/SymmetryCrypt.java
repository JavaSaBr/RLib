package rlib.util.crypt;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Модель симметричного криптора с использованием RC4 алгоритма.
 *
 * @author Ronn
 */
public class SymmetryCrypt {

    /**
     * Криптовщик.
     */
    private volatile Cipher ecipher;

    /**
     * Декриптовщик.
     */
    private volatile Cipher dcipher;

    /**
     * Ключ шифрования
     */
    private volatile SecretKey secretKey;

    /**
     * @param key 8 символов.
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    public SymmetryCrypt(final String key) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException {

        ecipher = Cipher.getInstance("RC4");
        dcipher = Cipher.getInstance("RC4");

        final byte[] bytes = key.getBytes("UTF-8");

        secretKey = new SecretKey() {

            private static final long serialVersionUID = -8907627571317506056L;

            @Override
            public String getAlgorithm() {
                return "RC4";
            }

            @Override
            public byte[] getEncoded() {
                return bytes;
            }

            @Override
            public String getFormat() {
                return "RAW";
            }
        };

        ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
        dcipher.init(Cipher.DECRYPT_MODE, secretKey);
    }

    /**
     * Расшифровать массив байтов.
     *
     * @param in     исходный массив.
     * @param offset отступ в исходном массиве.
     * @param length длинна дешифруемого части.
     * @param out    выходной массив.
     * @throws ShortBufferException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public void decrypt(final byte[] in, final int offset, final int length, final byte[] out) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        dcipher.doFinal(in, offset, length, out, offset);
    }

    /**
     * Зашифровать массив байтов.
     *
     * @param in     исходный массив.
     * @param offset отступ в исходном массиве.
     * @param length длинна шифруемой части.
     * @param out    выходной массив.
     * @throws ShortBufferException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public void encrypt(final byte[] in, final int offset, final int length, final byte[] out) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        ecipher.doFinal(in, offset, length, out, offset);
    }
}
