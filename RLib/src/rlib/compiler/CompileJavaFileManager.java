package rlib.compiler;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;

import rlib.util.array.Array;
import rlib.util.array.ArrayUtils;

/**
 * Файловый менеджер по загрузке классов для компиляции.
 * 
 * @author Ronn
 */
public class CompileJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

	/** список имен загруженных классов */
	private final Array<String> classNames;

	/** загрузчик скомпиленных классов */
	private final CompileClassLoader loader;

	public CompileJavaFileManager(StandardJavaFileManager fileManager, CompileClassLoader loader) {
		super(fileManager);

		this.loader = loader;
		this.classNames = ArrayUtils.toArray(String.class);
	}

	/**
	 * Очистка списка имен последних загруженных классов.
	 */
	public void clear() {
		classNames.clear();
	}

	/**
	 * @return список последних загруженны классов.
	 */
	public String[] getClassNames() {
		return classNames.toArray(new String[classNames.size()]);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String name, Kind kind, FileObject sibling) throws IOException {

		CompileByteCode byteCode = new CompileByteCode(name);

		loader.addByteCode(byteCode);
		classNames.add(name);

		return byteCode;
	}
}
