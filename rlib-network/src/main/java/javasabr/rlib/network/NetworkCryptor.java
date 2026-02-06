package javasabr.rlib.network;

import java.nio.ByteBuffer;
import org.jspecify.annotations.Nullable;

/**
 * The interface to implement network data encryption and decryption.
 *
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface NetworkCryptor {

  /**
   * Default NULL implementation that performs no encryption or decryption.
   *
   * @since 10.0.0
   */
  NetworkCryptor NULL = new NetworkCryptor() {

    @Nullable
    @Override
    public ByteBuffer decrypt(ByteBuffer data, int length, ByteBuffer toStore) {
      return null;
    }

    @Nullable
    @Override
    public ByteBuffer encrypt(ByteBuffer data, int length, ByteBuffer toStore) {
      return null;
    }
  };

  /**
   * Decrypts data from the source buffer.
   *
   * @param data the buffer with data to decrypt
   * @param length the data length
   * @param toStore the buffer to store decrypted data
   * @return the buffer with decrypted data or null if decryption is not needed
   * @since 10.0.0
   */
  @Nullable
  ByteBuffer decrypt(ByteBuffer data, int length, ByteBuffer toStore);

  /**
   * Encrypts data from the source buffer.
   *
   * @param data the buffer with data to encrypt
   * @param length the data length
   * @param toStore the buffer to store encrypted data
   * @return the buffer with encrypted data or null if encryption is not needed
   * @since 10.0.0
   */
  @Nullable
  ByteBuffer encrypt(ByteBuffer data, int length, ByteBuffer toStore);
}
