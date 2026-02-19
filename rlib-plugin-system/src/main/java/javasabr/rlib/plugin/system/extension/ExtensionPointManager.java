package javasabr.rlib.plugin.system.extension;

import javasabr.rlib.collections.dictionary.Dictionary;
import javasabr.rlib.collections.dictionary.DictionaryFactory;
import javasabr.rlib.collections.dictionary.LockableRefToRefDictionary;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Manager for creating and retrieving extension points.
 *
 * @since 10.0.0
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class ExtensionPointManager {

  private static final Logger LOGGER = LoggerManager.getLogger(ExtensionPointManager.class);

  private static final ExtensionPointManager INSTANCE = new ExtensionPointManager();

  /**
   * Returns the singleton instance of the extension point manager.
   *
   * @return the extension point manager instance
   * @since 10.0.0
   */
  public static ExtensionPointManager getInstance() {
    return INSTANCE;
  }

  /**
   * Registers a new extension point with the specified id.
   *
   * @param <T> the type of extensions
   * @param id the extension point id
   * @return the created extension point
   * @since 10.0.0
   */
  public static <T> ExtensionPoint<T> register(String id) {
    return getInstance().create(id);
  }

  LockableRefToRefDictionary<String, ExtensionPoint<?>> extensionPoints;

  /**
   * Creates a new extension point manager.
   *
   * @since 10.0.0
   */
  public ExtensionPointManager() {
    this.extensionPoints = DictionaryFactory.stampedLockBasedRefToRefDictionary();
  }

  /**
   * Creates a new extension point with the specified id.
   *
   * @param <T> the type of extensions
   * @param id the extension point id
   * @return the created extension point
   * @since 10.0.0
   */
  public <T> ExtensionPoint<T> create(String id) {
    long stamp = extensionPoints.writeLock();
    try {
      ExtensionPoint<?> exists = extensionPoints.get(id);
      if (exists != null) {
        LOGGER.warning(id, "Extension point:[%s] is already registered"::formatted);
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
   * Adds an extension to the extension point with the specified id.
   *
   * @param <T> the type of extension
   * @param id the extension point id
   * @param type the extension type class
   * @param extension the extension to add
   * @return this manager
   * @since 10.0.0
   */
  public <T> ExtensionPointManager addExtension(String id, Class<T> type, T extension) {
    getOrCreateExtensionPoint(id).register(extension);
    return this;
  }

  /**
   * Adds an extension to the extension point with the specified id.
   *
   * @param <T> the type of extension
   * @param id the extension point id
   * @param extension the extension to add
   * @return this manager
   * @since 10.0.0
   */
  public <T> ExtensionPointManager addExtension(String id, T extension) {
    getOrCreateExtensionPoint(id).register(extension);
    return this;
  }

  /**
   * Adds multiple extensions to the extension point with the specified id.
   *
   * @param <T> the type of extensions
   * @param id the extension point id
   * @param extensions the extensions to add
   * @return this manager
   * @since 10.0.0
   */
  public <T> ExtensionPointManager addExtension(String id, T... extensions) {
    getOrCreateExtensionPoint(id).register(extensions);
    return this;
  }

  /**
   * Returns or creates the extension point with the specified id.
   *
   * @param <T> the type of extensions
   * @param id the extension point id
   * @param type the extension type class
   * @return the extension point
   * @since 10.0.0
   */
  public <T> ExtensionPoint<T> getOrCreateExtensionPoint(String id, Class<T> type) {
    return getOrCreateExtensionPoint(id);
  }

  /**
   * Returns or creates the extension point with the specified id.
   *
   * @param <T> the type of extensions
   * @param id the extension point id
   * @return the extension point
   * @since 10.0.0
   */
  public <T> ExtensionPoint<T> getOrCreateExtensionPoint(String id) {

    ExtensionPoint<?> extensionPoint = extensionPoints
        .operations()
        .getInReadLock(id, Dictionary::get);

    if (extensionPoint != null) {
      return ClassUtils.unsafeNNCast(extensionPoint);
    }

    try {
      return create(id);
    } catch (IllegalArgumentException e) {
      return getOrCreateExtensionPoint(id);
    }
  }
}
