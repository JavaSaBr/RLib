package javasabr.rlib.common.compiler.impl;

import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ArrayFactory;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;

/**
 * The manager to load byte code of classes.
 *
 * @author JavaSaBr
 */
public class CompileJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

  /**
   * The list of names of loaded classes.
   */
  private final Array<String> classNames;

  /**
   * The loaded of compiled classes.
   */
  private final CompileClassLoader loader;

  public CompileJavaFileManager(
      final StandardJavaFileManager fileManager,
      final CompileClassLoader loader) {
    super(fileManager);
    this.loader = loader;
    this.classNames = ArrayFactory.newArray(String.class);
  }

  /**
   * Clear the list of names of loaded classes.
   */
  public void clear() {
    classNames.clear();
  }

  /**
   * Get class names.
   *
   * @return the list of names of loaded classes.
   */
  public String[] getClassNames() {
    return classNames.toArray(new String[classNames.size()]);
  }

  @Override
  public JavaFileObject getJavaFileForOutput(
      final Location location,
      final String name,
      final Kind kind,
      final FileObject sibling) {

    final CompileByteCode byteCode = new CompileByteCode(name);

    loader.addByteCode(byteCode);
    classNames.add(name);

    return byteCode;
  }
}
