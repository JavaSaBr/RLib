package rlib.network.server.client;

import org.jetbrains.annotations.Nullable;
import rlib.network.ConnectionOwner;

/**
 * The interface to implement a client.
 *
 * @author JavaSaBr
 */
public interface Client<A, P> extends ConnectionOwner {

    /**
     * @return the client account.
     */
    @Nullable
    A getAccount();

    /**
     * @param account the client account.
     */
    void setAccount(@Nullable A account);

    /**
     * @return the owner.
     */
    @Nullable
    P getOwner();

    /**
     * @param owner the owner.
     */
    void setOwner(@Nullable P owner);

    /**
     * Handle successful connect.
     */
    void successfulConnection();
}
