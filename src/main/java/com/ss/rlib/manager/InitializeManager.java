package com.ss.rlib.manager;

import com.ss.rlib.logging.Logger;
import com.ss.rlib.util.linkedlist.LinkedListFactory;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.util.linkedlist.LinkedList;

/**
 * The manager to initialize singletons in some order.
 *
 * @author JavaSaBr
 */
public final class InitializeManager {

    private static final Logger LOGGER = LoggerManager.getLogger(InitializeManager.class);

    @NotNull
    private static final String METHOD_NAME = "getInstance";

    @NotNull
    private static final LinkedList<Class<?>> QUEUE = LinkedListFactory.newLinkedList(Class.class);

    /**
     * Initialize.
     */
    public synchronized static void initialize() {

        for (final Iterator<Class<?>> iterator = QUEUE.iterator(); iterator.hasNext(); ) {

            final Class<?> next = iterator.next();

            try {

                final Method method = next.getMethod(METHOD_NAME);
                final Object instance = method.invoke(null);

                if (instance == null) {
                    LOGGER.warning("no initialize class " + next);
                }

                iterator.remove();

            } catch (final InvocationTargetException e) {
                LOGGER.warning(e.getTargetException());
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
                LOGGER.warning(e);
            }
        }
    }

    /**
     * Add a class with static method names 'getInstance'.
     *
     * @param cs the cs
     */
    public static synchronized void register(@NotNull final Class<?> cs) {

        try {
            cs.getMethod(METHOD_NAME);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        QUEUE.add(cs);
    }

    /**
     * Valid an order of a class.
     *
     * @param cs the class.
     */
    public static synchronized void valid(@NotNull final Class<?> cs) {
        if (QUEUE.getFirst() != cs) {
            Thread.dumpStack();
        }
    }

    private InitializeManager() {
        throw new RuntimeException();
    }
}
