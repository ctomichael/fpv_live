package com.dji.video.framing.internal.recorder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.util.Log;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.VideoFrame;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.decoder.decoderinterface.IVideoRecordDataListener;
import com.dji.video.framing.internal.recorder.RecorderBase;
import com.dji.video.framing.utils.DJIVideoUtil;
import java.nio.ByteBuffer;
import java.util.Locale;

public class RecorderMp4 extends RecorderBase implements IVideoRecordDataListener {
    private static final boolean DEBUG = false;
    public static String TAG = "RecorderMp4";
    private static MediaRecorder recorder;
    private long initial_original_pts = -1;
    private long lastVideoPts = -1;
    private boolean muxerInitialized = false;

    private void logToFile(String log) {
        log2File("mp4:" + log);
    }

    public RecorderMp4(RecorderManager manager, DJIVideoDecoder decoder, String fileName) {
        super(manager, decoder, fileName);
        logToFile("create instance: " + this);
    }

    /* access modifiers changed from: protected */
    public String getTAG() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public void onStartRecord() {
        this.muxerInitialized = false;
        this.numFrameWritten = 0;
        getCurrentDecoder().addRecordDataListener(this);
    }

    /* access modifiers changed from: protected */
    public void onEndRecord() {
        getCurrentDecoder().removeRecordDataListener(this);
        Log.i(TAG, "onEndRecord() completion");
    }

    public void onVideoFrameInput(VideoFrame frame) {
        if (checkIsCurrentRecorder()) {
            logDataInput("mp4 frame input count=" + this.numFrameWritten, dji.midware.media.DJIVideoDecoder.connectLosedelay, RecorderBase.LogDataMode.Video);
            Log.d(TAG, "onVideoFrameInput: frame=" + frame);
            if (!this.muxerInitialized) {
                byte[] sps_array = getCurrentDecoder().mSpsHeader;
                byte[] pps_array = getCurrentDecoder().mPpsHeader;
                int mwidth = getCurrentDecoder().mOutPutWidth;
                int mheight = getCurrentDecoder().mOutputHeight;
                if (sps_array == null || pps_array == null || mwidth == 0 || mheight == 0) {
                    Log.e(TAG, "failed to init muxer. sps or pps is null. width or height is 0");
                    logToFile("spsLen=" + (sps_array == null ? "null" : Integer.valueOf(sps_array.length)) + ", ppsLen=" + (pps_array == null ? "null" : Integer.valueOf(sps_array.length)) + ", wid=" + mwidth + ", hei=" + mheight);
                    return;
                }
                MediaFormat format = MediaFormat.createVideoFormat(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0], mwidth, mheight);
                format.setInteger("frame-rate", 30);
                format.setByteBuffer("csd-0", ByteBuffer.wrap(sps_array));
                format.setByteBuffer("csd-1", ByteBuffer.wrap(pps_array));
                this.muxer.setOrientationHint(0);
                this.muxer.addTrack(format);
                this.muxer.start();
                if (this.manager.getCallback() != null && this.manager.isCurrentRecorder(this)) {
                    this.manager.getCallback().onMuxerStarted();
                }
                Log.i(TAG, "muxer has added a track");
                this.initial_original_pts = 0;
                this.lastVideoPts = -1;
                this.muxerInitialized = true;
                logToFile("muxer inited");
            }
            ByteBuffer buffer = ByteBuffer.wrap(frame.data);
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            info.offset = 0;
            info.size = frame.size;
            info.flags = frame.isKeyFrame ? 1 : 0;
            info.presentationTimeUs = this.lastVideoPts < 0 ? 0 : this.lastVideoPts + DJIVideoUtil.getDurationPerFrameUs(getCurrentDecoder());
            this.lastVideoPts = info.presentationTimeUs;
            this.muxer.writeSampleData(0, buffer, info, 1);
            this.numFrameWritten++;
            VideoLog.i(TAG, String.format(Locale.US, "muxer write a frame. num=%d, size=%d, pts=%d, flags=%s (KEY=1 END=4)", Integer.valueOf(this.numFrameWritten), Integer.valueOf(info.size), Long.valueOf(info.presentationTimeUs), Integer.valueOf(info.flags)), new Object[0]);
        }
    }
}
