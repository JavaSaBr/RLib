package rlib.network;

/**
 * Интерфейся для конфигурирования асинхронной сети.
 * 
 * @author Ronn
 */
public interface NetworkConfig {

	/**
	 * @return название группы сети.
	 */
	public default String getGroupName() {
		return "NetworkThread";
	}

	/**
	 * @return кол-во потоков в группе сети.
	 */
	public default int getGroupSize() {
		return 1;
	}

	/**
	 * @return размер читаемого буфера сети.
	 */
	public default int getReadBufferSize() {
		return 2048;
	}

	/**
	 * @return класс потоков сети
	 */
	public default Class<? extends Thread> getThreadClass() {
		return Thread.class;
	}

	/**
	 * @return приоритет потокв сети.
	 */
	public default int getThreadPriority() {
		return Thread.NORM_PRIORITY;
	}

	/**
	 * @return размер записываемого буфера.
	 */
	public default int getWriteBufferSize() {
		return 2048;
	}

	/**
	 * @return отображать ли ошибки при чтении из сети.
	 */
	public default boolean isVisibleReadException() {
		return false;
	}

	/**
	 * @return отображать ли ошибки при записи в сеть.
	 */
	public default boolean isVisibleWriteException() {
		return false;
	}
}
