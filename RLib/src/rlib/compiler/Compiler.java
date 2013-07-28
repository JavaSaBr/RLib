package rlib.compiler;

import java.io.File;

import javax.tools.Diagnostic;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.array.Array;
import rlib.util.array.Arrays;

/**
 * Реализация обертки над java компилятором для удобной компиляции java кода в
 * runtime.
 * 
 * @author Ronn
 */
public class Compiler
{
	private static final Logger log = Loggers.getLogger(Compiler.class);

	/**
	 * @return доступен ли компилятор.
	 */
	public static boolean isAvailableCompiler()
	{
		return ToolProvider.getSystemJavaCompiler() != null;
	}

	/** слушатель ошибок компиляций */
	private final CompileListener listener;

	/** компилятор java кода */
	private final JavaCompiler compiler;

	/** загрузчик скомпилированных классов */
	private final CompileClassLoader loader;

	/** менедж по компилируемым ресурсам */
	private final CompileJavaFileManager fileManager;

	/** отображать ли ошибки компиляции */
	private final boolean showDiagnostic;

	/**
	 * @param showDiagnostic отображать ли ошибки компиляции.
	 */
	public Compiler(boolean showDiagnostic)
	{
		this.compiler = ToolProvider.getSystemJavaCompiler();
		this.listener = new CompileListener();
		this.loader = new CompileClassLoader();

		StandardJavaFileManager standardJavaFileManager = compiler.getStandardFileManager(listener, null, null);

		this.fileManager = new CompileJavaFileManager(standardJavaFileManager, loader);
		this.showDiagnostic = showDiagnostic;
	}

	/**
	 * @return отображать ли диагностику.
	 */
	public boolean isShowDiagnostic()
	{
		return showDiagnostic;
	}

	/**
	 * Скомпилировать указанные файлы.
	 * 
	 * @param files список файлов исходников.
	 * @return список полученных классов.
	 */
	public Class<?>[] compile(File... files)
	{
		if(files.length < 1)
			return null;

		Array<JavaFileObject> javaSource = Arrays.toArray(JavaFileObject.class, files.length);

		for(File file : files)
			javaSource.add(new JavaFileSource(file));

		return compile(null, javaSource);
	}

	/**
	 * Компиляция списка объектов.
	 * 
	 * @param options опции компиляции.
	 * @param source список исходников.
	 * @return список скомпиленых классов.
	 */
	protected synchronized Class<?>[] compile(Iterable<String> options, Iterable<? extends JavaFileObject> source)
	{
		JavaCompiler compiler = getCompiler();

		CompileJavaFileManager fileManager = getFileManager();
		CompileListener listener = getListener();
		CompileClassLoader loader = getLoader();

		try
		{
			CompilationTask task = compiler.getTask(null, fileManager, listener, options, null, source);
			task.call();

			Diagnostic<JavaFileObject>[] diagnostics = listener.getDiagnostics();

			if(isShowDiagnostic() && diagnostics.length > 1)
			{
				log.warning("errors:");

				for(Diagnostic<JavaFileObject> diagnostic : diagnostics)
					log.warning(String.valueOf(diagnostic));
			}

			Array<Class<?>> result = Arrays.toArray(Class.class);

			String[] classNames = fileManager.getClassNames();

			for(String className : classNames)
			{
				try
				{
					Class<?> cs = Class.forName(className, false, loader);
					result.add(cs);
				}
				catch(ClassNotFoundException e)
				{
					log.warning(e);
				}
			}

			return result.toArray(new Class[result.size()]);
		}
		finally
		{
			listener.clear();
			fileManager.clear();
		}
	}

	/**
	 * @return загрузчик скомпилированных классов.
	 */
	protected CompileClassLoader getLoader()
	{
		return loader;
	}

	/**
	 * @return менедж по компилируемым ресурсам.
	 */
	protected CompileJavaFileManager getFileManager()
	{
		return fileManager;
	}

	/**
	 * @return компилятор java кода.
	 */
	protected JavaCompiler getCompiler()
	{
		return compiler;
	}

	/**
	 * @return слушатель ошибок компиляций.
	 */
	protected CompileListener getListener()
	{
		return listener;
	}
}
