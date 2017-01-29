package me.theminecoder.appbase.arg;

import com.beust.jcommander.Parameter;

/**
 * @author theminecoder
 */
public class ArgConfigBase {

    @Parameter(names = {"--debug"}, description = "Enables debug log messages")
    private boolean debug = false;

    @Parameter(names = {"--help", "-h"}, help = true, description = "Displays this message and exits")
    private boolean displayHelp = false;

    @Parameter(names = {"--disable-ansi"}, description = "Disables color terminal output")
    private boolean disableAnsi = false;

    public boolean isDebug() {
        return debug;
    }

    public boolean isDisplayHelp() {
        return displayHelp;
    }

    public boolean isDisableAnsi() {
        return disableAnsi;
    }
}
