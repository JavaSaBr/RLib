package javasabr.rlib.common.plugin.system;

import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;
import javasabr.rlib.common.plugin.Version;
import javasabr.rlib.common.plugin.impl.PluginSystemFactory;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class PluginSystemTests {

    @Test
    public void test() {

        var pluginSystem = PluginSystemFactory.newBasePluginSystem();
        pluginSystem.setAppVersion(new Version("0.0.1"));
        pluginSystem.configureEmbeddedPluginPath(Paths.get("../gradle/"));

        pluginSystem.preLoad(ForkJoinPool.commonPool())
                .thenApply(system -> system.initialize(ForkJoinPool.commonPool()))
                .join();
    }
}
