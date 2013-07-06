package rlib.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rlib.logging.Loggers;

/**
 * Набор методов для работы со строками
 * 
 * @author Ronn
 * @created 27.03.2012
 */
public class Strings
{
	/** экземпляр пустой строки */
	public static final String EMPTY = "".intern();

	/** экземпляр пустого массива строк */
	public static final String[] EMPTY_ARRAY = new String[0];

	/** создаем регулярку для проверки почты */
	public static final Pattern emailPattern = Pattern.compile(
			"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", Pattern.DOTALL
					| Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	/** класс для получения хэша */
	private static final MessageDigest hashMD5 = getHashMD5();

	/**
	 * Проверка на корректность имейла.
	 * 
	 * @param email имеил.
	 * @return корректно ли введен.
	 */
	public static boolean checkEmail(String email)
	{
		Matcher matcher = emailPattern.matcher(email);

		return matcher.matches();
	}

	/**
	 * @return получаение алгоритма хеша.
	 */
	private static MessageDigest getHashMD5()
	{
		try
		{
			return MessageDigest.getInstance("MD5");
		}
		catch(NoSuchAlgorithmException e)
		{
			Loggers.warning("Strings", e);
		}

		return null;
	}

	/**
	 * Рассчет длинны строки для пакета
	 * 
	 * @param string
	 * @return length
	 */
	public static int byteCount(String string)
	{
		if(string == null || string.isEmpty())
			return 2;

		return string.length() * 2 + 2;
	}

	/**
	 * @return является ли строка пустой.
	 */
	public static boolean isEmpty(String string)
	{
		return string == null || string.isEmpty();
	}

	/**
	 * Сравнение 2х строк.
	 */
	public static boolean equals(String first, String second)
	{
		if(first == null || second == null)
			return false;

		return first.equals(second);
	}

	/**
	 * Сравнение 2х строк без учета регистра.
	 */
	public static boolean equalsIgnoreCase(String first, String second)
	{
		if(first == null || second == null)
			return false;

		return first.equalsIgnoreCase(second);
	}

	/**
	 * Получение хэша пароля.
	 * 
	 * @param password пароль.
	 * @return хэш пароля.
	 */
	public synchronized static String passwordToHash(String password)
	{
		// обновляем строку
		hashMD5.update(password.getBytes(), 0, password.length());

		// конвиктируем в хэш
		return new BigInteger(1, hashMD5.digest()).toString(16);
	}

	/**
	 * Генерирование случайной строки указанной длинны.
	 * 
	 * @param length длинна строки.
	 * @return итоговая строка.
	 */
	public static String generate(int length)
	{
		char[] array = new char[length];

		for(int i = 0; i < length; i++)
			array[i] = (char) Rnd.nextInt('a', 'z');

		return String.valueOf(array);
	}
}