package com.ss.rlib.common.compiler.impl;

import org.jetbrains.annotations.NotNull;

import javax.tools.SimpleJavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The implementation of java class source.
 *
 * @author JavaSaBr
 */
public class JavaFileSource extends SimpleJavaFileObject {

    protected JavaFileSource(@NotNull final File file) {
        super(file.toURI(), Kind.SOURCE);
    }

    protected JavaFileSource(@NotNull final Path path) {
        super(path.toUri(), Kind.SOURCE);
    }

    protected JavaFileSource(@NotNull final URI uri) {
        super(uri, Kind.SOURCE);
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
        return new String(Files.readAllBytes(path), "UTF-8");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (uri == null ? 0 : uri.hashCode());
        return result;
    }
}
