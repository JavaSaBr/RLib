package rlib.function;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeRunnable {

    void run() throws Exception;
}
