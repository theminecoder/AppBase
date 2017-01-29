/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.theminecoder.appbase.logger;

import jline.console.ConsoleReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;


public class LoggingOutputStream extends ByteArrayOutputStream {
    private final Logger logger;
    private final Level level;

    public LoggingOutputStream(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    @Override
    public void flush() throws IOException {
        // Get the content which is inside the underlying ByteArray and reset the ByteArray
        String contents = toString();
        super.reset();

        // Only take contents which are non empty
        if (!contents.isEmpty() && !contents.equals(ConsoleReader.CR)) {
            logger.logp(level, "", "", contents);
        }
    }
}