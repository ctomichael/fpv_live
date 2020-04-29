package dji.log;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Toast;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DJILogWarnHandler {
    private static final String INVALID_EMPTY_KEY = "Empty";
    private static final int STATISTICS_INIT = 1;
    private static final int WARN_BORDER = 100;
    private static final int WARN_CONTENT_CUT_OUT_LENGTH = 20;
    private static final int WARN_KEY_LENGTH = 50;
    private static final int WARN_TIME_MILLIS = 1000;
    /* access modifiers changed from: private */
    public Context context;
    public AtomicLong d = new AtomicLong();
    public AtomicLong e = new AtomicLong();
    private Handler handler;
    private ConcurrentHashMap<String, Integer> hashKeyStatistics = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Long> hashKeyTimeStatistics = new ConcurrentHashMap<>();
    public AtomicLong i = new AtomicLong();
    public AtomicLong v = new AtomicLong();
    public AtomicLong w = new AtomicLong();

    DJILogWarnHandler(Context context2) {
        this.context = context2.getApplicationContext();
        this.handler = new Handler(Looper.getMainLooper());
    }

    /* access modifiers changed from: package-private */
    public void handleWarnKey(String key, String content) {
        if (TextUtils.isEmpty(key) || !key.startsWith("DJIWarn│")) {
            if (TextUtils.isEmpty(key)) {
                emptyKeyWarn(INVALID_EMPTY_KEY, content);
            } else if (key.startsWith("DJIWarn│")) {
                return;
            } else {
                if (key.length() > 50) {
                    tooLongKeyWarn(key, content);
                }
            }
            if (this.hashKeyStatistics.containsKey(key)) {
                int previous = this.hashKeyStatistics.get(key).intValue();
                long uptimeMillis = this.hashKeyTimeStatistics.get(key).longValue();
                if (previous >= 99) {
                    long time = SystemClock.uptimeMillis() - uptimeMillis;
                    if (time <= 1000) {
                        highFrequencyWarn(key, content, ((float) time) / 1000.0f);
                    }
                    this.hashKeyStatistics.remove(key);
                    this.hashKeyTimeStatistics.remove(key);
                    return;
                }
                this.hashKeyStatistics.put(key, Integer.valueOf(previous + 1));
                return;
            }
            this.hashKeyTimeStatistics.put(key, Long.valueOf(SystemClock.uptimeMillis()));
            this.hashKeyStatistics.put(key, 1);
        }
    }

    private void emptyKeyWarn(String key, String content) {
    }

    private void tooLongKeyWarn(String key, String content) {
        dispatchWarn(TextUtils.substring(key, 0, 30) + "...", content, "关键字长度为" + key.length());
    }

    private void highFrequencyWarn(String key, String content, float seconds) {
        dispatchWarn(key, content, "100次打印或写入耗时" + seconds + "秒");
    }

    private void dispatchWarn(String key, String content, String warn) {
        String warnContent = TextUtils.isEmpty(content) ? content : TextUtils.substring(content, 0, Math.min(20, content.length() - 1));
        switch (DJILog.controller().warn) {
            case 1:
                consoleWarn(key, DJILogUtils.formatMessage(DJILogConstant.FORMAT_CONSOLE_WARN, key, warnContent, warn));
                return;
            case 2:
                toastWarn(DJILogUtils.formatMessage(DJILogConstant.FORMAT_TOAST_WARN, key, warnContent, warn));
                return;
            default:
                return;
        }
    }

    private void toastWarn(final String warn) {
        this.handler.post(new Runnable() {
            /* class dji.log.DJILogWarnHandler.AnonymousClass1 */

            public void run() {
                Toast.makeText(DJILogWarnHandler.this.context, warn, 1).show();
            }
        });
    }

    private void consoleWarn(String key, String warn) {
        DJILog.e("DJIWarn│" + key, warn, new Object[0]);
    }
}
