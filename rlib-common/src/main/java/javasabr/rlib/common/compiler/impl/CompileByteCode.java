package javasabr.rlib.common.compiler.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import javasabr.rlib.common.compiler.ByteCode;
import javasabr.rlib.common.compiler.Compiler;
import javax.tools.SimpleJavaFileObject;

/**
 * The implementation of byte code container.
 *
 * @author JavaSaBr
 */
public class CompileByteCode extends SimpleJavaFileObject implements ByteCode {

  /**
   * The stream with byte code.
   */
  private final ByteArrayOutputStream outputStream;

  public CompileByteCode(final String name) {
    super(URI.create("byte:///" + name.replace('/', '.') + Compiler.CLASS_EXTENSION), Kind.CLASS);
    this.outputStream = new ByteArrayOutputStream();
  }

  @Override
  public byte[] getByteCode() {
    return outputStream.toByteArray();
  }

  @Override
  public OutputStream openOutputStream() {
    return outputStream;
  }
}
