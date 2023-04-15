package icu.ngte.udma.core.tools.resourcebundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public class UTF8Control extends Control {

  public ResourceBundle newBundle(
      String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
      throws IOException {
    String resourceName = toResourceName(toBundleName(baseName, locale), "properties");
    if (reload) {
      URL url = loader.getResource(resourceName);
      if (url == null) {
        return null;
      }
      URLConnection connection = url.openConnection();
      if (connection == null) {
        return null;
      }
      connection.setUseCaches(false);
      try (InputStream stream = connection.getInputStream()) {
        return new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
      }
    } else {
      try (InputStream stream = loader.getResourceAsStream(resourceName)) {
        if (stream != null) {
          return new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
        }
      }
    }
    return null;
  }
}
