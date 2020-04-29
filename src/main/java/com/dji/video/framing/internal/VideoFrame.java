package com.dji.video.framing.internal;

import android.support.v4.util.Pools;
import com.dji.video.framing.internal.decoder.common.FrameFovType;

public class VideoFrame {
    public static final int POOL_SIZE = 120;
    private static final Pools.SynchronizedPool<VideoFrame> sPool = new Pools.SynchronizedPool<>(120);
    public long checkFrameCrc = -1;
    public int checkFrameLen = -1;
    public int checkIndex = -1;
    public boolean checkIsKeyFrame = false;
    public long codecOutputTime;
    public byte[] data;
    public long fedIntoCodecTime;
    public FrameFovType fovType = FrameFovType.Unknown;
    public int fps;
    public long frameIndex = -1;
    public int frameNum = -1;
    public int height;
    public long incomingTimeMs;
    public boolean isHevcFrame;
    public boolean isKeyFrame;
    public int ppsLen;
    public int ppsPos = -1;
    public long pts;
    public int size;
    public int spsLen;
    public int spsPos = -1;
    public int timeStamp = -1;
    public int width;
    public int zoomIndex = -1;

    private VideoFrame(byte[] data2, int size2, long pts2, boolean isKeyFrame2, int width2, int height2, int frameNum2, long frameIndex2, int spsPos2, int spsLen2, int ppsPos2, int ppsLen2, int fps2, boolean isHevcFrame2) {
        update(data2, size2, pts2, isKeyFrame2, width2, height2, frameNum2, frameIndex2, spsPos2, spsLen2, ppsPos2, ppsLen2, fps2, isHevcFrame2);
    }

    private void update(byte[] data2, int size2, long pts2, boolean isKeyFrame2, int width2, int height2, int frameNum2, long frameIndex2, int spsPos2, int spsLen2, int ppsPos2, int ppsLen2, int fps2, boolean isHevcFrame2) {
        this.data = data2;
        this.size = size2;
        this.pts = pts2;
        this.isKeyFrame = isKeyFrame2;
        this.width = width2;
        this.height = height2;
        this.frameNum = frameNum2;
        this.frameIndex = frameIndex2;
        this.spsPos = spsPos2;
        this.spsLen = spsLen2;
        this.ppsPos = ppsPos2;
        this.ppsLen = ppsLen2;
        this.fps = fps2;
        this.isHevcFrame = isHevcFrame2;
        this.fovType = FrameFovType.Unknown;
        this.zoomIndex = -1;
        this.timeStamp = -1;
        this.checkIndex = -1;
        this.checkFrameLen = -1;
        this.checkFrameCrc = -1;
        this.checkIsKeyFrame = false;
        this.incomingTimeMs = 0;
        this.fedIntoCodecTime = 0;
        this.codecOutputTime = 0;
    }

    public static VideoFrame obtain(byte[] data2, int size2, long pts2, boolean isKeyFrame2, int width2, int height2, int frameNum2, long frameIndex2, int spsPos2, int spsLen2, int ppsPos2, int ppsLen2, int fps2, boolean isHevcFrame2) {
        VideoFrame instance = sPool.acquire();
        if (instance == null) {
            return new VideoFrame(data2, size2, pts2, isKeyFrame2, width2, height2, frameNum2, frameIndex2, spsPos2, spsLen2, ppsPos2, ppsLen2, fps2, isHevcFrame2);
        }
        instance.update(data2, size2, pts2, isKeyFrame2, width2, height2, frameNum2, frameIndex2, spsPos2, spsLen2, ppsPos2, ppsLen2, fps2, isHevcFrame2);
        return instance;
    }

    public void recycle() {
        sPool.release(this);
    }
}
