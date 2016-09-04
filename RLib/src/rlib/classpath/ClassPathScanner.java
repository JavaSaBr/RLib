package rlib.classpath;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import rlib.util.array.Array;

/**
 * Сканер classpath для последущего поиска нужных классов.
 *
 * @author JavaSaBr
 */
public interface ClassPathScanner {

    String JAR_EXTENSION = ".jar";

    /**
     * Добавить в сканнер дополнительные классы.
     *
     * @param classes добавляемые классы.
     */
    void addClasses(@NotNull Array<Class<?>> classes);

    /**
     * Добавить в сканнер дополнительные ресурсы.
     *
     * @param resources добавляемые ресурсы.
     */
    void addResources(@NotNull Array<String> resources);

    /**
     * Найти все реализации указанного интерфейса.
     *
     * @param container      контейнер классов.
     * @param interfaceClass интересуемый интерфейс.
     */
    <T, R extends T> void findImplements(@NotNull Array<Class<R>> container, @NotNull Class<T> interfaceClass);

    /**
     * Найти всех наследников указанного класса.
     *
     * @param container   контейнер классов.
     * @param parentClass наследуемый класс.
     */
    <T, R extends T> void findInherited(@NotNull Array<Class<R>> container, @NotNull Class<T> parentClass);

    /**
     * Получить все найденные классы.
     *
     * @param container контейнер классов.
     */
    void getAll(@NotNull Array<Class<?>> container);

    /**
     * Получить все найденные ресурсы.
     *
     * @param container контейнер ресурсов.
     */
    void getAllResources(@NotNull Array<String> container);

    /**
     * Запустить сканирование classpath.
     */
    void scanning(@NotNull Function<String, Boolean> filter);
}
