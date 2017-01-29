package me.theminecoder.appbase.test;

import me.theminecoder.appbase.Application;
import me.theminecoder.appbase.arg.ArgConfigBase;
import me.theminecoder.appbase.config.ConfigBase;

import java.lang.reflect.Type;

/**
 * @author theminecoder
 */
public class TestApp extends Application<TestAppArgsConfig, ConfigBase> {
    public static void main(String[] args) {
        boot("Test App", TestApp.class, args);
    }


    protected TestApp(String name, String[] args) {
        super(name, args);
    }

    @Override
    protected Class<TestAppArgsConfig> getArgConfigType() {
        return TestAppArgsConfig.class;
    }

    @Override
    protected Class<ConfigBase> getConfigType() {
        return ConfigBase.class;
    }

    @Override
    protected void run() {
        this.getLogger().info("Test Arg: " + this.getArgConfig().test);

        runMainCommandLoop();
    }
}
