package rlib.network.packet.impl;

import rlib.concurrent.executor.TaskExecutor;
import rlib.concurrent.task.SimpleTask;
import rlib.util.pools.ReusablePool;

/**
 * Базовая реализация читаемого пакета с реализацией интерфейса {@link SimpleTask} для выполнения в
 * {@link TaskExecutor}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractTaskReadablePacket<C, L> extends AbstractReadablePacket<C> implements SimpleTask<L> {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void execute(final L local, final long currentTime) {
        try {
            executeImpl(local, currentTime);
        } catch (final Exception e) {
            LOGGER.warning(this, e);
        } finally {
            final ReusablePool pool = getPool();
            if (pool != null) pool.put(this);
        }
    }

    /**
     * Процесс выполнение пакета.
     */
    protected abstract void executeImpl(L local, long currentTime);

    /**
     * Можно переопределить метод и отдавать пул для автоматического складывания этого пакета в него
     * после выполнения.
     *
     * @return пулл для складывания этого пакета. может быть <code>null</code>.
     */
    protected abstract ReusablePool getPool();

    /**
     * @return нужно ли выполнять синхронно пакет.
     */
    public boolean isSynchronized() {
        return false;
    }

}
