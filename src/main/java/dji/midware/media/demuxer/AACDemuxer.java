package dji.midware.media.demuxer;

import android.media.MediaFormat;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.DJIAudioUtil;
import dji.midware.media.MediaLogger;
import dji.midware.util.BytesUtil;
import dji.thirdparty.afinal.core.Arrays;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.msgpack.core.MessagePack;

@EXClassNullAway
public class AACDemuxer implements DemuxerInterface {
    private static boolean DEBUG = false;
    private static final String TAG = "AACDemuxer";
    private byte[] buffer = new byte[DJIAudioUtil.AAC_PACKET_MAX_SIZE];
    private boolean eos = false;
    private BufferedInputStream file = null;
    private int frameIndex = 0;
    private int frameSize = 0;

    public boolean advance() {
        if (this.eos) {
            return false;
        }
        try {
            this.file.read(this.buffer, 0, 7);
            MediaLogger.i(DEBUG, TAG, "demuxer head=" + BytesUtil.byte2hex(Arrays.copyOfRange(this.buffer, 0, 7)));
            if ((this.buffer[0] & 255) == 255 && (this.buffer[1] & 240) == 240) {
                this.frameSize = ((this.buffer[3] & 3) << 11) | ((this.buffer[4] & 255) << 3) | ((this.buffer[5] & MessagePack.Code.NEGFIXINT_PREFIX) >>> 5);
                MediaLogger.i(DEBUG, TAG, "demuxer frameSize=" + this.frameSize);
                int actualRead = this.file.read(this.buffer, 7, this.frameSize - 7);
                if (actualRead == -1) {
                    this.eos = true;
                    return false;
                } else if (actualRead != this.frameSize - 7) {
                    throw new RuntimeException("error. expected reading=" + (this.frameSize - 7) + " actual reading=" + actualRead);
                } else {
                    this.frameIndex++;
                    return true;
                }
            } else {
                throw new RuntimeException("error");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getTrackCount() {
        return 0;
    }

    public int getSampleFlags() {
        return 0;
    }

    public long getSampleTime() {
        return 0;
    }

    public MediaFormat getTrackFormat(int index) {
        return null;
    }

    public void setDataSource(String filePath) throws IOException {
        this.file = new BufferedInputStream(new FileInputStream(filePath));
        if (this.file != null) {
            advance();
        }
    }

    public int readSampleData(ByteBuffer byteBuf, int offset) {
        byteBuf.clear();
        byteBuf.position(offset);
        byteBuf.put(this.buffer, 0, this.frameSize);
        return this.frameSize;
    }

    public void seekTo(long timeUs, int mode) {
    }

    public int getSampleTrackIndex() {
        return 0;
    }

    public void selectTrack(int index) {
    }

    public void unselectTrack(int index) {
    }

    public void release() {
        try {
            this.file.close();
        } catch (Exception e) {
        }
        this.file = null;
    }

    public int getAudioTrack() {
        return 0;
    }

    public int getVideoTrack() {
        return 0;
    }
}
