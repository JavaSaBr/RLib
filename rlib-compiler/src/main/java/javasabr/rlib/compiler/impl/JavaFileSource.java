package javasabr.rlib.compiler.impl;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.tools.SimpleJavaFileObject;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
public class JavaFileSource extends SimpleJavaFileObject {

  protected JavaFileSource(Path path) {
    super(path.toUri(), Kind.SOURCE);
  }

  protected JavaFileSource(URI uri) {
    super(uri, Kind.SOURCE);
  }

  @Override
  public boolean equals(@Nullable Object obj) {

    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (getClass() != obj.getClass()) {
      return false;
    }

    final JavaFileSource other = (JavaFileSource) obj;

    if (uri == null) {
      if (other.uri != null) {
        return false;
      }
    } else if (!uri.equals(other.uri)) {
      return false;
    }

    return true;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    return Files.readString(Paths.get(uri), StandardCharsets.UTF_8);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (uri == null ? 0 : uri.hashCode());
    return result;
  }
}
