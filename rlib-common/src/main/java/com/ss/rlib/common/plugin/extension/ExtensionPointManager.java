package com.ss.rlib.common.plugin.extension;

import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.dictionary.ConcurrentObjectDictionary;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.ObjectDictionary;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import org.jetbrains.annotations.NotNull;

/**
 * The manager to manage all extension points.
 *
 * @author JavaSaBr
 */
public class ExtensionPointManager {

    private static final Logger LOGGER = LoggerManager.getLogger(ExtensionPointManager.class);

    /**
     * Register a new extension point.
     *
     * @param id  the extension id.
     * @param <T> the extension's type.
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
    private final @NotNull ConcurrentObjectDictionary<String, ExtensionPoint<?>> extensionPoints;

    private ExtensionPointManager() {
        this.extensionPoints = DictionaryFactory.newConcurrentAtomicObjectDictionary();
    }

    private <T> @NotNull ExtensionPoint<T> create(@NotNull String id) {

        long stamp = extensionPoints.writeLock();
        try {

            var exists = extensionPoints.get(id);

            if (exists != null) {
                LOGGER.warning("The extension point with the id " + id + " is already registered.");
                return ClassUtils.unsafeNNCast(exists);
            }

            var extensionPoint = new ExtensionPoint<T>();

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
     * @param <T>       the extension's type.
     * @return this manager.
     */
    public <T> @NotNull ExtensionPointManager addExtension(@NotNull String id, @NotNull T extension) {
        getExtensionPoint(id).register(extension);
        return this;
    }

    /**
     * Add the new extensions to the extension point.
     *
     * @param id         the extension point's id.
     * @param extensions the new extensions.
     * @param <T>        the extension's type.
     * @return this manager.
     */
    public <T> @NotNull ExtensionPointManager addExtension(@NotNull String id, @NotNull T... extensions) {
        getExtensionPoint(id).register(extensions);
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
     * @param id  the id.
     * @param <T> the extension's type.
     * @return the extension point.
     */
    public <T> @NotNull ExtensionPoint<T> getExtensionPoint(@NotNull String id) {

        var extensionPoint = extensionPoints.getInReadLock(id, ObjectDictionary::get);

        if (extensionPoint != null) {
            return ClassUtils.unsafeNNCast(extensionPoint);
        }

        try {
            return create(id);
        } catch (IllegalArgumentException e) {
            return getExtensionPoint(id);
        }
    }
}
