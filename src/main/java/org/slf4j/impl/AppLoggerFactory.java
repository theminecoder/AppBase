package org.slf4j.impl;

import com.google.common.collect.Maps;
import me.theminecoder.appbase.Application;
import me.theminecoder.appbase.logger.Logger;
import me.theminecoder.appbase.logger.ModuleLogger;
import org.slf4j.ILoggerFactory;

import java.util.concurrent.ConcurrentMap;

public class AppLoggerFactory implements ILoggerFactory {
    private static Logger rootLogger;
    private static Application application;
    ConcurrentMap<String, org.slf4j.Logger> loggerMap = Maps.newConcurrentMap();

    public static final void initRoot(Logger logger, Application application) {
        if (rootLogger == null) {
            AppLoggerFactory.rootLogger = logger;
            AppLoggerFactory.application = application;
        }

    }

    public AppLoggerFactory() {
        java.util.logging.Logger.getLogger("");
    }

    public org.slf4j.Logger getLogger(String name) {
        if (name.equalsIgnoreCase("ROOT")) {
            name = "";
        }

        org.slf4j.Logger slf4jLogger = this.loggerMap.get(name);
        if (slf4jLogger != null) {
            return slf4jLogger;
        } else {
            org.slf4j.Logger newInstance = new AppLoggerAdaptor(new ModuleLogger("SLF4J/" + name, application.getLogger()));
            org.slf4j.Logger oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
            return oldInstance == null ? newInstance : oldInstance;
        }
    }
}
