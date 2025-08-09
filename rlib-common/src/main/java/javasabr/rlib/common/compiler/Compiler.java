package javasabr.rlib.common.compiler;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;

/**
 * The interface to implement a compiler.
 *
 * @author JavaSaBr
 */
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
  Class<?>[] compile(File... files);

  /**
   * Compile files.
   *
   * @param paths the file list.
   * @return the classes list.
   */
  Class<?>[] compile(Path... paths);

  /**
   * Compile resources.
   *
   * @param uris the resource list.
   * @return the classes list.
   */
  Class<?>[] compile(URI... uris);

  /**
   * Compile all classes from directories.
   *
   * @param files the directory list.
   * @return the classes list.
   */
  Class<?>[] compileDirectory(File... files);

  /**
   * Compile all classes from directories.
   *
   * @param paths the directory list.
   * @return the classes list.
   */
  Class<?>[] compileDirectory(Path... paths);
}
