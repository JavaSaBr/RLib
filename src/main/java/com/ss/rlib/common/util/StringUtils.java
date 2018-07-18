package com.ss.rlib.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class with utility methods for working with strings.
 *
 * @author JavaSaBr
 */
public class StringUtils {

    public static final String EMPTY = "";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+" +
                "(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
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
    @Deprecated
    public static @NotNull String toNotNull(@Nullable String string) {
        return emptyIfNull(string);
    }

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
    public static boolean checkEmail(@NotNull String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
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

        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);

        throwable.printStackTrace(printWriter);

        StringBuilder stackTrace = new StringBuilder(writer.toString());

        int level = 0;

        for (Throwable cause = throwable.getCause(); cause != null && level < deepLevel; cause = cause.getCause(), level++) {

            writer = new StringWriter();
            printWriter = new PrintWriter(writer);

            cause.printStackTrace(printWriter);

            stackTrace.append("\n caused by ");
            stackTrace.append(writer.toString());
        }

        return stackTrace.toString();
    }

    /**
     * Generate a random string.
     *
     * @param length the length.
     * @return the new string.
     */
    public static @NotNull String generate(int length) {

        final ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        final char[] array = new char[length];

        for (int i = 0; i < length; i++) {
            array[i] = (char) localRandom.nextInt('a', 'z');
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
}