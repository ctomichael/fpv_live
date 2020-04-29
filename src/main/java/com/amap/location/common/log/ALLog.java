package com.amap.location.common.log;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import com.amap.location.common.HeaderConfig;
import com.amap.location.common.log.LogConfig;
import com.amap.location.common.util.FileUtil;
import com.amap.location.common.util.a;
import java.io.File;
import java.io.FileFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class ALLog {
    private static final long CHECK_FULL_INTERVAL = 20000;
    private static final SimpleDateFormat FILE_NAME_TIME = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS", Locale.US);
    private static final int LOG_LEVEL_ERROR = 4;
    private static final int LOG_LEVEL_INFO = 1;
    private static final int LOG_LEVEL_WARN = 2;
    private static final SimpleDateFormat LOG_LINE_TIME = new SimpleDateFormat("MM-dd HH:mm:ss:SSS", Locale.US);
    private static final long MAX_BUFFER_SIZE = 5;
    private static final long MAX_DUMP_LOG_LINE_NUM = 5000;
    private static long MAX_FILE_NUM = 20;
    private static long MAX_LOG_CACHE_SIZE = 204800;
    private static long MAX_SINGLE_FILE_LENGTH = 1048576;
    private static final int MSG_BUFFER_FULL = 1;
    private static final int MSG_CHECK_BUFFER = 2;
    private static final String TAG = "ALLog";
    private static final String TRACE_PREFIX = "trace_";
    /* access modifiers changed from: private */
    public static volatile Handler mAALogHandler;
    /* access modifiers changed from: private */
    public static volatile HandlerThread mAALogHandlerThread;
    private static LinkedList<LinkedList<String>> mBufferList = new LinkedList<>();
    /* access modifiers changed from: private */
    public static volatile Context mContext;
    /* access modifiers changed from: private */
    public static volatile File mCurrLogFile = null;
    /* access modifiers changed from: private */
    public static volatile boolean mFileLogReady = false;
    private static volatile boolean mGlobalFileLogEnable = false;
    private static volatile boolean mGlobalLogcatEnable = false;
    private static volatile boolean mGlobalServerLogEnable = false;
    /* access modifiers changed from: private */
    public static final Runnable mInitLogFileTask = new Runnable() {
        /* class com.amap.location.common.log.ALLog.AnonymousClass2 */

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void run() {
            /*
                r5 = this;
                boolean r0 = com.amap.location.common.log.ALLog.prepareLogDir()     // Catch:{ Exception -> 0x0049 }
                if (r0 != 0) goto L_0x000a
                com.amap.location.common.log.ALLog.dispose()     // Catch:{ Exception -> 0x0049 }
            L_0x0009:
                return
            L_0x000a:
                java.lang.String r0 = com.amap.location.common.log.ALLog.mLogFileDir     // Catch:{ Exception -> 0x0049 }
                java.io.File[] r1 = com.amap.location.common.log.ALLog.getFilesByLastModify(r0)     // Catch:{ Exception -> 0x0049 }
                if (r1 == 0) goto L_0x002d
                int r0 = r1.length     // Catch:{ Exception -> 0x0049 }
                if (r0 <= 0) goto L_0x002d
                java.util.ArrayDeque r2 = com.amap.location.common.log.ALLog.mLogFileDeque     // Catch:{ Exception -> 0x0049 }
                monitor-enter(r2)     // Catch:{ Exception -> 0x0049 }
                r0 = 0
            L_0x001d:
                int r3 = r1.length     // Catch:{ all -> 0x0054 }
                if (r0 >= r3) goto L_0x002c
                java.util.ArrayDeque r3 = com.amap.location.common.log.ALLog.mLogFileDeque     // Catch:{ all -> 0x0054 }
                r4 = r1[r0]     // Catch:{ all -> 0x0054 }
                r3.offer(r4)     // Catch:{ all -> 0x0054 }
                int r0 = r0 + 1
                goto L_0x001d
            L_0x002c:
                monitor-exit(r2)     // Catch:{ all -> 0x0054 }
            L_0x002d:
                android.content.Context r0 = com.amap.location.common.log.ALLog.mContext     // Catch:{ Exception -> 0x0049 }
                java.lang.String r0 = com.amap.location.common.log.a.a(r0)     // Catch:{ Exception -> 0x0049 }
                java.lang.String unused = com.amap.location.common.log.ALLog.mLogFileHeaderString = r0     // Catch:{ Exception -> 0x0049 }
                java.io.File r0 = com.amap.location.common.log.ALLog.getNewLogFile()     // Catch:{ Exception -> 0x0049 }
                java.io.File unused = com.amap.location.common.log.ALLog.mCurrLogFile = r0     // Catch:{ Exception -> 0x0049 }
                java.io.File r0 = com.amap.location.common.log.ALLog.mCurrLogFile     // Catch:{ Exception -> 0x0049 }
                if (r0 != 0) goto L_0x0057
                com.amap.location.common.log.ALLog.dispose()     // Catch:{ Exception -> 0x0049 }
                goto L_0x0009
            L_0x0049:
                r0 = move-exception
                java.lang.String r1 = "ALLog"
                java.lang.String r2 = "InitLogFileTask  error "
                com.amap.location.common.log.ALLog.e(r1, r2, r0)
                goto L_0x0009
            L_0x0054:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x0054 }
                throw r0     // Catch:{ Exception -> 0x0049 }
            L_0x0057:
                r0 = 1
                boolean unused = com.amap.location.common.log.ALLog.mFileLogReady = r0     // Catch:{ Exception -> 0x0049 }
                android.os.Handler r0 = com.amap.location.common.log.ALLog.mAALogHandler     // Catch:{ Exception -> 0x0049 }
                r1 = 2
                android.os.Message r0 = r0.obtainMessage(r1)     // Catch:{ Exception -> 0x0049 }
                android.os.Handler r1 = com.amap.location.common.log.ALLog.mAALogHandler     // Catch:{ Exception -> 0x0049 }
                r2 = 20000(0x4e20, double:9.8813E-320)
                r1.sendMessageDelayed(r0, r2)     // Catch:{ Exception -> 0x0049 }
                goto L_0x0009
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.location.common.log.ALLog.AnonymousClass2.run():void");
        }
    };
    private static boolean mIsTraceUpToServer = true;
    private static boolean mIsTraceWriteToFile = false;
    private static volatile LinkedList<String> mLogBuffer = new LinkedList<>();
    private static final Object mLogBufferLock = new Object();
    private static long mLogCacheSize;
    /* access modifiers changed from: private */
    public static final ArrayDeque<File> mLogFileDeque = new ArrayDeque<>();
    /* access modifiers changed from: private */
    public static volatile String mLogFileDir = "";
    /* access modifiers changed from: private */
    public static String mLogFileHeaderString = "";
    private static LogConfig.a mLogToServerImpl;
    private static String mPid = "";
    private static LogConfig.Product mProduct = LogConfig.Product.SDK;
    private static String mProductStr = "sdk";
    private static final Date sDate = new Date();

    private ALLog() {
    }

    private static String currFomatTime(DateFormat dateFormat) {
        String format;
        synchronized (sDate) {
            sDate.setTime(System.currentTimeMillis());
            format = dateFormat.format(sDate);
        }
        return format;
    }

    /* access modifiers changed from: private */
    public static void dispose() {
        mFileLogReady = false;
        try {
            if (mAALogHandlerThread != null) {
                if (Build.VERSION.SDK_INT > 18) {
                    mAALogHandlerThread.quitSafely();
                } else {
                    mAALogHandlerThread.quit();
                }
            }
            mAALogHandler = null;
            mAALogHandlerThread = null;
            synchronized (mLogFileDeque) {
                mLogFileDeque.clear();
            }
            synchronized (mLogBufferLock) {
                mLogBuffer.clear();
                mBufferList.clear();
            }
        } catch (Exception e) {
            e(TAG, "dispose error ", e);
            mAALogHandler = null;
            mAALogHandlerThread = null;
            synchronized (mLogFileDeque) {
                mLogFileDeque.clear();
                synchronized (mLogBufferLock) {
                    mLogBuffer.clear();
                    mBufferList.clear();
                }
            }
        } catch (Throwable th) {
            mAALogHandler = null;
            mAALogHandlerThread = null;
            synchronized (mLogFileDeque) {
                mLogFileDeque.clear();
                synchronized (mLogBufferLock) {
                    mLogBuffer.clear();
                    mBufferList.clear();
                    throw th;
                }
            }
        }
    }

    public static void e(String str, String str2) {
        if (mGlobalLogcatEnable) {
            Log.e(str, str2);
        }
    }

    public static void e(String str, String str2, Throwable th) {
        if (mGlobalLogcatEnable) {
            Log.e(str, str2, th);
        }
    }

    public static void e(String str, String str2, Throwable th, boolean z) {
        e(str, str2, th);
        writeLog(4, str, str2 + Log.getStackTraceString(th), z, false);
    }

    public static void e(String str, String str2, Throwable th, boolean z, boolean z2) {
        e(str, str2, th);
        writeLog(4, str, str2 + Log.getStackTraceString(th), z, z2);
    }

    public static void e(String str, String str2, boolean z) {
        e(str, str2);
        writeLog(4, str, str2, z, false);
    }

    public static void e(String str, String str2, boolean z, boolean z2) {
        e(str, str2);
        writeLog(4, str, str2, z, z2);
    }

    /* access modifiers changed from: private */
    public static File[] getFilesByLastModify(String str) {
        File[] listFiles = new File(str).listFiles(new FileFilter() {
            /* class com.amap.location.common.log.ALLog.AnonymousClass3 */

            public final boolean accept(File file) {
                return !file.isDirectory();
            }
        });
        if (listFiles == null || listFiles.length == 0) {
            return null;
        }
        Arrays.sort(listFiles, new Comparator<File>() {
            /* class com.amap.location.common.log.ALLog.AnonymousClass4 */

            /* renamed from: a */
            public final int compare(File file, File file2) {
                long lastModified = file.lastModified() - file2.lastModified();
                if (lastModified > 0) {
                    return 1;
                }
                return lastModified < 0 ? -1 : 0;
            }
        });
        return listFiles;
    }

    public static String getLogFileDir() {
        return mLogFileDir;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.location.common.util.FileUtil.writeToFile(java.lang.String, java.io.File, boolean):boolean
     arg types: [java.lang.String, java.io.File, int]
     candidates:
      com.amap.location.common.util.FileUtil.writeToFile(byte[], java.io.File, boolean):boolean
      com.amap.location.common.util.FileUtil.writeToFile(java.lang.String, java.io.File, boolean):boolean */
    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002e, code lost:
        r0 = new java.io.File(com.amap.location.common.log.ALLog.mLogFileDir, getProductStr() + "_log_" + currFomatTime(com.amap.location.common.log.ALLog.FILE_NAME_TIME) + ".txt");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r0.createNewFile();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0067, code lost:
        if (android.text.TextUtils.isEmpty(com.amap.location.common.log.ALLog.mLogFileHeaderString) != false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0069, code lost:
        com.amap.location.common.util.FileUtil.writeToFile(com.amap.location.common.log.ALLog.mLogFileHeaderString + "\r\n-------------------\r\n", r0, true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        return null;
     */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.File getNewLogFile() {
        /*
            r1 = 0
            java.util.ArrayDeque<java.io.File> r2 = com.amap.location.common.log.ALLog.mLogFileDeque
            monitor-enter(r2)
            java.util.ArrayDeque<java.io.File> r0 = com.amap.location.common.log.ALLog.mLogFileDeque     // Catch:{ all -> 0x0084 }
            int r0 = r0.size()     // Catch:{ all -> 0x0084 }
            if (r0 <= 0) goto L_0x008a
            java.util.ArrayDeque<java.io.File> r0 = com.amap.location.common.log.ALLog.mLogFileDeque     // Catch:{ all -> 0x0084 }
            java.lang.Object r0 = r0.getLast()     // Catch:{ all -> 0x0084 }
            java.io.File r0 = (java.io.File) r0     // Catch:{ all -> 0x0084 }
        L_0x0014:
            if (r0 == 0) goto L_0x002d
            long r4 = r0.length()     // Catch:{ all -> 0x0084 }
            long r6 = com.amap.location.common.log.ALLog.MAX_SINGLE_FILE_LENGTH     // Catch:{ all -> 0x0084 }
            r8 = 2
            long r6 = r6 * r8
            r8 = 3
            long r6 = r6 / r8
            int r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r3 >= 0) goto L_0x002d
            java.util.ArrayDeque<java.io.File> r1 = com.amap.location.common.log.ALLog.mLogFileDeque     // Catch:{ all -> 0x0084 }
            r1.removeLast()     // Catch:{ all -> 0x0084 }
            monitor-exit(r2)     // Catch:{ all -> 0x0084 }
        L_0x002c:
            return r0
        L_0x002d:
            monitor-exit(r2)     // Catch:{ all -> 0x0084 }
            java.io.File r0 = new java.io.File
            java.lang.String r2 = com.amap.location.common.log.ALLog.mLogFileDir
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = getProductStr()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "_log_"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.text.SimpleDateFormat r4 = com.amap.location.common.log.ALLog.FILE_NAME_TIME
            java.lang.String r4 = currFomatTime(r4)
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = ".txt"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r0.<init>(r2, r3)
            r0.createNewFile()     // Catch:{ IOException -> 0x0087 }
            java.lang.String r1 = com.amap.location.common.log.ALLog.mLogFileHeaderString
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 != 0) goto L_0x002c
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = com.amap.location.common.log.ALLog.mLogFileHeaderString
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = "\r\n-------------------\r\n"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r2 = 1
            com.amap.location.common.util.FileUtil.writeToFile(r1, r0, r2)
            goto L_0x002c
        L_0x0084:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0084 }
            throw r0
        L_0x0087:
            r0 = move-exception
            r0 = r1
            goto L_0x002c
        L_0x008a:
            r0 = r1
            goto L_0x0014
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.location.common.log.ALLog.getNewLogFile():java.io.File");
    }

    public static LogConfig.Product getProduct() {
        return mProduct;
    }

    private static String getProductStr() {
        return mProductStr;
    }

    /* access modifiers changed from: private */
    public static void handleMsg(Message message) {
        switch (message.what) {
            case 1:
                LinkedList linkedList = null;
                synchronized (mLogBufferLock) {
                    if (mBufferList.size() > 0) {
                        linkedList = mBufferList.removeFirst();
                    }
                }
                System.currentTimeMillis();
                syncDataToFile(linkedList);
                System.currentTimeMillis();
                if (mAALogHandler != null) {
                    mAALogHandler.sendMessageDelayed(mAALogHandler.obtainMessage(2), CHECK_FULL_INTERVAL);
                    return;
                }
                return;
            case 2:
                synchronized (mLogBufferLock) {
                    if (mAALogHandler != null) {
                        mBufferList.add(mLogBuffer);
                        while (((long) mBufferList.size()) > 5) {
                            mBufferList.removeFirst();
                        }
                        mAALogHandler.obtainMessage(1).sendToTarget();
                        mLogBuffer = new LinkedList<>();
                        mLogCacheSize = 0;
                    } else {
                        mLogBuffer.clear();
                        mLogCacheSize = 0;
                    }
                }
                return;
            default:
                return;
        }
    }

    public static void i(String str, String str2) {
        if (mGlobalLogcatEnable) {
            Log.i(str, str2);
        }
    }

    public static void i(String str, String str2, boolean z) {
        i(str, str2);
        writeLog(1, str, str2, z, false);
    }

    public static void i(String str, String str2, boolean z, boolean z2) {
        i(str, str2);
        writeLog(1, str, str2, z, z2);
    }

    public static void init(Context context, LogConfig logConfig) {
        if (mContext == null) {
            mContext = context.getApplicationContext();
            mGlobalLogcatEnable = logConfig.isLogcatEnable();
            mGlobalFileLogEnable = logConfig.isFileLogEnable();
            mGlobalServerLogEnable = logConfig.isServerLogEnable();
            mLogFileDir = logConfig.getLogFileDir();
            mLogToServerImpl = logConfig.getLogToServerImpl();
            mIsTraceUpToServer = logConfig.isTraceUpToServer();
            mIsTraceWriteToFile = logConfig.isTraceWriteToFile();
            MAX_LOG_CACHE_SIZE = (long) logConfig.getLogMemoryBufferSize();
            MAX_FILE_NUM = (long) logConfig.getLogFileMaxCount();
            MAX_SINGLE_FILE_LENGTH = (long) logConfig.getSignalLogFileLimit();
            setProduct(logConfig.getProduct());
            mPid = String.valueOf(Process.myPid());
            if (mGlobalFileLogEnable) {
                initHandler();
            }
        }
    }

    private static void initHandler() {
        AnonymousClass1 r0 = new HandlerThread("allog" + Process.myPid()) {
            /* class com.amap.location.common.log.ALLog.AnonymousClass1 */

            /* access modifiers changed from: protected */
            public final void onLooperPrepared() {
                Looper looper = ALLog.mAALogHandlerThread.getLooper();
                if (looper != null) {
                    Handler unused = ALLog.mAALogHandler = new Handler(looper) {
                        /* class com.amap.location.common.log.ALLog.AnonymousClass1.AnonymousClass1 */

                        public void handleMessage(Message message) {
                            ALLog.handleMsg(message);
                        }
                    };
                    ALLog.mAALogHandler.post(ALLog.mInitLogFileTask);
                }
            }
        };
        mAALogHandlerThread = r0;
        r0.start();
    }

    public static boolean isGlobalFileLogEnable() {
        return mGlobalFileLogEnable;
    }

    public static boolean isGlobalLogcatEnable() {
        return mGlobalLogcatEnable;
    }

    public static boolean isGlobalServerLogEnable() {
        return mGlobalServerLogEnable;
    }

    public static boolean isTraceUseful() {
        try {
            return mGlobalServerLogEnable && mLogToServerImpl != null && mLogToServerImpl.a();
        } catch (Throwable th) {
            return false;
        }
    }

    public static String logEncode(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("@@_").append(a.a(str)).append("_@@");
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public static boolean prepareLogDir() {
        File file = new File(mLogFileDir);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        boolean exists = file.exists();
        if (!exists) {
            exists = file.mkdirs();
        }
        if (!exists || !file.canWrite()) {
            return false;
        }
        File file2 = new File(file, HeaderConfig.getProcessName());
        if (file2.exists() || file2.mkdir()) {
            mLogFileDir = file2.getAbsolutePath();
        }
        return true;
    }

    public static void setGlobalFileLogEnable(boolean z) {
        mGlobalFileLogEnable = z;
    }

    public static void setGlobalLogcatEnable(boolean z) {
        mGlobalLogcatEnable = z;
    }

    public static void setGlobalServerLogEnable(boolean z) {
        mGlobalServerLogEnable = z;
    }

    private static void setProduct(LogConfig.Product product) {
        mProduct = product;
        switch (product) {
            case FLP:
                mProductStr = "flp";
                return;
            case NLP:
                mProductStr = "nlp";
                return;
            default:
                return;
        }
    }

    public static void setTraceUpToServer(boolean z) {
        mIsTraceUpToServer = z;
    }

    public static void setTraceWriteToFile(boolean z) {
        mIsTraceWriteToFile = z;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void syncDataToFile(java.util.List<java.lang.String> r6) {
        /*
            if (r6 == 0) goto L_0x0008
            int r0 = r6.size()     // Catch:{ Exception -> 0x0022 }
            if (r0 != 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0022 }
            r1.<init>()     // Catch:{ Exception -> 0x0022 }
            java.util.Iterator r2 = r6.iterator()     // Catch:{ Exception -> 0x0022 }
        L_0x0012:
            boolean r0 = r2.hasNext()     // Catch:{ Exception -> 0x0022 }
            if (r0 == 0) goto L_0x002d
            java.lang.Object r0 = r2.next()     // Catch:{ Exception -> 0x0022 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0022 }
            r1.append(r0)     // Catch:{ Exception -> 0x0022 }
            goto L_0x0012
        L_0x0022:
            r0 = move-exception
            java.lang.String r1 = "ALLog"
            java.lang.String r2 = "DumpTask  error "
            e(r1, r2, r0)
            goto L_0x0008
        L_0x002d:
            java.lang.String r0 = r1.toString()     // Catch:{ Exception -> 0x0022 }
            java.io.File r1 = com.amap.location.common.log.ALLog.mCurrLogFile     // Catch:{ Exception -> 0x0022 }
            boolean r0 = writeToFile(r0, r1)     // Catch:{ Exception -> 0x0022 }
            if (r0 != 0) goto L_0x003d
            dispose()     // Catch:{ Exception -> 0x0022 }
            goto L_0x0008
        L_0x003d:
            java.util.ArrayDeque<java.io.File> r1 = com.amap.location.common.log.ALLog.mLogFileDeque     // Catch:{ Exception -> 0x0022 }
            monitor-enter(r1)     // Catch:{ Exception -> 0x0022 }
        L_0x0040:
            java.util.ArrayDeque<java.io.File> r0 = com.amap.location.common.log.ALLog.mLogFileDeque     // Catch:{ all -> 0x006e }
            int r0 = r0.size()     // Catch:{ all -> 0x006e }
            int r0 = r0 + 1
            long r2 = (long) r0     // Catch:{ all -> 0x006e }
            long r4 = com.amap.location.common.log.ALLog.MAX_FILE_NUM     // Catch:{ all -> 0x006e }
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x0071
            java.util.ArrayDeque<java.io.File> r0 = com.amap.location.common.log.ALLog.mLogFileDeque     // Catch:{ all -> 0x006e }
            java.lang.Object r0 = r0.poll()     // Catch:{ all -> 0x006e }
            java.io.File r0 = (java.io.File) r0     // Catch:{ all -> 0x006e }
            if (r0 == 0) goto L_0x0040
            boolean r2 = r0.exists()     // Catch:{ all -> 0x006e }
            if (r2 == 0) goto L_0x0040
            r0.delete()     // Catch:{ Exception -> 0x0063 }
            goto L_0x0040
        L_0x0063:
            r0 = move-exception
            java.lang.String r2 = "ALLog"
            java.lang.String r3 = "MAX_FILE_NUM delete  error "
            e(r2, r3, r0)     // Catch:{ all -> 0x006e }
            goto L_0x0040
        L_0x006e:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x006e }
            throw r0     // Catch:{ Exception -> 0x0022 }
        L_0x0071:
            monitor-exit(r1)     // Catch:{ all -> 0x006e }
            java.io.File r0 = com.amap.location.common.log.ALLog.mCurrLogFile     // Catch:{ Exception -> 0x0022 }
            long r0 = r0.length()     // Catch:{ Exception -> 0x0022 }
            long r2 = com.amap.location.common.log.ALLog.MAX_SINGLE_FILE_LENGTH     // Catch:{ Exception -> 0x0022 }
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 <= 0) goto L_0x0008
            java.util.ArrayDeque<java.io.File> r1 = com.amap.location.common.log.ALLog.mLogFileDeque     // Catch:{ Exception -> 0x0022 }
            monitor-enter(r1)     // Catch:{ Exception -> 0x0022 }
            java.util.ArrayDeque<java.io.File> r0 = com.amap.location.common.log.ALLog.mLogFileDeque     // Catch:{ all -> 0x0096 }
            java.io.File r2 = com.amap.location.common.log.ALLog.mCurrLogFile     // Catch:{ all -> 0x0096 }
            r0.offer(r2)     // Catch:{ all -> 0x0096 }
            monitor-exit(r1)     // Catch:{ all -> 0x0096 }
            java.io.File r0 = getNewLogFile()     // Catch:{ Exception -> 0x0022 }
            com.amap.location.common.log.ALLog.mCurrLogFile = r0     // Catch:{ Exception -> 0x0022 }
            if (r0 != 0) goto L_0x0008
            dispose()     // Catch:{ Exception -> 0x0022 }
            goto L_0x0008
        L_0x0096:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0096 }
            throw r0     // Catch:{ Exception -> 0x0022 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.location.common.log.ALLog.syncDataToFile(java.util.List):void");
    }

    public static void trace(Exception exc) {
        if (isTraceUseful()) {
            e(TRACE_PREFIX, "", exc, mIsTraceWriteToFile, mIsTraceUpToServer);
        }
    }

    public static void trace(String str, String str2) {
        if (isTraceUseful()) {
            w(TRACE_PREFIX + str, str2, mIsTraceWriteToFile, mIsTraceUpToServer);
        }
    }

    public static void trace(String str, String str2, Exception exc) {
        if (isTraceUseful()) {
            e(TRACE_PREFIX + str, str2, exc, mIsTraceWriteToFile, mIsTraceUpToServer);
        }
    }

    public static void w(String str, String str2) {
        if (mGlobalLogcatEnable) {
            Log.w(str, str2);
        }
    }

    public static void w(String str, String str2, boolean z) {
        w(str, str2);
        writeLog(2, str, str2, z, false);
    }

    public static void w(String str, String str2, boolean z, boolean z2) {
        w(str, str2);
        writeLog(2, str, str2, z, z2);
    }

    private static void writeLog(int i, String str, String str2, boolean z, boolean z2) {
        boolean z3 = true;
        boolean z4 = z && mGlobalFileLogEnable && mFileLogReady;
        if (!z2 || !mGlobalServerLogEnable || mLogToServerImpl == null || !mLogToServerImpl.a()) {
            z3 = false;
        }
        if (z4 || z3) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(System.currentTimeMillis())).append("|");
            switch (i) {
                case 1:
                    sb.append("info|");
                    break;
                case 2:
                    sb.append("warn|");
                    break;
                case 4:
                    sb.append("error|");
                    break;
            }
            sb.append(mPid).append("|");
            sb.append(String.valueOf((long) Process.myTid())).append("|");
            sb.append(str).append("|").append(str2).append("\n");
            if (z4) {
                writeToBuffer(sb.toString());
            }
            if (z3) {
                writeToServer(sb.substring(0, sb.length() - 1));
            }
        }
    }

    private static void writeToBuffer(String str) {
        synchronized (mLogBufferLock) {
            mLogBuffer.add(str);
            mLogCacheSize += (long) str.length();
            if (((long) mLogBuffer.size()) >= MAX_DUMP_LOG_LINE_NUM || mLogCacheSize > MAX_LOG_CACHE_SIZE) {
                if (mAALogHandler != null) {
                    mBufferList.add(mLogBuffer);
                    while (((long) mBufferList.size()) > 5) {
                        mBufferList.removeFirst();
                    }
                    mAALogHandler.obtainMessage(1).sendToTarget();
                    mAALogHandler.removeMessages(2);
                    mLogBuffer = new LinkedList<>();
                    mLogCacheSize = 0;
                } else {
                    mLogBuffer.clear();
                    mLogCacheSize = 0;
                }
            }
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.location.common.util.FileUtil.writeToFile(java.lang.String, java.io.File, boolean):boolean
     arg types: [java.lang.String, java.io.File, int]
     candidates:
      com.amap.location.common.util.FileUtil.writeToFile(byte[], java.io.File, boolean):boolean
      com.amap.location.common.util.FileUtil.writeToFile(java.lang.String, java.io.File, boolean):boolean */
    private static boolean writeToFile(String str, File file) {
        if (FileUtil.writeToFile(str + "\r\n-------------------\r\n", file, true)) {
            return true;
        }
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            File file2 = parentFile;
            int i = 0;
            while (true) {
                if (file2 != null) {
                    if (!file2.exists()) {
                        file2 = file2.getParentFile();
                        i++;
                        if (i >= 2) {
                            break;
                        }
                    } else if (file2.isFile()) {
                        file2.delete();
                    }
                } else {
                    break;
                }
            }
            if (parentFile != null) {
                try {
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    file.createNewFile();
                    return FileUtil.writeToFile(str + "\r\n-------------------\r\n", file, true);
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    private static void writeToServer(String str) {
        if (mLogToServerImpl != null) {
            mLogToServerImpl.a(str);
        }
    }
}
