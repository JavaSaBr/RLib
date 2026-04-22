package javasabr.rlib.network;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javasabr.rlib.network.client.ClientNetwork;
import javasabr.rlib.network.impl.DefaultBufferAllocator;
import javasabr.rlib.network.impl.StringDataMtlsServerConnection;
import javasabr.rlib.network.impl.StringDataSslConnection;
import javasabr.rlib.network.packet.impl.StringReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.StringWritableNetworkPacket;
import javasabr.rlib.network.server.ServerNetwork;
import javasabr.rlib.network.util.NetworkUtils;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for mutual TLS (mTLS) — server requires client certificate.
 *
 * @author JavaSaBr
 */
@CustomLog
public class SslMutualTlsTest extends BaseNetworkTest {

  @Test
  @SneakyThrows
  void shouldAcceptConnectionWithValidClientCertificate() {

    InputStream serverKeystoreStream = SslMutualTlsTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
    InputStream clientCertAsTrustStream = SslMutualTlsTest.class.getResourceAsStream("/ssl/rlib_test_client_cert.p12");

    SSLContext serverSslContext = NetworkUtils.createSslContext(
        "PKCS12", serverKeystoreStream, "test",
        "PKCS12", clientCertAsTrustStream, "testpw");
    ServerNetworkConfig serverConfig = ServerNetworkConfig.SimpleServerNetworkConfig.builder().build();
    BufferAllocator bufferAllocator = new DefaultBufferAllocator(serverConfig);

    ServerNetwork<StringDataMtlsServerConnection> serverNetwork = NetworkFactory.stringDataMtlsServerNetwork(serverConfig, bufferAllocator, serverSslContext);

    InetSocketAddress serverAddress = serverNetwork.start();
    CountDownLatch receivedLatch = new CountDownLatch(1);

    serverNetwork
        .accepted()
        .flatMap(Connection::receivedEvents)
        .subscribe(event -> {
          log.info(((StringReadableNetworkPacket<?>) event.packet()).data(), "mTLS server received: [%s]"::formatted);
          receivedLatch.countDown();
        });

    InputStream clientKeystoreStream = SslMutualTlsTest.class.getResourceAsStream("/ssl/rlib_test_client_cert.p12");
    SSLContext clientSslContext = createClientSslContextWithCert(clientKeystoreStream, "testpw");

    ClientNetwork<StringDataSslConnection> clientNetwork = NetworkFactory.stringDataSslClientNetwork(
        NetworkConfig.DEFAULT_CLIENT,
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT),
        clientSslContext);

