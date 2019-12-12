package com.ss.rlib.network.packet.impl;

import static com.ss.rlib.network.util.NetworkUtils.hexDump;
import com.ss.rlib.common.function.NotNullConsumer;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.WritablePacket;
import com.ss.rlib.network.util.NetworkUtils;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @param <R> the readable packet's type.
 * @param <C> the connection's type.
 */
public abstract class AbstractSSLPacketReader<R extends ReadablePacket, C extends Connection<R, ?>> extends
    AbstractPacketReader<R, C> {

    private static final Logger LOGGER = LoggerManager.getLogger(AbstractSSLPacketReader.class);

    private static final ByteBuffer[] EMPTY_BUFFERS = {
        NetworkUtils.EMPTY_BUFFER
    };

    private static final int SKIP_READ_PACKETS = -1;

    protected final @NotNull SSLEngine sslEngine;
    protected final @NotNull NotNullConsumer<WritablePacket> packetWriter;

    protected volatile @NotNull ByteBuffer sslNetworkBuffer;
    protected volatile @NotNull ByteBuffer sslDataBuffer;

    protected AbstractSSLPacketReader(
        @NotNull C connection,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull Runnable updateActivityFunction,
        @NotNull NotNullConsumer<? super R> readPacketHandler,
        @NotNull SSLEngine sslEngine,
        @NotNull NotNullConsumer<WritablePacket> packetWriter,
        int maxPacketsByRead
    ) {
        super(connection, channel, bufferAllocator, updateActivityFunction, readPacketHandler, maxPacketsByRead);
        this.sslEngine = sslEngine;
        this.sslDataBuffer = bufferAllocator.takeBuffer(sslEngine.getSession().getApplicationBufferSize());
        this.sslNetworkBuffer = bufferAllocator.takeBuffer(sslEngine.getSession().getPacketBufferSize());
        this.packetWriter = packetWriter;
    }

    @Override
    protected @NotNull ByteBuffer getBufferToReadFromChannel() {
        return sslNetworkBuffer;
    }

    @Override
    protected void handleReadData(@NotNull Integer result) {

        if (result == -1) {
            doHandshake(getBufferToReadFromChannel(), -1);
            return;
        }

        super.handleReadData(result);
    }

    @Override
    protected int readPackets(@NotNull ByteBuffer receivedBuffer) {

        var handshakeStatus = sslEngine.getHandshakeStatus();

        // ssl engine is ready to decrypt
        if (handshakeStatus == HandshakeStatus.FINISHED || handshakeStatus == HandshakeStatus.NOT_HANDSHAKING) {
            return decryptAndRead(receivedBuffer);
        } else {
            return doHandshake(receivedBuffer, receivedBuffer.limit());
        }
    }

    protected int doHandshake(@NotNull ByteBuffer receivedBuffer, int receivedBytes) {

        var handshakeStatus = sslEngine.getHandshakeStatus();

        while (handshakeStatus != HandshakeStatus.FINISHED && handshakeStatus != HandshakeStatus.NOT_HANDSHAKING) {

            SSLEngineResult result;

            switch (handshakeStatus) {
                case NEED_UNWRAP: {

                    if (receivedBytes == -1) {

                        if (sslEngine.isInboundDone() && sslEngine.isOutboundDone()) {
                            return SKIP_READ_PACKETS;
                        }

                        try {
                            sslEngine.closeInbound();
                        } catch (SSLException e) {
                            LOGGER.error("This engine was forced to close inbound, without having received the " +
                                "proper SSL/TLS close notification message from the peer, due to end of stream.");
                        }

                        sslEngine.closeOutbound();
                        // After closeOutbound the engine will be set to WRAP state,
                        // in order to try to send a close message to the client.
                        handshakeStatus = sslEngine.getHandshakeStatus();
                        break;

                    } else if (!receivedBuffer.hasRemaining()) {
                        sslNetworkBuffer.clear();
                        return SKIP_READ_PACKETS;
                    }

                    try {
                        LOGGER.debug(receivedBuffer, buff -> "Try to unwrap data:\n" + hexDump(buff));
                        result = sslEngine.unwrap(receivedBuffer, EMPTY_BUFFERS);
                        handshakeStatus = result.getHandshakeStatus();
                    } catch (SSLException sslException) {
                        LOGGER.error("A problem was encountered while processing the data that caused the " +
                            "SSLEngine to abort. Will try to properly close connection...");
                        LOGGER.error(sslException);
                        sslEngine.closeOutbound();
                        handshakeStatus = sslEngine.getHandshakeStatus();
                        break;
                    }

                    switch (result.getStatus()) {
                        case OK:
                            break;
                        case BUFFER_OVERFLOW:
                            throw new IllegalStateException("Unexpected state");
                        case BUFFER_UNDERFLOW:
                            LOGGER.debug("Increase ssl network buffer");
                            increaseNetworkBuffer();
                            break;
                        case CLOSED:
                            if (sslEngine.isOutboundDone()) {
                                return SKIP_READ_PACKETS;
                            } else {
                                sslEngine.closeOutbound();
                                handshakeStatus = sslEngine.getHandshakeStatus();
                                break;
                            }
                        default:
                            throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
                    }
                    break;
                }
                case NEED_WRAP:
                    LOGGER.debug("Send command to wrap data");
                    packetWriter.accept(SSLWritablePacket.getInstance());
                    sslNetworkBuffer.clear();
                    return SKIP_READ_PACKETS;
                case NEED_TASK:

                    Runnable task;

                    while ((task = sslEngine.getDelegatedTask()) != null) {
                        LOGGER.debug(task, t -> "Execute SSL Engine's task: " + t.getClass());
                        task.run();
                    }

                    handshakeStatus = sslEngine.getHandshakeStatus();

                    if (handshakeStatus == HandshakeStatus.NEED_UNWRAP && !receivedBuffer.hasRemaining()) {
                        sslNetworkBuffer.clear();
                        return SKIP_READ_PACKETS;
                    }

                    break;
                case FINISHED:
                case NOT_HANDSHAKING:
                    break;
                default:
                    throw new IllegalStateException("Invalid SSL status: " + handshakeStatus);
            }
        }

        return decryptAndRead(receivedBuffer);
    }

    protected int decryptAndRead(@NotNull ByteBuffer receivedBuffer) {

        SSLEngineResult result;
        try {
            result = sslEngine.unwrap(receivedBuffer, sslDataBuffer.clear());
        } catch (SSLException e) {
            if (e.getCause() instanceof BadPaddingException) {
                increaseNetworkBuffer();
                return SKIP_READ_PACKETS;
            }
            throw new IllegalStateException(e);
        }

        switch (result.getStatus()) {
            case OK:
                sslDataBuffer.flip();
                LOGGER.debug(sslDataBuffer, buf -> "Decrypted data:\n" + hexDump(buf));
                return readPackets(sslDataBuffer, pendingBuffer);
            case BUFFER_OVERFLOW:
                increaseDataBuffer();
                return decryptAndRead(receivedBuffer);
            case CLOSED:
                closeConnection();
                return SKIP_READ_PACKETS;
            default:
                throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
        }
    }

    private void increaseNetworkBuffer() {
        sslNetworkBuffer = NetworkUtils.increasePacketBuffer(
            sslNetworkBuffer,
            bufferAllocator,
            sslEngine
        );
    }

    private void increaseDataBuffer() {
        sslDataBuffer = NetworkUtils.increaseApplicationBuffer(
            sslDataBuffer,
            bufferAllocator,
            sslEngine
        );
    }

    protected void closeConnection() {
       try {
            sslEngine.closeOutbound();
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
