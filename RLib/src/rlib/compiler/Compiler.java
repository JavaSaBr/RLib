package rlib.compiler;

import java.io.File;
import java.nio.file.Path;

/**
 * Интерфейс для реализации компилятора.
 *
 * @author Ronn
 */
public interface Compiler {

    public static final String SOURCE_EXTENSION = ".java";
    public static final String CLASS_EXTENSION = ".class";

    /**
     * Скомпилировать указанные файлы.
     *
     * @param files список файлов исходников.
     * @return список полученных классов.
     */
    public Class<?>[] compile(File... files);

    /**
     * Скомпилировать указанные файлы.
     *
     * @param paths список файлов исходников.
     * @return список полученных классов.
     */
    public Class<?>[] compile(Path... paths);

    /**
     * Скомпилировать все классы в указанных дерикториях.
     *
     * @param files список дерикторий.
     * @return скомпилированные классы.
     */
    public Class<?>[] compileDirectory(File... files);

    /**
     * Скомпилировать все классы в указанных дерикториях.
     *
     * @param paths список дерикторий.
     * @return скомпилированные классы.
     */
    public Class<?>[] compileDirectory(Path... paths);
}
