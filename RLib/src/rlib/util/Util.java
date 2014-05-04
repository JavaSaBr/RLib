package rlib.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Callable;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * Утильный класс с набором статических вспомогательных методов.
 * 
 * @author Ronn
 * @created 27.03.2012
 */
public final class Util {

	private static final Logger LOGGER = LoggerManager.getLogger(Util.class);

	private Util() {
		throw new RuntimeException();
	}

	private static final ThreadLocal<SimpleDateFormat> LOCAL_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {

		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm:ss:SSS");
		}
	};

	private static final ThreadLocal<Date> LOCAL_DATE = new ThreadLocal<Date>() {

		@Override
		protected Date initialValue() {
			return new Date();
		}
	};

	/**
	 * Добавение параметров, указывающих что бы соединение к БД работало с UTF-8
	 * кодировкой.
	 * 
	 * @param properties проперти соединения к БД.
	 */
	public static final void addUTFToSQLConnectionProperties(final Properties properties) {
		properties.setProperty("useUnicode", "true");
		properties.setProperty("characterEncoding", "UTF-8");
	}

	/**
	 * Проверяет, занят ли указанный порт на указанном хосте.
	 * 
	 * @param host проверяемый хост.
	 * @param ports проверяемый порт.
	 * @return свободен ли порт.
	 */
	public static boolean checkFreePort(final String host, final int port) {

		try {
			final ServerSocket serverSocket = host.equalsIgnoreCase("*") ? new ServerSocket(port) : new ServerSocket(port, 50, InetAddress.getByName(host));
			serverSocket.close();
		} catch(final IOException e) {
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
	public static boolean checkFreePorts(final String host, final int[] ports) throws InterruptedException {

		for(final int port : ports) {
			try {
				final ServerSocket serverSocket = host.equalsIgnoreCase("*") ? new ServerSocket(port) : new ServerSocket(port, 50, InetAddress.getByName(host));
				serverSocket.close();
			} catch(final IOException e) {
				return false;
			}
		}

		return true;
	}

	/**
	 * форматирует время в секундах в дни/часы/минуты/секунды
	 */
	public static String formatTime(final long time) {

		final Date date = LOCAL_DATE.get();
		date.setTime(time);

		final SimpleDateFormat format = LOCAL_DATE_FORMAT.get();
		return format.format(date);
	}

	/**
	 * Получение ближайшего свободного порта от указанного.
	 * 
	 * @param port стартовый порт.
	 * @return свободный порт или -1, если такого нет.
	 */
	public static int getFreePort(final int port) {

		final int limit = Short.MAX_VALUE * 2;

		for(int i = port; i < limit; i++) {
			if(checkFreePort("*", i)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Получение папки, где лежит джарка указанного класса.
	 * 
	 * @param cs класс, для котого ищем папку с джаркой.
	 * @return адресс папки с джаркой.
	 */
	public static File getRootFolderFromClass(final Class<?> cs) {

		String className = cs.getName();

		final StringBuilder builder = new StringBuilder(className.length());
		builder.append('/');

		for(int i = 0, length = className.length(); i < length; i++) {

			char ch = className.charAt(i);

			if(ch == '.') {
				ch = '/';
			}

			builder.append(ch);
		}

		builder.append(".class");

		className = builder.toString();

		try {

			final URL url = Util.class.getResource(className);

			String path = url.getPath();
			path = path.substring(0, path.length() - className.length());
			path = path.substring(0, path.lastIndexOf('/'));

			final URI uri = new URI(path);
			path = uri.getPath();
			path = path.replaceAll("%20", " ");

			// замена сепараторов
			if(File.separatorChar != '/') {

				final StringBuilder pathBuilder = new StringBuilder();

				for(int i = 0, length = path.length(); i < length; i++) {

					char ch = path.charAt(i);

					if(ch == '/' && i == 0) {
						continue;
					}

					if(ch == '/') {
						ch = File.separatorChar;
					}

					pathBuilder.append(ch);
				}

				path = pathBuilder.toString();
			}

			File file = new File(path);

			while(path.lastIndexOf(File.separatorChar) != -1 && !file.exists()) {
				path = path.substring(0, path.lastIndexOf(File.separatorChar));
				file = new File(path);
			}

			return file;
		} catch(final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return путь к корневому каталогу приложения.
	 */
	public static String getRootPath() {
		return ".";
	}

	/**
	 * Получить short шы массива байтов.
	 * 
	 * @param bytes массив байтов.
	 * @param offset отступ в массиве.
	 * @return значение в short.
	 */
	public static short getShort(final byte[] bytes, final int offset) {
		return (short) (bytes[offset + 1] << 8 | bytes[offset] & 0xff);
	}

	/**
	 * Получение имя пользователя текущей системы.
	 * 
	 * @return имя пользователя системы.
	 */
	public static final String getUserName() {
		return System.getProperty("user.name");
	}

	/**
	 * Формирования дампа байтов в хексе.
	 * 
	 * @param array массив байтов.
	 * @return строка с дампом.
	 */
	public static String hexdump(final byte[] array, final int size) {
		return hexdump(array, 0, size);
	}

	/**
	 * Формирования дампа байтов в хексе.
	 * 
	 * @param array массив байтов.
	 * @return строка с дампом.
	 */
	public static String hexdump(final byte[] array, final int offset, final int size) {

		final StringBuilder builder = new StringBuilder();

		int count = 0;
		final int end = size - 1;

		final char[] chars = new char[16];

		for(int g = 0; g < 16; g++) {
			chars[g] = '.';
		}

		for(int i = offset; i < size; i++) {

			int val = array[i];

			if(val < 0) {
				val += 256;
			}

			String text = Integer.toHexString(val).toUpperCase();

			if(text.length() == 1) {
				text = "0" + text;
			}

			char ch = (char) val;

			if(ch < 33) {
				ch = '.';
			}

			if(i == end) {

				chars[count] = ch;

				builder.append(text);

				for(int j = 0; j < 15 - count; j++) {
					builder.append("   ");
				}

				builder.append("    ").append(chars).append('\n');
			} else if(count < 15) {
				chars[count++] = ch;
				builder.append(text).append(' ');
			} else {

				chars[15] = ch;

				builder.append(text).append("    ").append(chars).append('\n');

				count = 0;

				for(int g = 0; g < 16; g++) {
					chars[g] = 0x2E;
				}
			}
		}

		return builder.toString();
	}

	/**
	 * Безопасное выполнение задачи.
	 * 
	 * @param callable выполняемая задача.
	 */
	public static <V> V safeExecute(final Callable<V> callable) {

		try {
			return callable.call();
		} catch(final Throwable e) {
			LOGGER.warning(e);
		}

		return null;
	}

	/**
	 * Безопасное выполнение задачи.
	 * 
	 * @param runnable выполняемая задача.
	 */
	public static void safeExecute(final Runnable runnable) {
		try {
			runnable.run();
		} catch(final Throwable e) {
			LOGGER.warning(e);
		}
	}

	public static String toString(final Throwable throwable) {

		final StringBuilder builder = new StringBuilder(throwable.getClass().getSimpleName() + " : " + throwable.getMessage());

		builder.append(" : stack trace:\n");

		for(final StackTraceElement stack : throwable.getStackTrace()) {
			builder.append(stack).append("\n");
		}

		return builder.toString();
	}
}