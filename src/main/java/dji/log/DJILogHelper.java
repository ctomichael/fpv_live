package dji.log;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class DJILogHelper implements ComponentCallbacks2 {
    public static DJILogHelper INSTANCE = null;
    private static final long INTERVAL_TIME_PUT_TO_MEMORY = 100;
    private static final int MSG_LOG_TO_CACHE = 0;
    private static final int TRANSACTION_MAX_SIZE = 200;
    private static final int TRANSACTION_SIZE = 20;
    private static String[] filterTag = new String[0];
    private DJILogConsolePrinter consolePrinter;
    private Controller controller;
    private long currentTimePutToMemory;
    private DJILogFileManager fileManager;
    private LogHandler handler;
    private DJILogWarnHandler mConsoleWarnHandler;
    private DJILogWarnHandler mFileManagerHandler;
    private List<Interceptor> mInterceptors;
    private List<DJILogEntry> memoryCache = new LinkedList();

    private class LogHandler extends Handler {
        public LogHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DJILogHelper.this.putLogCacheToMemory((DJILogEntry) msg.obj);
                    return;
                default:
                    return;
            }
        }
    }

    public static DJILogHelper getInstance() {
        DJILogHelper dJILogHelper;
        synchronized (DJILogHelper.class) {
            if (INSTANCE == null) {
                INSTANCE = new DJILogHelper();
                INSTANCE.consolePrinter = new DJILogConsolePrinter();
                INSTANCE.fileManager = new DJILogFileManager();
            }
            dJILogHelper = INSTANCE;
        }
        return dJILogHelper;
    }

    private DJILogHelper() {
    }

    /* access modifiers changed from: package-private */
    public void init(Context context) {
        init(context, null, null);
    }

    /* access modifiers changed from: package-private */
    public void init(Context context, DJILogFileConfig fileConfig, DJILogConsoleConfig consoleConfig) {
        if (context == null) {
            throw new UnsupportedOperationException("context is null");
        }
        Context appContext = context.getApplicationContext();
        appContext.registerComponentCallbacks(this);
        this.mConsoleWarnHandler = new DJILogWarnHandler(appContext);
        this.mFileManagerHandler = new DJILogWarnHandler(appContext);
        this.handler = new LogHandler(Looper.getMainLooper());
        initLogHelper(appContext, fileConfig, consoleConfig);
    }

    private void initLogHelper(Context context, DJILogFileConfig fileConfig, DJILogConsoleConfig consoleConfig) {
        this.mInterceptors = new ArrayList();
        initManagers(context, fileConfig, consoleConfig);
    }

    private void initManagers(Context context, DJILogFileConfig fileConfig, DJILogConsoleConfig consoleConfig) {
        this.consolePrinter.init(context, consoleConfig);
        this.fileManager.init(context, fileConfig);
    }

    /* access modifiers changed from: package-private */
    public Controller controller() {
        if (this.controller == null) {
            this.controller = newController();
        }
        return this.controller;
    }

    /* access modifiers changed from: package-private */
    public void setController(Controller controller2) {
        this.controller = controller2;
    }

    /* access modifiers changed from: package-private */
    public Controller newController() {
        return new Controller();
    }

    /* access modifiers changed from: package-private */
    public String getRootDirectory() {
        return this.fileManager.getRootDirectory();
    }

    private String getCachePath() {
        return getCachePath(null);
    }

    private String getCachePath(String extraDir) {
        if (TextUtils.isEmpty(extraDir)) {
            return this.fileManager.getCachePath();
        }
        return this.fileManager.getCachePath(extraDir);
    }

    /* access modifiers changed from: package-private */
    public void v(String tag, String msg, Object... args) {
        dispatchLog(2, tag, msg, null, args);
    }

    /* access modifiers changed from: package-private */
    public void v(String tag, String msg, Throwable throwable, Object... args) {
        dispatchLog(2, tag, msg, throwable, args);
    }

    /* access modifiers changed from: package-private */
    public void d(String tag, String msg, Object... args) {
        dispatchLog(3, tag, msg, null, args);
    }

    /* access modifiers changed from: package-private */
    public void d(String tag, String msg, Throwable throwable, Object... args) {
        dispatchLog(3, tag, msg, throwable, args);
    }

    /* access modifiers changed from: package-private */
    public void i(String tag, String msg, Object... args) {
        dispatchLog(4, tag, msg, null, args);
    }

    /* access modifiers changed from: package-private */
    public void i(String tag, String msg, Throwable throwable, Object... args) {
        dispatchLog(4, tag, msg, throwable, args);
    }

    /* access modifiers changed from: package-private */
    public void w(String tag, String msg, Object... args) {
        dispatchLog(5, tag, msg, null, args);
    }

    /* access modifiers changed from: package-private */
    public void w(String tag, String msg, Throwable throwable, Object... args) {
        dispatchLog(5, tag, msg, throwable, args);
    }

    /* access modifiers changed from: package-private */
    public void e(String tag, String msg, Object... args) {
        dispatchLog(6, tag, msg, null, args);
    }

    /* access modifiers changed from: package-private */
    public void e(String tag, String msg, Throwable throwable, Object... args) {
        dispatchLog(6, tag, msg, throwable, args);
    }

    /* access modifiers changed from: package-private */
    public void println(int priority, String tag, String message) {
        println(priority, tag, message, null, new Object[0]);
    }

    private void println(int priority, String tag, String message, Throwable throwable, Object... args) {
        if (controller().print && priority >= controller().priority) {
            if (TextUtils.isEmpty(message)) {
                message = null;
            }
            if (message != null) {
                if (args != null && args.length > 0) {
                    message = DJILogUtils.formatMessage(message, args);
                }
                if (throwable != null) {
                    message = message + "\n" + DJILogUtils.getStackTraceString(throwable);
                }
            } else if (throwable != null) {
                message = DJILogUtils.getStackTraceString(throwable);
            } else {
                return;
            }
            if (controller().warn != 0) {
                try {
                    this.mConsoleWarnHandler.handleWarnKey(tag, message);
                    switch (priority) {
                        case 2:
                            this.mConsoleWarnHandler.v.incrementAndGet();
                            break;
                        case 3:
                            this.mConsoleWarnHandler.d.incrementAndGet();
                            break;
                        case 4:
                            this.mConsoleWarnHandler.i.incrementAndGet();
                            break;
                        case 5:
                            this.mConsoleWarnHandler.w.incrementAndGet();
                            break;
                        case 6:
                            this.mConsoleWarnHandler.e.incrementAndGet();
                            break;
                    }
                } catch (Exception e) {
                    try {
                        saveLog("warn error e=" + e.getMessage(), "DJIWarn│error");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }
            this.consolePrinter.println(priority, tag, message);
        }
    }

    /* access modifiers changed from: package-private */
    public long[] getPriorityRatio() {
        return new long[]{this.mConsoleWarnHandler.v.get(), this.mConsoleWarnHandler.d.get(), this.mConsoleWarnHandler.i.get(), this.mConsoleWarnHandler.w.get(), this.mConsoleWarnHandler.e.get()};
    }

    private void dispatchLog(int priority, String tag, String message, Throwable throwable, Object... args) {
        println(priority, tag, message, throwable, args);
    }

    /* access modifiers changed from: package-private */
    public void saveLog(String msg) {
        saveLog(msg, null);
    }

    /* access modifiers changed from: package-private */
    public void saveLog(String msg, String extraDir) {
        saveLog("DJIGo", msg, extraDir);
    }

    /* access modifiers changed from: package-private */
    public void saveLog(String tag, String msg, String extraDir) {
        saveLog(3, tag, msg, extraDir);
    }

    /* access modifiers changed from: package-private */
    public void saveLog(int level, String tag, String msg, String extraDir) {
        dispatchLogCache(level, tag, msg, getCachePath(extraDir), new Object[0]);
    }

    /* access modifiers changed from: package-private */
    public void saveExtraLog(String msg, String path) {
        saveExtraLog("DJIGo", msg, path);
    }

    /* access modifiers changed from: package-private */
    public void saveExtraLog(String tag, String msg, String path) {
        dispatchLogCache(3, tag, msg, this.fileManager.getExtraPath(path), new Object[0]);
    }

    /* access modifiers changed from: package-private */
    public void showView(String msg) {
        if (controller().print) {
        }
    }

    /* access modifiers changed from: package-private */
    public void flush() {
        if (this.memoryCache.size() > 0) {
            LinkedList<DJILogEntry> copy = new LinkedList<>();
            copy.addAll(this.memoryCache);
            this.memoryCache.clear();
            this.fileManager.saveLog(copy);
        }
    }

    /* access modifiers changed from: package-private */
    public void addInterceptors(Interceptor interceptor) {
        if (this.mInterceptors != null) {
            this.mInterceptors.add(interceptor);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeInterceptors(Interceptor interceptor) {
        if (this.mInterceptors != null && this.mInterceptors.contains(interceptor)) {
            this.mInterceptors.remove(interceptor);
        }
    }

    private void dispatchLogCache(int level, String tag, String msg, String path, Object... args) {
        if (controller().save) {
            if (controller().warn != 0) {
                String key = "";
                try {
                    String cachePath = this.fileManager.getCachePath();
                    String rootPath = this.fileManager.getRootDirectory();
                    if (path.startsWith(cachePath)) {
                        key = path.replace(cachePath, "");
                    } else if (path.startsWith(rootPath)) {
                        key = path.replace(rootPath, "");
                    }
                    if (!TextUtils.isEmpty(key)) {
                        this.mFileManagerHandler.handleWarnKey(key.replace(File.separator, ""), msg);
                    }
                } catch (Exception e) {
                    try {
                        saveLog("warn error e=" + e.getMessage(), "DJIWarn│error");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }
            this.handler.sendMessage(this.handler.obtainMessage(0, obtainLogEntry(level, tag, msg, path)));
        }
    }

    private DJILogEntry obtainLogEntry(int level, String tag, String msg, String path) {
        DJILogEntry entry = new DJILogEntry();
        entry.level = level;
        entry.tag = tag;
        entry.path = path;
        entry.msg = msg;
        entry.time = Calendar.getInstance().getTimeInMillis();
        entry.elapsedTime = System.currentTimeMillis() - this.currentTimePutToMemory;
        this.currentTimePutToMemory = System.currentTimeMillis();
        for (Interceptor interceptor : this.mInterceptors) {
            interceptor.interceptor(entry);
        }
        return entry;
    }

    /* access modifiers changed from: private */
    public void putLogCacheToMemory(DJILogEntry cache) {
        this.memoryCache.add(cache);
        if (this.memoryCache.size() < 20) {
            return;
        }
        if (cache.elapsedTime > INTERVAL_TIME_PUT_TO_MEMORY || this.memoryCache.size() >= 200) {
            flush();
        }
    }

    public void autoHandle() {
        if (!controller().print) {
        }
    }

    public void closeLog() {
    }

    public String getLogParentDir() {
        return getCachePath();
    }

    public String getLogName() {
        return this.fileManager.generateFileName();
    }

    public void LOGI(String tag, String msg) {
        i(tag, msg, new Object[0]);
    }

    public void LOGD(String tag, String msg) {
        d(tag, msg, new Object[0]);
    }

    public void LOGE(String tag, String msg) {
        e(tag, msg, new Object[0]);
    }

    public void LOGI(String tag, String msg, boolean isSaveToFile, boolean isShowOnView) {
        if (!checkTagFilter(tag)) {
            i(tag, msg, new Object[0]);
            if (isSaveToFile) {
                saveLog(msg);
            }
        }
    }

    public void LOGD(String tag, String msg, boolean isSaveToFile, boolean isShowOnView) {
        if (!checkTagFilter(tag)) {
            d(tag, msg, new Object[0]);
            if (isSaveToFile) {
                saveLog(msg);
            }
        }
    }

    public void LOGE(String tag, String msg, boolean isSaveToFile, boolean isShowOnView) {
        if (!checkTagFilter(tag)) {
            e(tag, msg, new Object[0]);
            if (isSaveToFile) {
                saveLog(msg);
            }
        }
    }

    public void LOGI(String tag, String msg, String dirName) {
        i(tag, msg, new Object[0]);
        saveLog(4, tag, msg, dirName);
    }

    public void LOGD(String tag, String msg, String dirName) {
        d(tag, msg, new Object[0]);
        saveLog(3, tag, msg, dirName);
    }

    public void LOGE(String tag, String msg, String dirName) {
        e(tag, msg, new Object[0]);
        saveLog(6, tag, msg, dirName);
    }

    private boolean checkTagFilter(String tag) {
        if (filterTag.length == 0) {
            return false;
        }
        for (String filter : filterTag) {
            if (filter.equals(tag)) {
                return false;
            }
        }
        return true;
    }

    public void logNoVideoData() {
        saveExtraLog(DJILogUtils.formatNow(DJILogUtils.FORMAT_2) + ": no video data", DJILogPaths.LOG_EXTRA_NO_VIDEO);
    }

    private void nfzSaveCrashInfo2File(String s) {
        saveExtraLog(s, DJILogPaths.LOG_EXTRA_NFZ);
    }

    public void NFZSavedLOGE(String tag, String msg, boolean isSaveToFile, boolean isShowOnView) {
        if (controller().print) {
            Log.e(tag, msg);
        }
        if (isSaveToFile) {
            nfzSaveCrashInfo2File(msg);
        }
    }

    public void writeMapLog(String log) {
        saveExtraLog(log, DJILogPaths.LOG_EXTRA_MAP);
    }

    public void onTrimMemory(int level) {
        flush();
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onLowMemory() {
    }
}
