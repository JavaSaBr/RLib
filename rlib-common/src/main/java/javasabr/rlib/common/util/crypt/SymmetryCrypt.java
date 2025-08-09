package javasabr.rlib.common.util.crypt;

import java.io.Serial;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * @author JavaSaBr
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SymmetryCrypt {

  public static final String ALG_RC_4 = "RC4";

  Cipher ecipher;
  Cipher dcipher;

  public SymmetryCrypt(String key)
      throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException {
    this(key, ALG_RC_4);
  }

  public SymmetryCrypt(String key, String algorithm)
      throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException {

    Cipher ecipher = Cipher.getInstance(algorithm);
    Cipher dcipher = Cipher.getInstance(algorithm);

    byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
    var secretKey = new SecretKey() {

      @Serial
      private static final long serialVersionUID = -8907627571317506056L;

      @Override
      public String getAlgorithm() {
        return algorithm;
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

  public void decrypt(byte[] in, int offset, int length, byte[] out)
      throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
    dcipher.doFinal(in, offset, length, out, offset);
  }

  public void encrypt(byte[] in, int offset, int length, byte[] out)
      throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
    ecipher.doFinal(in, offset, length, out, offset);
  }
}
