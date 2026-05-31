package javasabr.rlib.logger.impl.config;

import java.util.Optional;

public interface LoggerConfigLoader {
  
  Optional<LoggerConfig> tryToLoad();
  
  int order();
}
