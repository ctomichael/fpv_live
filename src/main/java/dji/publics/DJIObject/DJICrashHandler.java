package dji.publics.DJIObject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.dji.frame.util.V_DiskUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.reflect.MidwareInjectManager;
import dji.midware.reflect.MidwareToP3Injectable;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class DJICrashHandler implements Thread.UncaughtExceptionHandler {
    @Nullable
    private static DJICrashHandler INSTANCE;
    String buildName = "";
    String buildNum = "";
    @Nullable
    private Context context;
    @Nullable
    private CrashCallback crashCallback;
    @Nullable
    private String dirName;
    @NonNull
    private SimpleDateFormat formatData = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
    @Nullable
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public interface CrashCallback {
        void onCallback(String str);
    }

    public enum DJICrashEvent {
        Crashed
    }

    private DJICrashHandler() {
    }

    public static synchronized DJICrashHandler getInstance() {
        DJICrashHandler dJICrashHandler;
        synchronized (DJICrashHandler.class) {
            if (INSTANCE == null) {
                INSTANCE = new DJICrashHandler();
            }
            dJICrashHandler = INSTANCE;
        }
        return dJICrashHandler;
    }

    public void init(@NonNull Context context2) {
        this.context = context2;
        this.dirName = V_DiskUtil.getExternalCacheDirPath(context2, "/LOG/CRASH/");
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void setCrashCallback(CrashCallback callback) {
        this.crashCallback = callback;
    }

    public void uncaughtException(@NonNull Thread thread, Throwable ex) {
        ActivityManager am;
        flushLog();
        if (handleException(thread, ex) || this.mDefaultHandler == null) {
            EventBus.getDefault().post(DJICrashEvent.Crashed);
            Log.e("DJICrashHandler", "uncaughtException 3");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.mDefaultHandler != null) {
                this.mDefaultHandler.uncaughtException(thread, ex);
            }
            DJICrashExecutor.doAll(this.context);
            if (!(this.context == null || (am = (ActivityManager) this.context.getSystemService("activity")) == null)) {
                am.killBackgroundProcesses(this.context.getPackageName());
            }
            killMySelf();
            return;
        }
        this.mDefaultHandler.uncaughtException(thread, ex);
        killMySelf();
    }

    private boolean isInnerOrDebug() {
        MidwareToP3Injectable toP3Injectable = MidwareInjectManager.getMidwareToP3Injectable();
        return toP3Injectable != null && (toP3Injectable.isDevelopPackage() || toP3Injectable.isInnerPackage());
    }

    private void killMySelf() {
        if (!isInnerOrDebug()) {
            Process.killProcess(Process.myPid());
        }
    }

    private void flushLog() {
        try {
            DJILog.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean handleException(@NonNull Thread thread, @Nullable Throwable ex) {
        if (ex == null) {
            return false;
        }
        saveCrashInfo2File(getThreadInfo(thread, ex).toString(), ex);
        invokeCallback(ex);
        return true;
    }

    @NonNull
    private StringBuilder getThreadInfo(@NonNull Thread thread, @NonNull Throwable ex) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        int estimatedSize = topGroup.activeCount();
        StringBuilder builder = new StringBuilder();
        builder.append("Crash name:");
        builder.append(ex);
        builder.append("\n Cause is:");
        builder.append(ex.getCause());
        builder.append("\n Thread name is:");
        builder.append(thread.getName());
        builder.append(" ");
        builder.append(thread.getId());
        builder.append("\n Thread count is:");
        builder.append(estimatedSize);
        builder.append("\n Fd count is:");
        builder.append(getFDCount());
        builder.append("\n ");
        builder.append("\n");
        builder.append("").append(showFileContent("/proc/" + Process.myPid() + "/status"));
        Thread[] lstThreads = new Thread[estimatedSize];
        topGroup.enumerate(lstThreads);
        builder.append("\n============Start dump thread info============");
        builder.append("\ntotal =").append(lstThreads.length);
        for (int i = 0; i < estimatedSize; i++) {
            builder.append("\n线程号：").append(lstThreads[i].getId()).append(" = ").append(lstThreads[i].getName());
        }
        builder.append("\n============end of dump ==========\n\n");
        return builder;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0064 A[SYNTHETIC, Splitter:B:27:0x0064] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0069 A[SYNTHETIC, Splitter:B:30:0x0069] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0080 A[SYNTHETIC, Splitter:B:41:0x0080] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0085 A[SYNTHETIC, Splitter:B:44:0x0085] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x008a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getFDCount() {
        /*
            r11 = this;
            r5 = 0
            r2 = 0
            r6 = 0
            int r4 = android.os.Process.myPid()     // Catch:{ IOException -> 0x005e }
            java.lang.Runtime r8 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x005e }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x005e }
            r9.<init>()     // Catch:{ IOException -> 0x005e }
            java.lang.String r10 = "ls /proc/"
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ IOException -> 0x005e }
            java.lang.StringBuilder r9 = r9.append(r4)     // Catch:{ IOException -> 0x005e }
            java.lang.String r10 = "/fd"
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ IOException -> 0x005e }
            java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x005e }
            java.lang.Process r5 = r8.exec(r9)     // Catch:{ IOException -> 0x005e }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x005e }
            java.io.InputStream r8 = r5.getInputStream()     // Catch:{ IOException -> 0x005e }
            r3.<init>(r8)     // Catch:{ IOException -> 0x005e }
            java.io.BufferedReader r7 = new java.io.BufferedReader     // Catch:{ IOException -> 0x009f, all -> 0x0098 }
            r7.<init>(r3)     // Catch:{ IOException -> 0x009f, all -> 0x0098 }
            r0 = 0
        L_0x0039:
            java.lang.String r8 = r7.readLine()     // Catch:{ IOException -> 0x00a2, all -> 0x009b }
            if (r8 == 0) goto L_0x0042
            int r0 = r0 + 1
            goto L_0x0039
        L_0x0042:
            if (r7 == 0) goto L_0x0047
            r7.close()     // Catch:{ IOException -> 0x0054 }
        L_0x0047:
            if (r3 == 0) goto L_0x004c
            r3.close()     // Catch:{ IOException -> 0x0059 }
        L_0x004c:
            if (r5 == 0) goto L_0x0051
            r5.destroy()
        L_0x0051:
            r6 = r7
            r2 = r3
        L_0x0053:
            return r0
        L_0x0054:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0047
        L_0x0059:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x004c
        L_0x005e:
            r1 = move-exception
        L_0x005f:
            r1.printStackTrace()     // Catch:{ all -> 0x007d }
            if (r6 == 0) goto L_0x0067
            r6.close()     // Catch:{ IOException -> 0x0073 }
        L_0x0067:
            if (r2 == 0) goto L_0x006c
            r2.close()     // Catch:{ IOException -> 0x0078 }
        L_0x006c:
            if (r5 == 0) goto L_0x0071
            r5.destroy()
        L_0x0071:
            r0 = 0
            goto L_0x0053
        L_0x0073:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0067
        L_0x0078:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x006c
        L_0x007d:
            r8 = move-exception
        L_0x007e:
            if (r6 == 0) goto L_0x0083
            r6.close()     // Catch:{ IOException -> 0x008e }
        L_0x0083:
            if (r2 == 0) goto L_0x0088
            r2.close()     // Catch:{ IOException -> 0x0093 }
        L_0x0088:
            if (r5 == 0) goto L_0x008d
            r5.destroy()
        L_0x008d:
            throw r8
        L_0x008e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0083
        L_0x0093:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0088
        L_0x0098:
            r8 = move-exception
            r2 = r3
            goto L_0x007e
        L_0x009b:
            r8 = move-exception
            r6 = r7
            r2 = r3
            goto L_0x007e
        L_0x009f:
            r1 = move-exception
            r2 = r3
            goto L_0x005f
        L_0x00a2:
            r1 = move-exception
            r6 = r7
            r2 = r3
            goto L_0x005f
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.publics.DJIObject.DJICrashHandler.getFDCount():int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x004c A[SYNTHETIC, Splitter:B:31:0x004c] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0058 A[SYNTHETIC, Splitter:B:37:0x0058] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:28:0x0047=Splitter:B:28:0x0047, B:13:0x0029=Splitter:B:13:0x0029} */
    @android.support.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String showFileContent(java.lang.String r9) {
        /*
            r8 = this;
            r5 = 0
            boolean r6 = android.text.TextUtils.isEmpty(r9)
            if (r6 == 0) goto L_0x0008
        L_0x0007:
            return r5
        L_0x0008:
            r1 = 0
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile     // Catch:{ FileNotFoundException -> 0x0067, IOException -> 0x0046 }
            java.lang.String r6 = "r"
            r2.<init>(r9, r6)     // Catch:{ FileNotFoundException -> 0x0067, IOException -> 0x0046 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0027, IOException -> 0x0064, all -> 0x0061 }
            r4.<init>()     // Catch:{ FileNotFoundException -> 0x0027, IOException -> 0x0064, all -> 0x0061 }
        L_0x0016:
            java.lang.String r3 = r2.readLine()     // Catch:{ FileNotFoundException -> 0x0027, IOException -> 0x0064, all -> 0x0061 }
            if (r3 == 0) goto L_0x0037
            java.lang.StringBuilder r6 = r4.append(r3)     // Catch:{ FileNotFoundException -> 0x0027, IOException -> 0x0064, all -> 0x0061 }
            java.lang.String r7 = "\r\n"
            r6.append(r7)     // Catch:{ FileNotFoundException -> 0x0027, IOException -> 0x0064, all -> 0x0061 }
            goto L_0x0016
        L_0x0027:
            r0 = move-exception
            r1 = r2
        L_0x0029:
            r0.printStackTrace()     // Catch:{ all -> 0x0055 }
            if (r1 == 0) goto L_0x0007
            r1.close()     // Catch:{ IOException -> 0x0032 }
            goto L_0x0007
        L_0x0032:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0007
        L_0x0037:
            java.lang.String r5 = r4.toString()     // Catch:{ FileNotFoundException -> 0x0027, IOException -> 0x0064, all -> 0x0061 }
            if (r2 == 0) goto L_0x0007
            r2.close()     // Catch:{ IOException -> 0x0041 }
            goto L_0x0007
        L_0x0041:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0007
        L_0x0046:
            r0 = move-exception
        L_0x0047:
            r0.printStackTrace()     // Catch:{ all -> 0x0055 }
            if (r1 == 0) goto L_0x0007
            r1.close()     // Catch:{ IOException -> 0x0050 }
            goto L_0x0007
        L_0x0050:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0007
        L_0x0055:
            r5 = move-exception
        L_0x0056:
            if (r1 == 0) goto L_0x005b
            r1.close()     // Catch:{ IOException -> 0x005c }
        L_0x005b:
            throw r5
        L_0x005c:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x005b
        L_0x0061:
            r5 = move-exception
            r1 = r2
            goto L_0x0056
        L_0x0064:
            r0 = move-exception
            r1 = r2
            goto L_0x0047
        L_0x0067:
            r0 = move-exception
            goto L_0x0029
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.publics.DJIObject.DJICrashHandler.showFileContent(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0113 A[SYNTHETIC, Splitter:B:34:0x0113] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void saveCrashInfo2File(java.lang.String r19, @android.support.annotation.NonNull java.lang.Throwable r20) {
        /*
            r18 = this;
            r0 = r18
            java.lang.String r0 = r0.dirName
            r16 = r0
            if (r16 != 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            java.lang.StringBuffer r12 = new java.lang.StringBuffer
            r12.<init>()
            r0 = r18
            android.content.Context r0 = r0.context
            r16 = r0
            r0 = r18
            r1 = r16
            java.lang.String r16 = r0.getDeviceInfo(r1)
            r0 = r16
            java.lang.StringBuffer r16 = r12.append(r0)
            java.lang.String r17 = "\n=====\tCrash\t=====\n"
            r16.append(r17)
            java.io.StringWriter r13 = new java.io.StringWriter
            r13.<init>()
            java.io.PrintWriter r10 = new java.io.PrintWriter
            r10.<init>(r13)
            r0 = r20
            r0.printStackTrace(r10)
            java.lang.Throwable r2 = r20.getCause()
        L_0x003b:
            if (r2 == 0) goto L_0x0045
            r2.printStackTrace(r10)
            java.lang.Throwable r2 = r2.getCause()
            goto L_0x003b
        L_0x0045:
            r10.close()
            java.lang.String r11 = r13.toString()
            r12.append(r11)
            java.lang.String r16 = "\n=======Thread info======"
            r0 = r16
            java.lang.StringBuffer r16 = r12.append(r0)
            r0 = r16
            r1 = r19
            r0.append(r1)
            r0 = r18
            java.text.SimpleDateFormat r0 = r0.formatData
            r16 = r0
            java.util.Date r17 = new java.util.Date
            r17.<init>()
            java.lang.String r3 = r16.format(r17)
            java.lang.StringBuilder r16 = new java.lang.StringBuilder
            r16.<init>()
            java.lang.String r17 = "crash-"
            java.lang.StringBuilder r16 = r16.append(r17)
            r0 = r16
            java.lang.StringBuilder r16 = r0.append(r3)
            java.lang.String r17 = ".txt"
            java.lang.StringBuilder r16 = r16.append(r17)
            java.lang.String r6 = r16.toString()
            r7 = 0
            java.lang.String r16 = android.os.Environment.getExternalStorageState()
            java.lang.String r17 = "mounted"
            boolean r16 = r16.equals(r17)
            if (r16 == 0) goto L_0x0008
            java.io.File r4 = new java.io.File     // Catch:{ IOException -> 0x00f8 }
            r0 = r18
            java.lang.String r0 = r0.dirName     // Catch:{ IOException -> 0x00f8 }
            r16 = r0
            r0 = r16
            r4.<init>(r0)     // Catch:{ IOException -> 0x00f8 }
            boolean r16 = r4.exists()     // Catch:{ IOException -> 0x00f8 }
            if (r16 == 0) goto L_0x00f4
            long r14 = com.dji.frame.util.V_FileUtil.getFileSize(r4)     // Catch:{ IOException -> 0x00f8 }
            r9 = 52428800(0x3200000, float:4.7019774E-37)
            long r0 = (long) r9     // Catch:{ IOException -> 0x00f8 }
            r16 = r0
            int r16 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r16 <= 0) goto L_0x00bf
            com.dji.frame.util.V_FileUtil.deleteAllFile(r4)     // Catch:{ IOException -> 0x00f8 }
            r4.mkdirs()     // Catch:{ IOException -> 0x00f8 }
        L_0x00bf:
            java.io.FileOutputStream r8 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00f8 }
            java.lang.StringBuilder r16 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00f8 }
            r16.<init>()     // Catch:{ IOException -> 0x00f8 }
            r0 = r18
            java.lang.String r0 = r0.dirName     // Catch:{ IOException -> 0x00f8 }
            r17 = r0
            java.lang.StringBuilder r16 = r16.append(r17)     // Catch:{ IOException -> 0x00f8 }
            r0 = r16
            java.lang.StringBuilder r16 = r0.append(r6)     // Catch:{ IOException -> 0x00f8 }
            java.lang.String r16 = r16.toString()     // Catch:{ IOException -> 0x00f8 }
            r0 = r16
            r8.<init>(r0)     // Catch:{ IOException -> 0x00f8 }
            java.lang.String r16 = r12.toString()     // Catch:{ IOException -> 0x011f, all -> 0x011c }
            byte[] r16 = r16.getBytes()     // Catch:{ IOException -> 0x011f, all -> 0x011c }
            r0 = r16
            r8.write(r0)     // Catch:{ IOException -> 0x011f, all -> 0x011c }
            if (r8 == 0) goto L_0x0122
            r8.close()     // Catch:{ IOException -> 0x0109 }
            r7 = r8
            goto L_0x0008
        L_0x00f4:
            r4.mkdirs()     // Catch:{ IOException -> 0x00f8 }
            goto L_0x00bf
        L_0x00f8:
            r5 = move-exception
        L_0x00f9:
            r5.printStackTrace()     // Catch:{ all -> 0x0110 }
            if (r7 == 0) goto L_0x0008
            r7.close()     // Catch:{ IOException -> 0x0103 }
            goto L_0x0008
        L_0x0103:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0008
        L_0x0109:
            r5 = move-exception
            r5.printStackTrace()
            r7 = r8
            goto L_0x0008
        L_0x0110:
            r16 = move-exception
        L_0x0111:
            if (r7 == 0) goto L_0x0116
            r7.close()     // Catch:{ IOException -> 0x0117 }
        L_0x0116:
            throw r16
        L_0x0117:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0116
        L_0x011c:
            r16 = move-exception
            r7 = r8
            goto L_0x0111
        L_0x011f:
            r5 = move-exception
            r7 = r8
            goto L_0x00f9
        L_0x0122:
            r7 = r8
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.publics.DJIObject.DJICrashHandler.saveCrashInfo2File(java.lang.String, java.lang.Throwable):void");
    }

    private void invokeCallback(@Nullable Throwable ex) {
        String crashStr;
        if (this.crashCallback != null && ex != null) {
            try {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                crashStr = "\r\n" + sw.toString() + "\r\n";
            } catch (Exception e) {
                crashStr = "bad getErrorInfoFromException";
            }
            this.crashCallback.onCallback(crashStr);
        }
    }

    private String getDeviceInfo(@Nullable Context context2) {
        StringBuilder sb = new StringBuilder();
        loadBuildInfo(context2);
        sb.append("=====\tDevice Info\t=====").append("\nmanufacture:").append(Build.MANUFACTURER).append("\nproduct:").append(Build.PRODUCT).append("\nmodel:").append(Build.MODEL).append("\nversion:").append(Build.DISPLAY).append("\nandroid version:").append(Build.VERSION.RELEASE).append("\nbuild num:").append(this.buildNum).append("\nbuild name:").append(this.buildName).append("\nsdk version:").append(Build.VERSION.SDK_INT);
        if (context2 != null) {
            PackageInfo info = null;
            try {
                info = context2.getPackageManager().getPackageInfo(context2.getPackageName(), 1);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (info != null) {
                sb.append("\n=====\tApp Info\t=====").append("\nversionCode:").append(info.versionCode).append("\nversionName:").append(info.versionName);
            }
        }
        return sb.toString();
    }

    private void loadBuildInfo(Context context2) {
        InputStream is = null;
        BufferedReader bufReader = null;
        try {
            is = context2.getAssets().open("build");
            BufferedReader bufReader2 = new BufferedReader(new InputStreamReader(is));
            try {
                String number = bufReader2.readLine();
                if (number != null) {
                    int len = number.length();
                    if (len > 0 && number.substring(len - 1, len).equals(" ")) {
                        number = number.substring(0, len - 1);
                    }
                    this.buildName = number;
                    if (number.contains("-")) {
                        this.buildNum = number.split("-")[1];
                    } else {
                        this.buildNum = number;
                    }
                }
                close(bufReader2);
                close(is);
            } catch (Exception e) {
                bufReader = bufReader2;
            } catch (Throwable th) {
                th = th;
                bufReader = bufReader2;
                close(bufReader);
                close(is);
                throw th;
            }
        } catch (Exception e2) {
            close(bufReader);
            close(is);
        } catch (Throwable th2) {
            th = th2;
            close(bufReader);
            close(is);
            throw th;
        }
    }

    private static void close(Closeable o) {
        if (o != null) {
            try {
                o.close();
            } catch (Exception e) {
            }
        }
    }
}
