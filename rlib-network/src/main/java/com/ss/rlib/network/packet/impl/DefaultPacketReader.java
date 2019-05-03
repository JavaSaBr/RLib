package com.ss.rlib.network.packet.impl;

import static java.lang.Math.min;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.AsyncConnection;
import com.ss.rlib.network.NetworkCrypt;
import com.ss.rlib.network.impl.AbstractAsyncConnection;
import com.ss.rlib.network.packet.PacketReader;
import com.ss.rlib.network.packet.ReadablePacket;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class DefaultPacketReader implements PacketReader {

    protected static final Logger LOGGER = LoggerManager.getLogger(DefaultPacketReader.class);

    protected static final int MAX_PACKETS_BY_READ = Integer.parseInt(
        System.getProperty(
            AbstractAsyncConnection.class.getName() + ".maxPacketsByRead",
            "100"
        )
    );

    /**
     * The read handler.
     */
    @NotNull
    private final CompletionHandler<Integer, Void> readHandler = new CompletionHandler<>() {

        @Override
        public void completed(@NotNull Integer result, @NotNull Void attachment) {
            handleReadData(result);
        }

        @Override
        public void failed(@NotNull Throwable exc, @NotNull Void attachment) {
            handleFailedRead(exc);
        }
    };

    protected final AsyncConnection connection;
    protected final AsynchronousSocketChannel channel;

    protected final ByteBuffer readBuffer;
    protected final ByteBuffer swapBuffer;
    protected final ByteBuffer waitBuffer;

    protected final Runnable updateActivityFunction;
    protected final BiConsumer<ReadablePacket, ByteBuffer> readPacketFunction;

    @Override
    public final void startRead() {
        channel.read(readBuffer, null, readHandler);
    }

    /**
     * Read the buffer with received data.
     *
     * @param buffer the buffer with received data
     * @return count of read packets.
     */
    protected int readPacket(@NotNull ByteBuffer buffer) {

        var crypt = connection.getCrypt();

        int resultCount = 0;
        int waitedBytes = waitBuffer.remaining();

        // если есть кусок пакета ожидающего
        if (waitedBytes > 0) {
            takeFromWaitBuffer(buffer, waitBuffer);
        }

        int sizeByteCount = connection.getPacketSizeByteCount();
        int maxPacketsByRead = getMaxPacketsByRead();

        for (int i = 0, limit = 0, size; buffer.remaining() >= sizeByteCount && i < maxPacketsByRead; i++) {

            size = getPacketSize(buffer);
            limit += size;

            // если пакет не вместился в этот буффер
            if (limit > buffer.limit()) {

                var missedBytes = limit - buffer.limit();

                // проверка остались ли данные в ожидаемом буффере для до прочтения и если да,
                // то достаточно ли там байт что бы дочитать пакет
                if (waitedBytes > 0 && waitBuffer.position() > 0 && waitBuffer.remaining() >= missedBytes) {
                    takeMissedFromWaitBuffer(buffer, waitBuffer);
                    limit = size;
                } else {
                    saveDataToWaitBuffer(buffer, waitBuffer, sizeByteCount);
                    break;
                }
            }

            //FIXME
            decrypt(buffer, crypt, buffer.position(), size - sizeByteCount);

            ReadablePacket packet = createPacketFor(buffer);

            if (packet != null) {
                readPacketFunction.accept(packet, buffer);
                resultCount++;
            }
        }

        if (buffer.hasRemaining()) {
            if (buffer.remaining() < sizeByteCount) {
                saveDataToWaitBuffer(buffer, waitBuffer, 0);
            } else {
                LOGGER.warning(this, "Have not read data from the read buffer...");
            }
        }

        // если ожидаеющий буффер фрагментировал
        if (waitBuffer.position() > 0) {
            compactWaitBuffer(waitBuffer);
        }

        return resultCount;
    }

    /**
     * Take waited data from the wait buffer.
     *
     * @param buffer     the read buffer.
     * @param waitBuffer the wait buffer.
     */
    protected void takeFromWaitBuffer(@NotNull ByteBuffer buffer, @NotNull ByteBuffer waitBuffer) {

        int prevLimit = waitBuffer.limit();
        int prevPosition = waitBuffer.position();
        int length = buffer.limit() - buffer.position();

        // add all current data to the wait buffer
        waitBuffer.clear();
        waitBuffer.position(prevLimit);
        waitBuffer.put(buffer.array(), buffer.position(), length);
        waitBuffer.flip();
        waitBuffer.position(prevPosition);

        // clear the read buffer and put result data from wait buffer
        buffer.clear();
        buffer.put(waitBuffer.array(), prevPosition, min(waitBuffer.remaining(), buffer.remaining()));
        buffer.flip();

        // update the position of wait buffer to understand of existing wait data
        waitBuffer.position(waitBuffer.position() + buffer.limit());

        // clear wait buffer if it doesn't have wait data
        if (!waitBuffer.hasRemaining()) {
            waitBuffer.clear();
            waitBuffer.flip();
        }

        /* if (buffer.limit() >= 4) {
            final int size = buffer.getShort() & 0xFFFF;
            final int id = buffer.getShort() & 0xFFFF;
            buffer.position(0);
        } */
    }

    /**
     * Take missed bytes from the wait buffer.
     *
     * @param buffer     the read buffer.
     * @param waitBuffer the wait buffer.
     */
    protected void takeMissedFromWaitBuffer(@NotNull ByteBuffer buffer, @NotNull ByteBuffer waitBuffer) {

        // делаем отступ назад на кол-во байт которых осталось непрочитанных в буффере
        int newPosition = waitBuffer.position() - buffer.remaining();
        int prevLimit = waitBuffer.limit();

        waitBuffer.clear();
        waitBuffer.position(newPosition);

        // добавляем спереди непрочитанный кусок
        waitBuffer.put(buffer.array(), buffer.position(), buffer.remaining());
        waitBuffer.position(newPosition);
        waitBuffer.limit(prevLimit);

        buffer.clear();
        buffer.put(waitBuffer.array(), newPosition, min(waitBuffer.remaining(), buffer.remaining()));
        buffer.flip();

        // сдвигаем позицию на кол-во сколько скинули данных в буффер
        waitBuffer.position(waitBuffer.position() + buffer.limit());

        // очищаем ожидающий буффер если мы его весь прочитали
        if (!waitBuffer.hasRemaining()) {
            waitBuffer.clear();
            waitBuffer.flip();
        }

        /* if (waitBuffer.limit() >= 4) {
            final int size = waitBuffer.getShort() & 0xFFFF;
            final int id = waitBuffer.getShort() & 0xFFFF;
            waitBuffer.position(0);
        } */
    }

    /**
     * Save not read data to the wait buffer.
     *
     * @param buffer        the read buffer.
     * @param waitBuffer    the wait buffer.
     * @param sizeByteCount the byte count of packet size.
     */
    protected void saveDataToWaitBuffer(
        @NotNull ByteBuffer buffer,
        @NotNull ByteBuffer waitBuffer,
        int sizeByteCount
    ) {

        int offset = buffer.position() - sizeByteCount;
        int length = buffer.limit() - offset;

        // если ожидаеющий буффер фрагментировал
        if (waitBuffer.position() > 0) {

            swapBuffer.clear();
            swapBuffer.put(buffer.array(), offset, length);
            swapBuffer.put(waitBuffer.array(), waitBuffer.position(), waitBuffer.remaining());
            swapBuffer.flip();

            waitBuffer.clear();
            waitBuffer.put(swapBuffer.array(), 0, swapBuffer.remaining());
            waitBuffer.flip();

            /*if (waitBuffer.limit() >= 4) {
                final int size = waitBuffer.getShort() & 0xFFFF;
                final int id = waitBuffer.getShort() & 0xFFFF;
                waitBuffer.position(0);
            }*/

        } else {

            waitBuffer.clear();
            waitBuffer.put(buffer.array(), offset, length);
            waitBuffer.flip();

            /* if (waitBuffer.limit() >= 4) {
                final int size = waitBuffer.getShort() & 0xFFFF;
                final int id = waitBuffer.getShort() & 0xFFFF;
                waitBuffer.position(0);
            } */
        }

        buffer.clear().flip();
    }

    /**
     * Decrypt data using the crypt of the connection owner.
     *
     * @param buffer the data buffer.
     * @param crypt  the crypt.
     * @param offset the offset.
     * @param length the length.
     * @return the decrypted data or null if this crypt implementation does encrypting inside the passed byte array.
     */
    protected @Nullable byte[] decrypt(
        @NotNull ByteBuffer buffer,
        @NotNull NetworkCrypt crypt,
        int offset,
        int length
    ) {

        if (crypt.isNull()) {
            return null;
        }

        return crypt.decrypt(buffer.array(), offset, length);
    }

    /**
     * Compact the wait buffer.
     *
     * @param waitBuffer the wait buffer.
     */
    protected void compactWaitBuffer(@NotNull ByteBuffer waitBuffer) {

        swapBuffer.clear();
        swapBuffer.put(waitBuffer.array(), waitBuffer.position(), waitBuffer.remaining());
        swapBuffer.flip();

        waitBuffer.clear();
        waitBuffer.put(swapBuffer.array(), 0, swapBuffer.remaining());
        waitBuffer.flip();

        /* if (waitBuffer.limit() >= 4) {
            final int size = waitBuffer.getShort() & 0xFFFF;
            final int id = waitBuffer.getShort() & 0xFFFF;
            waitBuffer.position(0);
        } */
    }

    /**
     * Handle read data.
     *
     * @param result the count of read bytes.
     */
    protected void handleReadData(@NotNull Integer result) {
        updateActivityFunction.run();

        if (result == -1) {
            connection.close();
            return;
        }

        readBuffer.flip();
        try {

            if (isReadyToRead(readBuffer)) {
                readPacket(readBuffer);
            }

        } catch (Exception e) {
            waitBuffer.clear().flip();
            LOGGER.error(this, e);
        } finally {
            readBuffer.clear();
        }

        connection.startRead();
    }

    /**
     * Handle the exception during reading data.
     *
     * @param exception the exception.
     */
    protected void handleFailedRead(@NotNull Throwable exception) {

        var config = connection.getNetwork()
            .getConfig();

        if (config.isVisibleReadException()) {
            LOGGER.warning(this, exception);
        }

        connection.close();
    }

    /**
     * Check the buffer if this is ready to read.
     *
     * @param buffer the buffer.
     * @return true if the buffer is ready to read .
     */
    protected boolean isReadyToRead(@NotNull ByteBuffer buffer) {
        return true;
    }


    /**
     * Get the how many packets can be read by the one method call readPacket().
     *
     * @return the how many packets can be read by the one method call readPacket().
     */
    protected int getMaxPacketsByRead() {
        return MAX_PACKETS_BY_READ;
    }

    /**
     * Get the data size of the packet.
     *
     * @param buffer the packet data buffer.
     * @return the packet size.
     */
    protected int getPacketSize(@NotNull ByteBuffer buffer) {
        return buffer.getShort() & 0xFFFF;
    }

    /**
     * Create a packet to read the data buffer.
     *
     * @param buffer the buffer
     * @return the readable packet
     */
    protected @Nullable ReadablePacket createPacketFor(@NotNull ByteBuffer buffer) {

        if (buffer.remaining() < 2) {
            return null;
        }

        int packetId = buffer.getShort() & 0xFFFF;

        return connection.getNetwork()
            .getPacketRegistry()
            .findById(packetId);
    }
}
