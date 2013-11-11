package rlib.logging;

/**
 * Интерфейс для реализации логера.
 * 
 * @author Ronn
 */
public interface GameLogger {

	/**
	 * Завершение работы логгера.
	 */
	public void finish();

	/**
	 * Записать текст в лог.
	 * 
	 * @param text записываемый текст.
	 */
	public void write(String text);

	/**
	 * Запись и очистка кэша лога.
	 */
	public void writeCache();
}
