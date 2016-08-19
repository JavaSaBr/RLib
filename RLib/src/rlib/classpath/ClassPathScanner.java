package rlib.classpath;

import java.util.function.Function;

import rlib.util.array.Array;

/**
 * Сканер classpath для последущего поиска нужных классов.
 *
 * @author JavaSaBr
 */
public interface ClassPathScanner {

    public static final String JAR_EXTENSION = ".jar";

    /**
     * Добавить в сканнер дополнительные классы.
     *
     * @param classes добавляемые классы.
     */
    public void addClasses(Array<Class<?>> classes);

    /**
     * Добавить в сканнер дополнительные ресурсы.
     *
     * @param resources добавляемые ресурсы.
     */
    public void addResources(Array<String> resources);

    /**
     * Найти все реализации указанного интерфейса.
     *
     * @param container      контейнер классов.
     * @param interfaceClass интересуемый интерфейс.
     */
    public <T, R extends T> void findImplements(Array<Class<R>> container, Class<T> interfaceClass);

    /**
     * Найти всех наследников указанного класса.
     *
     * @param container   контейнер классов.
     * @param parentClass наследуемый класс.
     */
    public <T, R extends T> void findInherited(Array<Class<R>> container, Class<T> parentClass);

    /**
     * Получить все найденные классы.
     *
     * @param container контейнер классов.
     */
    public void getAll(Array<Class<?>> container);

    /**
     * Получить все найденные ресурсы.
     *
     * @param container контейнер ресурсов.
     */
    public void getAllResources(Array<String> container);

    /**
     * Запустить сканирование classpath.
     */
    public void scanning(Function<String, Boolean> filter);
}
