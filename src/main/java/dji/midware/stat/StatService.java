package dji.midware.stat;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Keep;
import com.dji.frame.util.V_DiskUtil;
import com.dji.frame.util.V_FileUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.media.MediaLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

@Keep
@EXClassNullAway
public class StatService {
    public static final long BYTES_IN_MEGA = 1048576;
    public static final int CPU_MEASURE_WINDOW = 1000;
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        /* class dji.midware.stat.StatService.AnonymousClass1 */

        {
            set(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US));
        }
    };
    /* access modifiers changed from: private */
    public static boolean DEBUG = false;
    private static final ThreadLocal<SimpleDateFormat> FILE_NAME_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        /* class dji.midware.stat.StatService.AnonymousClass2 */

        {
            set(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US));
        }
    };
    public static boolean OPEN = false;
    private static boolean SHOW_ON_VIEW = false;
    public static final int STAT_WINDOW_MS = 10000;
    /* access modifiers changed from: private */
    public static String TAG = "StatService";
    private static int c_decoder;
    private static int c_hub;
    private static int c_main;
    private static int c_preview;
    private static Context ctx;
    private static StatService instance = null;
    private static LinkedList objectSet = new LinkedList();
    private static ReferenceQueue q_decoder = new ReferenceQueue();
    private static ReferenceQueue q_hub = new ReferenceQueue();
    private static ReferenceQueue q_main = new ReferenceQueue();
    private static ReferenceQueue q_preview = new ReferenceQueue();
    private FileOutputStream fos;
    /* access modifiers changed from: private */
    public Handler handler;
    private LinuxUtils linuxUtils = new LinuxUtils();
    private HashMap<String, StatBase> statSet = new HashMap<>();
    private HandlerThread thread = new HandlerThread("DJIStatService");

    public static synchronized StatService getInstance() {
        StatService statService;
        synchronized (StatService.class) {
            if (instance == null) {
                instance = new StatService();
            }
            statService = instance;
        }
        return statService;
    }

    public static synchronized void destroyInstance() {
        synchronized (StatService.class) {
            if (instance != null) {
                instance.onDestroy();
            }
        }
    }

    public static void setContext(Context ctx2) {
        ctx = ctx2;
        getInstance();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException}
      ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException} */
    private void initLogFile() {
        if (ctx != null) {
            String fileName = "Stat_" + FILE_NAME_FORMAT.get().format(new Date()) + ".txt";
            String dirName = V_DiskUtil.getExternalCacheDirPath(ctx, "/LOG/STATISTICS/");
            if (Environment.getExternalStorageState().equals("mounted")) {
                try {
                    File dir = new File(dirName);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    } else if (dir.getUsableSpace() - dir.getFreeSpace() > 104857600) {
                        V_FileUtil.deleteAllFile(dir);
                        dir.mkdirs();
                    }
                    MediaLogger.e(TAG, "create statistics file: " + dirName + fileName);
                    this.fos = new FileOutputStream(dirName + fileName, true);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onDestroy() {
        try {
            if (this.fos != null) {
                this.fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StatService() {
        if (OPEN) {
            initLogFile();
            this.thread.start();
            this.handler = new Handler(this.thread.getLooper());
            this.handler.postDelayed(new Runnable() {
                /* class dji.midware.stat.StatService.AnonymousClass3 */

                public void run() {
                    MediaLogger.i(StatService.DEBUG, StatService.TAG, "stat service update");
                    try {
                        StatService.this.saveAllEventToFile();
                    } catch (Exception e) {
                        MediaLogger.e(StatService.TAG, e);
                    }
                    if (StatService.OPEN) {
                        StatService.this.handler.postDelayed(this, 10000);
                    }
                }
            }, 10000);
        }
    }

    /* access modifiers changed from: private */
    public void saveAllEventToFile() {
        StringBuilder strBuilder = new StringBuilder("Time=" + DATE_FORMAT.get().format(new Date()) + "\n");
        ArrayList<String> strList = new ArrayList<>();
        synchronized (this.statSet) {
            for (StatBase stat : this.statSet.values()) {
                strList.add("DJIStat : " + stat.getName() + "=" + stat.getValueAndReset() + "\n");
            }
        }
        Collections.sort(strList);
        Iterator it2 = strList.iterator();
        while (it2.hasNext()) {
            strBuilder.append((String) it2.next());
        }
        strBuilder.append("DJIStat : CPU=" + this.linuxUtils.syncGetSystemCpuUsage(1000) + "\n");
        saveProcessMemoryStat(strBuilder);
        strBuilder.append("\n");
        String str = strBuilder.toString();
        DJILogHelper.getInstance().LOGI("DJIStat", str, false, SHOW_ON_VIEW);
        try {
            if (this.fos != null) {
                this.fos.write(str.getBytes());
                this.fos.flush();
            }
        } catch (Exception e) {
            MediaLogger.e(TAG, e);
        }
    }

    private void saveSystemMemoryStat(StringBuilder strBuilder) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ((ActivityManager) ctx.getSystemService("activity")).getMemoryInfo(mi);
        strBuilder.append(String.format(Locale.US, "DJIStat : Mem: avail=%d, total=%d, low=%b\n", Long.valueOf(mi.availMem / 1048576), Long.valueOf(mi.totalMem / 1048576), Boolean.valueOf(mi.lowMemory)));
    }

    private void saveProcessMemoryStat(StringBuilder strBuilder) {
        Runtime info = Runtime.getRuntime();
        strBuilder.append(String.format(Locale.US, "DJIStat : Mem: free=%d, total=%d, max=%d\n", Long.valueOf(info.freeMemory() / 1048576), Long.valueOf(info.totalMemory() / 1048576), Long.valueOf(info.maxMemory() / 1048576)));
    }

    /* JADX WARN: Type inference failed for: r5v10, types: [java.lang.Object], assign insn: 0x0024: INVOKE  (r5v10 ? I:java.lang.Object) = 
      (r2v0 'cst' java.lang.reflect.Constructor<? extends dji.midware.stat.StatBase> A[D('cst' java.lang.reflect.Constructor<? extends dji.midware.stat.StatBase>)])
      (r5v9 java.lang.Object[])
     type: VIRTUAL call: java.lang.reflect.Constructor.newInstance(java.lang.Object[]):java.lang.Object */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void postEvent(java.lang.Class<? extends dji.midware.stat.StatBase> r11, java.lang.String r12, float r13) {
        /*
            r10 = this;
            boolean r5 = dji.midware.stat.StatService.OPEN
            if (r5 != 0) goto L_0x0005
        L_0x0004:
            return
        L_0x0005:
            java.util.HashMap<java.lang.String, dji.midware.stat.StatBase> r6 = r10.statSet     // Catch:{ Exception -> 0x0033 }
            monitor-enter(r6)     // Catch:{ Exception -> 0x0033 }
            java.util.HashMap<java.lang.String, dji.midware.stat.StatBase> r5 = r10.statSet     // Catch:{ all -> 0x0030 }
            java.lang.Object r4 = r5.get(r12)     // Catch:{ all -> 0x0030 }
            dji.midware.stat.StatBase r4 = (dji.midware.stat.StatBase) r4     // Catch:{ all -> 0x0030 }
            if (r4 != 0) goto L_0x0046
            r5 = 1
            java.lang.Class[] r5 = new java.lang.Class[r5]     // Catch:{ Exception -> 0x003a }
            r7 = 0
            java.lang.Class<java.lang.String> r8 = java.lang.String.class
            r5[r7] = r8     // Catch:{ Exception -> 0x003a }
            java.lang.reflect.Constructor r2 = r11.getConstructor(r5)     // Catch:{ Exception -> 0x003a }
            r5 = 1
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x003a }
            r7 = 0
            r5[r7] = r12     // Catch:{ Exception -> 0x003a }
            java.lang.Object r5 = r2.newInstance(r5)     // Catch:{ Exception -> 0x003a }
            r0 = r5
            dji.midware.stat.StatBase r0 = (dji.midware.stat.StatBase) r0     // Catch:{ Exception -> 0x003a }
            r4 = r0
        L_0x002c:
            if (r4 != 0) goto L_0x0041
            monitor-exit(r6)     // Catch:{ all -> 0x0030 }
            goto L_0x0004
        L_0x0030:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0030 }
            throw r5     // Catch:{ Exception -> 0x0033 }
        L_0x0033:
            r3 = move-exception
            java.lang.String r5 = dji.midware.stat.StatService.TAG
            dji.midware.media.MediaLogger.e(r5, r3)
            goto L_0x0004
        L_0x003a:
            r3 = move-exception
            java.lang.String r5 = dji.midware.stat.StatService.TAG     // Catch:{ all -> 0x0030 }
            dji.midware.media.MediaLogger.e(r5, r3)     // Catch:{ all -> 0x0030 }
            goto L_0x002c
        L_0x0041:
            java.util.HashMap<java.lang.String, dji.midware.stat.StatBase> r5 = r10.statSet     // Catch:{ all -> 0x0030 }
            r5.put(r12, r4)     // Catch:{ all -> 0x0030 }
        L_0x0046:
            double r8 = (double) r13     // Catch:{ all -> 0x0030 }
            r4.addEvent(r8)     // Catch:{ all -> 0x0030 }
            monitor-exit(r6)     // Catch:{ all -> 0x0030 }
            goto L_0x0004
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.stat.StatService.postEvent(java.lang.Class, java.lang.String, float):void");
    }

    public void countActivity() {
        while (q_preview.poll() != null) {
            c_preview--;
        }
        while (q_hub.poll() != null) {
            c_hub--;
        }
        while (q_main.poll() != null) {
            c_main--;
        }
        while (q_decoder.poll() != null) {
            c_decoder--;
        }
        MediaLogger.show("Preview=" + c_preview + " Hub=" + c_hub + " Decoder=" + c_decoder);
    }
}
