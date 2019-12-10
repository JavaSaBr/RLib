package com.ss.rlib.network.packet.impl;

import static com.ss.rlib.network.util.NetworkUtils.EMPTY_BUFFER;
import static com.ss.rlib.network.util.NetworkUtils.hexDump;
import com.ss.rlib.common.function.NotNullBiConsumer;
import com.ss.rlib.common.function.NotNullConsumer;
import com.ss.rlib.common.function.NullableSupplier;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.WritablePacket;
import com.ss.rlib.network.util.NetworkUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public abstract class AbstractSSLPacketWriter<W extends WritablePacket, C extends Connection<?, W>> extends
    AbstractPacketWriter<W, C> {

    private static final Logger LOGGER = LoggerManager.getLogger(AbstractSSLPacketWriter.class);

    private static final ByteBuffer[] EMPTY_BUFFERS = {
        NetworkUtils.EMPTY_BUFFER
    };

    protected final @NotNull SSLEngine sslEngine;
    protected final @NotNull NotNullConsumer<WritablePacket> packetWriter;

    protected volatile @NotNull ByteBuffer sslNetworkBuffer;

    public AbstractSSLPacketWriter(
        @NotNull C connection,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull Runnable updateActivityFunction,
        @NotNull NullableSupplier<WritablePacket> packetProvider,
        @NotNull NotNullConsumer<WritablePacket> writtenPacketHandler,
        @NotNull NotNullBiConsumer<WritablePacket, Boolean> sentPacketHandler,
        @NotNull SSLEngine sslEngine,
        @NotNull NotNullConsumer<WritablePacket> packetWriter
    ) {
        super(
            connection,
            channel,
            bufferAllocator,
            updateActivityFunction,
            packetProvider,
            writtenPacketHandler,
            sentPacketHandler
        );
        this.sslEngine = sslEngine;
        this.packetWriter = packetWriter;
        this.sslNetworkBuffer = bufferAllocator.takeBuffer(sslEngine.getSession().getPacketBufferSize());
    }

    @Override
    protected @NotNull ByteBuffer serialize(@NotNull WritablePacket packet) {

        var status = sslEngine.getHandshakeStatus();

        if (status == HandshakeStatus.FINISHED || status == HandshakeStatus.NOT_HANDSHAKING) {

            SSLEngineResult result;
            try {
                result = sslEngine.wrap(super.serialize(packet), sslNetworkBuffer.clear());
            } catch (SSLException e) {
                throw new RuntimeException(e);
            }

            switch (result.getStatus()) {
                case BUFFER_UNDERFLOW:
                    // peerNetData = handleBufferUnderflow(engine, peerNetData);
                    break;
                case BUFFER_OVERFLOW:
                    // peerAppData = enlargeApplicationBuffer(engine, peerAppData);
                    break;
                case OK:
                    return sslNetworkBuffer.flip();
                case CLOSED:
                    closeConnection();
                    return EMPTY_BUFFER;
            }
        }

        ByteBuffer bufferToWrite;
        try {
            bufferToWrite = doHandshake(packet);
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }

        if (bufferToWrite != null) {
            return bufferToWrite;
        }

        throw new IllegalStateException();
    }

    protected @Nullable ByteBuffer doHandshake(@NotNull WritablePacket packet) throws SSLException {

        if (!(packet instanceof SSLWritablePacket)) {
            throw new IllegalStateException();
        }

        var handshakeStatus = sslEngine.getHandshakeStatus();

        while (handshakeStatus != HandshakeStatus.FINISHED && handshakeStatus != HandshakeStatus.NOT_HANDSHAKING) {

            SSLEngineResult result;

            switch (handshakeStatus) {

                case NEED_WRAP:
                    try {
                        // check result
                        result = sslEngine.wrap(EMPTY_BUFFERS, sslNetworkBuffer.clear());
                        handshakeStatus = result.getHandshakeStatus();
                    } catch (SSLException sslException) {
                        LOGGER.error("A problem was encountered while processing the data that caused the SSLEngine " +
                            "to abort. Will try to properly close connection...");
                        sslEngine.closeOutbound();
                        handshakeStatus = sslEngine.getHandshakeStatus();
                        break;
                    }
                    switch (result.getStatus()) {
                        case OK:

                            sslNetworkBuffer.flip();

                            if (handshakeStatus == HandshakeStatus.NEED_WRAP) {
                                LOGGER.debug("Send command to wrap data again");
                                packetWriter.accept(SSLWritablePacket.getInstance());
                            }

                            LOGGER.debug(sslNetworkBuffer,
                                result,
                                (buf, res) -> "Send wrapped data:\n" + hexDump(buf, res)
                            );

                            return sslNetworkBuffer;
                        case BUFFER_OVERFLOW:
                            sslNetworkBuffer = NetworkUtils.enlargePacketBuffer(bufferAllocator, sslEngine);
                            break;
                        case BUFFER_UNDERFLOW:
                            throw new SSLException(
                                "Buffer underflow occured after a wrap. I don't think we should ever get here."
                            );
                        case CLOSED:
                            try {
                                return EMPTY_BUFFER;
                                // At this point the handshake status will probably be NEED_UNWRAP so we make sure that peerNetData is clear to read.
                                //peerNetData.clear();
                            } catch (Exception e) {
                                LOGGER.error("Failed to send server's CLOSE message due to socket channel's failure.");
                                handshakeStatus = sslEngine.getHandshakeStatus();
                            }
                            break;
                        default:
                            throw new IllegalStateException("Invalid SSL status: " + result.getStatus());
                    }
                    break;
                case NEED_TASK:
                    Runnable task;
                    while ((task = sslEngine.getDelegatedTask()) != null) {
                        task.run();
                    }
                    handshakeStatus = sslEngine.getHandshakeStatus();
                    break;
                case FINISHED:
                    break;
                case NOT_HANDSHAKING:
                    break;
                case NEED_UNWRAP:
                    return EMPTY_BUFFER;
                default:
                    throw new IllegalStateException("Invalid SSL status: " + handshakeStatus);
            }
        }

        return null;
    }

    protected void closeConnection() {
        /**try {
            sslEngine.closeOutbound();
            doHandshake(NULL_PACKET);
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }
}
