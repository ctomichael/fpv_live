package dji.publics;

import android.os.AsyncTask;
import android.os.Build;
import android.os.HandlerThread;
import android.os.Looper;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DJIExecutor {
    private static final int CORE_POOL_SIZE = (CPU_COUNT + 1);
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE = 1;
    private static final Object LOCK = new Object();
    private static final int MAXIMUM_POOL_SIZE = (URGENT_MAXIMUM_POOL_SIZE * 2);
    private static final String TAG = DJIExecutor.class.getCanonicalName();
    private static final int URGENT_MAXIMUM_POOL_SIZE = Math.min(Math.max(10, CORE_POOL_SIZE * 2), Math.max(5, CORE_POOL_SIZE));
    private static volatile ExecutorService sDefaultExecutor;
    static volatile HandlerThread sHandlerThread;
    private static volatile ExecutorService sIOExecutor;
    private static volatile ExecutorService sUrgentExecutor;

    public enum Purpose {
        NORMAL,
        URGENT,
        IO
    }

    private static ExecutorService getDefaultExecutor() {
        if (sDefaultExecutor == null) {
            synchronized (LOCK) {
                if (sDefaultExecutor == null) {
                    ExecutorService executor = getAsyncTaskExecutor();
                    if (executor == null) {
                        ThreadFactory threadFactory = new ThreadFactory() {
                            /* class dji.publics.DJIExecutor.AnonymousClass1 */
                            private final AtomicInteger mCount = new AtomicInteger(1);

                            public Thread newThread(Runnable r) {
                                Thread thread = new Thread(r, "DJIExecutor Nomal #" + this.mCount.getAndIncrement());
                                thread.setPriority(5);
                                return thread;
                            }
                        };
                        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(128), threadFactory);
                    }
                    sDefaultExecutor = executor;
                }
            }
        }
        return sDefaultExecutor;
    }

    private static ExecutorService getIOExecutor() {
        if (sIOExecutor == null) {
            synchronized (LOCK) {
                if (sIOExecutor == null) {
                    sIOExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
                        /* class dji.publics.DJIExecutor.AnonymousClass2 */
                        private final AtomicInteger mCount = new AtomicInteger(1);

                        public Thread newThread(Runnable r) {
                            Thread thread = new Thread(r, "DJIExecutor IO #" + this.mCount.getAndIncrement());
                            thread.setPriority(3);
                            return thread;
                        }
                    });
                }
            }
        }
        return sIOExecutor;
    }

    private static ExecutorService getUrgentExecutor() {
        if (sUrgentExecutor == null) {
            synchronized (LOCK) {
                if (sUrgentExecutor == null) {
                    sUrgentExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, URGENT_MAXIMUM_POOL_SIZE, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
                        /* class dji.publics.DJIExecutor.AnonymousClass3 */
                        private final AtomicInteger mCount = new AtomicInteger(1);

                        public Thread newThread(Runnable r) {
                            Thread thread = new Thread(r, "DJIExecutor Urgent #" + this.mCount.getAndIncrement());
                            thread.setPriority(7);
                            return thread;
                        }
                    });
                }
            }
        }
        return sUrgentExecutor;
    }

    public static ExecutorService getExecutorFor(Purpose purpose) {
        switch (purpose) {
            case NORMAL:
                return getDefaultExecutor();
            case IO:
                return getIOExecutor();
            case URGENT:
                return getUrgentExecutor();
            default:
                return getDefaultExecutor();
        }
    }

    public static ExecutorService getExecutor() {
        return getExecutorFor(Purpose.NORMAL);
    }

    private static ExecutorService getAsyncTaskExecutor() {
        if (Build.VERSION.SDK_INT < 11) {
            return null;
        }
        try {
            try {
                Object executorObject = AsyncTask.class.getField("THREAD_POOL_EXECUTOR").get(null);
                if (executorObject == null) {
                    return null;
                }
                if (!(executorObject instanceof Executor)) {
                    return null;
                }
                return (ExecutorService) executorObject;
            } catch (IllegalAccessException e) {
                return null;
            }
        } catch (NoSuchFieldException e2) {
            return null;
        }
    }

    public static Looper getLooper() {
        if (sHandlerThread == null) {
            synchronized (DJIExecutor.class) {
                if (sHandlerThread == null) {
                    HandlerThread handlerThread = new HandlerThread("DJIExecutor:handlerThread");
                    handlerThread.start();
                    sHandlerThread = handlerThread;
                }
            }
        }
        return sHandlerThread.getLooper();
    }
}
