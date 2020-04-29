package com.dji.video.framing.internal.decoder;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dji.video.framing.DJIVideoHEVCFomatManager;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.VideoFrame;
import com.dji.video.framing.internal.decoder.common.FrameFovType;
import com.dji.video.framing.utils.BytesUtil;
import com.dji.video.framing.utils.DJIVideoUtil;

public class DJIDecodeClient {
    private static final int FILE_LOG_ERROR_STATUS_INTERVAL = 5000;
    private static final int FILE_LOG_INTERVAL = 2000;
    private static final int MAX_PARSE_SEI_LENGTH = 64;
    private static final String TAG = "DJIDecodeClient";
    private long checkSum;
    private int frameHead;
    private int frameLen;
    private HandlerThread inoutThread;
    private int lastCheckIndex = -1;
    private long lastLogErrorStatusTime = 0;
    @NonNull
    private DJIVideoDecoder mDjiVideoDecoder;
    @Nullable
    AbsDjiDecodeServer server;

    public DJIDecodeClient(@NonNull DJIVideoDecoder decoder) {
        this.mDjiVideoDecoder = decoder;
    }

    @SuppressLint({"NewApi"})
    public synchronized void stopServer() {
        if (this.inoutThread != null && this.inoutThread.isAlive()) {
            if (this.server != null) {
                this.server.removeCallbacksAndMessages(null);
            }
            this.inoutThread.quitSafely();
            try {
                this.inoutThread.join(3000);
            } catch (InterruptedException e) {
                VideoLog.e(TAG, e);
            }
            if (this.server != null) {
                this.server.releaseDecoder();
                this.server = null;
            }
        }
        return;
    }

    public synchronized void resetKeyFrame() {
        if (!(this.inoutThread == null || !this.inoutThread.isAlive() || this.server == null)) {
            this.server.sendEmptyMessage(10);
        }
    }

