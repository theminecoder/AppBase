/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.theminecoder.appbase.logger;

import me.theminecoder.appbase.Application;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

/**
 * A async dispatcher for log messages
 *
 * @author geNAZt
 * @version 1.0
 */
public class LogDispatcher implements Runnable {
    private final Application application;
    private final Logger logger;
    private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();

    public LogDispatcher(Application application, Logger logger) {
        this.application = application;
        this.logger = logger;
    }

    @Override
    public void run() {
        // Don't run if the application has been shutdown
        while (this.application.isRunning()) {
            LogRecord record;

            try {
                record = queue.poll(5, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                continue;
            }

            if (record != null) {
                logger.doLog(record);
            }
        }

        // When the application did shut down, log the remaining messages from the queue
        this.queue.forEach(logger::doLog);
    }

    public void queue(LogRecord record) {
        if (this.application.isRunning()) {
            queue.add(record);
        }
    }
}