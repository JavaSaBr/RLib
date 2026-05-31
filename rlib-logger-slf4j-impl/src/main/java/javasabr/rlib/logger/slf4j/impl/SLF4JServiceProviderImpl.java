package javasabr.rlib.logger.slf4j.impl;

import org.jspecify.annotations.Nullable;
import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

public class SLF4JServiceProviderImpl implements SLF4JServiceProvider {

  public static final String REQUESTED_API_VERSION = "2.0.17";
  
  private final IMarkerFactory markerFactory = new BasicMarkerFactory();
  private final MDCAdapter mdcAdapter = new NOPMDCAdapter();
  @Nullable
  private ILoggerFactory loggerFactory;
  
  @Override
  public ILoggerFactory getLoggerFactory() {
    if (loggerFactory == null) {
      throw new IllegalStateException("SLF4JServiceProviderImpl is not initialized yet");
    }
    return loggerFactory;
  }

  @Override
  public IMarkerFactory getMarkerFactory() {
    return markerFactory;
  }

  @Override
  public MDCAdapter getMDCAdapter() {
    return mdcAdapter;
  }

  @Override
  public String getRequestedApiVersion() {
    return REQUESTED_API_VERSION;
  }

  @Override
  public void initialize() {
    this.loggerFactory = new Slf4jLoggerFactoryImpl();
  }
}
