package javasabr.rlib.network.server;

import java.net.InetSocketAddress;
import java.util.function.Consumer;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

/**
 * The interface to implement a server network.
 *
 * @author JavaSaBr
 */
public interface ServerNetwork<C extends Connection<?, ?>> extends Network<C> {

    /**
     * Start a server using any available address.
     *
     * @return this server's address.
     */
    @NotNull InetSocketAddress start();

    /**
     * Start a server by the address.
     *
     * @param serverAddress the sever address.
     * @param <S>           the server network's type.
     * @return this network.
     */
    <S extends ServerNetwork<C>> @NotNull S start(@NotNull InetSocketAddress serverAddress);

    /**
     * Register a consumer of new connections.
     *
     * @param consumer the consumer of new connections.
     */
    void onAccept(@NotNull Consumer<? super C> consumer);

    /**
     * Get a stream of new accepted connections.
     *
     * @return the stream of new accepted connections.
     */
    @NotNull Flux<? extends C> accepted();
}
