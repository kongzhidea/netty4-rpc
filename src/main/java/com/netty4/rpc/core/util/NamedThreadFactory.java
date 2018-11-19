package com.netty4.rpc.core.util;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadPoolExecutor threadPool = newFixedThreadPool(8, new NamedThreadFactory("MyService"));
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final String namePrefix;
    private final boolean isDaemon;

    public NamedThreadFactory() {
        this("pool");
    }

    public NamedThreadFactory(String name) {
        this(name, false);
    }

    public NamedThreadFactory(String preffix, boolean daemon) {
        SecurityManager s = System.getSecurityManager();

        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();

        namePrefix = preffix + "-" + poolNumber.getAndIncrement() + "-thread-";

        isDaemon = daemon;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);

        //Default value is parent thread's
        t.setContextClassLoader(NamedThreadFactory.class.getClassLoader());

        t.setPriority(Thread.MAX_PRIORITY);

        t.setDaemon(isDaemon);
        return t;
    }

}