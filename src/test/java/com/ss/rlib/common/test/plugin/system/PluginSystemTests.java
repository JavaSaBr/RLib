package com.ss.rlib.common.test.plugin.system;

import com.ss.rlib.common.plugin.Version;
import com.ss.rlib.common.plugin.impl.PluginSystemFactory;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;

/**
 * @author JavaSaBr
 */
public class PluginSystemTests {

    @Test
    public void test() {

        var pluginSystem = PluginSystemFactory.newBasePluginSystem();
        pluginSystem.setAppVersion(new Version("0.0.1"));
        pluginSystem.configureEmbeddedPluginPath(Paths.get("./gradle/"));

        pluginSystem.preLoad(ForkJoinPool.commonPool())
                .thenApply(system -> system.initialize(ForkJoinPool.commonPool()))
                .join();
    }
}
