package com.ss.rlib.common.manager;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * The manager to initialize singletons in some order.
 *
 * @author JavaSaBr
 */
public final class InitializeManager {

    private static final Logger LOGGER = LoggerManager.getLogger(InitializeManager.class);

    private static final String METHOD_NAME = "getInstance";

    private static final Deque<Class<?>> QUEUE = new ArrayDeque<>();

    /**
     * Initialize.
     */
    public synchronized static void initialize() {

        for (Iterator<Class<?>> iterator = QUEUE.iterator(); iterator.hasNext(); ) {

            Class<?> next = iterator.next();
            try {

                Method method = next.getMethod(METHOD_NAME);
                Object instance = method.invoke(null);

                if (instance == null) {
                    LOGGER.warning("no initialize class " + next);
                }

                iterator.remove();

            } catch (InvocationTargetException e) {
                LOGGER.warning(e.getTargetException());
            } catch (Throwable e) {
                LOGGER.warning(e);
            }
        }
    }

    /**
     * Add a class with static method 'getInstance' to initialization queue.
     *
     * @param cs the class.
     */
    public static synchronized void register(@NotNull Class<?> cs) {

        try {
            cs.getMethod(METHOD_NAME);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        QUEUE.add(cs);
    }

    /**
     * Check validity of the class in initialization order.
     *
     * @param cs the class.
     */
    public static synchronized void valid(@NotNull Class<?> cs) {
        if (QUEUE.getFirst() != cs) {
            throw new IllegalStateException("The class has invalid initialization position.");
        }
    }

    private InitializeManager() {
        throw new RuntimeException();
    }
}
