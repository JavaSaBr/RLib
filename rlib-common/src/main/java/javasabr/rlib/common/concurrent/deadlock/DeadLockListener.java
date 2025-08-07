package javasabr.rlib.common.concurrent.deadlock;

import java.lang.management.ThreadInfo;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a deadlock listener.
 *
 * @author JavaSaBr
 */
public interface DeadLockListener {

    /**
     * Notify about deadlock detecting.
     *
     * @param info the information about thread.
     */
    void onDetected(@NotNull ThreadInfo info);
}
