package rlib.compiler.impl;

import javax.tools.SimpleJavaFileObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Объект исходного java кода.
 *
 * @author Ronn
 */
public class JavaFileSource extends SimpleJavaFileObject {

    protected JavaFileSource(final File file) {
        super(file.toURI(), Kind.SOURCE);
    }

    protected JavaFileSource(final Path path) {
        super(path.toUri(), Kind.SOURCE);
    }

    @Override
    public boolean equals(final Object obj) {

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
    public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
        final Path path = Paths.get(uri);
        final String content = new String(Files.readAllBytes(path), "UTF-8");
        return content;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (uri == null ? 0 : uri.hashCode());
        return result;
    }
}
