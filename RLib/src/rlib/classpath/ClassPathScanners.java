package rlib.classpath;

/**
 * Фабрика сканеров classpath.
 * 
 * @author Ronn
 */
public final class ClassPathScanners
{
	private ClassPathScanners()
	{
		throw new RuntimeException();
	}

	public static final ClassPathScaner newDefaultScanner()
	{
		return new ClassPathScanerImpl();
	}
}
