package it.sauronsoftware.ftp4j.extrecognizers;

public class DefaultTextualExtensionRecognizer extends ParametricTextualExtensionRecognizer {
    private static DefaultTextualExtensionRecognizer instance = null;
    private static final Object lock = new Object();

    public static DefaultTextualExtensionRecognizer getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new DefaultTextualExtensionRecognizer();
            }
        }
        return instance;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0036 A[SYNTHETIC, Splitter:B:13:0x0036] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0047 A[SYNTHETIC, Splitter:B:23:0x0047] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x003a A[EDGE_INSN: B:32:0x003a->B:15:0x003a ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x001f A[Catch:{ Exception -> 0x0032, all -> 0x004f }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private DefaultTextualExtensionRecognizer() {
        /*
            r7 = this;
            r7.<init>()
            r1 = 0
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0052, all -> 0x0044 }
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0052, all -> 0x0044 }
            java.lang.Class r5 = r7.getClass()     // Catch:{ Exception -> 0x0052, all -> 0x0044 }
            java.lang.String r6 = "textualexts"
            java.io.InputStream r5 = r5.getResourceAsStream(r6)     // Catch:{ Exception -> 0x0052, all -> 0x0044 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0052, all -> 0x0044 }
            r2.<init>(r4)     // Catch:{ Exception -> 0x0052, all -> 0x0044 }
        L_0x0019:
            java.lang.String r0 = r2.readLine()     // Catch:{ Exception -> 0x0032, all -> 0x004f }
            if (r0 == 0) goto L_0x003a
            java.util.StringTokenizer r3 = new java.util.StringTokenizer     // Catch:{ Exception -> 0x0032, all -> 0x004f }
            r3.<init>(r0)     // Catch:{ Exception -> 0x0032, all -> 0x004f }
        L_0x0024:
            boolean r4 = r3.hasMoreTokens()     // Catch:{ Exception -> 0x0032, all -> 0x004f }
            if (r4 == 0) goto L_0x0019
            java.lang.String r4 = r3.nextToken()     // Catch:{ Exception -> 0x0032, all -> 0x004f }
            r7.addExtension(r4)     // Catch:{ Exception -> 0x0032, all -> 0x004f }
            goto L_0x0024
        L_0x0032:
            r4 = move-exception
            r1 = r2
        L_0x0034:
            if (r1 == 0) goto L_0x0039
            r1.close()     // Catch:{ Throwable -> 0x004b }
        L_0x0039:
            return
        L_0x003a:
            if (r2 == 0) goto L_0x0054
            r2.close()     // Catch:{ Throwable -> 0x0041 }
            r1 = r2
            goto L_0x0039
        L_0x0041:
            r4 = move-exception
            r1 = r2
            goto L_0x0039
        L_0x0044:
            r4 = move-exception
        L_0x0045:
            if (r1 == 0) goto L_0x004a
            r1.close()     // Catch:{ Throwable -> 0x004d }
        L_0x004a:
            throw r4
        L_0x004b:
            r4 = move-exception
            goto L_0x0039
        L_0x004d:
            r5 = move-exception
            goto L_0x004a
        L_0x004f:
            r4 = move-exception
            r1 = r2
            goto L_0x0045
        L_0x0052:
            r4 = move-exception
            goto L_0x0034
        L_0x0054:
            r1 = r2
            goto L_0x0039
        */
        throw new UnsupportedOperationException("Method not decompiled: it.sauronsoftware.ftp4j.extrecognizers.DefaultTextualExtensionRecognizer.<init>():void");
    }
}
