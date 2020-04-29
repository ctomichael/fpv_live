package com.dji.video.framing.internal.recorder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.encoder.FullFrameHardwareTranscoder;
import com.dji.video.framing.internal.recorder.RecorderBase;
import com.dji.video.framing.utils.DJIVideoUtil;
import java.nio.ByteBuffer;
import java.util.Locale;

public class RecorderFullFrame extends RecorderBase implements FullFrameHardwareTranscoder.FullFrameTranscoderListener {
    private static final boolean DEBUG = false;
    public static String TAG = "RecorderFullFrame";
    private static RecorderFullFrame instance = null;
    private long initialOriginalTime = -1;
    private long initial_original_pts = -1;
    private long lastInputTime = -1;
    private long last_original_pts = -1;
    private long last_written_pts = -1;
    private boolean muxerInitialized = false;

    private void logToFile(String log) {
        log2File("fullframe:" + log);
    }

    public RecorderFullFrame(RecorderManager manager, DJIVideoDecoder decoder, String fileName) {
        super(manager, decoder, fileName);
        logToFile("create instance: " + this);
    }

    /* access modifiers changed from: protected */
    public String getTAG() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public int[] getRecordWidthHeight() {
        return FullFrameHardwareTranscoder.getInstance().getEncodeWidthHeight(getCurrentDecoder());
    }

    /* access modifiers changed from: protected */
    public void onStartRecord() {
        Log.d(TAG, "onStartRecord: 0");
        this.muxerInitialized = false;
        this.numFrameWritten = 0;
        FullFrameHardwareTranscoder.getInstance().setDecoder(getCurrentDecoder());
        FullFrameHardwareTranscoder.getInstance().setBitRate((int) (10 * RecorderManager.MB));
        FullFrameHardwareTranscoder.getInstance().setKeyFrameRate(30);
        FullFrameHardwareTranscoder.getInstance().setFrameInterval(DJIVideoUtil.getFPS(getCurrentDecoder()) == 60 ? 2 : 1);
        FullFrameHardwareTranscoder.getInstance().addListener(this);
    }

    /* access modifiers changed from: protected */
    public void onEndRecord() {
        Log.d(TAG, "onEndRecord: 0");
        FullFrameHardwareTranscoder.getInstance().removeListener(this);
        Log.i(TAG, "onEndRecord() completion");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.min(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.min(double, double):double}
      ClspMth{java.lang.Math.min(float, float):float}
      ClspMth{java.lang.Math.min(int, int):int}
      ClspMth{java.lang.Math.min(long, long):long} */
    public synchronized void onFrameInput(ByteBuffer buffer, MediaCodec.BufferInfo info, int width, int height, boolean isKeyFrame) {
        Log.d(TAG, "onFrameInput: wudi: width: " + width + ", height: " + height + ", is key: " + isKeyFrame + ", flags: " + info.flags + ", pts: " + info.presentationTimeUs);
        if (checkIsCurrentRecorder()) {
            logDataInput("fullframe frame input count=" + this.numFrameWritten, dji.midware.media.DJIVideoDecoder.connectLosedelay, RecorderBase.LogDataMode.Video);
            long time = System.currentTimeMillis();
            if (!this.muxerInitialized) {
                byte[] sps_array = FullFrameHardwareTranscoder.getInstance().getSps();
                byte[] pps_array = FullFrameHardwareTranscoder.getInstance().getPps();
                int mwidth = width;
                int mheight = height;
                if (sps_array == null || pps_array == null || mwidth == 0 || mheight == 0) {
                    logToFile("spsLen=" + (sps_array == null ? "null" : Integer.valueOf(sps_array.length)) + ", ppsLen=" + (pps_array == null ? "null" : Integer.valueOf(sps_array.length)) + ", wid=" + mwidth + ", hei=" + mheight);
                    Log.e(TAG, "failed to init muxer. sps or pps is null. width or height is 0");
                } else {
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
                    this.initialOriginalTime = time;
                    this.lastInputTime = 0;
                    this.last_written_pts = 0;
                    this.muxerInitialized = true;
                    logToFile("muxer inited");
                }
            }
            if (this.last_written_pts >= 0) {
                long j = 1000 * (time - this.lastInputTime);
                info.presentationTimeUs = this.last_written_pts + Math.min(41667L, Math.max(DJIVideoUtil.getDurationPerFrameUs(getCurrentDecoder()), (this.initial_original_pts + (1000 * (time - this.initialOriginalTime))) - this.last_written_pts));
            }
            this.lastInputTime = time;
            this.last_written_pts = info.presentationTimeUs;
            this.muxer.writeSampleData(0, buffer, info, 1);
            this.numFrameWritten++;
            VideoLog.i(TAG, String.format(Locale.US, "muxer write a frame. num=%d, size=%d, pts=%d, flags=%s (KEY=1 END=4)", Integer.valueOf(this.numFrameWritten), Integer.valueOf(info.size), Long.valueOf(info.presentationTimeUs), Integer.valueOf(info.flags)), new Object[0]);
        }
    }

    public void onFrameInput(byte[] buffer, MediaCodec.BufferInfo info, int width, int height, boolean isKeyFrame) {
    }
}
