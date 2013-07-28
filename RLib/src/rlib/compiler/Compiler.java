package rlib.compiler;

import java.io.File;

import javax.tools.JavaFileObject.Kind;

/**
 * Интерфейс для реализации компилятора.
 * 
 * @author Ronn
 */
public interface Compiler
{
	public static final String SOURCE_EXTENSION = Kind.SOURCE.extension;
	public static final String CLASS_EXTENSION = Kind.CLASS.extension;

	/**
	 * Скомпилировать указанные файлы.
	 * 
	 * @param files список файлов исходников.
	 * @return список полученных классов.
	 */
	public Class<?>[] compile(File... files);

	/**
	 * Скомпилировать все классы в указанных дерикториях.
	 * 
	 * @param files список дерикторий.
	 * @return скомпилированные классы.
	 */
	public Class<?>[] compileDirectory(File... files);
}
