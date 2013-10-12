package rlib.classpath;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import rlib.util.Util;
import rlib.util.array.Array;
import rlib.util.array.Arrays;

/**
 * @author Ronn
 */
public class ManifestClassPathScannerImpl extends ClassPathScanerImpl {

	/** рутовый класс для приложения */
	private final Class<?> rootClass;
	/** ключ, по которому будет извлекаться дополнительный classpath */
	private final String classPathKey;

	public ManifestClassPathScannerImpl(Class<?> rootClass, String classPathKey) {
		this.rootClass = rootClass;
		this.classPathKey = classPathKey;
	}

	public String[] getManifestClassPath() {

		File rootFolder = Util.getRootFolderFromClass(rootClass);

		if(rootFolder == null) {
			return new String[0];
		}

		Array<String> result = Arrays.toArray(String.class);

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		Enumeration<URL> urls;

		try {

			urls = loader.getResources(JarFile.MANIFEST_NAME);

			while(urls.hasMoreElements()) {

				try {

					URL url = urls.nextElement();

					InputStream is = url.openStream();

					if(is != null) {

						Manifest manifest = new Manifest(is);
						Attributes attributes = manifest.getMainAttributes();

						String value = attributes.getValue(classPathKey);

						if(value == null) {
							continue;
						}

						String[] classpath = attributes.getValue(classPathKey).split(" ");

						for(String path : classpath) {

							File file = new File(rootFolder.getAbsolutePath() + File.separator + path);

							if(file.exists()) {
								result.add(file.getAbsolutePath());
							}
						}
					}

				} catch(Exception e) {
					LOGGER.warning(e);
				}
			}
		} catch(IOException e1) {
			LOGGER.warning(e1);
		}

		result.trimToSize();
		return result.array();
	}

	@Override
	protected String[] getPaths() {

		Array<String> result = Arrays.toArraySet(String.class);
		result.addAll(super.getPaths());
		result.addAll(getManifestClassPath());
		result.trimToSize();

		return result.array();
	}
}
