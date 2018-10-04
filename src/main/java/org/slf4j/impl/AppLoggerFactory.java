package org.slf4j.impl;

import com.google.common.collect.Maps;
import me.theminecoder.appbase.Application;
import me.theminecoder.appbase.logger.Logger;
import me.theminecoder.appbase.logger.ModuleLogger;
import org.slf4j.ILoggerFactory;

import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

public class AppLoggerFactory implements ILoggerFactory {
    private static Logger rootLogger;
    private static Application application;
    ConcurrentMap<String, AppLoggerAdaptor> loggerMap = Maps.newConcurrentMap();

    private Level level = Level.ALL;

    public static final void initRoot(Logger logger, Application application) {
        if (rootLogger == null) {
            AppLoggerFactory.rootLogger = logger;
            AppLoggerFactory.application = application;
        }

    }

    public AppLoggerFactory() {
        java.util.logging.Logger.getLogger("");
    }

    public void setLevel(Level level) {
        this.level = level;
        loggerMap.values().forEach(logger -> logger.setLevel(level));
    }

    public org.slf4j.Logger getLogger(String name) {
        if (name.equalsIgnoreCase("ROOT")) {
            name = "";
        }

        AppLoggerAdaptor slf4jLogger = this.loggerMap.get(name);
        if (slf4jLogger != null) {
            return slf4jLogger;
        } else {
            AppLoggerAdaptor newInstance = new AppLoggerAdaptor(new ModuleLogger("SLF4J/" + name, application.getLogger())).setLevel(level);
            AppLoggerAdaptor oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
            return oldInstance == null ? newInstance : oldInstance;
        }
    }
}
