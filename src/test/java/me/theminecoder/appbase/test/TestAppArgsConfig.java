package me.theminecoder.appbase.test;

import com.beust.jcommander.Parameter;
import me.theminecoder.appbase.arg.ArgConfigBase;

/**
 * @author theminecoder
 */
public class TestAppArgsConfig extends ArgConfigBase {

    public enum TEST {
        ONE, TWO
    }

    @Parameter(names = {"--test"})
    public TEST test;

}
