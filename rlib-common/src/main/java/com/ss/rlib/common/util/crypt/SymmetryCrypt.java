package com.ss.rlib.common.util.crypt;

import org.jetbrains.annotations.NotNull;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
     * Instantiates a new Symmetry crypt.
     *
     * @param key the key.
     * @throws NoSuchAlgorithmException     the no such algorithm exception
     * @throws NoSuchPaddingException       the no such padding exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @throws InvalidKeyException          the invalid key exception
     */
    public SymmetryCrypt(@NotNull final String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException {

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
     * @throws ShortBufferException      the short buffer exception
     * @throws IllegalBlockSizeException the illegal block size exception
     * @throws BadPaddingException       the bad padding exception
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
     * @throws ShortBufferException      the short buffer exception
     * @throws IllegalBlockSizeException the illegal block size exception
     * @throws BadPaddingException       the bad padding exception
     */
    public void encrypt(@NotNull final byte[] in, final int offset, final int length, @NotNull final byte[] out)
            throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        ecipher.doFinal(in, offset, length, out, offset);
    }
}
