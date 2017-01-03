package rlib.compiler;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

/**
 * The interface to implement a compiler.
 *
 * @author JavaSaBr
 */
public interface Compiler {

    String SOURCE_EXTENSION = ".java";
    String CLASS_EXTENSION = ".class";

    /**
     * Compile files.
     *
     * @param files the file list.
     * @return the classes list.
     */
    @NotNull
    Class<?>[] compile(@NotNull File... files);

    /**
     * Compile files.
     *
     * @param paths the file list.
     * @return the classes list.
     */
    @NotNull
    Class<?>[] compile(@NotNull Path... paths);

    /**
     * Compile all classes from directories.
     *
     * @param files the directory list.
     * @return the classes list.
     */
    @NotNull
    Class<?>[] compileDirectory(@NotNull File... files);

    /**
     * Compile all classes from directories.
     *
     * @param paths the directory list.
     * @return the classes list.
     */
    @NotNull
    Class<?>[] compileDirectory(@NotNull Path... paths);
}
