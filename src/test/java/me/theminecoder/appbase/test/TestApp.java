package me.theminecoder.appbase.test;

import me.theminecoder.appbase.Application;

/**
 * @author theminecoder
 */
public class TestApp extends Application {
    public static void main(String[] args) {
        boot("Test App", TestApp.class, args);
    }


    protected TestApp(String name, String[] args) {
        super(name, args);
    }

    @Override
    protected void run() {
        this.getLogger().info("Test Arg: " + this.getArgConfig(TestAppArgsConfig.class).test);
        runMainCommandLoop();
    }
}
