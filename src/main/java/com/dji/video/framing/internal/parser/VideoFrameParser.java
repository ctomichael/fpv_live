package com.dji.video.framing.internal.parser;

import android.support.annotation.Keep;
import com.dji.video.framing.DJIVideoHEVCFomatManager;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.decoder.ErrorStatusManager;
import com.dji.video.framing.internal.parser.IFrameParser;
import dji.component.accountcenter.MemberInfo;

@Keep
public class VideoFrameParser implements IFrameParser {
    private static final byte BYTE_00 = ((byte) Integer.parseInt("00", 16));
    private static final byte BYTE_01 = ((byte) Integer.parseInt(MemberInfo.VIP_LEVEL_01, 16));
    private static final byte BYTE_264_1 = ((byte) Integer.parseInt("09", 16));
    private static final byte BYTE_264_2 = ((byte) Integer.parseInt("10", 16));
    private static final byte BYTE_265_1 = ((byte) Integer.parseInt("46", 16));
    private static final byte BYTE_265_2 = ((byte) Integer.parseInt(MemberInfo.VIP_LEVEL_01, 16));
    private static final String TAG = "VideoFrameParser";
    private IFrameParser.OnFrameParserListener listener;
    private int mCheckIndex = 0;
    private final boolean mIsHevcParser;
    private boolean mIsWrongFormat = true;
    private long nativePtr;

    public native void native_feed(long j, byte[] bArr, int i, int i2);

    public native void native_finish(long j);

    public native long native_init(boolean z);

    static {
        System.loadLibrary("video-framing");
    }

    public VideoFrameParser(boolean isHevcMode) {
        this.mIsHevcParser = isHevcMode;
        this.nativePtr = native_init(isHevcMode);
        VideoLog.w(TAG, "VideoFrameParser() ptr=" + this.nativePtr + " isHevcMode:" + isHevcMode, new Object[0]);
    }

    public synchronized void feedData(byte[] data, int offset, int len) {
        if (!DJIVideoHEVCFomatManager.getInstance().isPlaybackStatus() && data != null && data.length > 6) {
            this.mCheckIndex = 0;
            while (this.mCheckIndex < data.length - 1) {
                if (this.mCheckIndex > 3) {
                    if (data[this.mCheckIndex] == BYTE_264_1) {
                        if (data[this.mCheckIndex - 4] == BYTE_00 && data[this.mCheckIndex - 3] == BYTE_00 && data[this.mCheckIndex - 2] == BYTE_00 && data[this.mCheckIndex - 1] == BYTE_01 && data[this.mCheckIndex + 1] == BYTE_264_2) {
                            if (DJIVideoHEVCFomatManager.getInstance().checkVideoParserIsUnmatched(false)) {
                                VideoLog.w(TAG, "feedData() get 264 frame, while current is 265", new Object[0]);
                                this.mIsWrongFormat = true;
                            } else {
                                this.mIsWrongFormat = false;
                            }
                        }
                    } else if (data[this.mCheckIndex] == BYTE_265_1 && data[this.mCheckIndex - 4] == BYTE_00 && data[this.mCheckIndex - 3] == BYTE_00 && data[this.mCheckIndex - 2] == BYTE_00 && data[this.mCheckIndex - 1] == BYTE_01 && data[this.mCheckIndex + 1] == BYTE_265_2) {
                        if (DJIVideoHEVCFomatManager.getInstance().checkVideoParserIsUnmatched(true)) {
                            VideoLog.w(TAG, "feedData() get 265 frame, while current is 264", new Object[0]);
                            this.mIsWrongFormat = true;
                        } else {
                            this.mIsWrongFormat = false;
                        }
                    }
                }
                this.mCheckIndex++;
            }
        }
        if (!this.mIsWrongFormat || DJIVideoHEVCFomatManager.getInstance().isPlaybackStatus()) {
            native_feed(this.nativePtr, data, offset, len);
        } else if (ErrorStatusManager.getInstance().needCheckFrame()) {
            VideoLog.e(TAG, "feedData() wrong video format, need request I frame", new Object[0]);
            ErrorStatusManager.getInstance().onErrorStatusChange(true);
        }
    }

    public void stop() {
        native_finish(this.nativePtr);
    }

    public void setFrameParserListener(IFrameParser.OnFrameParserListener listener2) {
        this.listener = listener2;
    }

    public void resetCheckStatus() {
        VideoLog.w(TAG, "resetCheckStatus()", new Object[0]);
        if (!this.mIsWrongFormat) {
            this.mIsWrongFormat = true;
        }
    }

    public void JniDataRecvCallBack(byte[] videoBuffer, int size, boolean isKeyFrame, int width, int height, int frameNum, int spsPos, int spsLen, int ppsPos, int ppsLen, int fps) {
        VideoLog.d(TAG, "JniDataRecvCallBack() videoBuffer size:" + size + " isKeyFrame:" + isKeyFrame + " width:" + width + " height:" + height + " spsPos:" + spsPos + " spsLen:" + spsLen + " ppsPos:" + ppsPos + " ppsLen:" + ppsLen + " fps:" + fps + " IsHevcParser：" + this.mIsHevcParser, new Object[0]);
        this.listener.onRecvData(videoBuffer, size, isKeyFrame, width, height, frameNum, spsPos, spsLen, ppsPos, ppsLen, fps, this.mIsHevcParser);
    }
}
