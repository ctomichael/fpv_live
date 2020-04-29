package dji.midware.media;

import android.os.Build;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dji.video.framing.internal.decoder.common.FrameFovType;
import com.mapzen.android.lost.internal.Clock;
import dji.log.DJILog;
import dji.log.DJILogPaths;
import dji.midware.media.DJIDecodeServer;
import dji.midware.media.DJIVideoUtil;
import dji.midware.stat.StatService;
import dji.midware.stat.StatSum;
import dji.midware.util.BytesUtil;
import dji.midware.util.DebugFlag;
import java.util.Locale;

public class DJIDecodeClient {
    private static final boolean DEBUG = false;
    private static final int FILE_LOG_ERROR_STATUS_INTERVAL = 5000;
    private static final int FILE_LOG_INTERVAL = 2000;
    private static final int MAX_PARSE_SEI_LENGTH = 32;
    private static final String TAG = "DJIDecodeClient";
    long checkFrameTime;
    long checkFrameTimeSum;
    private long checkSum;
    @NonNull
    private DJIVideoDecoder decoder;
    private int frameHead;
    private int frameLen;
    private HandlerThread inoutThread;
    int invokeCount = 0;
    private int lastCheckIndex;
    private long lastLogErrorStatusTime = 0;
    private int last_input_frame_num = -1;
    private SaveLogHelper logHelper;
    @Nullable
    DJIDecodeServer server;

