package dji.midware.media.demuxer;

import android.media.MediaFormat;
import dji.fieldAnnotation.EXClassNullAway;
import java.io.IOException;
import java.nio.ByteBuffer;

@EXClassNullAway
public interface DemuxerInterface {
    boolean advance();

    int getAudioTrack();

    int getSampleFlags();

    long getSampleTime();

    int getSampleTrackIndex();

    int getTrackCount();

    MediaFormat getTrackFormat(int i);

    int getVideoTrack();

    int readSampleData(ByteBuffer byteBuffer, int i);

    void release();

    void seekTo(long j, int i);

    void selectTrack(int i);

    void setDataSource(String str) throws IOException;

    void unselectTrack(int i);
}
