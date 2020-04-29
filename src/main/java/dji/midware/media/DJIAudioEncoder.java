package dji.midware.media;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.DJIAudioRecordWrapper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

@EXClassNullAway
public class DJIAudioEncoder implements DJIAudioRecordWrapper.DJIAudioRecordListenter {
    public static final int FRAMES_PER_BUFFER = 25;
    private static final String MIME_TYPE = "audio/mp4a-latm";
    public static final int SAMPLES_PER_FRAME = 1024;
    private static final String TAG = "DJIAudioEncoder";
    protected static final int TIMEOUT_USEC = 10000;
    private static DJIAudioEncoder instance;
    private MediaCodec audioEncoder;
    private Object encodeLock = new Object();
    private AtomicBoolean isExit = new AtomicBoolean(true);
    private boolean isInited = false;
    private DJIAudioEncoderListener listener;
    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();
    private long prevOutputPTSUs = 0;

    public interface DJIAudioEncoderListener {
        void onDataEncoded(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo, long j);

        void onEncoderInit(MediaFormat mediaFormat);

        void onFormatChanged(MediaFormat mediaFormat);
    }

    public void setListener(DJIAudioEncoderListener listener2) {
        this.listener = listener2;
    }

    private void invokeOnEncoderInit(MediaFormat format) {
        if (this.listener != null) {
            this.listener.onEncoderInit(format);
        }
    }

    private void invokeOnDataEncoded(ByteBuffer buffer, MediaCodec.BufferInfo bufferInfo, long ptsInUs) {
        if (this.listener != null) {
            this.listener.onDataEncoded(buffer, bufferInfo, ptsInUs);
        }
    }

    private void invokeOnFormatChanged(MediaFormat format) {
        if (this.listener != null) {
            this.listener.onFormatChanged(format);
        }
    }

    public static synchronized DJIAudioEncoder getInstance() {
        DJIAudioEncoder dJIAudioEncoder;
        synchronized (DJIAudioEncoder.class) {
            if (instance == null) {
                instance = new DJIAudioEncoder();
            }
            dJIAudioEncoder = instance;
        }
        return dJIAudioEncoder;
    }

    private DJIAudioEncoder() {
    }

    public synchronized void release() {
        synchronized (this.encodeLock) {
            if (!this.isExit.get() && this.audioEncoder != null) {
                this.isExit.set(true);
                this.audioEncoder.stop();
                this.audioEncoder.release();
                this.audioEncoder = null;
            }
        }
    }

    public void destroy() {
        release();
        instance = null;
    }

    private static final MediaCodecInfo selectAudioCodec(String mimeType) {
        String[] types;
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (codecInfo.isEncoder()) {
                for (String str : codecInfo.getSupportedTypes()) {
                    if (str.equalsIgnoreCase(mimeType) && 0 == 0) {
                        return codecInfo;
                    }
                }
                continue;
            }
        }
        return null;
    }

    private void addADTStoPacket(byte[] packet, int packetLen, int profile, int freqIdx, int chanCfg) {
        packet[0] = -1;
        packet[1] = -7;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 2047) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 31);
        packet[6] = -4;
    }

    public void startAudioEncoder() throws IOException {
        synchronized (this.encodeLock) {
            this.isInited = false;
            if (selectAudioCodec(MIME_TYPE) != null) {
                if (this.audioEncoder != null) {
                    release();
                }
                MediaFormat audioFormat = MediaFormat.createAudioFormat(MIME_TYPE, 44100, 1);
                audioFormat.setInteger("aac-profile", 2);
                audioFormat.setInteger("channel-mask", 16);
                audioFormat.setInteger("bitrate", 64000);
                audioFormat.setInteger("channel-count", 1);
                this.audioEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
                this.audioEncoder.configure(audioFormat, (Surface) null, (MediaCrypto) null, 1);
                this.audioEncoder.start();
                this.isExit.set(false);
            }
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, boolean):void}
     arg types: [int, int]
     candidates:
      ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, long):void}
      ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, boolean):void} */
    public boolean encode(ByteBuffer buffer, int length, long presentationTimeUs) {
        int encoderStatus;
        boolean z;
        synchronized (this.encodeLock) {
            if (this.audioEncoder == null) {
                z = false;
            } else if (this.isExit.get()) {
                z = false;
            } else {
                ByteBuffer[] inputBuffers = this.audioEncoder.getInputBuffers();
                int inputBufferIndex = this.audioEncoder.dequeueInputBuffer(10000);
                if (inputBufferIndex >= 0) {
                    ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                    inputBuffer.clear();
                    if (buffer != null) {
                        if (buffer.remaining() > inputBuffer.remaining()) {
                            buffer.limit(buffer.position() + inputBuffer.remaining());
                        }
                        inputBuffer.put(buffer);
                    }
                    if (length <= 0) {
                        this.audioEncoder.queueInputBuffer(inputBufferIndex, 0, 0, presentationTimeUs, 4);
                    } else {
                        this.audioEncoder.queueInputBuffer(inputBufferIndex, 0, length, presentationTimeUs, 0);
                    }
                } else if (inputBufferIndex == -1) {
                }
                if (this.listener == null) {
                    z = false;
                } else {
                    ByteBuffer[] encoderOutputBuffers = this.audioEncoder.getOutputBuffers();
                    do {
                        encoderStatus = this.audioEncoder.dequeueOutputBuffer(this.mBufferInfo, 0);
                        if (encoderStatus != -1) {
                            if (encoderStatus == -3) {
                                encoderOutputBuffers = this.audioEncoder.getOutputBuffers();
                                continue;
                            } else if (encoderStatus == -2) {
                                this.listener.onFormatChanged(this.audioEncoder.getOutputFormat());
                                continue;
                            } else if (encoderStatus < 0) {
                                continue;
                            } else {
                                if (!this.isInited) {
                                    this.isInited = true;
                                    invokeOnEncoderInit(this.audioEncoder.getOutputFormat());
                                }
                                ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                                if ((this.mBufferInfo.flags & 2) != 0) {
                                    this.mBufferInfo.size = 0;
                                }
                                if (!(this.mBufferInfo.size == 0 || this.listener == null)) {
                                    this.mBufferInfo.presentationTimeUs = getPTSUs();
                                    invokeOnDataEncoded(encodedData, this.mBufferInfo, getPTSUs());
                                    this.prevOutputPTSUs = this.mBufferInfo.presentationTimeUs;
                                }
                                this.audioEncoder.releaseOutputBuffer(encoderStatus, false);
                                continue;
                            }
                        }
                    } while (encoderStatus >= 0);
                    z = true;
                }
            }
        }
        return z;
    }

    private long getPTSUs() {
        long result = System.nanoTime() / 1000;
        if (result < this.prevOutputPTSUs) {
            return result + (this.prevOutputPTSUs - result);
        }
        return result;
    }

    public void onAudioDataRead(ByteBuffer buffer, int length) {
        buffer.position(length);
        buffer.flip();
        encode(buffer, length, getPTSUs());
    }

    public void onVolumeRefresh(double volumn) {
    }
}
