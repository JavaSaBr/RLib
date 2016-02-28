package rlib.classpath;

import java.util.function.Function;

import rlib.util.array.Array;

/**
 * Сканер classpath для последущего поиска нужных классов.
 * 
 * @author Ronn
 */
public interface ClassPathScanner {

	public static final String JAR_EXTENSION = ".jar";

	/**
	 * Добавить в сканнер дополнительные классы.
	 * 
	 * @param added добавляемые классы.
	 */
	public void addClasses(Array<Class<?>> added);

    public void addResources(Array<String> added);

	/**
	 * Найти все реализации указанного интерфейса.
	 * 
	 * @param container контейнер классов.
	 * @param interfaceClass интересуемый интерфейс.
	 */
	public <T, R extends T> void findImplements(Array<Class<R>> container, Class<T> interfaceClass);

	/**
	 * Найти всех наследников указанного класса.
	 * 
	 * @param container контейнер классов.
	 * @param parentClass наследуемый класс.
	 */
	public <T, R extends T> void findInherited(Array<Class<R>> container, Class<T> parentClass);

	/**
	 * Получить все найденные классы.
	 * 
	 * @param container контейнер классов.
	 */
	public void getAll(Array<Class<?>> container);

	public void getAllResources(Array<String> container);

	/**
	 * Запустить сканирование classpath.
	 */
	public void scanning(Function<String, Boolean> filter);
}
