package javasabr.rlib.network.packet.impl;

import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.IdBasedReadablePacket;
import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
public abstract class AbstractIdBasedReadablePacket<C extends Connection<?, ?>,
    S extends AbstractIdBasedReadablePacket<C, S>> extends
    AbstractReadablePacket<C> implements IdBasedReadablePacket<S> {

  private static final Logger LOGGER = LoggerManager.getLogger(AbstractIdBasedReadablePacket.class);

  @Override
  public void execute(@NotNull Connection<?, ?> connection) {
    try {
      executeImpl(ClassUtils.unsafeNNCast(connection));
    } catch (Exception e) {
      LOGGER.error(e);
    }
  }

  protected void executeImpl(@NotNull C connection) {
  }
}
