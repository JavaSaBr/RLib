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
 * Класс для работы с файлами в игровом режиме.
 * 
 * @author Ronn
 * @created 01.03.2012
 */
public class Files
{
	private static final Logger log = Loggers.getLogger("Files");

	/** кэш текста файлов */
	private static final Table<String, String> cache = Tables.newObjectTable();
	/** кэш файлов */
	private static final Table<String, File> cacheFiles = Tables.newObjectTable();

	/**
	 * Очистка кэша файлов.
	 */
	public static void clean()
	{
		cache.clear();
	}

	/**
	 * Определят, подходит ли по формату фаил.
	 * 
	 * @param formats набор форматов.
	 * @param file проверяемый фаил.
	 * @return подходит ли.
	 */
	public static boolean containsFormat(String[] formats, File file)
	{
		return containsFormat(formats, file.getName());
	}

	/**
	 * Определят, подходит ли по формату фаил.
	 * 
	 * @param formats набор форматов.
	 * @param file проверяемый фаил.
	 * @return подходит ли.
	 */
	public static boolean containsFormat(String[] formats, String path)
	{
		for(int i = 0, length = formats.length; i < length; i++)
			if(path.endsWith(formats[i]))
				return true;

		return false;
	}

	/**
	 * Копирует информацию с одного файла в другой.
	 * 
	 * @param pathSource адресс исходного файла.
	 * @param pathDest адресс конечного файла.
	 * @return скопирован ли фаил.
	 */
	public static boolean copyFile(String pathSource, String pathDest)
	{
		try(FileInputStream source = new FileInputStream(pathSource))
		{
			try(FileOutputStream destination = new FileOutputStream(pathDest))
			{
				FileChannel sourceChannel = source.getChannel();
				FileChannel destinationChannel = destination.getChannel();

				destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
			}

			return true;
		}
		catch(IOException e)
		{
			log.warning(e);
		}

		return false;
	}

	/**
	 * Получаем все файлы в папке.
	 * 
	 * @param dir папка.
	 * @return все файлы.
	 */
	public static File[] getFiles(File dir)
	{
		return getFiles(dir, Strings.EMPTY_ARRAY);
	}

	/**
	 * Получаем все файлы в папке.
	 * 
	 * @param dir папка.
	 * @param formats набор нужных форматов.
	 * @return все файлы.
	 */
	public static File[] getFiles(File dir, String... formats)
	{
		Array<File> array = Arrays.toArray(File.class);

		File[] files = dir.listFiles();

		for(int i = 0, length = files.length; i < length; i++)
		{
			File file = files[i];

			if(file.isDirectory())
				array.addAll(getFiles(file, formats));
			else if(formats == Strings.EMPTY_ARRAY || containsFormat(formats, file))
				array.add(file);
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
	public static File[] getFiles(Package pckg)
	{
		return getFiles(pckg, Strings.EMPTY_ARRAY);
	}

	/**
	 * Получаем все файлы в пакете нужных форматов.
	 * 
	 * @param pckg пакет.
	 * @param formats набор нужных форматов.
	 * @return все файлы.
	 */
	public static File[] getFiles(Package pckg, String... formats)
	{
		// пробуем получить загрузчик классов
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		// список файлов
		Enumeration<URL> urls = null;

		try
		{
			urls = classLoader.getResources(pckg.getName().replace('.', '/'));
		}
		catch(IOException e)
		{
			Loggers.warning(Files.class, e);
		}

		// если файлов нет, вовращаем путсой массив
		if(urls == null)
			return new File[0];

		Array<File> files = Arrays.toArray(File.class);

		while(urls.hasMoreElements())
		{
			// получаем урл
			URL next = urls.nextElement();

			// получаем полный путь файла
			String path = next.getFile();

			if(path.contains("%20"))
				path = path.replace("%20", " ");

			// получаем фаил по пути
			File file = new File(path);

			// если это дерриктория
			if(file.isDirectory())
				// получаем файлы из дериктории
				files.addAll(getFiles(file, formats));
			// если это файл с нужным форматом
			else if(formats == Strings.EMPTY_ARRAY || containsFormat(formats, path))
				// добавляем
				files.add(file);
		}

		files.trimToSize();

		return files.array();
	}

	/**
	 * Извлекает время последней модификации файла.
	 * 
	 * @param name имя файла.
	 */
	public static long lastModified(String name)
	{
		if(name == null)
			return 0;

		File file = cacheFiles.get(name);

		if(file == null)
		{
			file = new File("./" + name);
			cacheFiles.put(name, file);
		}

		return file.lastModified();
	}

	/**
	 * Извлекает текст из файла.
	 * 
	 * @param path имя файла.
	 */
	public static String read(String path)
	{
		// если пути нет ,возвращаем пустоту
		if(path == null)
			return null;

		// если есть в кеше
		if(cache.containsKey(path))
			// возвращаем из кеша
			return cache.get(path);

		// пробуем получить сам файл из кеша
		File file = cacheFiles.get(path);

		// если файла нет
		if(file == null)
		{
			// создаем его
			file = new File(path);
			// добавляем в кеш
			cacheFiles.put(path, file);
		}

		// если файла такого не существует, выходим
		if(!file.exists())
			return null;

		// создаем билдер текста
		StringBuilder content = new StringBuilder();

		// получаем поток для считывания файла
		try(FileReader in = new FileReader(file))
		{
			CharBuffer buffer = CharBuffer.allocate(512);

			while(in.ready())
			{
				// очищаем буффер
				buffer.clear();

				// вносим данные в буффер
				in.read(buffer);

				// подготавливаем
				buffer.flip();

				// вносим данные в билдер
				content.append(buffer.array(), 0, buffer.limit());
			}
		}
		catch(IOException e)
		{
			log.warning(e);
		}

		// вносим в кеш
		cache.put(path, content.toString());

		// возвращаем результат
		return content.toString();
	}

	/**
	 * Чтение контента файла.
	 * 
	 * @param file читаемый фаил.
	 * @return массив байтов фаила.
	 */
	public static byte[] getContent(File file)
	{
		try(FileInputStream fileInputStream = new FileInputStream(file))
		{
			FileChannel channel = fileInputStream.getChannel();

			byte[] content = new byte[(int) channel.size()];

			fileInputStream.read(content);

			return content;
		}
		catch(IOException e)
		{
			log.warning(e);
		}

		return null;
	}
}