    try {
      clientNetwork
          .connectReactive(serverAddress)
          .doOnNext(connection -> connection.sendInBackground(new StringWritableNetworkPacket<>("Hello mTLS")))
          .doOnError(Throwable::printStackTrace)
          .subscribe();

      assertThat(receivedLatch.await(15, TimeUnit.SECONDS))
          .as("Server should receive packet from mTLS client")
          .isTrue();
    } finally {
      serverNetwork.shutdown();
      clientNetwork.shutdown();
    }
  }

  @Test
  @SneakyThrows
  void shouldAcceptConnectionUsingConfigEmbeddedSslContext() {

    InputStream serverKeystoreStream = SslMutualTlsTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
    InputStream clientCertAsTrustStream = SslMutualTlsTest.class.getResourceAsStream("/ssl/rlib_test_client_cert.p12");

    SSLContext serverSslContext = NetworkUtils.createSslContext(
        "PKCS12", serverKeystoreStream, "test",
        "PKCS12", clientCertAsTrustStream, "testpw");
    ServerNetworkConfig serverConfig = ServerNetworkConfig.SimpleServerNetworkConfig.builder().build();
    BufferAllocator bufferAllocator = new DefaultBufferAllocator(serverConfig);

    ServerNetwork<StringDataSslConnection> serverNetwork = NetworkFactory.stringDataSslServerNetwork(serverConfig, bufferAllocator, serverSslContext);

    InetSocketAddress serverAddress = serverNetwork.start();
    CountDownLatch receivedLatch = new CountDownLatch(1);

    serverNetwork
        .accepted()
        .flatMap(Connection::receivedEvents)
        .subscribe(event -> receivedLatch.countDown());

    InputStream clientKeystoreStream = SslMutualTlsTest.class.getResourceAsStream("/ssl/rlib_test_client_cert.p12");
    SSLContext clientSslContext = createClientSslContextWithCert(clientKeystoreStream, "testpw");
    NetworkConfig clientConfig = NetworkConfig.SimpleNetworkConfig.builder().build();
    bufferAllocator = new DefaultBufferAllocator(clientConfig);

    ClientNetwork<StringDataSslConnection> clientNetwork = NetworkFactory.stringDataSslClientNetwork(clientConfig, bufferAllocator, clientSslContext);

    try {
      clientNetwork
          .connectReactive(serverAddress)
          .doOnNext(connection -> connection.sendInBackground(new StringWritableNetworkPacket<>("Hello config-embedded mTLS")))
          .doOnError(Throwable::printStackTrace)
          .subscribe();

      assertThat(receivedLatch.await(15, TimeUnit.SECONDS))
          .as("Server should receive packet when SSL context is embedded in config")
          .isTrue();
    } finally {
      serverNetwork.shutdown();
      clientNetwork.shutdown();
    }
  }

  @SneakyThrows
  private static SSLContext createClientSslContextWithCert(InputStream keystoreStream, String password) {
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    keyStore.load(keystoreStream, password.toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keyStore, password.toCharArray());
    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(kmf.getKeyManagers(), new TrustManager[]{new NetworkUtils.AllTrustManager()}, new SecureRandom());
    return sslContext;
  }

  /**
   * Regression test for the {@code sslEngine.setNeedClientAuth(true)} call
   * in {@code StringDataMtlsServerConnection}.
   */
  @Test
  @SneakyThrows
  void serverShouldRejectClientWithoutCertificateWhenNeedClientAuthIsTrue() {
    InputStream serverKeystoreStream = SslMutualTlsTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
    SSLContext serverSslContext = NetworkUtils.createSslContext(serverKeystoreStream, "test");
    ServerNetworkConfig serverConfig = ServerNetworkConfig.SimpleServerNetworkConfig.builder().build();
    BufferAllocator bufferAllocator = new DefaultBufferAllocator(serverConfig);

    ServerNetwork<StringDataMtlsServerConnection> serverNetwork = NetworkFactory.stringDataMtlsServerNetwork(serverConfig, bufferAllocator, serverSslContext);

    InetSocketAddress serverAddress = serverNetwork.start();
    CountDownLatch dataReceivedByServer = new CountDownLatch(1);

    serverNetwork
        .accepted()
        .flatMap(Connection::receivedEvents)
        .subscribe(event -> dataReceivedByServer.countDown());

    SSLContext clientWithoutCertContext = NetworkUtils.createAllTrustedClientSslContext();
    ClientNetwork<StringDataSslConnection> clientNetwork = NetworkFactory.stringDataSslClientNetwork(
        NetworkConfig.DEFAULT_CLIENT,
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT),
        clientWithoutCertContext);

    try {
      clientNetwork
          .connectReactive(serverAddress)
          .doOnNext(connection -> connection.sendInBackground(new StringWritableNetworkPacket<>("no cert")))
          .subscribe();

      assertThat(dataReceivedByServer.await(5, TimeUnit.SECONDS))
          .as("Server must reject a client that presents no certificate when requireClientAuth=true.")
          .isFalse();
    } finally {
      serverNetwork.shutdown();
      clientNetwork.shutdown();
    }
  }

  @Test
  void plainTcpShouldWorkWhenNoSslContextConfigured() {
    try (TestNetwork<javasabr.rlib.network.impl.StringDataConnection> network = buildStringNetwork()) {
      network.clientToServer.sendInBackground(
          new javasabr.rlib.network.packet.impl.StringWritableNetworkPacket<>("plain TCP"));
      assertThat(network.serverToClient
          .receivedValidPackets()
          .blockFirst(Duration.ofSeconds(5)))
          .as("Plain TCP should still work when no SSL is configured")
          .isNotNull();
    }
  }
}
