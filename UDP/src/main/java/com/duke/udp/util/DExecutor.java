package com.duke.udp.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author: duke
 * dateTime: 2019-04-05 16:15
 * description: 线程池
 */
public class DExecutor {

    private static volatile DExecutor instance = null;
    private static volatile ThreadPoolExecutor executor = null;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread #" + this.mCount.getAndIncrement());
        }
    };

    private DExecutor() {
    }

    public static DExecutor get() {
        if (instance == null) {
            synchronized (DExecutor.class) {
                if (instance == null) {
                    instance = new DExecutor();
                    //可变线程的线程池
                    executor = (ThreadPoolExecutor) Executors.newCachedThreadPool(sThreadFactory);
                    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
                    //单线程的线程池
                    //Executors.newSingleThreadExecutor().execute(this);
                }
            }
        }
        return instance;
    }

    public Executor getInternalExecutor() {
        return executor;
    }

    public void execute(Runnable command) {
        executor.execute(command);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void shutdownNow() {
        executor.shutdownNow();
    }

}