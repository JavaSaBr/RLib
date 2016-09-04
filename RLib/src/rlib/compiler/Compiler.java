package rlib.compiler;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

/**
 * Интерфейс для реализации компилятора.
 *
 * @author JavaSaBr
 */
public interface Compiler {

    String SOURCE_EXTENSION = ".java";
    String CLASS_EXTENSION = ".class";

    /**
     * Скомпилировать указанные файлы.
     *
     * @param files список файлов исходников.
     * @return список полученных классов.
     */
    @NotNull
    Class<?>[] compile(@NotNull File... files);

    /**
     * Скомпилировать указанные файлы.
     *
     * @param paths список файлов исходников.
     * @return список полученных классов.
     */
    @NotNull
    Class<?>[] compile(@NotNull Path... paths);

    /**
     * Скомпилировать все классы в указанных дерикториях.
     *
     * @param files список дерикторий.
     * @return скомпилированные классы.
     */
    @NotNull
    Class<?>[] compileDirectory(@NotNull File... files);

    /**
     * Скомпилировать все классы в указанных дерикториях.
     *
     * @param paths список дерикторий.
     * @return скомпилированные классы.
     */
    @NotNull
    Class<?>[] compileDirectory(@NotNull Path... paths);
}
