package com.loc;

import java.util.concurrent.ThreadFactory;

final class df implements ThreadFactory {
    df() {
    }

    public final Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("httpdns worker");
        thread.setDaemon(false);
        thread.setUncaughtExceptionHandler(new dm());
        return thread;
    }
}
