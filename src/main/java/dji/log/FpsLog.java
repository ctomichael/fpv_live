package dji.log;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.BackgroundLooper;
import java.util.concurrent.locks.ReentrantLock;

@Keep
@EXClassNullAway
public class FpsLog {
    /* access modifiers changed from: private */
    public StringBuilder builder;
    private long lastTime;
    ReentrantLock lock;
    /* access modifiers changed from: private */
    public Runnable logRunnable;

    private FpsLog() {
        this.lastTime = -1;
        this.logRunnable = new Runnable() {
            /* class dji.log.FpsLog.AnonymousClass1 */

            public void run() {
                String log = FpsLog.this.builder.toString();
                if (log != null && !log.isEmpty()) {
                    StringBuilder unused = FpsLog.this.builder = new StringBuilder();
                    DJILogHelper.getInstance().LOGI("FpsLog", log, "FpsLog");
                    BackgroundLooper.postDelayed(FpsLog.this.logRunnable, 1000);
                }
            }
        };
        this.lock = new ReentrantLock();
        this.builder = new StringBuilder();
        BackgroundLooper.postDelayed(this.logRunnable, 1000);
    }

    public static FpsLog getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void d(String log) {
        this.lock.lock();
        try {
            this.builder.append("\r\n");
            this.builder.append(log);
        } finally {
            this.lock.unlock();
        }
    }

    @Keep
    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final FpsLog INSTANCE = new FpsLog();

        private SingletonHolder() {
        }
    }
}
