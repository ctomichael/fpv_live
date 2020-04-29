package com.loc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

final class de {
    private static final TimeUnit a = TimeUnit.SECONDS;
    private static final ThreadFactory b = new df();
    private static final ExecutorService c = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 1, a, new SynchronousQueue(), b);

    static ExecutorService a() {
        return c;
    }
}
