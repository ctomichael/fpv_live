package dji.midware.media.transcode.offline;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.MediaLogger;

@EXClassNullAway
public class H264FileLoader implements Runnable {
    private static final int FILE_READ_CHUNK_LENGTH = 102400;
    private static String TAG = "H264FileLoader";
    private long bytesHasRead;
    private long inputFileLength;
    private String inputFullFileName = "";
    private boolean isPause;
    private boolean isStop;
    private ProgressListener progressListener = null;
    private Thread threadLoadFile = null;

    public interface ProgressListener {
        void onProgress(int i);
    }

    public void LoadFile(String inputFullFileName2) {
        this.isPause = false;
        this.isStop = false;
        this.inputFileLength = 0;
        this.bytesHasRead = 0;
        this.inputFullFileName = inputFullFileName2;
        this.threadLoadFile = new Thread(this);
        this.threadLoadFile.start();
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0079 A[Catch:{ Exception -> 0x00dd }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00bc A[SYNTHETIC, Splitter:B:32:0x00bc] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x017d A[SYNTHETIC, Splitter:B:58:0x017d] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x01a3 A[SYNTHETIC, Splitter:B:65:0x01a3] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01c4 A[Catch:{ Exception -> 0x00dd }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:55:0x0160=Splitter:B:55:0x0160, B:29:0x009f=Splitter:B:29:0x009f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r24 = this;
            r9 = 0
            r12 = 0
            r5 = 102400(0x19000, float:1.43493E-40)
            java.io.File r10 = new java.io.File     // Catch:{ FileNotFoundException -> 0x01e1, Exception -> 0x015f }
            r0 = r24
            java.lang.String r0 = r0.inputFullFileName     // Catch:{ FileNotFoundException -> 0x01e1, Exception -> 0x015f }
            r20 = r0
            r0 = r20
            r10.<init>(r0)     // Catch:{ FileNotFoundException -> 0x01e1, Exception -> 0x015f }
            long r20 = r10.length()     // Catch:{ FileNotFoundException -> 0x01e4, Exception -> 0x01da, all -> 0x01d3 }
            r0 = r20
            r2 = r24
            r2.inputFileLength = r0     // Catch:{ FileNotFoundException -> 0x01e4, Exception -> 0x01da, all -> 0x01d3 }
            java.lang.String r20 = dji.midware.media.transcode.offline.H264FileLoader.TAG     // Catch:{ FileNotFoundException -> 0x01e4, Exception -> 0x01da, all -> 0x01d3 }
            java.lang.StringBuilder r21 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x01e4, Exception -> 0x01da, all -> 0x01d3 }
            r21.<init>()     // Catch:{ FileNotFoundException -> 0x01e4, Exception -> 0x01da, all -> 0x01d3 }
            java.lang.String r22 = "file length="
            java.lang.StringBuilder r21 = r21.append(r22)     // Catch:{ FileNotFoundException -> 0x01e4, Exception -> 0x01da, all -> 0x01d3 }
            r0 = r24
            long r0 = r0.inputFileLength     // Catch:{ FileNotFoundException -> 0x01e4, Exception -> 0x01da, all -> 0x01d3 }
            r22 = r0
            java.lang.StringBuilder r21 = r21.append(r22)     // Catch:{ FileNotFoundException -> 0x01e4, Exception -> 0x01da, all -> 0x01d3 }
            java.lang.String r21 = r21.toString()     // Catch:{ FileNotFoundException -> 0x01e4, Exception -> 0x01da, all -> 0x01d3 }
            android.util.Log.i(r20, r21)     // Catch:{ FileNotFoundException -> 0x01e4, Exception -> 0x01da, all -> 0x01d3 }
            java.io.FileInputStream r13 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x01e4, Exception -> 0x01da, all -> 0x01d3 }
            r13.<init>(r10)     // Catch:{ FileNotFoundException -> 0x01e4, Exception -> 0x01da, all -> 0x01d3 }
            byte[] r11 = new byte[r5]     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r4 = 0
            r18 = -1
            r16 = -1
        L_0x0047:
            int r4 = r13.read(r11)     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r20 = -1
            r0 = r20
            if (r4 == r0) goto L_0x006a
            r0 = r24
            long r0 = r0.bytesHasRead     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r20 = r0
            long r0 = (long) r4     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r22 = r0
            long r20 = r20 + r22
            r0 = r20
            r2 = r24
            r2.bytesHasRead = r0     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r0 = r24
            boolean r0 = r0.isStop     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r20 = r0
            if (r20 == 0) goto L_0x0082
        L_0x006a:
            if (r13 == 0) goto L_0x006f
            r13.close()     // Catch:{ Exception -> 0x013f }
        L_0x006f:
            r12 = r13
            r9 = r10
        L_0x0071:
            r0 = r24
            boolean r0 = r0.isStop     // Catch:{ Exception -> 0x00dd }
            r20 = r0
            if (r20 == 0) goto L_0x01c4
            java.lang.String r20 = dji.midware.media.transcode.offline.H264FileLoader.TAG     // Catch:{ Exception -> 0x00dd }
            java.lang.String r21 = "is stopped"
            android.util.Log.i(r20, r21)     // Catch:{ Exception -> 0x00dd }
        L_0x0081:
            return
        L_0x0082:
            r0 = r24
            boolean r0 = r0.isPause     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r20 = r0
            if (r20 != 0) goto L_0x0096
            int r20 = dji.midware.natives.FPVController.native_getQueueSize()     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r21 = 20
            r0 = r20
            r1 = r21
            if (r0 <= r1) goto L_0x00fb
        L_0x0096:
            r20 = 10
            java.lang.Thread.sleep(r20)     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            goto L_0x0082
        L_0x009c:
            r8 = move-exception
            r12 = r13
            r9 = r10
        L_0x009f:
            java.lang.String r20 = dji.midware.media.transcode.offline.H264FileLoader.TAG     // Catch:{ all -> 0x01a0 }
            java.lang.StringBuilder r21 = new java.lang.StringBuilder     // Catch:{ all -> 0x01a0 }
            r21.<init>()     // Catch:{ all -> 0x01a0 }
            java.lang.String r22 = "Fine not found"
            java.lang.StringBuilder r21 = r21.append(r22)     // Catch:{ all -> 0x01a0 }
            r0 = r21
            java.lang.StringBuilder r21 = r0.append(r8)     // Catch:{ all -> 0x01a0 }
            java.lang.String r21 = r21.toString()     // Catch:{ all -> 0x01a0 }
            android.util.Log.e(r20, r21)     // Catch:{ all -> 0x01a0 }
            if (r12 == 0) goto L_0x0071
            r12.close()     // Catch:{ Exception -> 0x00c0 }
            goto L_0x0071
        L_0x00c0:
            r14 = move-exception
            java.lang.String r20 = dji.midware.media.transcode.offline.H264FileLoader.TAG     // Catch:{ Exception -> 0x00dd }
            java.lang.StringBuilder r21 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00dd }
            r21.<init>()     // Catch:{ Exception -> 0x00dd }
            java.lang.String r22 = "Error while closing stream: "
            java.lang.StringBuilder r21 = r21.append(r22)     // Catch:{ Exception -> 0x00dd }
            r0 = r21
            java.lang.StringBuilder r21 = r0.append(r14)     // Catch:{ Exception -> 0x00dd }
            java.lang.String r21 = r21.toString()     // Catch:{ Exception -> 0x00dd }
            android.util.Log.e(r20, r21)     // Catch:{ Exception -> 0x00dd }
            goto L_0x0071
        L_0x00dd:
            r8 = move-exception
        L_0x00de:
            java.io.StringWriter r19 = new java.io.StringWriter
            r19.<init>()
            java.io.PrintWriter r20 = new java.io.PrintWriter
            r0 = r20
            r1 = r19
            r0.<init>(r1)
            r0 = r20
            r8.printStackTrace(r0)
            java.lang.String r20 = dji.midware.media.transcode.offline.H264FileLoader.TAG
            java.lang.String r21 = r19.toString()
            android.util.Log.e(r20, r21)
            goto L_0x0081
        L_0x00fb:
            dji.midware.media.DJIVideoUtil$ExtraMemInvokePoint r20 = dji.midware.media.DJIVideoUtil.ExtraMemInvokePoint.H264FileLoader     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r21 = 0
            r0 = r20
            r1 = r21
            byte[] r20 = dji.midware.media.DJIVideoUtil.extraMemForParsing(r0, r11, r1, r4)     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r0 = r20
            dji.midware.natives.FPVController.native_transferVideoData(r0, r4)     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r0 = r24
            dji.midware.media.transcode.offline.H264FileLoader$ProgressListener r0 = r0.progressListener     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r20 = r0
            if (r20 == 0) goto L_0x0047
            int r15 = r24.getProgress()     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r0 = r18
            if (r15 == r0) goto L_0x0047
            r20 = 100
            r0 = r20
            if (r15 == r0) goto L_0x0047
            long r20 = r6 - r16
            r22 = 500(0x1f4, double:2.47E-321)
            int r20 = (r20 > r22 ? 1 : (r20 == r22 ? 0 : -1))
            if (r20 <= 0) goto L_0x0047
            r0 = r24
            dji.midware.media.transcode.offline.H264FileLoader$ProgressListener r0 = r0.progressListener     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r20 = r0
            r0 = r20
            r0.onProgress(r15)     // Catch:{ FileNotFoundException -> 0x009c, Exception -> 0x01dd, all -> 0x01d6 }
            r18 = r15
            r16 = r6
            goto L_0x0047
        L_0x013f:
            r14 = move-exception
            java.lang.String r20 = dji.midware.media.transcode.offline.H264FileLoader.TAG     // Catch:{ Exception -> 0x01ce }
            java.lang.StringBuilder r21 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01ce }
            r21.<init>()     // Catch:{ Exception -> 0x01ce }
            java.lang.String r22 = "Error while closing stream: "
            java.lang.StringBuilder r21 = r21.append(r22)     // Catch:{ Exception -> 0x01ce }
            r0 = r21
            java.lang.StringBuilder r21 = r0.append(r14)     // Catch:{ Exception -> 0x01ce }
            java.lang.String r21 = r21.toString()     // Catch:{ Exception -> 0x01ce }
            android.util.Log.e(r20, r21)     // Catch:{ Exception -> 0x01ce }
            r12 = r13
            r9 = r10
            goto L_0x0071
        L_0x015f:
            r14 = move-exception
        L_0x0160:
            java.lang.String r20 = dji.midware.media.transcode.offline.H264FileLoader.TAG     // Catch:{ all -> 0x01a0 }
            java.lang.StringBuilder r21 = new java.lang.StringBuilder     // Catch:{ all -> 0x01a0 }
            r21.<init>()     // Catch:{ all -> 0x01a0 }
            java.lang.String r22 = "Exception while reading file "
            java.lang.StringBuilder r21 = r21.append(r22)     // Catch:{ all -> 0x01a0 }
            r0 = r21
            java.lang.StringBuilder r21 = r0.append(r14)     // Catch:{ all -> 0x01a0 }
            java.lang.String r21 = r21.toString()     // Catch:{ all -> 0x01a0 }
            android.util.Log.e(r20, r21)     // Catch:{ all -> 0x01a0 }
            if (r12 == 0) goto L_0x0071
            r12.close()     // Catch:{ Exception -> 0x0182 }
            goto L_0x0071
        L_0x0182:
            r14 = move-exception
            java.lang.String r20 = dji.midware.media.transcode.offline.H264FileLoader.TAG     // Catch:{ Exception -> 0x00dd }
            java.lang.StringBuilder r21 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00dd }
            r21.<init>()     // Catch:{ Exception -> 0x00dd }
            java.lang.String r22 = "Error while closing stream: "
            java.lang.StringBuilder r21 = r21.append(r22)     // Catch:{ Exception -> 0x00dd }
            r0 = r21
            java.lang.StringBuilder r21 = r0.append(r14)     // Catch:{ Exception -> 0x00dd }
            java.lang.String r21 = r21.toString()     // Catch:{ Exception -> 0x00dd }
            android.util.Log.e(r20, r21)     // Catch:{ Exception -> 0x00dd }
            goto L_0x0071
        L_0x01a0:
            r20 = move-exception
        L_0x01a1:
            if (r12 == 0) goto L_0x01a6
            r12.close()     // Catch:{ Exception -> 0x01a7 }
        L_0x01a6:
            throw r20     // Catch:{ Exception -> 0x00dd }
        L_0x01a7:
            r14 = move-exception
            java.lang.String r21 = dji.midware.media.transcode.offline.H264FileLoader.TAG     // Catch:{ Exception -> 0x00dd }
            java.lang.StringBuilder r22 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00dd }
            r22.<init>()     // Catch:{ Exception -> 0x00dd }
            java.lang.String r23 = "Error while closing stream: "
            java.lang.StringBuilder r22 = r22.append(r23)     // Catch:{ Exception -> 0x00dd }
            r0 = r22
            java.lang.StringBuilder r22 = r0.append(r14)     // Catch:{ Exception -> 0x00dd }
            java.lang.String r22 = r22.toString()     // Catch:{ Exception -> 0x00dd }
            android.util.Log.e(r21, r22)     // Catch:{ Exception -> 0x00dd }
            goto L_0x01a6
        L_0x01c4:
            java.lang.String r20 = dji.midware.media.transcode.offline.H264FileLoader.TAG     // Catch:{ Exception -> 0x00dd }
            java.lang.String r21 = "All data has been loaded"
            android.util.Log.i(r20, r21)     // Catch:{ Exception -> 0x00dd }
            goto L_0x0081
        L_0x01ce:
            r8 = move-exception
            r12 = r13
            r9 = r10
            goto L_0x00de
        L_0x01d3:
            r20 = move-exception
            r9 = r10
            goto L_0x01a1
        L_0x01d6:
            r20 = move-exception
            r12 = r13
            r9 = r10
            goto L_0x01a1
        L_0x01da:
            r14 = move-exception
            r9 = r10
            goto L_0x0160
        L_0x01dd:
            r14 = move-exception
            r12 = r13
            r9 = r10
            goto L_0x0160
        L_0x01e1:
            r8 = move-exception
            goto L_0x009f
        L_0x01e4:
            r8 = move-exception
            r9 = r10
            goto L_0x009f
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.transcode.offline.H264FileLoader.run():void");
    }

    public void join() {
        try {
            this.threadLoadFile.join();
        } catch (InterruptedException ie) {
            MediaLogger.e(TAG, ie);
        }
    }

    public void pause() {
        this.isPause = true;
    }

    public void resume() {
        this.isPause = false;
    }

    public void stop() {
        this.isStop = true;
        join();
        MediaLogger.show("", "H264FileLoader thread ends");
    }

    private int getProgress() {
        int progress = this.inputFileLength == 0 ? 0 : (int) ((100.0d * ((double) this.bytesHasRead)) / ((double) this.inputFileLength));
        if (progress > 100) {
            Log.e(TAG, "progress>100, is " + progress);
            progress = 100;
        }
        if (progress >= 0) {
            return progress;
        }
        Log.e(TAG, "progress<0, is " + progress);
        return 0;
    }

    public void setProgressListener(ProgressListener listener) {
        this.progressListener = listener;
    }
}
