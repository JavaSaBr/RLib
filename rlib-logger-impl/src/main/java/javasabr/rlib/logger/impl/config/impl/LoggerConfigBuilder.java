package javasabr.rlib.logger.impl.config.impl;

import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.collections.dictionary.DictionaryFactory;
import javasabr.rlib.collections.dictionary.MutableRefToRefDictionary;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.config.LoggerConfig;
import javasabr.rlib.logger.impl.config.consumer.LogMessageConsumer;
import javasabr.rlib.logger.impl.config.impl.DefaultLoggerConfig.LoggerConsumersKey;
import javasabr.rlib.logger.impl.config.render.LogMessageRender;

public class LoggerConfigBuilder {

  final MutableRefToRefDictionary<String, LoggerLevel> loggerLevels;
  final MutableRefToRefDictionary<String, LogMessageRender> renders;
  final MutableRefToRefDictionary<String, LogMessageConsumer> consumers;
  final MutableRefToRefDictionary<LoggerConsumersKey, MutableArray<LogMessageConsumer>> loggerConsumers;

  public LoggerConfigBuilder() {
    this.loggerLevels = DictionaryFactory.mutableRefToRefDictionary();
    this.renders = DictionaryFactory.mutableRefToRefDictionary();
    this.consumers = DictionaryFactory.mutableRefToRefDictionary();
    this.loggerConsumers = DictionaryFactory.mutableRefToRefDictionary();
  }
  
  public void registerLoggerLevel(String loggerName, LoggerLevel level) {
    loggerLevels.put(loggerName, level);
  }
  
  public void registerLoggerConsumer(String loggerName, LoggerLevel level, LogMessageConsumer consumer) {
    var key = new LoggerConsumersKey(loggerName, level);
    loggerConsumers
        .getOrCompute(key, () -> ArrayFactory.mutableArray(LogMessageConsumer.class))
        .add(consumer);
  }
  
  public LoggerConfig build() {
    var tempDictionary = DictionaryFactory
        .<LoggerConsumersKey, Array<LogMessageConsumer>>mutableRefToRefDictionary();
    loggerConsumers.forEach((key, consumers) -> 
        tempDictionary.put(key, Array.copyOf(consumers)));
    return new DefaultLoggerConfig(loggerLevels.toReadOnly(), tempDictionary.toReadOnly());
  }
}
