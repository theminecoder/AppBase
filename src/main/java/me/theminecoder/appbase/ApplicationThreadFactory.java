package me.theminecoder.appbase;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author theminecoder
 */
public class ApplicationThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger();
    private final ThreadGroup threadGroup;
    private final AtomicInteger threadNumber = new AtomicInteger();
    private final String prefix;

    public ApplicationThreadFactory(Application application) {
        this.threadGroup = Thread.currentThread().getThreadGroup();
        this.prefix = application.getApplicationName().replace(' ', '-') + "-pool-" + poolNumber.incrementAndGet() + "-thread-";
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(threadGroup, r, prefix + threadNumber.incrementAndGet(), 0);
        return thread;
    }
}
