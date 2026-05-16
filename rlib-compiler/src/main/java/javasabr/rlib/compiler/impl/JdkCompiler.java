package javasabr.rlib.compiler.impl;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayCollectors;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.compiler.Compiler;
import javasabr.rlib.io.util.FileUtils;
import javax.tools.Diagnostic;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@Getter
@CustomLog
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class JdkCompiler implements Compiler {

  private static final Array<String> EMPTY_OPTIONS = Array.empty(String.class);
  private static final Class<?>[] EMPTY_CLASSES = new Class[0];

  CompileEventListener listener;
  JavaCompiler compiler;
  CompiledClassesClassLoader loader;
  CompiledJavaFileManager fileManager;

  boolean showDiagnostic;

  public JdkCompiler(boolean showDiagnostic) {
    this.compiler = ToolProvider.getSystemJavaCompiler();
    this.listener = new CompileEventListener();
    this.loader = new CompiledClassesClassLoader();

    StandardJavaFileManager standardJavaFileManager = compiler
        .getStandardFileManager(listener, null, null);

    this.fileManager = new CompiledJavaFileManager(standardJavaFileManager, loader);
    this.showDiagnostic = showDiagnostic;
  }

  @Override
  public Array<Class<?>> compileByUrls(Array<URI> urls) {
    return compile(
        EMPTY_OPTIONS,
        urls
            .stream()
            .map(JavaFileSource::new)
            .collect(ArrayCollectors.toArray(JavaFileObject.class)));
  }

  @Override
  public Array<Class<?>> compileFiles(Array<Path> files) {
    return compile(
        EMPTY_OPTIONS,
        files
            .stream()
            .map(JavaFileSource::new)
            .collect(ArrayCollectors.toArray(JavaFileObject.class)));
  }

  @Override
  public Array<Class<?>> compileDirectories(Array<Path> directories) {

    MutableArray<Path> files = MutableArray.ofType(Path.class);

    for (Path directory : directories) {
      if (!Files.exists(directory) || !Files.isDirectory(directory)) {
        continue;
      }

      FileUtils.collectFilesTo(files, directory, false, Compiler.SOURCE_EXTENSION);
    }

    if (files.isEmpty()) {
      return Array.empty(Class.class);
    }

    return compile(
        EMPTY_OPTIONS,
        files
            .stream()
            .map(JavaFileSource::new)
            .collect(ArrayCollectors.toArray(JavaFileObject.class)));
  }

  protected synchronized Array<Class<?>> compile(
      @Nullable Array<String> options,
      Array<JavaFileObject> sources) {

    log.debug(sources.size(), "Start compiling [%s] source files..."::formatted);

    JavaCompiler compiler = compiler();
    CompiledJavaFileManager fileManager = fileManager();
    CompileEventListener listener = listener();
    CompiledClassesClassLoader loader = loader();
    try {

      CompilationTask task = compiler.getTask(null, fileManager, listener, options, null, sources);
      task.call();

      Array<Diagnostic<? extends JavaFileObject>> diagnostics = listener.diagnostics();
      if (showDiagnostic() && !diagnostics.isEmpty()) {
        log.warn("Compilation messages:");
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
          log.warn(String.valueOf(diagnostic));
        }
      }

      MutableArray<Class<?>> result = MutableArray.ofType(Class.class);
      String[] classNames = fileManager.classNames();

      log.debug(classNames.length, "Got [%s] compiled class names"::formatted);

      for (String className : classNames) {
        log.debug(className, "Try to load class:[%s]"::formatted);
        try {
          Class<?> klass = Class.forName(className, false, loader);
          result.add(klass);
        } catch (ClassNotFoundException e) {
          log.warn(e);
        }
      }

      return result;
    } finally {
      listener.clear();
      fileManager.clear();
    }
  }
}
