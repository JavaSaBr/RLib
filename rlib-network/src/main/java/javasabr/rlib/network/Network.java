package javasabr.rlib.network;

/**
 * The interface to implement an asynchronous network.
 *
 * @author JavaSaBr
 */
public interface Network<C extends Connection<?, ?>> {

    /**
     * Shutdown this network.
     */
    void shutdown();
}
