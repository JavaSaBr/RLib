package rlib.network;

/**
 * Интерфейся для конфигурирования асинхронной сети.
 *
 * @author JavaSaBr
 */
public interface NetworkConfig {

    /**
     * @return название группы сети.
     */
    default String getGroupName() {
        return "NetworkThread";
    }

    /**
     * @return кол-во потоков в группе сети.
     */
    default int getGroupSize() {
        return 1;
    }

    /**
     * @return размер читаемого буфера сети.
     */
    default int getReadBufferSize() {
        return 2048;
    }

    /**
     * @return использовать ли буфера байтов в нативной памяти.
     */
    default boolean isDirectByteBuffer() {
        return false;
    }

    /**
     * @return класс потоков сети
     */
    default Class<? extends Thread> getThreadClass() {
        return Thread.class;
    }

    /**
     * @return приоритет потокв сети.
     */
    default int getThreadPriority() {
        return Thread.NORM_PRIORITY;
    }

    /**
     * @return размер записываемого буфера.
     */
    default int getWriteBufferSize() {
        return 2048;
    }

    /**
     * @return отображать ли ошибки при чтении из сети.
     */
    default boolean isVisibleReadException() {
        return false;
    }

    /**
     * @return отображать ли ошибки при записи в сеть.
     */
    default boolean isVisibleWriteException() {
        return false;
    }
}
