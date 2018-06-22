package com.ss.rlib.common.plugin.extension;

import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.dictionary.ConcurrentObjectDictionary;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.ObjectDictionary;
import org.jetbrains.annotations.NotNull;

/**
 * The manager to manage all extension points.
 *
 * @author JavaSaBr
 */
public class ExtensionPointManager {

    /**
     * Register a new extension point.
     *
     * @param id the extension id.
     * @return the new extension point.
     * @throws IllegalArgumentException if an extension with the id is already exists.
     */
    public static <T> @NotNull ExtensionPoint<T> register(@NotNull String id) {
        return getInstance().create(id);
    }

    private static final ExtensionPointManager INSTANCE = new ExtensionPointManager();

    public static @NotNull ExtensionPointManager getInstance() {
        return INSTANCE;
    }

    /**
     * All created extension points.
     */
    @NotNull
    private final ConcurrentObjectDictionary<String, ExtensionPoint<?>> extensionPoints;

    private ExtensionPointManager() {
        this.extensionPoints = DictionaryFactory.newConcurrentAtomicObjectDictionary();
    }

    private <T> @NotNull ExtensionPoint<T> create(@NotNull String id) {

        long stamp = extensionPoints.writeLock();
        try {

            ExtensionPoint<?> exists = extensionPoints.get(id);

            if (exists != null) {
                throw new IllegalArgumentException("The extension point with the id " +
                        id + " is already registered.");
            }

            ExtensionPoint<T> extensionPoint = new ExtensionPoint<>();

            extensionPoints.put(id, extensionPoint);

            return extensionPoint;

        } finally {
            extensionPoints.writeUnlock(stamp);
        }
    }

    /**
     * Add the new extension to the extension point.
     *
     * @param id        the extension point's id.
     * @param type      the extension's type.
     * @param extension the new extension.
     * @param <T>       the extension's type.
     * @return this manager.
     */
    public <T> @NotNull ExtensionPointManager addExtension(
            @NotNull String id,
            @NotNull Class<T> type,
            @NotNull T extension
    ) {
        getExtensionPoint(id).register(extension);
        return this;
    }

    /**
     * Add the new extension to the extension point.
     *
     * @param id        the extension point's id.
     * @param extension the new extension.
     * @return this manager.
     */
    public @NotNull ExtensionPointManager addExtension(@NotNull String id, @NotNull Object extension) {
        getExtensionPoint(id).register(extension);
        return this;
    }

    /**
     * Get or create an extension point.
     *
     * @param id   the id.
     * @param type the extension's type.
     * @param <T>  the extension's type.
     * @return the extension point.
     */
    public <T> @NotNull ExtensionPoint<T> getExtensionPoint(@NotNull String id, @NotNull Class<T> type) {
        return getExtensionPoint(id);
    }

    /**
     * Get or create an extension point.
     *
     * @param id the id.
     * @return the extension point.
     */
    public <T> @NotNull ExtensionPoint<T> getExtensionPoint(@NotNull String id) {

        ExtensionPoint<?> extensionPoint = extensionPoints.getInReadLock(id, ObjectDictionary::get);
        if (extensionPoint != null) {
            return ClassUtils.unsafeCast(extensionPoint);
        }

        try {
            return create(id);
        } catch (IllegalArgumentException e) {
            return getExtensionPoint(id);
        }
    }
}
