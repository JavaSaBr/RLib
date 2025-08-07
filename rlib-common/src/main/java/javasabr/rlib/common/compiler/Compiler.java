package javasabr.rlib.common.compiler;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * The interface to implement a compiler.
 *
 * @author JavaSaBr
 */
@NullMarked
public interface Compiler {

  /**
   * The constant SOURCE_EXTENSION.
   */
  String SOURCE_EXTENSION = ".java";

  /**
   * The constant CLASS_EXTENSION.
   */
  String CLASS_EXTENSION = ".class";

  /**
   * Compile files.
   *
   * @param files the file list.
   * @return the classes list.
   */
  Class<?>[] compile(@NotNull File... files);

  /**
   * Compile files.
   *
   * @param paths the file list.
   * @return the classes list.
   */
  Class<?>[] compile(@NotNull Path... paths);

  /**
   * Compile resources.
   *
   * @param uris the resource list.
   * @return the classes list.
   */
  Class<?>[] compile(@NotNull URI... uris);

  /**
   * Compile all classes from directories.
   *
   * @param files the directory list.
   * @return the classes list.
   */
  Class<?>[] compileDirectory(@NotNull File... files);

  /**
   * Compile all classes from directories.
   *
   * @param paths the directory list.
   * @return the classes list.
   */
  Class<?>[] compileDirectory(@NotNull Path... paths);
}
