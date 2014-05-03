package rlib.classpath;

import rlib.classpath.impl.ClassPathScanerImpl;
import rlib.classpath.impl.ManifestClassPathScannerImpl;

/**
 * Фабрика сканеров classpath.
 * 
 * @author Ronn
 */
public final class ClassPathScannerFactory {

	public static final ClassPathScaner newDefaultScanner() {
		return new ClassPathScanerImpl();
	}

	public static final ClassPathScaner newManifestScanner(final Class<?> rootClass, final String classPathKey) {
		return new ManifestClassPathScannerImpl(rootClass, classPathKey);
	}

	private ClassPathScannerFactory() {
		throw new RuntimeException();
	}
}
