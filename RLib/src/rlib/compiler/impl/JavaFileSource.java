package rlib.compiler.impl;

import java.io.File;
import java.io.IOException;

import javax.tools.SimpleJavaFileObject;

import rlib.util.FileUtils;

/**
 * Объект исходного java кода.
 * 
 * @author Ronn
 */
public class JavaFileSource extends SimpleJavaFileObject {

	protected JavaFileSource(final File file) {
		super(file.toURI(), Kind.SOURCE);
	}

	@Override
	public boolean equals(final Object obj) {

		if(this == obj) {
			return true;
		} else if(obj == null) {
			return false;
		} else if(getClass() != obj.getClass()) {
			return false;
		}

		final JavaFileSource other = (JavaFileSource) obj;

		if(uri == null) {
			if(other.uri != null) {
				return false;
			}
		} else if(!uri.equals(other.uri)) {
			return false;
		}

		return true;
	}

	@Override
	public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
		final File file = new File(uri);
		final String content = new String(FileUtils.getContent(file), "UTF-8");
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
