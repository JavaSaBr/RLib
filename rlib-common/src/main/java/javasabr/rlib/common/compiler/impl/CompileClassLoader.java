package javasabr.rlib.common.compiler.impl;

import javasabr.rlib.common.compiler.ByteCode;
import javasabr.rlib.common.util.Utils;
import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ArrayFactory;
import org.jspecify.annotations.Nullable;

/**
 * The implementation of a class loader of compiled classes.
 *
 * @author JavaSaBr
 */
public class CompileClassLoader extends ClassLoader {

  /**
   * The list of byte codes of loaded classes.
   */
  private final Array<ByteCode> byteCode;

  public CompileClassLoader() {
    this.byteCode = ArrayFactory.newArray(ByteCode.class);
  }

  /**
   * Add a new compiled class.
   *
   * @param byteCode the byte code
   */
  public synchronized void addByteCode(ByteCode byteCode) {
    this.byteCode.add(byteCode);
  }

  @Override
  protected synchronized @Nullable Class<?> findClass(String name) {

    if (byteCode.isEmpty()) {
      return null;
    }

    for (var byteCode : this.byteCode) {
      var content = byteCode.getByteCode();
      return Utils.uncheckedGet(() -> defineClass(name, content, 0, content.length));
    }

    return null;
  }
}
