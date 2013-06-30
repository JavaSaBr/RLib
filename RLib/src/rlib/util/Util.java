package rlib.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Класс различных дополнительных статик методов.
 *
 * @author Ronn
 * @created 27.03.2012
 */
public abstract class Util
{
	/** формат даты для лога */
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS");

	/**
	 * Проверяет, занят ли указанный порт на указанном хосте.
	 *
	 * @param host проверяемый хост.
	 * @param ports проверяемый порт.
	 * @return свободен ли порт.
	 * @throws InterruptedException
	 */
	public static boolean checkFreePorts(String host, int port) throws InterruptedException
	{
		try
		{
			ServerSocket serverSocket = host.equalsIgnoreCase("*") ? new ServerSocket(port) : new ServerSocket(port, 50, InetAddress.getByName(host));
			
			serverSocket.close();
		}
		catch(IOException e)
		{
			return false;
		}

		return true;
	}

	/**
	 * Проверяет, заняты ли указанные порты на указанном хосте.
	 *
	 * @param host проверяемый хост.
	 * @param ports проверяемые порты.
	 * @return свободны ли все порты.
	 * @throws InterruptedException
	 */
	public static boolean checkFreePorts(String host, int[] ports) throws InterruptedException
	{
		for(int port : ports)
		{
			try
			{
				ServerSocket serverSocket = host.equalsIgnoreCase("*") ? new ServerSocket(port) : new ServerSocket(port, 50, InetAddress.getByName(host));

				serverSocket.close();
			}
			catch(IOException e)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * форматирует время в секундах в дни/часы/минуты/секунды
	 */
	public static String formatTime(long time)
	{
		return timeFormat.format(new Date(time));
	}

	/**
	 * Получение ближайшего свободного порта от указанного.
	 *
	 * @param port стартовый порт.
	 * @return свободный порт или -1, если такого нет.
	 */
	public static int getFreePort(int port)
	{
		int limit = Short.MAX_VALUE * 2;

		while(port < limit)
		{
			try
			{
				new ServerSocket(port).close();

				return port;
			}
			catch(IOException e)
			{
				port++;
			}
		}

		return -1;
	}

	/**
	 * @return путь к корневому каталогу приложения.
	 */
	public static String getRootPath()
	{
		return ".";
	}

	/**
	 * Получить short шы массива байтов.
	 * 
	 * @param bytes массив байтов.
	 * @param offset отступ в массиве.
	 * @return значение в short.
	 */
	public static short getShort(byte[] bytes, int offset)
	{
		return (short) (bytes[offset + 1] << 8 | bytes[offset] & 0xff);
	}

	/**
	 * Получение имя пользователя текущей системы.
	 *
	 * @return имя пользователя системы.
	 */
	public static final String getUserName()
	{
		return System.getProperty("user.name");
	}

	/**
	 * Добавение параметров,  указывающих что бы соединение к БД работало с UTF-8 кодировкой.
	 * 
	 * @param properties проперти соединения к БД.
	 */
	public static final void addUTFToSQLConnectionProperties(Properties properties)
	{
		properties.setProperty("useUnicode","true");
        properties.setProperty("characterEncoding","UTF-8");
	}
	
	/**
	 * Формирования дампа байтов в хексе.
	 *
	 * @param array массив байтов.
	 * @return строка с дампом.
	 */
	public static String hexdump(byte[] array, int size)
	{
		// создаем билдер
		StringBuilder builder = new StringBuilder();

		// текущий индекс байта в строке дампа
		int count = 0;
		// вычисляем индекс последнего байта
		int end = size - 1;

		// создаем массив символов 1 строки в дампе
		char[] chars = new char[16];

		// заполняем ее точками
		for(int g = 0; g < 16; g++)
			chars[g] = '.';

		// перебираем массив байтов
		for(int i = 0; i < size; i++)
		{
			// получаем байт
			int val = array[i];

			// делаем беззнаковым
			if(val < 0)
				val += 256;

			// конвектируем в хекс значение байта
			String text = Integer.toHexString(val).toUpperCase();

			// если значение из 1 символа
			if(text.length() == 1)
				// добавляем нолик
				text = "0" + text;

			// получаем символ байта
			char ch = (char) val;

			// если ему принадлежит пустой символ
			if(ch < 33)
				// заменяем на точку
				ch = '.';

			// если это последний ,fqn
			if(i == end)
			{
				// вносим его символ в текстовую часть дампа
				chars[count] = ch;

				// добавляем сам байт
				builder.append(text);

				// добавляем недостающий отступ
				for(int j = 0; j < 15 - count; j++)
					builder.append("   ");

				// добавляем текстовое представление
				builder.append("    ").append(chars).append('\n');
			}
			// если еще не завершена строка дампа
			else if(count < 15)
			{
				// вносим в текстовое представление символ байта
				chars[count++] = ch;

				// добавляем сам байт
				builder.append(text).append(' ');
			}
			else
			{
				// вносим символ байта
				chars[15] = ch;

				// завершаем строку дампа
				builder.append(text).append("    ").append(chars).append('\n');

				// обнуляем счетчик для новой строки дампа
				count = 0;

				// очищаем массив текстового представления байтов
				for(int g = 0; g < 16; g++)
					chars[g] = 0x2E;
			}
		}

		// получаем строку дампа
		return builder.toString();
	}
}