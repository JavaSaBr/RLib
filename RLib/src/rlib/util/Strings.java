package rlib.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rlib.logging.Logger;
import rlib.logging.Loggers;

/**
 * Набор методов для работы со строками
 * 
 * @author Ronn
 * @created 27.03.2012
 */
public class Strings {

	private static final Logger LOGGER = Loggers.getLogger(Strings.class);

	/** экземпляр пустой строки */
	public static final String EMPTY = "".intern();

	/** экземпляр пустого массива строк */
	public static final String[] EMPTY_ARRAY = new String[0];

	/** создаем регулярку для проверки почты */
	public static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE
			| Pattern.MULTILINE);

	private static final ThreadLocal<MessageDigest> LOCAL_HASH_MD = new ThreadLocal<MessageDigest>() {

		@Override
		protected MessageDigest initialValue() {
			return getHashMD5();
		}
	};

	/**
	 * Рассчет длинны строки для пакета
	 * 
	 * @param string
	 * @return length
	 */
	public static int byteCount(final String string) {

		if(string == null || string.isEmpty())
			return 2;

		return string.length() * 2 + 2;
	}

	/**
	 * Проверка на корректность имейла.
	 * 
	 * @param email имеил.
	 * @return корректно ли введен.
	 */
	public static boolean checkEmail(final String email) {
		final Matcher matcher = EMAIL_PATTERN.matcher(email);
		return matcher.matches();
	}

	/**
	 * Сравнение 2х строк.
	 */
	public static boolean equals(final String first, final String second) {

		if(first == null || second == null)
			return false;

		return first.equals(second);
	}

	/**
	 * Сравнение 2х строк без учета регистра.
	 */
	public static boolean equalsIgnoreCase(final String first, final String second) {

		if(first == null || second == null)
			return false;

		return first.equalsIgnoreCase(second);
	}

	/**
	 * Конверктация эксепшена в строку.
	 * 
	 * @param throwable полученный эксепшен.
	 * @return строковое представление.
	 */
	public static String format(final Throwable throwable) {

		final StringBuilder builder = new StringBuilder(throwable.getClass().getSimpleName() + " : " + throwable.getMessage());

		builder.append(" : stack trace:\n");

		for(final StackTraceElement stack : throwable.getStackTrace())
			builder.append(stack).append("\n");

		return builder.toString();
	}

	/**
	 * Генерирование случайной строки указанной длинны.
	 * 
	 * @param length длинна строки.
	 * @return итоговая строка.
	 */
	public static String generate(final int length) {

		final char[] array = new char[length];

		for(int i = 0; i < length; i++)
			array[i] = (char) Rnd.nextInt('a', 'z');

		return String.valueOf(array);
	}

	/**
	 * @return получаение алгоритма хеша.
	 */
	private static MessageDigest getHashMD5() {

		try {
			return MessageDigest.getInstance("MD5");
		} catch(final NoSuchAlgorithmException e) {
			LOGGER.warning(e);
		}

		return null;
	}

	/**
	 * @return является ли строка пустой.
	 */
	public static boolean isEmpty(final String string) {
		return string == null || string.isEmpty();
	}

	/**
	 * Получение хэша пароля.
	 * 
	 * @param password пароль.
	 * @return хэш пароля.
	 */
	public static String passwordToHash(final String password) {

		final MessageDigest hashMD5 = LOCAL_HASH_MD.get();

		hashMD5.update(password.getBytes(), 0, password.length());

		return new BigInteger(1, hashMD5.digest()).toString(16);
	}
}