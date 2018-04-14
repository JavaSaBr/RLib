package com.ss.rlib.common.network.client.server;

import com.ss.rlib.common.network.AsyncConnection;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement a server connection.
 *
 * @author JavaSaBr
 */
public interface ServerConnection extends AsyncConnection {

    @Override
    @Nullable Server getOwner();
}
