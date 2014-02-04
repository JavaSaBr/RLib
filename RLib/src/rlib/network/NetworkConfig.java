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
	public String getGroupName();

	/**
	 * @return кол-во потоков в группе сети.
	 */
	public int getGroupSize();

	/**
	 * @return размер читаемого буфера сети.
	 */
	public int getReadBufferSize();

	/**
	 * @return класс потоков сети
	 */
	public Class<? extends Thread> getThreadClass();

	/**
	 * @return приоритет потокв сети.
	 */
	public int getThreadPriority();

	/**
	 * @return размер записываемого буфера.
	 */
	public int getWriteBufferSize();

	/**
	 * @return отображать ли эксепшены читаемой сети.
	 */
	public boolean isVesibleReadException();

	/**
	 * @return отображать ли эксепшены записываемой сети.
	 */
	public boolean isVesibleWriteException();
}
