package me.theminecoder.appbase;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import jline.console.ConsoleReader;
import me.theminecoder.appbase.arg.ArgConfigBase;
import me.theminecoder.appbase.command.Command;
import me.theminecoder.appbase.command.CommandManager;
import me.theminecoder.appbase.config.ConfigBase;
import me.theminecoder.appbase.logger.Logger;
import me.theminecoder.appbase.logger.LoggingOutputStream;
import me.theminecoder.appbase.util.ConsoleColor;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.impl.AppLoggerFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

/**
 * @author theminecoder
 */
public abstract class Application<A extends ArgConfigBase, C extends ConfigBase> {

    static {
        System.setProperty("library.jansi.version", "CustomApplication");
        System.setProperty("jline.WindowsTerminal.directConsole", "false");
        AnsiConsole.systemInstall();
    }

    public static void boot(String name, Class<? extends Application> applicationClazz, String[] args) {
        Application application = null;
        try {
            Constructor<Application> constructor = (Constructor<Application>) applicationClazz.getDeclaredConstructor(String.class, String[].class);
            constructor.setAccessible(true);
            application = constructor.newInstance(name, args);
            application.run();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        } finally {
            if (application != null) {
                application.running.set(false);
                application.getExecutorService().shutdown();
            }
        }
    }

    private final AtomicBoolean running = new AtomicBoolean(true);

    private ConsoleReader consoleReader = null;
    private Logger logger = null;

    private final String applicationName;
    private final ScheduledExecutorService executorService;

    private A argConfig;
    private C config;

    protected Application(String name, String[] args) {
        this.applicationName = name;
        this.executorService = new ScheduledThreadPoolExecutor(8, new ApplicationThreadFactory(this));

        try {
            argConfig = getArgConfigType().newInstance();
            config = getConfigType().newInstance();
        } catch (ReflectiveOperationException e) {
            System.err.println("Cannot construct config objects! (Must have public no-arg constructor)");
            e.printStackTrace();
            return;
        }

        String jarName = new java.io.File(Application.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
        if (jarName.equalsIgnoreCase("classes")) jarName = "app.jar";
        String appLine = "java -jar " + jarName;

        try {
            JCommander jCommander = new JCommander(argConfig, args);
            if (argConfig.isDisplayHelp()) {
                jCommander.setProgramName(appLine);
                jCommander.usage();
                System.exit(0);
            }

            if (argConfig.isDisableAnsi()) {
                Ansi.setEnabled(false);
            }
        } catch (ParameterException e) {
            System.err.println("Could not parse arguments: " + e.getMessage());
            System.err.println("See \"" + appLine + " --help\" for more information");
            System.exit(1);
        }

        try {
            Config loadedConfig = ConfigFactory.load();
            if (argConfig.isDebug()) {
                System.out.println("Loaded config:");
                System.out.println(loadedConfig.toString());
            }
            this.config = ConfigBeanFactory.create(loadedConfig, this.getConfigType());
        } catch (Exception e) {
            System.err.println("Could not parse config: " + e.getMessage());
            System.exit(1);
        }

        try {
            this.consoleReader = new ConsoleReader();
            this.consoleReader.setExpandEvents(false);

            this.logger = new Logger(this, this.consoleReader);
            AppLoggerFactory.initRoot(this.logger, this);

            System.setErr(new PrintStream(new LoggingOutputStream(this.logger, Level.SEVERE), true));
            System.setOut(new PrintStream(new LoggingOutputStream(this.logger, Level.INFO), true));

            Runtime.getRuntime().addShutdownHook(new Thread("JLine Cleanup Thread") {
                @Override
                public void run() {
                    try {
                        consoleReader.getTerminal().restore();
                    } catch (Exception ex) {
                        // Ignored
                    }
                }
            });
        } catch (IOException e) {
            System.err.println("Could not set up logging system! Exiting...");
            throw new RuntimeException(e);
        }
    }

    public final String getApplicationName() {
        return applicationName;
    }

    public final A getArgConfig() {
        return argConfig;
    }

    public final C getConfig() {
        return config;
    }

    public final ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    public Logger getLogger() {
        return logger;
    }

    public final boolean isRunning() {
        return this.running.get();
    }

    protected abstract Class<A> getArgConfigType();

    protected abstract Class<C> getConfigType();

    protected abstract void run();

    protected final void runMainCommandLoop() {
        try {
            while (isRunning()) {
                String line = this.consoleReader.readLine(this.applicationName + " > ");
                if (line != null && line.length() > 0) {
                    String[] split = line.split(" ");
                    String commandName = split[0].toLowerCase();

                    // Find the command
                    Command command = CommandManager.getCommand(commandName);
                    if (command != null) {
                        // Reapply the arguments
                        String[] arguments = new String[split.length - 1];
                        System.arraycopy(split, 1, arguments, 0, split.length - 1);

                        // Run the command
                        try {
                            command.invoke(arguments);
                        } catch (Throwable e) {
                            this.logger.log(Level.SEVERE, "Error running command '" + commandName + "':", e);
                        }
                    } else {
                        this.logger.log(Level.INFO, ConsoleColor.RED + "Command not found, try 'help'?");
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}
