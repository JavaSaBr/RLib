package com.ss.rlib.network.test;

import static com.ss.rlib.network.NetworkFactory.*;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerLevel;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.ServerNetworkConfig;
import com.ss.rlib.network.impl.DefaultBufferAllocator;
import com.ss.rlib.network.packet.impl.AbstractPacketWriter;
import com.ss.rlib.network.packet.impl.AbstractSSLPacketReader;
import com.ss.rlib.network.packet.impl.AbstractSSLPacketWriter;
import com.ss.rlib.network.packet.impl.StringWritablePacket;
import com.ss.rlib.network.util.NetworkUtils;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.net.ssl.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * The tests of string based network.
 *
 * @author JavaSaBr
 */
public class StringSSLNetworkTest extends BaseNetworkTest {

    private static final Logger LOGGER = LoggerManager.getLogger(StringSSLNetworkTest.class);

    @Test
    @SneakyThrows
    void certificatesTest() {

        System.setProperty("javax.net.debug", "all");

        var keystoreFile = StringSSLNetworkTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
        var sslContext = NetworkUtils.createSslContext(keystoreFile, "test");
        var clientSSLContext = NetworkUtils.createAllTrustedClientSslContext();

        var serverPort = NetworkUtils.getAvailablePort(10000);

        var serverSocketFactory = sslContext.getServerSocketFactory();
        var serverSocket = serverSocketFactory.createServerSocket(serverPort);

        var clientSocketFactory = clientSSLContext.getSocketFactory();
        var clientSocket = (SSLSocket) clientSocketFactory.createSocket("localhost", serverPort);

        var clientSocketOnServer = serverSocket.accept();

        new Thread(() -> Utils.unchecked(() -> {
            var clientOutStream = new PrintWriter(clientSocket.getOutputStream());
            clientOutStream.println("Hello SSL");
            clientOutStream.flush();
        })).start();

        var serverIn = new Scanner(clientSocketOnServer.getInputStream());
        var receivedOnServer = serverIn.next() + " " + serverIn.next();

        Assertions.assertEquals("Hello SSL", receivedOnServer);
    }

    @Test
    @SneakyThrows
    void echoNetworkTest() {

        //System.setProperty("javax.net.debug", "all");

        //LoggerManager.getLogger(AbstractPacketWriter.class).setEnabled(LoggerLevel.DEBUG, true);
        LoggerManager.getLogger(AbstractSSLPacketWriter.class).setEnabled(LoggerLevel.DEBUG, true);
        LoggerManager.getLogger(AbstractSSLPacketReader.class).setEnabled(LoggerLevel.DEBUG, true);

        var keystoreFile = StringSSLNetworkTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
        var sslContext = NetworkUtils.createSslContext(keystoreFile, "test");

        var serverNetwork = newStringDataSSLServerNetwork(
            ServerNetworkConfig.DEFAULT_SERVER,
            new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_CLIENT),
            sslContext
        );

        var serverAddress = serverNetwork.start();
        var counter = new CountDownLatch(90);

        serverNetwork.accepted()
            .flatMap(Connection::receivedEvents)
            .subscribe(event -> {
                var message = event.packet.getData();
                LOGGER.info("Received from client: " + message);
                event.connection.send(new StringWritablePacket("Echo: " + message));
            });

        var clientSslContext = NetworkUtils.createAllTrustedClientSslContext();
        var sslSocketFactory = clientSslContext.getSocketFactory();
        var sslSocket = (SSLSocket) sslSocketFactory.createSocket(serverAddress.getHostName(), serverAddress.getPort());

        var out = new PrintWriter(sslSocket.getOutputStream());
        out.println("Hello SSL");
        out.flush();

       // var in = new Scanner(sslSocket.getInputStream());
        //String next = in.next();

        Assertions.assertTrue(
            counter.await(1000000, TimeUnit.MILLISECONDS),
            "Still wait for " + counter.getCount() + " packets..."
        );

        serverNetwork.shutdown();

        LoggerManager.getLogger(AbstractSSLPacketWriter.class).setEnabled(LoggerLevel.DEBUG, false);
        LoggerManager.getLogger(AbstractSSLPacketReader.class).setEnabled(LoggerLevel.DEBUG, false);
        //LoggerManager.getLogger(AbstractPacketWriter.class).setEnabled(LoggerLevel.DEBUG, false);
    }


    private static @NotNull StringWritablePacket newMessage(int minMessageLength, int maxMessageLength) {
        return new StringWritablePacket(StringUtils.generate(minMessageLength, maxMessageLength));
    }
}
