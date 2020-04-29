package dji.midware.media.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;
import com.drew.metadata.photoshop.PhotoshopDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.util.BytesUtil;
import dji.thirdparty.afinal.core.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.msgpack.core.MessagePack;

@EXClassNullAway
public class DJIAudioStreamPlayer implements AudioManager.OnAudioFocusChangeListener {
    private static final int Buffer_Size = 16384;
    private static final boolean DEBUG = false;
    private static final int Max_AAC_Frame_Size = 4096;
    private static final int ReadOutputTryMaxTimes = 3;
    private static final int ReadOutputWaitDurationUsEachTime = 5;
    private static final String TAG = "DJIAudioStreamPlayer";
    private static final int[] samplingFreq = {96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050, 16000, 12000, 11025, PhotoshopDirectory.TAG_LIGHTROOM_WORKFLOW};
    private static final boolean saveFileToggle = false;
    private AudioTrack at = null;
    AudioManager audioManager = null;
    private File dataFile = new File("/sdcard/mydjiaudio.aac");
    private MediaCodec decoder = null;
    private boolean initialized = false;
    private boolean isPlay = true;
    private MediaCodec.BufferInfo outputBufferInfo = new MediaCodec.BufferInfo();
    private short[] outputShortArray = new short[4096];
    private FileOutputStream outputStream = null;
    private PacketFormat packetFormat;

    public enum PacketFormat {
        ADTS,
        AAC,
        PCM
    }

    public DJIAudioStreamPlayer(PacketFormat packetFormat2) {
        this.packetFormat = packetFormat2;
    }

