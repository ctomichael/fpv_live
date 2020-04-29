package dji.log;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import dji.log.DJILogFileConfig;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

class DJILogFileManager {
    private static final String TAG = "DJILogFileManager";
    private DJILogFileConfig config;
    private ExecutorService pool;

    DJILogFileManager() {
    }

    public void init(Context context, DJILogFileConfig config2) {
        if (config2 == null) {
            config2 = new DJILogFileConfig.Builder(context).build();
        }
        this.config = config2;
        this.pool = config2.service;
    }

    /* access modifiers changed from: package-private */
    public void saveLog(List<DJILogEntry> logEntries) {
        executeTransactionJob(logEntries);
    }

    /* access modifiers changed from: protected */
    public String getRootDirectory() {
        return this.config.LOG_PATH_ROOT;
    }

    /* access modifiers changed from: protected */
    public String getCachePath() {
        return this.config.LOG_PATH_ROOT + DJILogPaths.LOG_EXTRA_CACHE + File.separator;
    }

    /* access modifiers changed from: protected */
    public String getCachePath(String extraDir) {
        return getCachePath() + extraDir + File.separator;
    }

    /* access modifiers changed from: package-private */
    public String getExtraPath(String extraDir) {
        return this.config.LOG_PATH_ROOT + extraDir + File.separator;
    }

    /* access modifiers changed from: package-private */
    public String generateFileName() {
        String fileType;
        if (TextUtils.isEmpty(this.config.LOG_FILE_TYPE)) {
            fileType = DJILog.controller().encrypt ? ".log" : ".txt";
        } else {
            fileType = this.config.LOG_FILE_TYPE;
        }
        return this.config.LOG_FILE_PREFIX + DJILogUtils.formatNow() + fileType;
    }