    public DJIDecodeClient(@NonNull DJIVideoDecoder decoder2) {
        this.decoder = decoder2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0041, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0042, code lost:
        dji.midware.media.MediaLogger.e(dji.midware.media.DJIDecodeClient.TAG, r0);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void stopServer() {
        /*
            r4 = this;
            monitor-enter(r4)
            android.os.HandlerThread r1 = r4.inoutThread     // Catch:{ all -> 0x0038 }
            if (r1 == 0) goto L_0x000d
            android.os.HandlerThread r1 = r4.inoutThread     // Catch:{ all -> 0x0038 }
            boolean r1 = r1.isAlive()     // Catch:{ all -> 0x0038 }
            if (r1 != 0) goto L_0x000f
        L_0x000d:
            monitor-exit(r4)
            return
        L_0x000f:
            dji.midware.media.DJIDecodeServer r1 = r4.server     // Catch:{ all -> 0x0038 }
            if (r1 == 0) goto L_0x0019
            dji.midware.media.DJIDecodeServer r1 = r4.server     // Catch:{ all -> 0x0038 }
            r2 = 0
            r1.removeCallbacksAndMessages(r2)     // Catch:{ all -> 0x0038 }
        L_0x0019:
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0038 }
            r2 = 18
            if (r1 < r2) goto L_0x003b
            android.os.HandlerThread r1 = r4.inoutThread     // Catch:{ all -> 0x0038 }
            r1.quitSafely()     // Catch:{ all -> 0x0038 }
        L_0x0024:
            android.os.HandlerThread r1 = r4.inoutThread     // Catch:{ InterruptedException -> 0x0041 }
            r2 = 3000(0xbb8, double:1.482E-320)
            r1.join(r2)     // Catch:{ InterruptedException -> 0x0041 }
        L_0x002b:
            dji.midware.media.DJIDecodeServer r1 = r4.server     // Catch:{ all -> 0x0038 }
            if (r1 == 0) goto L_0x000d
            dji.midware.media.DJIDecodeServer r1 = r4.server     // Catch:{ all -> 0x0038 }
            r1.releaseDecoder()     // Catch:{ all -> 0x0038 }
            r1 = 0
            r4.server = r1     // Catch:{ all -> 0x0038 }
            goto L_0x000d
        L_0x0038:
            r1 = move-exception
            monitor-exit(r4)
            throw r1
        L_0x003b:
            android.os.HandlerThread r1 = r4.inoutThread     // Catch:{ all -> 0x0038 }
            r1.quit()     // Catch:{ all -> 0x0038 }
            goto L_0x0024
        L_0x0041:
            r0 = move-exception
            java.lang.String r1 = "DJIDecodeClient"
            dji.midware.media.MediaLogger.e(r1, r0)     // Catch:{ all -> 0x0038 }
            goto L_0x002b
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.DJIDecodeClient.stopServer():void");
    }

    public synchronized void resetKeyFrame() {
        if (this.inoutThread != null && this.inoutThread.isAlive()) {
            this.server.sendEmptyMessage(10);
        }
    }

    public synchronized void startServer() {
        if (this.inoutThread == null || !this.inoutThread.isAlive()) {
            if (this.decoder != null) {
                this.inoutThread = new HandlerThread("DJIDecodeInoutHandlerThread", -1);
                DebugFlag.printfLog(TAG, "DJIDecodeInoutHandlerThread STARTED.");
                this.inoutThread.start();
                if (Build.VERSION.SDK_INT < 24 || this.decoder.isPlayBackMode() || this.decoder.needLowFrequencyForSmoothing()) {
                    this.server = new DJIDecodeServerApi16(this.inoutThread.getLooper(), this.decoder);
                } else {
                    this.server = new DJIDecodeServerApi21(this.inoutThread.getLooper(), this.decoder);
                }
                this.server.sendEmptyMessageDelayed(2, 1);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void queueInFrame(byte[] videoBuffer, int size, int frameNum, boolean isKeyFrame, long frameIndex, long com_pts, int width, int height, int spsPos, int spsLen, int ppsPos, int ppsLen) {
        int skip;
        MediaLogger.i(false, TAG, "cline on queuein before checking inoutThread status");
        if (this.inoutThread == null || !this.inoutThread.isAlive()) {
            DJIVideoDecoder.log2File("dec thread dead");
        } else {
            MediaLogger.i(false, TAG, "cline on queuein after checking inoutThread status");
            if (this.server != null) {
                DJIDecodeServer.InputFrame newFrame = DJIDecodeServer.InputFrame.obtain(videoBuffer, size, com_pts, System.currentTimeMillis(), isKeyFrame, frameNum, frameIndex, width, height, spsPos, spsLen, ppsPos, ppsLen);
                int seiOffset = parseSei(newFrame, true, 32);
                if (DJIVideoDecoder.needFetchIframe()) {
                    if (!checkFrame(newFrame, seiOffset)) {
                        DJIVideoDecoder.setErrorStatus(true);
                    } else if (newFrame.isKeyFrame) {
                        DJIVideoDecoder.setErrorStatus(false);
                    } else {
                        DJIVideoDecoder.setErrorStatus(DJIVideoDecoder.getErrorStatus());
                    }
                    long time = System.currentTimeMillis();
                    if (time - this.lastLogErrorStatusTime > 5000) {
                        DJILog.saveLog("errStatus=" + DJIVideoDecoder.getErrorStatus(), DJILogPaths.LOG_PIGEON_STREAM_TEST);
                        this.lastLogErrorStatusTime = time;
                    }
                }
                if (!DJIVideoDecoder.needFetchIframe() || !DJIVideoDecoder.getErrorStatus()) {
                    this.server.obtainMessage(1, newFrame).sendToTarget();
                } else {
                    newFrame.recycle();
                }
            } else {
                DJIVideoDecoder.log2File("dec server null");
            }
            int cur_frame_num = DJIVideoUtil.getFrameNum(com_pts);
            if (cur_frame_num >= 16) {
                StatService.getInstance().postEvent(StatSum.class, "FrameNum_Abnormal", 1.0f);
                MediaLogger.i(false, TAG, String.format(Locale.US, "[FrameNum_Sequence_Abnormal] time=%3.3f index=%d last=%d cur=%d", Double.valueOf(((double) (DJIVideoUtil.getPtsMs(com_pts) % Clock.MS_TO_NS)) / 1000.0d), Integer.valueOf(DJIVideoUtil.getFrameIndex(com_pts)), Integer.valueOf(this.last_input_frame_num), Integer.valueOf(cur_frame_num)));
                this.last_input_frame_num = -1;
            } else {
                if (this.last_input_frame_num != -1 && (skip = ((cur_frame_num - this.last_input_frame_num) + 16) % 16) > 1) {
                    StatService.getInstance().postEvent(StatSum.class, "FrameNum_Skip", (float) skip);
                    MediaLogger.i(false, TAG, String.format(Locale.US, "[FrameNum_Sequence_Skip] time=%3.3f index=%d last=%d cur=%d", Double.valueOf(((double) (DJIVideoUtil.getPtsMs(com_pts) % Clock.MS_TO_NS)) / 1000.0d), Integer.valueOf(DJIVideoUtil.getFrameIndex(com_pts)), Integer.valueOf(this.last_input_frame_num), Integer.valueOf(cur_frame_num)));
                }
                this.last_input_frame_num = cur_frame_num;
            }
        }
    }

    private int parseSei(DJIDecodeServer.InputFrame frame, boolean isFromTail, int maxCheckLength) {
        if (frame == null || frame.videoBuffer == null) {
            return -1;
        }
        int rst = -1;
        int start = isFromTail ? frame.videoBuffer.length - maxCheckLength : 0;
        int curIndex = start;
        while (true) {
            int curIndex2 = DJIVideoUtil.findNextNal(frame.videoBuffer, curIndex, (maxCheckLength - curIndex) + start);
            if (curIndex2 <= 0 || curIndex2 + 4 >= frame.videoBuffer.length) {
                return rst;
            }
            if ((frame.videoBuffer[curIndex2 + 3] & 31) != DJIVideoUtil.NalType.Sei.value() || curIndex2 + 5 >= frame.videoBuffer.length) {
                curIndex = curIndex2 + 4;
            } else {
                if (rst < 0) {
                    rst = curIndex2;
                }
                onSeiGet(frame, frame.videoBuffer[curIndex2 + 4] & 255, curIndex2 + 5);
                curIndex = curIndex2 + 6;
            }
        }
    }

    private void onSeiGet(DJIDecodeServer.InputFrame frame, int type, int dataOffset) {
        boolean z = true;
        if (frame != null) {
            switch (type) {
                case 31:
                    if (dataOffset + 5 < frame.videoBuffer.length) {
                        frame.timeStamp = BytesUtil.getIntFromBytes(frame.videoBuffer, dataOffset + 1, dataOffset + 2, dataOffset + 4, dataOffset + 5);
                        return;
                    }
                    return;
                case 32:
                    if (dataOffset + 1 < frame.videoBuffer.length) {
                        frame.zoomIndex = frame.videoBuffer[dataOffset + 1] & 255;
                        return;
                    }
                    return;
                case 240:
                    if (dataOffset + 22 < frame.videoBuffer.length) {
                        frame.checkIndex = BytesUtil.getIntFromBytes(frame.videoBuffer, dataOffset + 1, dataOffset + 2, dataOffset + 4);
                        frame.checkFrameLen = BytesUtil.getIntFromBytes(frame.videoBuffer, dataOffset + 5, dataOffset + 7, dataOffset + 8);
                        frame.checkFrameCrc = BytesUtil.getLongFromBytes(frame.videoBuffer, dataOffset + 10, dataOffset + 11, dataOffset + 13, dataOffset + 14);
                        frame.timeStamp = BytesUtil.getIntFromBytes(frame.videoBuffer, dataOffset + 16, dataOffset + 17, dataOffset + 19, dataOffset + 20);
                        if (dataOffset + 24 < frame.videoBuffer.length) {
                            if ((frame.videoBuffer[dataOffset + 24] & 1) == 0) {
                                z = false;
                            }
                            frame.checkIsKeyFrame = z;
                        }
                        frame.fovType = FrameFovType.find((frame.videoBuffer[dataOffset + 22] >> 6) & 3);
                        frame.zoomIndex = this.decoder.isRealTimeStream() ? frame.videoBuffer[dataOffset + 22] & 63 : -1;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void log2TestFile(String msg) {
        if (this.logHelper == null) {
            this.logHelper = new SaveLogHelper(DJILogPaths.LOG_PIGEON_STREAM_TEST, 2000);
        }
        this.logHelper.log(msg);
    }

    private boolean checkFrame(DJIDecodeServer.InputFrame frame, int seiOffset) {
        this.checkFrameTime = System.nanoTime();
        this.invokeCount++;
        if (frame.checkFrameCrc <= 0) {
            return false;
        }
        if (frame.isKeyFrame || frame.checkIndex == this.lastCheckIndex + 1 || frame.checkIndex == 0) {
            this.lastCheckIndex = frame.checkIndex;
            this.frameHead = DJIVideoUtil.findFrameHead(frame.videoBuffer, 0, frame.size);
            this.frameLen = seiOffset - this.frameHead;
            if (this.frameLen != frame.checkFrameLen) {
                log2TestFile("checkFrame: len=" + this.frameLen + " checkLen=" + frame.checkFrameLen);
                return false;
            }
            this.checkSum = BytesUtil.calcPerByteSum(frame.videoBuffer, this.frameHead, this.frameLen);
            if (frame.checkFrameCrc == this.checkSum) {
                return true;
            }
            log2TestFile("checkFrame: checksum=" + this.checkSum + " checkLen=" + frame.checkFrameCrc);
            return false;
        }
        log2TestFile("checkFrame: lastId=" + this.lastCheckIndex + " id=" + frame.checkIndex);
        this.lastCheckIndex = frame.checkIndex;
        return false;
    }
}
