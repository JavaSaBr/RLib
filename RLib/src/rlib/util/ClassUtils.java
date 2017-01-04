package rlib.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * The class with utility methods.
 *
 * @author JavaSaBr
 */
public final class ClassUtils {

    public static <T> Class<T> getClass(final String name) {
        try {
            return unsafeCast(Class.forName(name));
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение конструктора по указанным параметрам указанного класса.
     *
     * @param cs      интересуемый класс.
     * @param classes набор параметров конструктора.
     * @return конструктор класса.
     */
    public static <T> Constructor<T> getConstructor(final Class<?> cs, final Class<?>... classes) {
        try {
            return unsafeCast(cs.getConstructor(classes));
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение конструктора класса по его имени и набору параметров.
     *
     * @param className название класса.
     * @param classes   список параметров конструктора.
     * @return конструктор класса.
     */
    public static <T> Constructor<T> getConstructor(final String className, final Class<?>... classes) {
        try {
            final Class<?> cs = Class.forName(className);
            return unsafeCast(cs.getConstructor(classes));
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создание нового экземпляра класса через стандартный конструктор.
     *
     * @param cs интересуемый класс.
     * @return новый экземпляр класса.
     */
    public static <T> T newInstance(final Class<?> cs) {
        try {
            return unsafeCast(cs.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создание нового экземпляра класса через указанный конструктор.
     *
     * @param constructor конструктор класса.
     * @param objects     набор параметров дял конструктора.
     * @return новый экземпляр класса.
     */
    public static <T> T newInstance(final Constructor<?> constructor, final Object... objects) {
        try {
            return unsafeCast(constructor.newInstance(objects));
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(final String className) {
        try {
            return unsafeCast(Class.forName(className).newInstance());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Небезопасный каст переданного объекта в требуемый тип.
     *
     * @param object объект который надо скастить.
     * @return объект скастенный в нужный тип.
     */
    public static <T> T unsafeCast(final Object object) {
        return (T) object;
    }

    /**
     * Небезопасный каст переданного объекта в требуемый тип.
     *
     * @param object объект который надо скастить.
     * @param type   тип в который хотим скастить объект.
     * @return объект скастенный в нужный тип.
     */
    public static <T> T unsafeCast(final Class<T> type, final Object object) {
        return type.cast(object);
    }

    /**
     * Безопасный каст переданного объекта в требуемый тип.
     *
     * @param object объект который надо скастить.
     * @param type   тип в который хотим скастить объект.
     * @return объект скастенный в нужный тип либо null если он не подходит по типу.
     */
    public static <T> T cast(final Class<T> type, final Object object) {
        return type.isInstance(object) ? type.cast(object) : null;
    }

    private ClassUtils() {
        throw new RuntimeException();
    }
}
