package javasabr.rlib.common.compiler.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import javasabr.rlib.common.compiler.Compiler;
import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ArrayCollectors;
import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import javax.tools.Diagnostic;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.jspecify.annotations.Nullable;

/**
 * The base implementation of a compiler using a compiler from JDK.
 *
 * @author JavaSaBr
 */
public class CompilerImpl implements Compiler {

  private static final Logger LOGGER = LoggerManager.getLogger(Compiler.class);

  /**
   * The constant EMPTY_CLASSES.
   */
  private static final Class<?>[] EMPTY_CLASSES = new Class[0];

  /**
   * The compile listener.
   */
  private final CompileListener listener;

  /**
   * The java compiler.
   */
  private final JavaCompiler compiler;

  /**
   * The class loader.
   */
  private final CompileClassLoader loader;

  /**
   * The java files manager.
   */
  private final CompileJavaFileManager fileManager;

  /**
   * The flag of showing reports.
   */
  private final boolean showDiagnostic;

  public CompilerImpl(final boolean showDiagnostic) {
    this.compiler = ToolProvider.getSystemJavaCompiler();
    this.listener = new CompileListener();
    this.loader = new CompileClassLoader();

    final StandardJavaFileManager standardJavaFileManager = compiler.getStandardFileManager(listener, null, null);

    this.fileManager = new CompileJavaFileManager(standardJavaFileManager, loader);
    this.showDiagnostic = showDiagnostic;
  }

  @Override
  public Class<?>[] compile(final File... files) {
    if (files.length < 1) {
      return EMPTY_CLASSES;
    }

    final Array<JavaFileObject> javaSource = Arrays
        .stream(files)
        .map(JavaFileSource::new)
        .collect(ArrayCollectors.toArray(JavaFileObject.class));

    return compile(null, javaSource);
  }

  @Override
  public Class<?>[] compile(final Path... paths) {
    if (paths.length < 1) {
      return EMPTY_CLASSES;
    }

    final Array<JavaFileObject> javaSource = Arrays
        .stream(paths)
        .map(JavaFileSource::new)
        .collect(ArrayCollectors.toArray(JavaFileObject.class));

    return compile(null, javaSource);
  }

  @Override
  public Class<?>[] compile(final URI... uris) {

    final Array<JavaFileObject> javaSource = Arrays
        .stream(uris)
        .map(JavaFileSource::new)
        .collect(ArrayCollectors.toArray(JavaFileObject.class));

    return compile(null, javaSource);
  }

  /**
   * Compile the list of sources with the list of options.
   *
   * @param options the compile options.
   * @param source the list of sources.
   * @return the list of compiled classes.
   */
  protected synchronized Class<?>[] compile(
      @Nullable final Iterable<String> options,
      final Iterable<? extends JavaFileObject> source) {

    final JavaCompiler compiler = getCompiler();

    final CompileJavaFileManager fileManager = getFileManager();
    final CompileListener listener = getListener();
    final CompileClassLoader loader = getLoader();

    try {

      final CompilationTask task = compiler.getTask(null, fileManager, listener, options, null, source);
      task.call();

      final Diagnostic<JavaFileObject>[] diagnostics = listener.getDiagnostics();

      if (isShowDiagnostic() && diagnostics.length > 0) {

        LOGGER.warning("errors:");

        for (final Diagnostic<JavaFileObject> diagnostic : diagnostics) {
          LOGGER.warning(String.valueOf(diagnostic));
        }
      }

      final Array<Class<?>> result = ArrayFactory.newArray(Class.class);
      final String[] classNames = fileManager.getClassNames();

      for (final String className : classNames) {
        try {
          final Class<?> cs = Class.forName(className, false, loader);
          result.add(cs);
        } catch (final ClassNotFoundException e) {
          LOGGER.warning(e);
        }
      }

      return result.toArray(new Class[result.size()]);

    } finally {
      listener.clear();
      fileManager.clear();
    }
  }

  /**
   * Compile classes from a directory.
   *
   * @param container the container.
   * @param directory the directory.
   */
  private void compileDirectory(final Array<Class<?>> container, final File directory) {

    final File[] files = directory.listFiles();
    if (files == null || files.length < 1) {
      return;
    }

    for (final File file : files) {
      if (file.isDirectory()) {
        compileDirectory(container, file);
      } else if (file
          .getName()
          .endsWith(Compiler.SOURCE_EXTENSION)) {
        container.addAll(compile(file));
      }
    }
  }

  @Override
  public Class<?>[] compileDirectory(final File... files) {

    final Array<Class<?>> container = ArrayFactory.newArray(Class.class);

    for (final File directory : files) {

      if (!directory.exists() || !directory.isDirectory()) {
        continue;
      }

      compileDirectory(container, directory);
    }

    return container.toArray(new Class[container.size()]);
  }

  /**
   * Compile classes from a directory.
   *
   * @param container the container.
   * @param directory the directory.
   */
  private void compileDirectory(final Array<Class<?>> container, final Path directory) {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {

      for (final Path path : stream) {
        if (Files.isDirectory(path)) {
          compileDirectory(container, path);
        } else if (path
            .toString()
            .endsWith(Compiler.SOURCE_EXTENSION)) {
          container.addAll(compile(path));
        }
      }

    } catch (IOException e) {
      LOGGER.warning(e);
    }
  }

  @Override
  public Class<?>[] compileDirectory(Path... paths) {

    final Array<Class<?>> container = ArrayFactory.newArray(Class.class);

    for (final Path path : paths) {

      if (!Files.exists(path) || !Files.isDirectory(path)) {
        continue;
      }

      compileDirectory(container, path);
    }

    return container.toArray(new Class[container.size()]);

  }

  /**
   * Gets compiler.
   *
   * @return the java compiler.
   */
  protected JavaCompiler getCompiler() {
    return compiler;
  }

  /**
   * Gets file manager.
   *
   * @return the java files manager.
   */
  protected CompileJavaFileManager getFileManager() {
    return fileManager;
  }

  /**
   * Gets listener.
   *
   * @return the compile listener.
   */
  protected CompileListener getListener() {
    return listener;
  }

  /**
   * Gets loader.
   *
   * @return the class loader.
   */
  protected CompileClassLoader getLoader() {
    return loader;
  }

  /**
   * @return true id need o show reports.
   */
  private boolean isShowDiagnostic() {
    return showDiagnostic;
  }
}