    public DJIAudioStreamPlayer(PacketFormat packetFormat2, Context context) {
        this.packetFormat = packetFormat2;
        this.audioManager = (AudioManager) context.getSystemService("audio");
        requestFocus();
        try {
            this.outputStream = new FileOutputStream(this.dataFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        if (this.at != null) {
            this.at.pause();
            this.at.flush();
            this.at.release();
            this.at = null;
        }
        if (this.decoder != null) {
            this.decoder.release();
            this.decoder = null;
        }
        if (this.outputStream != null) {
            try {
                this.outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        abandonFocus();
        this.outputShortArray = null;
    }

    public void init() {
    }

    public void init(int sampleRateHz, int channelCount, int aac_profile) {
        MediaFormat decoderFormat = MediaFormat.createAudioFormat(DJIVideoUtil.AUDIO_ENCODING_FORMAT[0], sampleRateHz, channelCount);
        int sampleRateIndex = -1;
        int i = 0;
        while (true) {
            if (i >= samplingFreq.length) {
                break;
            } else if (sampleRateHz == samplingFreq[i]) {
                sampleRateIndex = i;
                break;
            } else {
                i++;
            }
        }
        if (sampleRateIndex == -1) {
            throw new RuntimeException("unsupported sample rate");
        }
        decoderFormat.setByteBuffer("csd-0", ByteBuffer.wrap(new byte[]{(byte) (((aac_profile + 1) << 3) | (sampleRateIndex >> 1)), (byte) (((sampleRateIndex << 7) & 128) | ((channelCount & 15) << 3))}));
        init(decoderFormat);
    }

    public void init(MediaFormat format) {
        if (!this.initialized) {
            this.initialized = true;
            Integer sampleRate = Integer.valueOf(format.getInteger("sample-rate"));
            if (sampleRate == null) {
                throw new RuntimeException("should set sampleRate");
            }
            Integer channelCount = Integer.valueOf(format.getInteger("channel-count"));
            if (channelCount == null) {
                throw new RuntimeException("should set channelCount");
            }
            ByteBuffer csd_0 = format.getByteBuffer("csd-0");
            if (csd_0 == null) {
                throw new RuntimeException("should set csd-0");
            }
            MediaLogger.i(false, TAG, "csd-0=" + BytesUtil.byte2hex(Arrays.copyOfRange(csd_0.array(), csd_0.arrayOffset(), csd_0.limit() - csd_0.position())));
            try {
                this.decoder = MediaCodec.createDecoderByType(format.getString("mime"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            MediaFormat decoderFormat = MediaFormat.createAudioFormat(format.getString("mime"), sampleRate.intValue(), channelCount.intValue());
            decoderFormat.setByteBuffer("csd-0", csd_0);
            this.decoder.configure(decoderFormat, (Surface) null, (MediaCrypto) null, 0);
            this.decoder.start();
            this.at = new AudioTrack(3, sampleRate.intValue(), 12, 2, 16384, 1);
            this.at.play();
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, boolean):void}
     arg types: [int, int]
     candidates:
      ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, long):void}
      ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, boolean):void} */
    public void onFrameReceive(ByteBuffer aacInput, int offset, int size, long ptsUs) {
        MediaLogger.i(true, TAG, "test 1 ptsUs=" + ((1000 * ptsUs) / 90));
        if (aacInput.capacity() < offset + size) {
            throw new RuntimeException("invalid offset and size. offset=" + offset + " size=" + size + " capacity=" + aacInput.capacity());
        }
        aacInput.clear();
        if (!this.initialized) {
            if (this.packetFormat != PacketFormat.ADTS) {
                throw new RuntimeException("should call prepare() with non-empty parameters if the stream is raw ACC instead of in ADTS format");
            } else if (size < 7) {
                throw new RuntimeException("invalid ADTS head");
            } else {
                byte[] head = new byte[7];
                aacInput.position(offset);
                aacInput.get(head);
                MediaLogger.i(false, TAG, "Frame head=" + BytesUtil.byte2hex(head));
                int profile = (head[2] & MessagePack.Code.NIL) >>> 6;
                int sampleRate = samplingFreq[(head[2] & 60) >> 2];
                int channel = ((head[2] & 1) << 2) | ((head[3] & MessagePack.Code.NIL) >>> 6);
                MediaLogger.i(false, TAG, "profile=" + profile + " sampleRate=" + sampleRate + " channel=" + channel);
                init(sampleRate, channel, profile);
            }
        }
        boolean crc = false;
        if (this.packetFormat == PacketFormat.ADTS) {
            if (size < 7) {
                throw new RuntimeException("invalid ADTS head");
            }
            byte[] head2 = new byte[7];
            aacInput.position(offset);
            aacInput.get(head2);
            crc = (head2[1] & 1) == 0;
            MediaLogger.i(false, TAG, "CRC=" + crc);
        }
        int inputIndex = this.decoder.dequeueInputBuffer(0);
        if (inputIndex >= 0) {
            ByteBuffer decoderInputBuffer = this.decoder.getInputBuffers()[inputIndex];
            decoderInputBuffer.clear();
            int sentSize = size;
            if (this.packetFormat == PacketFormat.ADTS) {
                sentSize -= crc ? 9 : 7;
                aacInput.position((crc ? 9 : 7) + offset);
            } else {
                aacInput.position(offset);
            }
            aacInput.limit(offset + size);
            decoderInputBuffer.put(aacInput);
            this.decoder.queueInputBuffer(inputIndex, 0, sentSize, (1000 * ptsUs) / 90, 1);
            MediaLogger.i(true, TAG, "test 2");
            int outputIndex = -1;
            for (int i = 0; i < 3 && outputIndex < 0; i++) {
                outputIndex = this.decoder.dequeueOutputBuffer(this.outputBufferInfo, 5);
            }
            MediaLogger.i(true, TAG, "test 3");
            while (outputIndex >= 0) {
                MediaLogger.i(false, TAG, "decoder outputs " + this.outputBufferInfo.size + " bytes");
                if (this.outputBufferInfo.size == 0) {
                    this.decoder.releaseOutputBuffer(outputIndex, false);
                } else {
                    ByteBuffer outBuffer = this.decoder.getOutputBuffers()[outputIndex];
                    outBuffer.position(this.outputBufferInfo.offset);
                    outBuffer.limit(this.outputBufferInfo.offset + this.outputBufferInfo.size);
                    int sizeInShorts = this.outputBufferInfo.size / 2;
                    outBuffer.asShortBuffer().get(this.outputShortArray, 0, sizeInShorts);
                    this.decoder.releaseOutputBuffer(outputIndex, false);
                    if (this.isPlay) {
                        MediaLogger.i(true, TAG, "write " + this.at.write(this.outputShortArray, 0, sizeInShorts) + " shorts to the audio sink. Taken Time=" + (System.currentTimeMillis() - System.currentTimeMillis()));
                    }
                    outputIndex = this.decoder.dequeueOutputBuffer(this.outputBufferInfo, 0);
                }
            }
            MediaLogger.i(true, TAG, "test 4");
        }
    }

    public void setVolume(double left, double right) {
    }

    public boolean requestFocus() {
        boolean re = true;
        if (this.audioManager == null) {
            return false;
        }
        if (1 != this.audioManager.requestAudioFocus(this, 3, 1)) {
            re = false;
        }
        setPlaying(re);
        return re;
    }

    public boolean abandonFocus() {
        boolean re = true;
        if (this.audioManager == null) {
            return false;
        }
        if (1 != this.audioManager.abandonAudioFocus(this)) {
            re = false;
        }
        setPlaying(re);
        return re;
    }

    public void setPlaying(boolean isPlaying) {
        if (isPlaying) {
            this.isPlay = true;
        } else {
            this.isPlay = false;
        }
    }

    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case -3:
                MediaLogger.e(TAG, "audio focus: loss transient can duck");
                setVolume(0.10000000149011612d, 0.10000000149011612d);
                return;
            case -2:
                MediaLogger.e(TAG, "audio focus: loss transient");
                return;
            case -1:
                MediaLogger.e(TAG, "audio focus: loss");
                return;
            case 0:
            default:
                return;
            case 1:
                MediaLogger.e(TAG, "audio focus: gain");
                this.isPlay = true;
                setVolume(1.0d, 1.0d);
                return;
        }
    }
}
