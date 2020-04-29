package com.dji.video.framing.internal.muxer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import java.io.IOException;
import java.nio.ByteBuffer;

public interface DJIMuxerInterface {
    int addTrack(MediaFormat mediaFormat);

    int getNumTrack();

    void init(String str) throws IOException;

    void release();

    void setOrientationHint(int i);

    void start();

    void stop();

    void writeSampleData(int i, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo, long j);
}
