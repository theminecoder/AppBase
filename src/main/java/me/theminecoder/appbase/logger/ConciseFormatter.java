/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.theminecoder.appbase.logger;

import me.theminecoder.appbase.util.ConsoleColor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A JDK Logger based Logrecord formatter. This can color different Loglevels if wanted. The colors used are:
 * <p>
 * INFO: Blue
 * WARNING: Dark Red
 * SEVERE: Red
 * All other: Yellow
 *
 * @author geNAZt
 * @version 1.0
 */
public class ConciseFormatter extends Formatter {
    private final DateFormat date = new SimpleDateFormat("HH:mm:ss");
    private final boolean colored;

    /**
     * Construct a new Formatter
     *
     * @param colored true when it should color the records based on its level, false if it shouldn't
     */
    public ConciseFormatter(boolean colored) {
        this.colored = colored;
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Override
    public String format(LogRecord record) {
        StringBuilder formatted = new StringBuilder();

        if (colored) {
            ConsoleColor color = ConsoleColor.YELLOW;

            if (record.getLevel().equals(Level.INFO)) {
                color = ConsoleColor.BLUE;
            } else if (record.getLevel().equals(Level.WARNING)) {
                color = ConsoleColor.GOLD;
            } else if (record.getLevel().equals(Level.SEVERE)) {
                color = ConsoleColor.RED;
            }

            formatted.append(color);
        }

        formatted.append("[")
                .append(date.format(record.getMillis()))
                .append(" - ")
                .append(record.getLevel().getLocalizedName())
                .append("] [")
                .append(record.getLoggerName())
                .append("] ");

        if (colored) {
            formatted.append(ConsoleColor.RESET);
        }

        formatted.append(formatMessage(record));
        formatted.append('\n');

        if (record.getThrown() != null) {
            StringWriter writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            formatted.append(writer);
        }

        return formatted.toString();
    }
}