package rlib.util.crypt;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;

/**
 * The symmetry crypt based on RC4.
 *
 * @author JavaSaBr
 */
public class SymmetryCrypt {

    /**
     * The crypter.
     */
    @NotNull
    private final Cipher ecipher;

    /**
     * The encrypter.
     */
    @NotNull
    private final Cipher dcipher;

    /**
     * THe secret key.
     */
    @NotNull
    private final SecretKey secretKey;

    /**
     * @param key 8 символов.
     */
    public SymmetryCrypt(@NotNull final String key) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException {

        final Cipher ecipher = Cipher.getInstance("RC4");
        final Cipher dcipher = Cipher.getInstance("RC4");

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

        this.ecipher = ecipher;
        this.dcipher = dcipher;
    }

    /**
     * Decrypt data.
     *
     * @param in     the encrypted data.
     * @param offset the offset.
     * @param length the length.
     * @param out    the buffer to store decrypted data.
     */
    public void decrypt(@NotNull final byte[] in, final int offset, final int length, @NotNull final byte[] out)
            throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        dcipher.doFinal(in, offset, length, out, offset);
    }

    /**
     * Encrypt data.
     *
     * @param in     the decrypted data.
     * @param offset the offset.
     * @param length the length.
     * @param out    the buffer to store encrypted data.
     */
    public void encrypt(@NotNull final byte[] in, final int offset, final int length, @NotNull final byte[] out)
            throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        ecipher.doFinal(in, offset, length, out, offset);
    }
}