    public synchronized void startServer() {
        if (this.inoutThread == null || !this.inoutThread.isAlive()) {
            this.inoutThread = new HandlerThread("DJIDecodeInoutHandlerThread", -1);
            this.inoutThread.start();
            if (Build.VERSION.SDK_INT < 24 || this.mDjiVideoDecoder.needLowFrequencyForSmoothing()) {
                this.server = new DJIDecodeServerImplApi24(this.inoutThread.getLooper(), this.mDjiVideoDecoder);
            } else {
                this.server = new DJIDecodeServerImpl(this.inoutThread.getLooper(), this.mDjiVideoDecoder);
            }
            this.server.sendEmptyMessageDelayed(2, 1);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void queueInFrame(VideoFrame frame) {
        int seiOffset;
        VideoLog.i(TAG, "cline on queuein before checking inoutThread status", new Object[0]);
        if (this.inoutThread == null || !this.inoutThread.isAlive()) {
            DJIVideoDecoder.log2File("dec thread dead");
        } else {
            VideoLog.i(TAG, "cline on queuein after checking inoutThread status", new Object[0]);
            if (this.server != null) {
                if (DJIVideoHEVCFomatManager.getInstance().isInHevcMode()) {
                    seiOffset = parseHevcSei(frame, true, 64);
                } else {
                    seiOffset = parseSei(frame, true, 64);
                }
                if (ErrorStatusManager.getInstance().isDemandI()) {
                    if (!checkFrame(frame, seiOffset)) {
                        ErrorStatusManager.getInstance().onErrorStatusChange(true);
                    } else if (frame.isKeyFrame) {
                        ErrorStatusManager.getInstance().onErrorStatusChange(false);
                    } else if (ErrorStatusManager.getInstance().getErrorStatus()) {
                        ErrorStatusManager.getInstance().onErrorStatusChange(true);
                    }
                    long time = System.currentTimeMillis();
                    if (time - this.lastLogErrorStatusTime > 5000) {
                        this.lastLogErrorStatusTime = time;
                    }
                }
                if (!ErrorStatusManager.getInstance().needCheckFrame() || !ErrorStatusManager.getInstance().getErrorStatus()) {
                    VideoLog.d(TAG, "queueInFrame: frameNum=" + frame.frameNum, new Object[0]);
                    this.server.obtainMessage(1, frame).sendToTarget();
                } else {
                    frame.recycle();
                }
            } else {
                DJIVideoDecoder.log2File("dec server null");
            }
        }
    }

    private int parseSei(VideoFrame frame, boolean isFromTail, int maxCheckLength) {
        if (frame == null || frame.data == null) {
            return -1;
        }
        int rst = -1;
        int start = isFromTail ? frame.data.length - maxCheckLength : 0;
        int curIndex = start;
        while (true) {
            int curIndex2 = DJIVideoUtil.findNextNal(frame.data, curIndex, (maxCheckLength - curIndex) + start);
            if (curIndex2 <= 0 || curIndex2 + 4 >= frame.data.length) {
                return rst;
            }
            if ((frame.data[curIndex2 + 3] & 31) != DJIVideoUtil.NalType.Sei.value() || curIndex2 + 5 >= frame.data.length) {
                curIndex = curIndex2 + 4;
            } else {
                if (rst < 0) {
                    rst = curIndex2;
                }
                onSeiGet(frame, frame.data[curIndex2 + 4] & 255, curIndex2 + 5);
                curIndex = curIndex2 + 6;
            }
        }
    }

    private int parseHevcSei(VideoFrame frame, boolean isFromTail, int maxCheckLength) {
        int start;
        boolean isHevcSeiNal;
        if (frame == null || frame.data == null) {
            return -1;
        }
        int rst = -1;
        if (isFromTail) {
            start = frame.data.length - maxCheckLength;
        } else {
            start = 0;
        }
        int curIndex = start;
        while (true) {
            int curIndex2 = DJIVideoUtil.findNextNal(frame.data, curIndex, (maxCheckLength - curIndex) + start);
            if (curIndex2 < 0 || curIndex2 + 4 >= frame.data.length) {
                return rst;
            }
            int nalType = (frame.data[curIndex2 + 3] >> 1) & 63;
            if (nalType == DJIVideoUtil.HEVCNalType.NAL_SEI_PREFIX.value() || nalType == DJIVideoUtil.HEVCNalType.NAL_SEI_SUFFIX.value()) {
                isHevcSeiNal = true;
            } else {
                isHevcSeiNal = false;
            }
            if (!isHevcSeiNal || curIndex2 + 5 >= frame.data.length) {
                curIndex = curIndex2 + 5;
            } else {
                if (rst < 0) {
                    rst = curIndex2;
                }
                onSeiGet(frame, frame.data[curIndex2 + 5] & 255, curIndex2 + 6);
                curIndex = curIndex2 + 7;
            }
        }
    }

    private void onSeiGet(VideoFrame frame, int type, int dataOffset) {
        boolean z = true;
        if (frame != null) {
            switch (type) {
                case 31:
                    if (dataOffset + 5 < frame.data.length) {
                        frame.timeStamp = BytesUtil.getIntFromBytes(frame.data, dataOffset + 1, dataOffset + 2, dataOffset + 4, dataOffset + 5);
                        frame.checkIndex = BytesUtil.getIntFromBytes(frame.data, dataOffset + 7, dataOffset + 8);
                        return;
                    }
                    return;
                case 32:
                    if (dataOffset + 1 < frame.data.length) {
                        frame.zoomIndex = frame.data[dataOffset + 1] & 255;
                        return;
                    }
                    return;
                case 239:
                    if (dataOffset + 22 < frame.data.length) {
                        frame.timeStamp = BytesUtil.getIntFromBytes(frame.data, dataOffset + 1, dataOffset + 2, dataOffset + 4, dataOffset + 5);
                        return;
                    }
                    return;
                case 240:
                    if (dataOffset + 22 < frame.data.length) {
                        frame.checkIndex = BytesUtil.getIntFromBytes(frame.data, dataOffset + 1, dataOffset + 2, dataOffset + 4);
                        frame.checkFrameLen = BytesUtil.getIntFromBytes(frame.data, dataOffset + 5, dataOffset + 7, dataOffset + 8);
                        frame.checkFrameCrc = BytesUtil.getLongFromBytes(frame.data, dataOffset + 10, dataOffset + 11, dataOffset + 13, dataOffset + 14);
                        frame.timeStamp = BytesUtil.getIntFromBytes(frame.data, dataOffset + 16, dataOffset + 17, dataOffset + 19, dataOffset + 20);
                        if (dataOffset + 24 < frame.data.length) {
                            if ((frame.data[dataOffset + 24] & 1) == 0) {
                                z = false;
                            }
                            frame.checkIsKeyFrame = z;
                        }
                        frame.fovType = FrameFovType.find((frame.data[dataOffset + 22] >> 6) & 3);
                        frame.zoomIndex = frame.data[dataOffset + 22] & 63;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private boolean checkFrame(VideoFrame frame, int seiOffset) {
        if (!ErrorStatusManager.getInstance().needCheckFrame()) {
            return true;
        }
        if (frame.checkFrameCrc <= 0) {
            return false;
        }
        if (frame.isKeyFrame || this.lastCheckIndex == -1 || frame.checkIndex == this.lastCheckIndex + 1 || frame.checkIndex == 0) {
            this.lastCheckIndex = frame.checkIndex;
            if (DJIVideoHEVCFomatManager.getInstance().isInHevcMode()) {
                this.frameHead = DJIVideoUtil.findHevcFrameHead(frame.data, 0, frame.size);
            } else {
                this.frameHead = DJIVideoUtil.findFrameHead(frame.data, 0, frame.size);
            }
            this.frameLen = seiOffset - this.frameHead;
            if (this.frameLen != frame.checkFrameLen) {
                VideoLog.e(TAG, "checkFrame()  len=" + this.frameLen + " checkLen=" + frame.checkFrameLen, new Object[0]);
                return false;
            }
            this.checkSum = BytesUtil.calcPerByteSum(frame.data, this.frameHead, this.frameLen);
            if (frame.checkFrameCrc == this.checkSum) {
                return true;
            }
            VideoLog.e(TAG, "checkFrame()  checksum=" + this.checkSum + " checkLen=" + frame.checkFrameCrc, new Object[0]);
            return false;
        }
        VideoLog.e(TAG, "checkFrame() lastId=" + this.lastCheckIndex + " id=" + frame.checkIndex, new Object[0]);
        this.lastCheckIndex = frame.checkIndex;
        return false;
    }
}
