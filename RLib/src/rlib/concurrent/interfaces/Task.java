package rlib.concurrent.interfaces;

/**
 * Интерфейс для создания запускаемой задачи с получением на вход контейнер
 * локальных объектов.
 * 
 * @author Ronn
 */
public interface Task<L> {

	public void run(L localObjects);
}
