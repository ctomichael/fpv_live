package dji.midware.stat;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class LinuxUtils {
    private static final int FIRST_SYS_CPU_COLUMN_INDEX = 2;
    private static final int IDLE_SYS_CPU_COLUMN_INDEX = 5;

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0030 A[SYNTHETIC, Splitter:B:22:0x0030] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String readSystemStat() {
        /*
            r7 = this;
            r3 = 0
            r2 = 0
            java.io.RandomAccessFile r4 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x001e }
            java.lang.String r5 = "/proc/stat"
            java.lang.String r6 = "r"
            r4.<init>(r5, r6)     // Catch:{ IOException -> 0x001e }
            java.lang.String r2 = r4.readLine()     // Catch:{ IOException -> 0x003c, all -> 0x0039 }
            if (r4 == 0) goto L_0x003f
            r4.close()     // Catch:{ IOException -> 0x0018 }
            r3 = r4
        L_0x0017:
            return r2
        L_0x0018:
            r0 = move-exception
            r0.printStackTrace()
            r3 = r4
            goto L_0x0017
        L_0x001e:
            r1 = move-exception
        L_0x001f:
            r1.printStackTrace()     // Catch:{ all -> 0x002d }
            if (r3 == 0) goto L_0x0017
            r3.close()     // Catch:{ IOException -> 0x0028 }
            goto L_0x0017
        L_0x0028:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0017
        L_0x002d:
            r5 = move-exception
        L_0x002e:
            if (r3 == 0) goto L_0x0033
            r3.close()     // Catch:{ IOException -> 0x0034 }
        L_0x0033:
            throw r5
        L_0x0034:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0033
        L_0x0039:
            r5 = move-exception
            r3 = r4
            goto L_0x002e
        L_0x003c:
            r1 = move-exception
            r3 = r4
            goto L_0x001f
        L_0x003f:
            r3 = r4
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.stat.LinuxUtils.readSystemStat():java.lang.String");
    }

    public float getSystemCpuUsage(String start, String end) {
        String[] stat = start.split("\\s");
        long idle1 = getSystemIdleTime(stat);
        long up1 = getSystemUptime(stat);
        String[] stat2 = end.split("\\s");
        long idle2 = getSystemIdleTime(stat2);
        long up2 = getSystemUptime(stat2);
        if (idle1 < 0 || up1 < 0 || idle2 < 0 || up2 < 0 || up2 + idle2 <= up1 + idle1 || up2 < up1) {
            return -1.0f;
        }
        return (((float) (up2 - up1)) / ((float) ((up2 + idle2) - (up1 + idle1)))) * 100.0f;
    }

    public long getSystemUptime(String[] stat) {
        long l = 0;
        for (int i = 2; i < stat.length; i++) {
            if (i != 5) {
                try {
                    l += Long.parseLong(stat[i]);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    return -1;
                }
            }
        }
        return l;
    }

    public long getSystemIdleTime(String[] stat) {
        try {
            return Long.parseLong(stat[5]);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0048 A[SYNTHETIC, Splitter:B:22:0x0048] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String readProcessStat(int r8) {
        /*
            r7 = this;
            r3 = 0
            r2 = 0
            java.io.RandomAccessFile r4 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x0036 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0036 }
            r5.<init>()     // Catch:{ IOException -> 0x0036 }
            java.lang.String r6 = "/proc/"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ IOException -> 0x0036 }
            java.lang.StringBuilder r5 = r5.append(r8)     // Catch:{ IOException -> 0x0036 }
            java.lang.String r6 = "/stat"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ IOException -> 0x0036 }
            java.lang.String r5 = r5.toString()     // Catch:{ IOException -> 0x0036 }
            java.lang.String r6 = "r"
            r4.<init>(r5, r6)     // Catch:{ IOException -> 0x0036 }
            java.lang.String r2 = r4.readLine()     // Catch:{ IOException -> 0x0054, all -> 0x0051 }
            if (r4 == 0) goto L_0x0057
            r4.close()     // Catch:{ IOException -> 0x0030 }
            r3 = r4
        L_0x002f:
            return r2
        L_0x0030:
            r0 = move-exception
            r0.printStackTrace()
            r3 = r4
            goto L_0x002f
        L_0x0036:
            r1 = move-exception
        L_0x0037:
            r1.printStackTrace()     // Catch:{ all -> 0x0045 }
            if (r3 == 0) goto L_0x002f
            r3.close()     // Catch:{ IOException -> 0x0040 }
            goto L_0x002f
        L_0x0040:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x002f
        L_0x0045:
            r5 = move-exception
        L_0x0046:
            if (r3 == 0) goto L_0x004b
            r3.close()     // Catch:{ IOException -> 0x004c }
        L_0x004b:
            throw r5
        L_0x004c:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x004b
        L_0x0051:
            r5 = move-exception
            r3 = r4
            goto L_0x0046
        L_0x0054:
            r1 = move-exception
            r3 = r4
            goto L_0x0037
        L_0x0057:
            r3 = r4
            goto L_0x002f
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.stat.LinuxUtils.readProcessStat(int):java.lang.String");
    }

    public float getProcessCpuUsage(String start, String end, long uptime) {
        long up1 = getProcessUptime(start.split("\\s"));
        long up2 = getProcessUptime(end.split("\\s"));
        if (up1 < 0 || up2 < up1 || ((double) uptime) <= 0.0d) {
            return -1.0f;
        }
        return (100.0f * ((float) (up2 - up1))) / ((float) uptime);
    }

    public long getProcessUptime(String[] stat) {
        return Long.parseLong(stat[14]) + Long.parseLong(stat[15]);
    }

    public long getProcessIdleTime(String[] stat) {
        return Long.parseLong(stat[16]) + Long.parseLong(stat[17]);
    }

    public float syncGetSystemCpuUsage(long elapse) {
        String stat1 = readSystemStat();
        if (stat1 == null) {
            return -1.0f;
        }
        try {
            Thread.sleep(elapse);
        } catch (Exception e) {
        }
        String stat2 = readSystemStat();
        if (stat2 != null) {
            return getSystemCpuUsage(stat1, stat2);
        }
        return -1.0f;
    }

    public float syncGetProcessCpuUsage(int pid, long elapse) {
        String pidStat1 = readProcessStat(pid);
        String totalStat1 = readSystemStat();
        if (pidStat1 == null || totalStat1 == null) {
            return -1.0f;
        }
        try {
            Thread.sleep(elapse);
            String pidStat2 = readProcessStat(pid);
            String totalStat2 = readSystemStat();
            if (pidStat2 == null || totalStat2 == null) {
                return -1.0f;
            }
            return getProcessCpuUsage(pidStat1, pidStat2, getSystemUptime(totalStat2.split("\\s")) - getSystemUptime(totalStat1.split("\\s")));
        } catch (Exception e) {
            e.printStackTrace();
            return -1.0f;
        }
    }

    public float[] syncGetTotalAndProcessCpuUsage(int pid, long elapse) {
        float[] rst = {-1.0f, -1.0f};
        String pidStat1 = readProcessStat(pid);
        String totalStat1 = readSystemStat();
        if (totalStat1 != null) {
            try {
                Thread.sleep(elapse);
                String pidStat2 = readProcessStat(pid);
                String totalStat2 = readSystemStat();
                if (totalStat2 != null) {
                    long cpu1 = getSystemUptime(totalStat1.split("\\s"));
                    long cpu2 = getSystemUptime(totalStat2.split("\\s"));
                    rst[0] = getSystemCpuUsage(totalStat1, totalStat2);
                    if (!(pidStat1 == null || pidStat2 == null)) {
                        rst[1] = getProcessCpuUsage(pidStat1, pidStat2, cpu2 - cpu1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rst;
    }
}
