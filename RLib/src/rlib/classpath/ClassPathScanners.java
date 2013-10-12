package rlib.classpath;

/**
 * Фабрика сканеров classpath.
 * 
 * @author Ronn
 */
public final class ClassPathScanners {

	public static final ClassPathScaner newDefaultScanner() {
		return new ClassPathScanerImpl();
	}

	public static final ClassPathScaner newManifestScanner(Class<?> rootClass, String classPathKey) {
		return new ManifestClassPathScannerImpl(rootClass, classPathKey);
	}

	private ClassPathScanners() {
		throw new RuntimeException();
	}
}
