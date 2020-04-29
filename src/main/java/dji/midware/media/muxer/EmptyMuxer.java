package dji.midware.media.muxer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.MediaLogger;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

@EXClassNullAway
public class EmptyMuxer implements DJIMuxerInterface {
    private static final int FlushDuration = 15;
    private static final String TAG = "EmptyMuxer";
    private BufferedOutputStream h264BufferedOutputStream = null;
    private OutputStream h264OutputStream = null;
    private int numFrameWritten;

    public void init(String filePath) throws IOException {
        this.numFrameWritten = 0;
        this.h264OutputStream = new FileOutputStream(filePath);
        if (this.h264OutputStream != null) {
            this.h264BufferedOutputStream = new BufferedOutputStream(this.h264OutputStream);
            Log.i(TAG, "An H264 File has been opened");
            return;
        }
        Log.e(TAG, "error in creating H264 File");
    }

    public int getNumTrack() {
        return 0;
    }

    public int addTrack(MediaFormat format) {
        return 0;
    }

    public void release() {
    }

    public void start() {
    }

    public void stop() {
        try {
            if (this.h264BufferedOutputStream != null) {
                this.h264BufferedOutputStream.flush();
                this.h264BufferedOutputStream.close();
                this.h264BufferedOutputStream = null;
            }
            if (this.h264OutputStream != null) {
                this.h264OutputStream.flush();
                this.h264OutputStream.close();
                this.h264OutputStream = null;
            }
            Log.i(TAG, "H264 file has been closed");
        } catch (Exception e) {
            Log.e(TAG, "error when closing H264 file");
            e.printStackTrace();
        }
    }

    public void writeSampleData(int trackIndex, ByteBuffer byteBuf, MediaCodec.BufferInfo bufferInfo, long durationUs) {
        try {
            this.h264BufferedOutputStream.write(byteBuf.array(), bufferInfo.offset, bufferInfo.size);
            this.numFrameWritten++;
            if (this.numFrameWritten % 15 == 0) {
                this.h264BufferedOutputStream.flush();
            }
        } catch (IOException e) {
            MediaLogger.e(TAG, e);
        }
    }
}
