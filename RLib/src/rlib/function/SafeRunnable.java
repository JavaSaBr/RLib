package rlib.function;

/**
 * Функциональный интерфейс.
 */
@FunctionalInterface
public interface SafeRunnable {

    public void run() throws Exception;
}
