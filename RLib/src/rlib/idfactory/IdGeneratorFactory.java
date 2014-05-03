package rlib.idfactory;

import java.util.concurrent.ScheduledExecutorService;

import rlib.database.ConnectFactory;
import rlib.idfactory.impl.BitSetIdGenerator;
import rlib.idfactory.impl.SimpleIdGenerator;

/**
 * Фабрика ид генераторов.
 * 
 * @author Ronn
 */
public class IdGeneratorFactory {

	/**
	 * Получение генератора ид на основе BitSet.
	 * 
	 * @param connects фабрика подключения к БД.
	 * @param executor исполнитель.
	 * @param tables таблицы извлекаемых ид из БД.
	 * @return новый генератор.
	 */
	public static IdGenerator newBitSetIdGeneratoe(final ConnectFactory connects, final ScheduledExecutorService executor, final String[][] tables) {
		return new BitSetIdGenerator(connects, executor, tables);
	}

	/**
	 * Создание простого генератора ид в указанном промежутке.
	 * 
	 * @param start стартовый ид.
	 * @param end конечный ид.
	 * @return новый генератор.
	 */
	public static IdGenerator newSimpleIdGenerator(final int start, final int end) {
		return new SimpleIdGenerator(start, end);
	}

	private IdGeneratorFactory() {
		throw new IllegalArgumentException();
	}
}
