package javasabr.rlib.common.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ResourceClassLoader extends ClassLoader {

  private final Map<String, byte[]> resources;
  private final Set<String> hiddenResources;

  public ResourceClassLoader(Map<String, String> resources, Set<String> hiddenResources) {
    super(Thread
        .currentThread()
        .getContextClassLoader());
    this.resources = resources
        .entrySet()
        .stream()
        .collect(Collectors.toUnmodifiableMap(
            Map.Entry::getKey,
            entry -> entry.getValue().getBytes(StandardCharsets.UTF_8)));
    this.hiddenResources = Set.copyOf(hiddenResources);
  }

  @Override
  public InputStream getResourceAsStream(String name) {
    byte[] loaded = resources.get(name);
    if (loaded != null) {
      return new ByteArrayInputStream(loaded);
    }
    if (hiddenResources.contains(name)) {
      return null;
    }
    return super.getResourceAsStream(name);
  }
}
