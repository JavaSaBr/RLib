package rlib.classpath.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

import rlib.classpath.ClassPathScaner;
import rlib.compiler.Compiler;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ArrayUtils;
import rlib.util.StringUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * Реализация обычного сканера classpath.
 * 
 * @author Ronn
 */
public class ClassPathScanerImpl implements ClassPathScaner {

	protected static final Logger LOGGER = LoggerManager.getLogger(ClassPathScaner.class);

	private static final String CLASS_PATH = System.getProperty("java.class.path");
	private static final String PATH_SEPARATOR = File.pathSeparator;
	private static final String CLASS_EXTENSION = ".class";

	/** загрузчик классов */
	private final ClassLoader loader;

	/** найденные классы */
	private Class<?>[] classes;

	public ClassPathScanerImpl() {
		this.loader = getClass().getClassLoader();
	}

	@Override
	public void addClasses(final Array<Class<?>> added) {
		this.classes = ArrayUtils.combine(classes, added.toArray(new Class[added.size()]), Class.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, R extends T> void findImplements(final Array<Class<R>> container, final Class<T> interfaceClass) {

		if(!interfaceClass.isInterface()) {
			throw new RuntimeException("class " + interfaceClass + " is not interface.");
		}

		for(final Class<?> cs : getClasses()) {

			if(cs.isInterface() || !interfaceClass.isAssignableFrom(cs) || Modifier.isAbstract(cs.getModifiers())) {
				continue;
			}

			container.add((Class<R>) cs);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, R extends T> void findInherited(final Array<Class<R>> container, final Class<T> parentClass) {

		if(Modifier.isFinal(parentClass.getModifiers())) {
			throw new RuntimeException("class " + parentClass + " is final class.");
		}

		for(final Class<?> cs : getClasses()) {

			if(cs.isInterface() || cs == parentClass || !parentClass.isAssignableFrom(cs) || Modifier.isAbstract(cs.getModifiers())) {
				continue;
			}

			container.add((Class<R>) cs);
		}
	}

	@Override
	public void getAll(final Array<Class<?>> container) {
		container.addAll(getClasses());
	}

	private Class<?>[] getClasses() {
		return classes;
	}

	/**
	 * @return загрузчик классов.
	 */
	private ClassLoader getLoader() {
		return loader;
	}

	protected String[] getPaths() {
		return CLASS_PATH.split(PATH_SEPARATOR);
	}

	/**
	 * Загрузка класса по его имени в контейнер.
	 * 
	 * @param name название класса.
	 * @param container контейнер загруженных классов.
	 */
	private void loadClass(final String name, final Array<Class<?>> container) {

		if(!name.endsWith(CLASS_EXTENSION)) {
			return;
		}

		String className = null;

		try {

			className = name.replace(CLASS_EXTENSION, StringUtils.EMPTY);

			final StringBuilder result = new StringBuilder(className.length());

			for(int i = 0, length = className.length(); i < length; i++) {

				char ch = className.charAt(i);

				if(ch == '/') {
					ch = '.';
				}

				result.append(ch);
			}

			className = result.toString();
		} catch(final Exception e) {
			LOGGER.info("incorrect replace " + name + " to java path, separator " + File.separator);
			return;
		}

		try {
			container.add(getLoader().loadClass(className));
		} catch(final NoClassDefFoundError ex) {
		} catch(final VerifyError error) {
		} catch(final ClassNotFoundException e) {

			LOGGER.warning(e);
		}
	}

	/**
	 * Сканирование папки на наличие в ней классов или jar.
	 * 
	 * @param container контейнер загружаемых классов.
	 * @param directory сканируемая папка.
	 */
	private void scaningDirectory(final String rootPath, final Array<Class<?>> container, final File directory) {

		final File[] files = directory.listFiles();

		for(final File file : files) {

			if(file.isDirectory()) {
				scaningDirectory(rootPath, container, file);
			} else if(file.isFile()) {

				final String name = file.getName();

				if(name.endsWith(Compiler.SOURCE_EXTENSION)) {
					scaningJar(container, file);
				} else if(name.endsWith(CLASS_EXTENSION)) {
					try {

						String path = file.getAbsolutePath();
						path = path.substring(rootPath.length(), path.length());

						if(path.startsWith(File.separator)) {
							path = path.substring(1, path.length());
						}

						loadClass(path, container);
					} catch(final Exception e) {
						LOGGER.info("incorrect replace " + file.getAbsolutePath() + " from root " + rootPath);
					}
				}
			}
		}
	}

	/**
	 * Сканирование .jar для подгрузки классов.
	 * 
	 * @param container контейнер подгруженных классов.
	 * @param jarFile ссылка на .jar фаил.
	 */
	private void scaningJar(final Array<Class<?>> container, final File jarFile) {

		if(!jarFile.exists()) {
			LOGGER.warning("not exists " + jarFile);
			return;
		}

		try(JarFile jar = new JarFile(jarFile)) {

			for(final Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {

				final JarEntry entry = entries.nextElement();

				if(entry.isDirectory()) {
					continue;
				}

				loadClass(entry.getName(), container);
			}

		} catch(final ZipException e) {
			LOGGER.warning("can't open zip file " + jarFile.getAbsolutePath());
		} catch(final IOException e) {
			LOGGER.warning(e);
		}
	}

	@Override
	public void scanning() {

		if(classes != null) {
			throw new RuntimeException("scanning is already.");
		}

		final String[] paths = getPaths();

		final Array<Class<?>> container = ArrayFactory.newArray(Class.class);

		for(final String path : paths) {

			final File file = new File(path);

			LOGGER.info("scanning " + file);

			if(!file.exists()) {
				continue;
			}

			if(file.isDirectory()) {
				scaningDirectory(path, container, file);
			} else if(file.isFile() && file.getName().endsWith(JAR_EXTENSION)) {
				scaningJar(container, file);
			}
		}

		classes = container.toArray(new Class[container.size()]);
		LOGGER.info("scanned for " + classes.length + " classes.");
	}
}
