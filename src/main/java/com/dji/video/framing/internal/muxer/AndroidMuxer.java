package com.dji.video.framing.internal.muxer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import java.io.IOException;
import java.nio.ByteBuffer;

public class AndroidMuxer implements DJIMuxerInterface {
    private static final String TAG = "AndroidMuxer";
    private MediaMuxer muxer;
    private int numTrack = 0;

    public int addTrack(MediaFormat format) {
        int rst = -1;
        try {
            rst = this.muxer.addTrack(format);
            this.numTrack++;
            return rst;
        } catch (IllegalStateException e) {
            Log.e(TAG, "addTrack: ", e);
            return rst;
        }
    }

    public int getNumTrack() {
        return this.numTrack;
    }

    public void release() {
        this.muxer.release();
    }

    public void start() {
        try {
            this.muxer.start();
        } catch (IllegalStateException e) {
            Log.e(TAG, "start: ", e);
        }
    }

    public void stop() {
        try {
            this.muxer.stop();
        } catch (IllegalStateException e) {
            Log.e(TAG, "stop: ", e);
        }
    }

    public void setOrientationHint(int degree) {
        this.muxer.setOrientationHint(degree);
    }

    public void writeSampleData(int trackIndex, ByteBuffer byteBuf, MediaCodec.BufferInfo bufferInfo, long durationUs) {
        try {
            this.muxer.writeSampleData(trackIndex, byteBuf, bufferInfo);
        } catch (IllegalStateException e) {
            Log.e(TAG, "writeSampleData: ", e);
        }
    }

    public void init(String filePath) throws IOException {
        this.muxer = new MediaMuxer(filePath, 0);
    }
}
