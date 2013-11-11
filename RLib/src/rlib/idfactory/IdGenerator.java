package rlib.idfactory;

/**
 * Интерфейс для реализации генератора ид.
 * 
 * @author Ronn
 */
public interface IdGenerator {

	/**
	 * @return новый свободный ид.
	 */
	public int getNextId();

	/**
	 * Подготовка фабрики.
	 */
	public void prepare();

	/**
	 * Добавление нового освободившегося ид.
	 * 
	 * @param id освободившийся ид.
	 */
	public void releaseId(int id);

	/**
	 * @return кол-во использованных ид.
	 */
	public int usedIds();
}
