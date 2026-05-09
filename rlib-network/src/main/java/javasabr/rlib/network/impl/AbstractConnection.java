package javasabr.rlib.network.impl;

import static javasabr.rlib.common.util.Utils.unchecked;

import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.StampedLock;
import java.util.function.BiConsumer;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.LockableArray;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.collections.deque.DequeFactory;
import javasabr.rlib.collections.operation.LockableOperations;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.UnsafeConnection;
import javasabr.rlib.network.exception.ConnectionClosedException;
import javasabr.rlib.network.packet.NetworkPacketReader;
import javasabr.rlib.network.packet.NetworkPacketWriter;
import javasabr.rlib.network.packet.ReadableNetworkPacket;
import javasabr.rlib.network.packet.WritableNetworkPacket;
import javasabr.rlib.network.packet.impl.WritablePacketWrapper;
import javasabr.rlib.network.util.NetworkUtils;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * The base implementation of {@link Connection}.
 *
 * @author JavaSaBr
 */
@CustomLog
@Accessors(fluent = true, chain = false)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractConnection<C extends AbstractConnection<C>> implements UnsafeConnection<C> {

  private static class WritablePacketWithFeedback<C extends Connection<C>>
      extends WritablePacketWrapper<CompletableFuture<Boolean>, C> {

    public WritablePacketWithFeedback(CompletableFuture<Boolean> attachment, WritableNetworkPacket<C> packet) {
      super(attachment, packet);
    }
  }

  @Getter
  final String remoteAddress;
  @Getter
  final Network<C> network;
  @Getter
  final BufferAllocator bufferAllocator;
  @Getter
  final AsynchronousSocketChannel channel;

  final Deque<WritableNetworkPacket<C>> pendingPackets;
  final StampedLock lock;
  final AtomicBoolean closed;

  final MutableArray<BiConsumer<C, ? super ReadableNetworkPacket<C>>> validPacketSubscribers;
  final MutableArray<BiConsumer<C, ? super ReadableNetworkPacket<C>>> invalidPacketSubscribers;
  final LockableArray<FluxSink<?>> activeSinks;
  final LockableOperations<LockableArray<FluxSink<?>>> activeSinksOperations;

  final int maxPacketsByRead;

  @Getter
  volatile long lastActivity;

  public AbstractConnection(
      Network<C> network,
      AsynchronousSocketChannel channel,
      BufferAllocator bufferAllocator,
      int maxPacketsByRead) {
    this.bufferAllocator = bufferAllocator;
    this.maxPacketsByRead = maxPacketsByRead;
    this.lock = new StampedLock();
    this.channel = channel;
    this.pendingPackets = DequeFactory.arrayBased(WritableNetworkPacket.class);
    this.network = network;
    this.closed = new AtomicBoolean(false);
    this.validPacketSubscribers = ArrayFactory.copyOnModifyArray(BiConsumer.class);
    this.invalidPacketSubscribers = ArrayFactory.copyOnModifyArray(BiConsumer.class);
    this.activeSinks = ArrayFactory.stampedLockBasedArray(FluxSink.class);
    this.activeSinksOperations = activeSinks.operations();
    this.remoteAddress = String.valueOf(NetworkUtils.getRemoteAddress(channel));
  }

  @Override
  public void onConnected() {}

  protected abstract NetworkPacketReader packetReader();

  protected abstract NetworkPacketWriter packetWriter();

  @Override
  public void onReceiveValidPacket(BiConsumer<C, ? super ReadableNetworkPacket<C>> consumer) {
    validPacketSubscribers.add(consumer);
    network.inNetworkThread(() -> packetReader().startRead());
  }

  @Override
  public void onReceiveInvalidPacket(BiConsumer<C, ? super ReadableNetworkPacket<C>> consumer) {
    invalidPacketSubscribers.add(consumer);
    network.inNetworkThread(() -> packetReader().startRead());
  }

  @Override
  public Flux<ReceivedPacketEvent<C, ? extends ReadableNetworkPacket<C>>> receivedEvents() {
    return Flux.create(this::registerFluxOnReceivedEvents);
  }

  @Override
  public Flux<? extends ReadableNetworkPacket<C>> receivedValidPackets() {
    return Flux.create(this::registerFluxOnReceivedValidPackets);
  }

  @Override
  public Flux<? extends ReadableNetworkPacket<C>> receivedInvalidPackets() {
    return Flux.create(this::registerFluxOnReceivedInvalidPackets);
  }

  protected void registerFluxOnReceivedEvents(
      FluxSink<ReceivedPacketEvent<C, ? extends ReadableNetworkPacket<C>>> sink) {

    BiConsumer<C, ReadableNetworkPacket<C>> validListener =
      (connection, packet) -> sink.next(new ReceivedPacketEvent<>(connection,
        packet, true));
    BiConsumer<C, ReadableNetworkPacket<C>> invalidListener =
        (connection, packet) -> sink.next(new ReceivedPacketEvent<>(connection,
            packet, false));


    validPacketSubscribers.add(validListener);
    invalidPacketSubscribers.add(invalidListener);
    activeSinksOperations.inWriteLock(sink, Collection::add);

    sink.onDispose(() -> {
      validPacketSubscribers.remove(validListener);
      invalidPacketSubscribers.remove(invalidListener);
      activeSinksOperations.inWriteLock(sink, Collection::remove);
    });

    network.inNetworkThread(() -> packetReader().startRead());
  }

  protected void registerFluxOnReceivedValidPackets(FluxSink<? super ReadableNetworkPacket<C>> sink) {
    BiConsumer<C, ReadableNetworkPacket<C>> listener = (connection, packet) -> sink.next(packet);
    validPacketSubscribers.add(listener);
    activeSinksOperations.inWriteLock(sink, Collection::add);
    sink.onDispose(() -> {
      validPacketSubscribers.remove(listener);
      activeSinksOperations.inWriteLock(sink, Collection::remove);
    });
    network.inNetworkThread(() -> packetReader().startRead());
  }

  protected void registerFluxOnReceivedInvalidPackets(FluxSink<? super ReadableNetworkPacket<C>> sink) {
    BiConsumer<C, ReadableNetworkPacket<C>> listener = (connection, packet) -> sink.next(packet);
    invalidPacketSubscribers.add(listener);
    activeSinksOperations.inWriteLock(sink, Collection::add);
    sink.onDispose(() -> {
      invalidPacketSubscribers.remove(listener);
      activeSinksOperations.inWriteLock(sink, Collection::remove);
    });
    network.inNetworkThread(() -> packetReader().startRead());
  }

  @Nullable
  protected WritableNetworkPacket<C> nextPacketToWrite() {
    long stamp = lock.writeLock();
    try {
      return pendingPackets.poll();
    } finally {
      lock.unlockWrite(stamp);
    }
  }

  @Override
  public void close() {
    if (closed.compareAndSet(false, true)) {
      doClose();
    }
  }

  /**
   * Does the process of closing this connection.
   */
  protected void doClose() {
    if (channel.isOpen()) {
      unchecked(channel, AsynchronousChannel::close);
    }
    clearWaitPackets();
    packetReader().close();
    packetWriter().close();
    notifyActiveSinks();
  }

  protected void notifyActiveSinks() {
    Boolean noActiveSinks = activeSinksOperations.getInReadLock(Array::isEmpty);
    if (noActiveSinks) {
      return;
    }
    notifySinksWithError(new ConnectionClosedException(remoteAddress));
    activeSinksOperations.inWriteLock(Collection::clear);
  }

  protected void notifySinksWithError(Throwable error) {
    Array<FluxSink<?>> localActiveSinks = activeSinksOperations.getInReadLock(Array::copyOf);
    localActiveSinks.iterations().forEach(
        error, (sink, exc) -> {
          try {
            sink.error(exc);
          } catch (RuntimeException e) {
            log.error(e.getMessage(), "Failed to notify sink of connection closure: "::formatted);
          }
        });
  }

  /**
   * Update the time of last activity.
   */
  protected void updateLastActivity() {
    this.lastActivity = System.currentTimeMillis();
  }

  @Override
  public boolean closed() {
    return closed.get();
  }

  protected void serializedPacket(WritableNetworkPacket<?> packet) {}

  protected void handleReceivedValidPacket(ReadableNetworkPacket<C> packet) {
    log.debug(packet, remoteAddress, "Handle received valid packet:[%s] from:[%s]"::formatted);
    validPacketSubscribers
        .iterations()
        .forEach((C) this, packet, BiConsumer::accept);
  }

  protected void handleReceivedInvalidPacket(ReadableNetworkPacket<C> packet) {
    log.debug(packet, remoteAddress, "Handle failed received packet:[%s] from:[%s]"::formatted);
    invalidPacketSubscribers
        .iterations()
        .forEach((C) this, packet, BiConsumer::accept);
  }

  protected void handleSentPacket(WritableNetworkPacket<?> packet, boolean result) {
    if (packet instanceof WritablePacketWithFeedback<?> withFeedback) {
      withFeedback
          .getAttachment()
          .complete(result);
    }
  }

  @Override
  public final void sendInBackground(WritableNetworkPacket<C> packet) {
    sendImpl(packet);
  }

  protected void sendImpl(WritableNetworkPacket<C> packet) {
    if (closed()) {
      return;
    }
    long stamp = lock.writeLock();
    try {
      pendingPackets.addLast(packet);
    } finally {
      lock.unlockWrite(stamp);
    }
    packetWriter().tryToSendNextPacket();
  }

  protected void queueAtFirst(WritableNetworkPacket<C> packet) {
    long stamp = lock.writeLock();
    try {
      pendingPackets.addFirst(packet);
    } finally {
      lock.unlockWrite(stamp);
    }
  }

  @Override
  public CompletableFuture<Boolean> sendAsync(WritableNetworkPacket<C> packet) {
    var asyncResult = new CompletableFuture<Boolean>();
    sendImpl(new WritablePacketWithFeedback<>(asyncResult, packet));
    if (closed()) {
      return CompletableFuture.completedFuture(Boolean.FALSE);
    }
    return asyncResult;
  }

  /**
   * Clear waited packets.
   */
  protected void clearWaitPackets() {
    long stamp = lock.writeLock();
    try {
      doClearWaitPackets();
    } finally {
      lock.unlockWrite(stamp);
    }
  }

  protected void doClearWaitPackets() {
    for (var pendingPacket : pendingPackets) {
      handleSentPacket(pendingPacket, false);
    }
    pendingPackets.clear();
  }

  @Override
  public String toString() {
    return "{" + remoteAddress + '}';
  }
}
