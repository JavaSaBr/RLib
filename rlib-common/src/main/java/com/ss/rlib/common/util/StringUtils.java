package com.ss.rlib.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

/**
 * The class with utility methods for working with strings.
 *
 * @author JavaSaBr
 */
public class StringUtils {

    public static final String EMPTY = "";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[_\\p{L}0-9-]+(\\.[_\\p{L}0-9-]+)*@[\\p{L}0-9]+" +
                "(\\.[\\p{L}0-9]+)*(\\.[\\p{L}]{2,})$",
            Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
    );

    private static final ThreadLocal<MessageDigest> LOCAL_HASH_MD =
            ThreadLocal.withInitial(StringUtils::getHashMD5);

    /**
     * Return an empty string if the received string is null.
     *
     * @param string the string.
     * @return an empty string if the received string is null.
     */
    public static @NotNull String emptyIfNull(@Nullable String string) {
        return string == null ? EMPTY : string;
    }

    /**
     * Return the another string if the received string is empty.
     *
     * @param string  the string.
     * @param another the another string.
     * @return the another string if the received string is empty.
     */
    public static @NotNull String ifEmpty(@Nullable String string, @NotNull String another) {
        return isEmpty(string) ? another : string;
    }

    /**
     * Check a string email.
     *
     * @param email the string email.
     * @return true if the email is correct.
     */
    public static boolean isValidEmail(@NotNull String email) {
        var matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    /**
     * Simple check of the string value about does it look like an email.
     *
     * @param value the string value.
     * @return true if it looks like an email.
     */
    public static boolean isEmail(@NotNull String value) {

        var lastDotIndex = value.lastIndexOf('.');
        var lastSignIndex = value.lastIndexOf('@');

        return lastSignIndex > 1 &&
            lastDotIndex > lastSignIndex &&
            lastDotIndex < value.length() - 1;
    }

    /**
     * Compare two strings.
     *
     * @param first  the first string.
     * @param second the second string.
     * @return 1 if the first string is null or is greater then second, 0 if the first string is the same or -1.
     */
    public static int compare(@Nullable String first, @Nullable String second) {
        if (first == null) return 1;
        else if (second == null) return -1;
        return first.compareTo(second);
    }

    /**
     * Compare two strings with ignoring case.
     *
     * @param first  the first string.
     * @param second the second string.
     * @return 1 if the first string is null or is greater then second, 0 if the first string is the same or -1.
     */
    public static int compareIgnoreCase(@Nullable String first, @Nullable String second) {
        if (first == null) return 1;
        else if (second == null) return -1;
        return first.compareToIgnoreCase(second);
    }

    /**
     * Compare two strings.
     *
     * @param first  the first string.
     * @param second the second string.
     * @return true if these strings are equal.
     */
    public static boolean equals(@Nullable String first, @Nullable String second) {
        return !(first == null || second == null) && first.equals(second);
    }

    /**
     * Compare two strings with ignoring case.
     *
     * @param first  the first string.
     * @param second the second string.
     * @return true if these strings are equal.
     */
    public static boolean equalsIgnoreCase(@Nullable String first, @Nullable String second) {
        return !(first == null || second == null) && first.equalsIgnoreCase(second);
    }

    /**
     * Print stack trace of an exception to a string.
     *
     * @param throwable the exception.
     * @return the stack trace.
     */
    public static @NotNull String toString(@NotNull Throwable throwable) {
        return toString(throwable, 6);
    }

    /**
     * Print stack trace of an exception to a string.
     *
     * @param throwable the exception.
     * @param deepLevel the max level of deep.
     * @return the stack trace.
     */
    public static @NotNull String toString(@NotNull Throwable throwable, int deepLevel) {

        var writer = new StringWriter();
        var printWriter = new PrintWriter(writer);

        throwable.printStackTrace(printWriter);

        var stackTrace = new StringBuilder(writer.toString());

        int level = 0;

        for (var cause = throwable.getCause(); cause != null && level < deepLevel; cause = cause.getCause(), level++) {

            writer = new StringWriter();
            printWriter = new PrintWriter(writer);

            cause.printStackTrace(printWriter);

            stackTrace.append("\n caused by ");
            stackTrace.append(writer.toString());
        }

        return stackTrace.toString();
    }

    /**
     * Generate a random string using Aa-Zz characters.
     *
     * @param length the length of result string.
     * @return the new string.
     */
    public static @NotNull String generate(int length) {
        return generate(length, length);
    }

    /**
     * Generate a random string using Aa-Zz characters.
     *
     * @param minLength the min length of result string.
     * @param maxLength the max length of result string.
     * @return the new string.
     */
    public static @NotNull String generate(int minLength, int maxLength) {

        var localRandom = ThreadLocalRandom.current();
        var length = minLength == maxLength ? maxLength : localRandom.nextInt(minLength, maxLength);
        var array = new char[length];
        var min = Math.min('a', 'A');
        var max = Math.max('z', 'z');

        for (int i = 0; i < length; i++) {
            array[i] = (char) localRandom.nextInt(min, max);
        }

        return String.valueOf(array);
    }

    /**
     * @return the md5 message digest.
     */
    private static @NotNull MessageDigest getHashMD5() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns true if the string empty or null.
     *
     * @param string the string.
     * @return true if the string is null or empty.
     */
    public static boolean isEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Returns true if the string isn't empty.
     *
     * @param string the string.
     * @return true if the string isn't empty.
     */
    public static boolean isNotEmpty(@Nullable String string) {
        return !isEmpty(string);
    }

    /**
     * Gets the length of the string.
     *
     * @param string the string.
     * @return length or 0 if a string is null or empty.
     */
    public static int length(@Nullable String string) {
        return string == null ? 0 : string.length();
    }

    /**
     * Encode the string to hash MD5.
     *
     * @param string a string.
     * @return the encoded string.
     */
    public static @NotNull String toMD5(@NotNull String string) {
        final MessageDigest hashMD5 = LOCAL_HASH_MD.get();
        hashMD5.update(string.getBytes(), 0, string.length());
        return new BigInteger(1, hashMD5.digest()).toString(16);
    }

    /**
     * Present the byte array as a hex string.
     *
     * @param array the byte array.
     * @return the hex string.
     * @since 9.0.3
     */
    public static @NotNull String bytesToHexString(@NotNull byte[] array) {

        var builder = new StringBuilder(array.length * 2);

        for (var value : array) {

            var element = Integer.toHexString(value & 0xFF);

            if (element.length() == 1) {
                element = "0" + element;
            }

            builder.append(element);
        }

        return builder.toString();
    }

    /**
     * Parse the hex string to a byte array.
     *
     * @param string the hex string.
     * @return the byte array.
     * @since 9.0.3
     */
    public static @NotNull byte[] hexStringToBytes(@NotNull String string) {

        var array = string.toCharArray();
        var result = new byte[string.length() / 2];

        for (int i = 0, g = 0, length = array.length - 1; i < length; i += 2) {
            var element = String.valueOf(array, i, 2);
            result[g++] = (byte) Integer.parseInt(element, 16);
        }

        return result;
    }

    /**
     * Convert the string from HEX to a plain string.
     *
     * @param string the HEX string.
     * @return the plain string.
     * @since 9.0.3
     */
    public static @NotNull String fromHex(@NotNull String string) {

        var array = string.toCharArray();
        var builder = new StringBuilder(string.length() / 4);

        for (int i = 0, length = array.length - 3; i < length; i += 4) {
            var element = String.valueOf(array, i, 4);
            builder.append((char) Integer.parseInt(element, 16));
        }

        return builder.toString();
    }

    /**
     * Convert the plain string to a HEX string.
     *
     * @param string the plain string.
     * @return the hex string.
     * @since 9.0.3
     */
    public static @NotNull String toHex(@NotNull String string) {

        var builder = new StringBuilder(string.length() * 2);

        for (int i = 0, length = string.length(); i < length; i++) {

            var charAt = string.charAt(i);
            var element = Integer.toHexString(charAt);

            if (element.length() == 1) {
                element = "000" + element;
            } else if (element.length() == 2) {
                element = "00" + element;
            } else if (element.length() == 3) {
                element = "0" + element;
            }

            builder.append(element);
        }

        return builder.toString();
    }

    /**
     * Replace the variable in the string.
     *
     * @param string    the source string.
     * @param firstName the variable's name.
     * @param firstVal  the variable's value.
     * @return result string.
     */
    public static @NotNull String replace(@NotNull String string, @NotNull String firstName, @NotNull String firstVal) {

        var capacity = string.length();
        capacity = capacity - firstName.length() + firstVal.length();
        capacity = Math.max(capacity, string.length());

        var builder = new StringBuilder(capacity);
        builder.append(string);

        replace(builder, firstName, firstVal);

        return builder.toString();
    }

    /**
     * Replace the variables in the string.
     *
     * @param string     the source string.
     * @param firstName  the first variable's name.
     * @param firstVal   the first variable's value.
     * @param secondName the second variable's name.
     * @param secondVal  the second variable's value.
     * @return result string.
     */
    public static @NotNull String replace(
        @NotNull String string,
        @NotNull String firstName,
        @NotNull String firstVal,
        @NotNull String secondName,
        @NotNull String secondVal
    ) {

        var capacity = string.length();
        capacity = capacity - firstName.length() + firstVal.length();
        capacity = capacity - secondName.length() + secondVal.length();
        capacity = Math.max(capacity, string.length());

        var builder = new StringBuilder(capacity);
        builder.append(string);

        replace(builder, firstName, firstVal);
        replace(builder, secondName, secondVal);

        return builder.toString();
    }

    /**
     * Replace the variables in the string.
     *
     * @param string the source string.
     * @param params the variable's parameters, name -&gt; val, name -&gt; val.
     * @return result string.
     * @throws IllegalArgumentException if params count &lt; 2 or % 2 != 0
     */
    public static @NotNull String replace(@NotNull String string, @NotNull String... params) {

        if (params.length < 2 || params.length % 2 != 0) {
            throw new IllegalArgumentException("Wrong params count.");
        }

        var capacity = string.length();

        for (int i = 0; i < params.length - 1; i += 2) {
            capacity = capacity - params[i].length() + params[i + 1].length();
        }

        capacity = Math.max(capacity, string.length());

        var builder = new StringBuilder(capacity);
        builder.append(string);

        for (var i = 0; i < params.length - 1; i += 2) {
            replace(builder, params[i], params[i + 1]);
        }

        return builder.toString();
    }

    private static void replace(@NotNull StringBuilder builder, @NotNull String name, @NotNull String value) {

        for (int index = builder.indexOf(name), count = 0;
             index != -1 && count < 100; index = builder.indexOf(name), count++) {
            builder.replace(index, index + name.length(), value);
        }
    }
}
