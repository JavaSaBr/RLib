package rlib.monitoring;

import javax.management.MXBean;

/**
 * Реализация менеджера по мониторингу состояния Java процесса с помощью {@link MXBean}.
 *
 * @author Ronn
 */
public final class MonitoringManager {

    private static MonitoringManager instance;

    public static MonitoringManager getInstance() {

        if (instance == null) {
            instance = new MonitoringManager();
        }

        return instance;
    }

    /**
     * Мониторинг использования памяти в Java процессе.
     */
    private final MemoryMonitoring memoryMonitoring;

    /**
     * Мониторинг работы Java процесса.
     */
    private final RuntimeMonitoring runtimeMonitoring;

    /**
     * Мониторинг состояния потоков Java процесса.
     */
    private final ThreadMonitoring threadMonitoring;

    private MonitoringManager() {
        this.memoryMonitoring = new MemoryMonitoring();
        this.runtimeMonitoring = new RuntimeMonitoring();
        this.threadMonitoring = new ThreadMonitoring();
    }

    /**
     * @return мониторинг использования памяти в Java процессе.
     */
    public MemoryMonitoring getMemoryMonitoring() {
        return memoryMonitoring;
    }

    /**
     * @return мониторинг работы Java процесса.
     */
    public RuntimeMonitoring getRuntimeMonitoring() {
        return runtimeMonitoring;
    }

    /**
     * @return мониторинг состояния потоков Java процесса.
     */
    public ThreadMonitoring getThreadMonitoring() {
        return threadMonitoring;
    }

    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder("\n");
        builder.append("#==========Java Process Info==========#").append('\n');
        builder.append("#=============Memory Info=============#");
        builder.append('\n').append(getMemoryMonitoring()).append('\n');
        builder.append("#============Runtime Info=============#");
        builder.append('\n').append(getRuntimeMonitoring()).append('\n');
        // builder.append("#============Current state============#");
        // builder.append('\n').append(getThreadMonitoring()).append('\n');
        builder.append("#=====================================#");

        return builder.toString();
    }
}
