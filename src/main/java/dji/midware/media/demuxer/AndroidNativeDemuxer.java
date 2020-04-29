package dji.midware.media.demuxer;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.MediaLogger;
import java.io.IOException;
import java.nio.ByteBuffer;

@EXClassNullAway
public class AndroidNativeDemuxer extends DemuxerBase implements DemuxerInterface {
    private static String TAG = "AndroidNativeDemuxer";
    MediaExtractor etx = new MediaExtractor();

    public AndroidNativeDemuxer() {
        MediaLogger.i(TAG, "create a AndroidNativeDemuxer");
    }

    public boolean advance() {
        return this.etx.advance();
    }

    public int getTrackCount() {
        return this.etx.getTrackCount();
    }

    public int getSampleFlags() {
        return this.etx.getSampleFlags();
    }

    public long getSampleTime() {
        return this.etx.getSampleTime();
    }

    public MediaFormat getTrackFormat(int index) {
        return this.etx.getTrackFormat(index);
    }

    public void setDataSource(String filePath) throws IOException {
        this.etx.setDataSource(filePath);
    }

    public int readSampleData(ByteBuffer byteBuf, int offset) {
        return this.etx.readSampleData(byteBuf, offset);
    }

    public void seekTo(long timeUs, int mode) {
        this.etx.seekTo(timeUs, mode);
    }

    public int getSampleTrackIndex() {
        return this.etx.getSampleTrackIndex();
    }

    public void selectTrack(int index) {
        this.etx.selectTrack(index);
    }

    public void unselectTrack(int index) {
        this.etx.unselectTrack(index);
    }

    public void release() {
        this.etx.release();
    }
}
