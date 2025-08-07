package javasabr.rlib.network.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.stream.IntStream;
import javasabr.rlib.common.util.Utils;
import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.network.BufferAllocator;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author JavaSaBr
 */
public class NetworkUtils {

    public static final @NotNull ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0);

    public static class AllTrustManager implements X509TrustManager {

        public static final @NotNull X509Certificate[] EMPTY_CERTS = new X509Certificate[0];

        public @NotNull X509Certificate[] getAcceptedIssuers() {
            return EMPTY_CERTS;
        }

        public void checkClientTrusted(@NotNull X509Certificate[] certificates, @NotNull String arg1) { }
        public void checkServerTrusted(@NotNull X509Certificate[] certificates, @NotNull String arg1) { }
    }

    public static @NotNull SocketAddress getRemoteAddress(@NotNull AsynchronousSocketChannel socketChannel) {
        return Utils.uncheckedGet(socketChannel, AsynchronousSocketChannel::getRemoteAddress);
    }

    public static @NotNull SSLContext createSslContext(
        @NotNull InputStream keyStoreData,
        @NotNull String keyStorePassword
    ) {
        return createSslContext(
            "PKCS12",
            keyStoreData,
            keyStorePassword,
            null,
            null,
            null
        );
    }

    public static @NotNull SSLContext createSslContext(
        @NotNull String keyStoreType,
        @NotNull InputStream keyStoreData,
        @NotNull String keyStorePassword,
        @Nullable String trustStoreType,
        @Nullable InputStream trustStoreData,
        @Nullable String trustStorePassword
    ) {

        try {

            var keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(keyStoreData, keyStorePassword.toCharArray());

            var kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keyStorePassword.toCharArray());

            var keyManagers = kmf.getKeyManagers();

            TrustManager[] trustManagers = null;

            if (trustStoreType != null && trustStoreData != null) {

                var trustStore = KeyStore.getInstance(trustStoreType);
                trustStore.load(trustStoreData, trustStorePassword == null ? null : trustStorePassword.toCharArray());

                var tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(trustStore);

                trustManagers = tmf.getTrustManagers();
            }

            var sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagers, trustManagers, new SecureRandom());

            return sslContext;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull SSLContext createAllTrustedClientSslContext() {

        try {

            var sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, ArrayFactory.toArray(new AllTrustManager()), new SecureRandom());

            return sslContext;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Prepare a string like 'HEX DUMP' by passed byte buffer.
     *
     * @param buffer the byte buffer.
     * @return the hex dump string.
     * @since 9.9.0
     */
    public static @NotNull String hexDump(@NotNull ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return "<empty data>";
        } else {
            return hexDump(buffer.array(), buffer.position(), buffer.limit());
        }
    }

    /**
     * Prepare a string like 'HEX DUMP' by passed buffer and ssl engine result.
     *
     * @param buffer the byte buffer.
     * @param result the ssl engine result.
     * @return the hex dump string.
     * @since 9.9.0
     */
    public static @NotNull String hexDump(@NotNull ByteBuffer buffer, @NotNull SSLEngineResult result) {
        if (result.bytesProduced() < 1) {
            return "<empty data>";
        } else {
            return hexDump(buffer.array(), 0, result.bytesProduced());
        }
    }

    /**
     * Prepare a string like 'HEX DUMP' by passed array.
     *
     * @param array the bytes array.
     * @param length the length.
     * @return the hex dump string.
     */
    public static @NotNull String hexDump(@NotNull byte[] array, int length) {
        return hexDump(array, 0, length);
    }

    /**
     * Prepare a string like 'HEX DUMP' by passed array.
     *
     * @param array  the byte array.
     * @param offset the offset.
     * @param length the length.
     * @return the hex dump string.
     */
    public static @NotNull String hexDump(@NotNull byte[] array, int offset, int length) {

        var builder = new StringBuilder();
        var count = 0;
        var end = length - 1;
        var chars = new char[16];

        for (int g = 0; g < 16; g++) {
            chars[g] = '.';
        }

        hexDigit(builder, (byte) ((offset >>> 8) & 0xFF));
        hexDigit(builder, (byte) (offset & 0xFF));
        builder.append(": ");

        for (int i = offset; i < length; i++) {

            byte val = array[i];

            char ch;

            if (val < ' ' || val > 'z') {
                ch = '.';
            } else {
                ch = (char) val;
            }

            if (i == end) {

                chars[count] = ch;

                hexDigit(builder, val)
                    .append("   ".repeat(15 - count))
                    .append("    ");

                if (count < 9) {
                    builder.append("  ");
                }

                builder.append(chars);

            } else if (count < 15) {

                chars[count++] = ch;
                hexDigit(builder, val).append(' ');

                if (count == 8) {
                    builder.append("  ");
                }

            } else {

                chars[15] = ch;

                hexDigit(builder, val)
                    .append("    ")
                    .append(chars)
                    .append('\n');

                var nextPos = i + 1;

                hexDigit(builder, (byte) ((nextPos >>> 8) & 0xFF));
                hexDigit(builder, (byte) (nextPos & 0xFF));
                builder.append(": ");

                count = 0;

                for (int g = 0; g < 16; g++) {
                    chars[g] = '.';
                }
            }
        }

        return builder.toString();
    }

    private static @NotNull StringBuilder hexDigit(@NotNull StringBuilder builder, byte value) {

        char ch = (char) ((value >> 4) & 0xF);

        if (ch > 9) {
            ch = (char) ((ch - 10) + 'A');
        } else {
            ch = (char) (ch + '0');
        }

        builder.append(ch);

        ch = (char) (value & 0xF);

        if (ch > 9) {
            ch = (char) ((ch - 10) + 'A');
        } else {
            ch = (char) (ch + '0');
        }

        builder.append(ch);

        return builder;
    }

    /**
     * Check a network port to know is available it or not to open a new socket.
     *
     * @param port the port.
     * @return true if the port is available.
     */
    public static boolean isPortAvailable(int port) {
        return isPortAvailable("*", port);
    }

    /**
     * Check a network port to know is available it or not to open a new socket.
     *
     * @param host the host.
     * @param port the port.
     * @return true if the port is available.
     */
    public static boolean isPortAvailable(@NotNull String host, int port) {
        try (var ignored = "*".equals(host) ? new ServerSocket(port) :
            new ServerSocket(port, 50, InetAddress.getByName(host))) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Get a nearest available network port from a start port.
     *
     * @param port the start port.
     * @return the nearest available network port or -1.
     */
    public static int getAvailablePort(int port) {
        return IntStream.range(port, Short.MAX_VALUE * 2)
            .filter(NetworkUtils::isPortAvailable)
            .findFirst()
            .orElse(-1);
    }

    public static @NotNull ByteBuffer enlargePacketBuffer(
        @NotNull BufferAllocator allocator,
        @NotNull SSLEngine engine
    ) {
        return allocator.takeBuffer(engine.getSession().getPacketBufferSize());
    }

    public static @NotNull ByteBuffer increaseApplicationBuffer(
        @NotNull ByteBuffer current,
        @NotNull BufferAllocator allocator,
        @NotNull SSLEngine engine
    ) {

        var newBuffer = allocator.takeBuffer(engine.getSession().getApplicationBufferSize());
        newBuffer.put(current);

        if (current.capacity() > 0) {
            allocator.putBuffer(current);
        }

        return newBuffer;
    }

    public static @NotNull ByteBuffer increasePacketBuffer(
        @NotNull ByteBuffer current,
        @NotNull BufferAllocator allocator,
        @NotNull SSLEngine engine
    ) {

        var newBuffer = allocator.takeBuffer(engine.getSession().getPacketBufferSize());
        newBuffer.put(current);

        if (current.capacity() > 0) {
            allocator.putBuffer(current);
        }

        return newBuffer;
    }

    public static @NotNull ByteBuffer enlargeApplicationBuffer(
        @NotNull BufferAllocator allocator,
        @NotNull SSLEngine engine
    ) {
        return allocator.takeBuffer(engine.getSession().getApplicationBufferSize());
    }
}
