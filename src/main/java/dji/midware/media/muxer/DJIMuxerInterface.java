package dji.midware.media.muxer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import dji.fieldAnnotation.EXClassNullAway;
import java.io.IOException;
import java.nio.ByteBuffer;

@EXClassNullAway
public interface DJIMuxerInterface {
    int addTrack(MediaFormat mediaFormat);

    int getNumTrack();

    void init(String str) throws IOException;

    void release();

    void start();

    void stop();

    void writeSampleData(int i, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo, long j);
}
