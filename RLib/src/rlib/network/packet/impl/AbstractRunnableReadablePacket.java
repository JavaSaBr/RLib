package rlib.network.packet.impl;

import java.util.concurrent.ExecutorService;

import rlib.util.pools.FoldablePool;

/**
 * Базовая реализация читаемого пакета с реализацией интерфейса Runnable для выполнения в {@link
 * ExecutorService}.
 *
 * @author Ronn
 */
public abstract class AbstractRunnableReadablePacket<C> extends AbstractReadablePacket<C> implements Runnable {

    /**
     * Можно переопределить метод и отдавать пул для автоматического складывания этого пакета в него
     * после выполнения.
     *
     * @return пулл для складывания этого пакета. может быть <code>null</code>.
     */
    @SuppressWarnings("rawtypes")
    protected abstract FoldablePool getPool();

    /**
     * @return нужно ли выполнять синхронно пакет.
     */
    public boolean isSynchronized() {
        return false;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void run() {
        try {
            runImpl();
        } catch (final Exception e) {
            LOGGER.warning(this, e);
        } finally {

            final FoldablePool pool = getPool();

            if (pool != null) {
                pool.put(this);
            }
        }
    }

    /**
     * Процесс выполнение пакета.
     */
    protected abstract void runImpl();
}
