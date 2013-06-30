package rlib.util.pools;

/**
 * Интерфейс для реализации механизма складывания объектов в пул.
 * 
 * @author Ronn
 */
public interface Foldable
{
	/**
	 * Очисть объект от занятых ресурсов.
	 */
	public void finalyze();
	
	/**
	 * Реинициализировать объект.
	 */
	public void reinit();
}