    /* access modifiers changed from: protected */
    public boolean isOpen() {
        return this.config.open;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException}
     arg types: [java.io.File, int]
     candidates:
      ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException}
      ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException} */
    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00bc A[SYNTHETIC, Splitter:B:36:0x00bc] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeLog2File(java.lang.String r13, java.lang.String r14) {
        /*
            r12 = this;
            dji.log.DJILogFileConfig r8 = r12.config
            if (r8 == 0) goto L_0x000a
            dji.log.DJILogFileConfig r8 = r12.config
            boolean r8 = r8.open
            if (r8 != 0) goto L_0x000b
        L_0x000a:
            return
        L_0x000b:
            java.lang.String r3 = r12.generateFileName()
            java.lang.String r8 = android.os.Environment.getExternalStorageState()
            java.lang.String r9 = "mounted"
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L_0x000a
            r4 = 0
            java.io.File r0 = new java.io.File     // Catch:{ IOException -> 0x00a8 }
            r0.<init>(r13)     // Catch:{ IOException -> 0x00a8 }
            boolean r8 = r0.exists()     // Catch:{ IOException -> 0x00a8 }
            if (r8 == 0) goto L_0x00a4
            long r8 = r0.getUsableSpace()     // Catch:{ IOException -> 0x00a8 }
            long r10 = r0.getFreeSpace()     // Catch:{ IOException -> 0x00a8 }
            long r6 = r8 - r10
            dji.log.DJILogFileConfig r8 = r12.config     // Catch:{ IOException -> 0x00a8 }
            long r8 = r8.SPACE_MARGINAL     // Catch:{ IOException -> 0x00a8 }
            int r8 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r8 <= 0) goto L_0x0040
            dji.log.DJIFileUtil.delAllFiles(r0)     // Catch:{ IOException -> 0x00a8 }
            r0.mkdirs()     // Catch:{ IOException -> 0x00a8 }
        L_0x0040:
            java.io.File r2 = new java.io.File     // Catch:{ IOException -> 0x00a8 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00a8 }
            r8.<init>()     // Catch:{ IOException -> 0x00a8 }
            java.lang.StringBuilder r8 = r8.append(r13)     // Catch:{ IOException -> 0x00a8 }
            java.lang.StringBuilder r8 = r8.append(r3)     // Catch:{ IOException -> 0x00a8 }
            java.lang.String r8 = r8.toString()     // Catch:{ IOException -> 0x00a8 }
            r2.<init>(r8)     // Catch:{ IOException -> 0x00a8 }
            boolean r8 = r2.exists()     // Catch:{ IOException -> 0x00a8 }
            if (r8 != 0) goto L_0x0084
            java.lang.String r14 = r12.getHeader(r14)     // Catch:{ IOException -> 0x00a8 }
            java.lang.String r8 = "DJILogFileManager"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00a8 }
            r9.<init>()     // Catch:{ IOException -> 0x00a8 }
            java.lang.String r10 = "create file: "
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ IOException -> 0x00a8 }
            java.lang.String r10 = r2.getPath()     // Catch:{ IOException -> 0x00a8 }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ IOException -> 0x00a8 }
            java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x00a8 }
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ IOException -> 0x00a8 }
            dji.log.DJILog.d(r8, r9, r10)     // Catch:{ IOException -> 0x00a8 }
            r2.createNewFile()     // Catch:{ IOException -> 0x00a8 }
        L_0x0084:
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00a8 }
            r8 = 1
            r5.<init>(r2, r8)     // Catch:{ IOException -> 0x00a8 }
            byte[] r8 = r14.getBytes()     // Catch:{ IOException -> 0x00c8, all -> 0x00c5 }
            r5.write(r8)     // Catch:{ IOException -> 0x00c8, all -> 0x00c5 }
            r5.flush()     // Catch:{ IOException -> 0x00c8, all -> 0x00c5 }
            r5.close()     // Catch:{ IOException -> 0x00c8, all -> 0x00c5 }
            if (r5 == 0) goto L_0x000a
            r5.close()     // Catch:{ IOException -> 0x009e }
            goto L_0x000a
        L_0x009e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x000a
        L_0x00a4:
            r0.mkdirs()     // Catch:{ IOException -> 0x00a8 }
            goto L_0x0040
        L_0x00a8:
            r1 = move-exception
        L_0x00a9:
            r1.printStackTrace()     // Catch:{ all -> 0x00b9 }
            if (r4 == 0) goto L_0x000a
            r4.close()     // Catch:{ IOException -> 0x00b3 }
            goto L_0x000a
        L_0x00b3:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x000a
        L_0x00b9:
            r8 = move-exception
        L_0x00ba:
            if (r4 == 0) goto L_0x00bf
            r4.close()     // Catch:{ IOException -> 0x00c0 }
        L_0x00bf:
            throw r8
        L_0x00c0:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00bf
        L_0x00c5:
            r8 = move-exception
            r4 = r5
            goto L_0x00ba
        L_0x00c8:
            r1 = move-exception
            r4 = r5
            goto L_0x00a9
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.log.DJILogFileManager.writeLog2File(java.lang.String, java.lang.String):void");
    }

    /* access modifiers changed from: private */
    public String generateLogMsg(long info_time, int info_level, String info_tag, String info_msg) {
        String level;
        String result = info_msg;
        switch (info_level) {
            case 2:
                level = "v";
                break;
            case 3:
                level = "d";
                break;
            case 4:
                level = "i";
                break;
            case 5:
                level = "w";
                break;
            case 6:
                level = "e";
                break;
            default:
                level = "d";
                break;
        }
        if (this.config.fileFormat != null) {
            result = this.config.fileFormat.format(info_time, level, info_tag, info_msg);
        }
        return encrypt(result) + "\r\n";
    }

    private String encrypt(String info) {
        if (!DJILog.controller().encrypt) {
            return info;
        }
        String result = info;
        if (this.config.encryption != null) {
            try {
                result = this.config.encryption.encrypt(info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private void executeTransactionJob(List<DJILogEntry> logCaches) {
        if (logCaches != null && logCaches.size() != 0) {
            executeJob(new TransactionJob(logCaches));
        }
    }

    private void executeJob(Runnable job) {
        if (this.pool != null) {
            this.pool.execute(job);
        }
    }

    private class TransactionJob implements Runnable {
        final List<DJILogEntry> logCaches;

        public TransactionJob(List<DJILogEntry> logCaches2) {
            this.logCaches = logCaches2;
        }

        public void run() {
            Map<String, DJILogEntry> result = new HashMap<>();
            for (DJILogEntry entry : this.logCaches) {
                if (!TextUtils.isEmpty(entry.path)) {
                    if (result.containsKey(entry.path)) {
                        DJILogEntry existEntry = (DJILogEntry) result.get(entry.path);
                        existEntry.msg += DJILogFileManager.this.generateLogMsg(entry.time, entry.level, entry.tag, entry.msg);
                    } else {
                        DJILogEntry newEntry = new DJILogEntry();
                        newEntry.path = entry.path;
                        newEntry.msg = DJILogFileManager.this.generateLogMsg(entry.time, entry.level, entry.tag, entry.msg);
                        result.put(entry.path, newEntry);
                    }
                }
            }
            for (DJILogEntry value : result.values()) {
                DJILogFileManager.this.writeLog2File(value.path, value.msg);
            }
            this.logCaches.clear();
        }
    }

    private String getHeader(String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(encrypt("====== Device Info ======")).append("\r\n");
        sb.append(encrypt("manufacture:" + Build.MANUFACTURER)).append("\r\n");
        sb.append(encrypt("product:" + Build.PRODUCT)).append("\r\n");
        sb.append(encrypt("model:" + Build.MODEL)).append("\r\n");
        sb.append(encrypt("version:" + Build.DISPLAY)).append("\r\n");
        sb.append(encrypt("android version:" + Build.VERSION.RELEASE)).append("\r\n");
        sb.append(encrypt("sdk version:" + Build.VERSION.SDK_INT)).append("\r\n");
        sb.append(encrypt("======== App Info ========")).append("\r\n");
        sb.append(encrypt("app version:" + this.config.versionName)).append("\r\n");
        sb.append(encrypt("==========================")).append("\r\n");
        sb.append(msg);
        return sb.toString();
    }
}
