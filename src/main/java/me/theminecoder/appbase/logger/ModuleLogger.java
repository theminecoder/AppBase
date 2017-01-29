/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.theminecoder.appbase.logger;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ModuleLogger extends Logger {
    private final Logger parent;

	/**
	 * Construct a new ModuleLogger
	 *
	 * @param name		of the Module which should be suffix
	 * @param parent	to log into
	 */
    public ModuleLogger( String name, Logger parent ) {
        super( parent.getName() + "/" + name, null );
        this.parent = parent;
        setLevel( parent.getLevel() );
    }

    @Override
    public void log( LogRecord logRecord ) {
        parent.log( logRecord );
    }
}
