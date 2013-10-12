package rlib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.util.Enumeration;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.array.Array;
import rlib.util.array.Arrays;
import rlib.util.table.Table;
import rlib.util.table.Tables;

/**
 * Класс для работы с файлами.
 * 
 * @author Ronn
 * @created 01.03.2012
 */
public class Files {

	private static final Logger log = Loggers.getLogger("Files");

	/** кэш текста файлов */
	private static final Table<String, String> cache = Tables.newObjectTable();
	/** кэш файлов */
	private static final Table<String, File> cacheFiles = Tables.newObjectTable();

	/**
	 * Очистка кэша файлов.
	 */
	public static void clean() {
		cache.clear();
	}

	/**
	 * Определят, подходит ли по формату фаил.
	 * 
	 * @param formats набор форматов.
	 * @param file проверяемый фаил.
	 * @return подходит ли.
	 */
	public static boolean containsFormat(final String[] formats, final File file) {
		return containsFormat(formats, file.getName());
	}

	/**
	 * Определят, подходит ли по формату фаил.
	 * 
	 * @param formats набор форматов.
	 * @param file проверяемый фаил.
	 * @return подходит ли.
	 */
	public static boolean containsFormat(final String[] formats, final String path) {

		for(int i = 0, length = formats.length; i < length; i++) {
			if(path.endsWith(formats[i])) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Копирует информацию с одного файла в другой.
	 * 
	 * @param pathSource адресс исходного файла.
	 * @param pathDest адресс конечного файла.
	 * @return скопирован ли фаил.
	 */
	public static boolean copyFile(final String pathSource, final String pathDest) {

		try(FileInputStream source = new FileInputStream(pathSource)) {

			try(FileOutputStream destination = new FileOutputStream(pathDest)) {

				final FileChannel sourceChannel = source.getChannel();
				final FileChannel destinationChannel = destination.getChannel();

				destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
			}

			return true;
		} catch(final IOException e) {
			log.warning(e);
		}

		return false;
	}

	/**
	 * Чтение контента файла.
	 * 
	 * @param file читаемый фаил.
	 * @return массив байтов фаила.
	 */
	public static byte[] getContent(final File file) {

		try(FileInputStream fileInputStream = new FileInputStream(file)) {

			final FileChannel channel = fileInputStream.getChannel();

			final byte[] content = new byte[(int) channel.size()];

			fileInputStream.read(content);

			return content;
		} catch(final IOException e) {
			log.warning(e);
		}

		return null;
	}

	/**
	 * Получаем все файлы в папке.
	 * 
	 * @param dir папка.
	 * @return все файлы.
	 */
	public static File[] getFiles(final File dir) {
		return getFiles(dir, Strings.EMPTY_ARRAY);
	}

	/**
	 * Получаем все файлы в папке.
	 * 
	 * @param dir папка.
	 * @param formats набор нужных форматов.
	 * @return все файлы.
	 */
	public static File[] getFiles(final File dir, final String... formats) {

		final Array<File> array = Arrays.toArray(File.class);

		final File[] files = dir.listFiles();

		for(int i = 0, length = files.length; i < length; i++) {

			final File file = files[i];

			if(file.isDirectory()) {
				array.addAll(getFiles(file, formats));
			} else if(formats == Strings.EMPTY_ARRAY || containsFormat(formats, file)) {
				array.add(file);
			}
		}

		array.trimToSize();

		return array.array();
	}

	/**
	 * Получаем все файлы в пакете.
	 * 
	 * @param pckg пакет.
	 * @return все файлы.
	 */
	public static File[] getFiles(final Package pckg) {
		return getFiles(pckg, Strings.EMPTY_ARRAY);
	}

	/**
	 * Получаем все файлы в пакете нужных форматов.
	 * 
	 * @param pckg пакет.
	 * @param formats набор нужных форматов.
	 * @return все файлы.
	 */
	public static File[] getFiles(final Package pckg, final String... formats) {

		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		Enumeration<URL> urls = null;

		try {
			urls = classLoader.getResources(pckg.getName().replace('.', '/'));
		} catch(final IOException e) {
			Loggers.warning(Files.class, e);
		}

		if(urls == null)
			return new File[0];

		final Array<File> files = Arrays.toArray(File.class);

		while(urls.hasMoreElements()) {

			final URL next = urls.nextElement();

			String path = next.getFile();

			if(path.contains("%20")) {
				path = path.replaceAll("%20", " ");
			}

			final File file = new File(path);

			if(file.isDirectory()) {
				files.addAll(getFiles(file, formats));
			} else if(formats == Strings.EMPTY_ARRAY || containsFormat(formats, path)) {
				files.add(file);
			}
		}

		files.trimToSize();

		return files.array();
	}

	/**
	 * Извлекает время последней модификации файла.
	 * 
	 * @param name имя файла.
	 */
	public static long lastModified(final String name) {

		if(name == null)
			return 0;

		File file = cacheFiles.get(name);

		if(file == null) {
			file = new File("./" + name);
			cacheFiles.put(name, file);
		}

		return file.lastModified();
	}

	/**
	 * Чтение текста из файла по указанному пути.
	 * 
	 * @param path путь к файлу.
	 */
	public static String read(final String path) {

		if(path == null) {
			return null;
		}

		if(cache.containsKey(path)) {
			return cache.get(path);
		}

		File file = cacheFiles.get(path);

		if(file == null) {
			file = new File(path);
			cacheFiles.put(path, file);
		}

		if(!file.exists()) {
			return null;
		}

		final StringBuilder content = new StringBuilder();

		try(FileReader in = new FileReader(file)) {

			final CharBuffer buffer = CharBuffer.allocate(512);

			while(in.ready()) {

				buffer.clear();
				in.read(buffer);
				buffer.flip();

				content.append(buffer.array(), 0, buffer.limit());
			}

		} catch(final IOException e) {
			log.warning(e);
		}

		cache.put(path, content.toString());
		return content.toString();
	}
}