package javasabr.rlib.common.compiler.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import javasabr.rlib.common.compiler.ByteCode;
import javasabr.rlib.common.compiler.Compiler;
import javax.tools.SimpleJavaFileObject;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of byte code container.
 *
 * @author JavaSaBr
 */
public class CompileByteCode extends SimpleJavaFileObject implements ByteCode {

  /**
   * The stream with byte code.
   */
  @NotNull
  private final ByteArrayOutputStream outputStream;

  public CompileByteCode(@NotNull final String name) {
    super(URI.create("byte:///" + name.replace('/', '.') + Compiler.CLASS_EXTENSION), Kind.CLASS);
    this.outputStream = new ByteArrayOutputStream();
  }

  @Override
  public @NotNull byte[] getByteCode() {
    return outputStream.toByteArray();
  }

  @Override
  public @NotNull OutputStream openOutputStream() {
    return outputStream;
  }
}
