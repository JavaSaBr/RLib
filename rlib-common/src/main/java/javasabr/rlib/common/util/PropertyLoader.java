package javasabr.rlib.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The property loader using UTF-8 encoding.
 *
 * @author JavaSaBr
 */
@NullMarked
public class PropertyLoader extends ResourceBundle.Control {

  private static final PropertyLoader INSTANCE = new PropertyLoader();

  /**
   * Gets instance.
   *
   * @return the instance
   */
  public static PropertyLoader getInstance() {
    return INSTANCE;
  }

  @Nullable
  @Override
  public ResourceBundle newBundle(
      String baseName,
      Locale locale,
      String format,
      ClassLoader loader,
      boolean reload)
      throws IOException {

    // The below is a copy of the default implementation.
    String bundleName = toBundleName(baseName, locale);
    String resourceName = toResourceName(bundleName, "properties");

    ResourceBundle bundle = null;
    InputStream stream = null;

    if (reload) {

      final URL url = loader.getResource(resourceName);

      if (url != null) {
        final URLConnection connection = url.openConnection();
        if (connection != null) {
          connection.setUseCaches(false);
          stream = connection.getInputStream();
        }
      }

    } else {
      stream = loader.getResourceAsStream(resourceName);
    }

    if (stream != null) {
      try {
        // Only this line is changed to make it to read properties files as UTF-8.
        bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
      } finally {
        stream.close();
      }
    }

    return bundle;
  }
}
