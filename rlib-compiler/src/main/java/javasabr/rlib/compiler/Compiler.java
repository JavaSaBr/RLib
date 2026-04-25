package javasabr.rlib.compiler;

import java.net.URI;
import java.nio.file.Path;
import javasabr.rlib.collections.array.Array;

/**
 * Runtime Java compiler interface for compiling Java source files.
 *
 * @since 10.0.0
 */
public interface Compiler {

  /**
   * The Java source file extension.
   */
  String SOURCE_EXTENSION = ".java";

  /**
   * The compiled class file extension.
   */
  String CLASS_EXTENSION = ".class";

  /**
   * Compiles Java source files from the specified URIs.
   *
   * @param urls the URIs of the source files to compile
   * @return an array of compiled classes
   * @since 10.0.0
   */
  Array<Class<?>> compileByUrls(Array<URI> urls);

  /**
   * Compiles Java source files from the specified paths.
   *
   * @param files the paths to the source files to compile
   * @return an array of compiled classes
   * @since 10.0.0
   */
  Array<Class<?>> compileFiles(Array<Path> files);

  /**
   * Compiles all Java source files in the specified directories.
   *
   * @param directories the directories containing source files to compile
   * @return an array of compiled classes
   * @since 10.0.0
   */
  Array<Class<?>> compileDirectories(Array<Path> directories);
}
