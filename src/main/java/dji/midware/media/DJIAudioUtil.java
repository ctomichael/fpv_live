package dji.midware.media;

import com.drew.metadata.photoshop.PhotoshopDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import java.nio.ByteBuffer;

@EXClassNullAway
public class DJIAudioUtil {
    public static final int AAC_FRAME_MAX_SIZE = 20480;
    public static final int AAC_PACKET_MAX_SIZE = 102400;
    private static final int[] samplingFreq = {96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050, 16000, 12000, 11025, PhotoshopDirectory.TAG_LIGHTROOM_WORKFLOW};

    public enum AVSampleFormat {
        AV_SAMPLE_FMT_U8,
        AV_SAMPLE_FMT_S16,
        AV_SAMPLE_FMT_S32,
        AV_SAMPLE_FMT_FLT,
        AV_SAMPLE_FMT_DBL,
        AV_SAMPLE_FMT_U8P,
        AV_SAMPLE_FMT_S16P,
        AV_SAMPLE_FMT_S32P,
        AV_SAMPLE_FMT_FLTP,
        AV_SAMPLE_FMT_DBLP,
        AV_SAMPLE_FMT_NB
    }

    public static ByteBuffer makeAudioCsd0(int aac_profile, int sampleRate, int channelCount) {
        int sampleRateIndex = -1;
        int i = 0;
        while (true) {
            if (i >= samplingFreq.length) {
                break;
            } else if (sampleRate == samplingFreq[i]) {
                sampleRateIndex = i;
                break;
            } else {
                i++;
            }
        }
        return ByteBuffer.wrap(new byte[]{(byte) (((aac_profile + 1) << 3) | (sampleRateIndex >> 1)), (byte) (((sampleRateIndex << 7) & 128) | ((channelCount & 15) << 3))});
    }
}
