package dji.midware.media.transcode.offline;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import com.dji.mapkit.lbs.configuration.Defaults;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.colors.ColorFormatConv;
import dji.midware.media.muxer.DJIMuxerInterface;
import dji.midware.media.muxer.MuxerManager;
import java.io.IOException;
import java.nio.ByteBuffer;

@EXClassNullAway
public class EncoderMuxer {
    private String TAG = "EncoderMuxer";
    private int colorFormat;
    private ColorFormatConv conv = null;
    private MediaCodec encoder = null;
    MediaCodec.BufferInfo encoderOutputBufferInfo = new MediaCodec.BufferInfo();
    EncoderOutputMonitor encoderOutputMonitor = null;
    private int height;
    /* access modifiers changed from: private */
    public boolean isContinue = true;
    private DJIMuxerInterface muxer = null;
    int numFrameJumped = 0;
    int numFrameMuxerReceived = 0;
    int num_track_in_muxer = 0;
    private String outputFullFileName;
    int sample_flag;
    int sample_index;
    int sample_time;
    private int width;
    byte[] yuv_source;
    byte[] yuv_target;

    public void onEncoderInput(ByteBuffer decoderOutputBuffer, MediaCodec.BufferInfo bufferInfo) {
        int i = 0;
        if (this.numFrameJumped == 0) {
            try {
                int encoderInputBufferIndex = this.encoder.dequeueInputBuffer(-1);
                ByteBuffer encoderInputBuffer = this.encoder.getInputBuffers()[encoderInputBufferIndex];
                encoderInputBuffer.clear();
                this.sample_time = this.sample_index * (Defaults.SECOND_IN_NANOS / DJIVideoUtil.getFPS());
                if (this.sample_index % 15 == 0) {
                    i = 1;
                }
                this.sample_flag = i;
                this.sample_index++;
                if (this.conv != null) {
                    if (this.yuv_source == null) {
                        this.yuv_source = new byte[bufferInfo.size];
                    }
                    if (this.yuv_target == null) {
                        this.yuv_target = new byte[(encoderInputBuffer.limit() - encoderInputBuffer.position())];
                    }
                    decoderOutputBuffer.get(this.yuv_source, 0, this.yuv_source.length);
                    this.conv.convert(this.yuv_source, this.yuv_target, this.width, this.height);
                    encoderInputBuffer.put(ByteBuffer.wrap(this.yuv_target, 0, this.yuv_target.length));
                    this.encoder.queueInputBuffer(encoderInputBufferIndex, 0, this.yuv_target.length, (long) this.sample_time, this.sample_flag);
                    return;
                }
                decoderOutputBuffer.position(bufferInfo.offset);
                decoderOutputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                if (bufferInfo.size > encoderInputBuffer.limit() - encoderInputBuffer.position()) {
                    Log.i(this.TAG, "when moving from decoder to encoder: buffer overflow");
                    if (this.yuv_source == null) {
                        this.yuv_source = new byte[(encoderInputBuffer.limit() - encoderInputBuffer.position())];
                    }
                    decoderOutputBuffer.get(this.yuv_source, 0, this.yuv_source.length);
                    encoderInputBuffer.put(this.yuv_source);
                } else {
                    encoderInputBuffer.put(decoderOutputBuffer);
                }
                this.encoder.queueInputBuffer(encoderInputBufferIndex, 0, bufferInfo.size, (long) this.sample_time, this.sample_flag);
            } catch (Exception e) {
                MediaLogger.show(e);
            }
        } else {
            this.numFrameJumped--;
            Log.i(this.TAG, "encoder get a frame for the upstream, but will jump (remaining to be jumped=" + this.numFrameJumped + ")");
        }
    }

    public void initVideoEncoder(MediaFormat mediaFormat) throws Exception {
        Log.i(this.TAG, "decoder's output color format is: " + mediaFormat.getInteger("color-format"));
        MediaFormat encoderFormat = MediaFormat.createVideoFormat(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0], mediaFormat.getInteger("width"), mediaFormat.getInteger("height"));
        encoderFormat.setInteger("bitrate", DJIVideoUtil.MP4_BIT_RATE);
        encoderFormat.setInteger("frame-rate", DJIVideoUtil.getFPS());
        encoderFormat.setInteger("i-frame-interval", 1);
        encoderFormat.setInteger("color-format", this.colorFormat);
        Log.i(this.TAG, "set color of encoder as " + this.colorFormat);
        MediaLogger.show("set color of encoder as " + this.colorFormat);
        Log.i(this.TAG, "decoder's output format: width=" + mediaFormat.getInteger("width") + " height=" + mediaFormat.getInteger("height"));
        try {
            this.encoder.configure(encoderFormat, (Surface) null, (MediaCrypto) null, 1);
            this.encoder.start();
            this.encoderOutputMonitor = new EncoderOutputMonitor();
            this.encoderOutputMonitor.start();
        } catch (Exception e) {
            MediaLogger.show(e);
            this.encoder.release();
            this.encoder = null;
            throw e;
        }
    }

    private void initVideoMuxer() throws IOException {
        try {
            this.muxer = MuxerManager.createMuxer(MuxerManager.MuxerType.FFMPEG);
            this.muxer.init(this.outputFullFileName);
            MediaLogger.show("successfully created muxer");
        } catch (IOException e2) {
            MediaLogger.show(e2);
            throw e2;
        }
    }

    protected class EncoderOutputMonitor extends Thread {
        protected EncoderOutputMonitor() {
        }

        public void run() {
            while (EncoderMuxer.this.isContinue) {
                try {
                    EncoderMuxer.this.onEncoderOutput();
                } catch (Exception e) {
                    MediaLogger.show(e);
                    return;
                }
            }
            MediaLogger.show("", "Encoder monitor thread ends");
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, boolean):void}
     arg types: [int, int]
     candidates:
      ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, long):void}
      ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, boolean):void} */
    /* access modifiers changed from: private */
    public void onEncoderOutput() throws IOException {
        int encoderOutputBufferIndex = this.encoder.dequeueOutputBuffer(this.encoderOutputBufferInfo, 50);
        if (encoderOutputBufferIndex < 0 && encoderOutputBufferIndex != -1) {
            MediaLogger.show("encoder outputs bufferIndex=" + encoderOutputBufferIndex + ", -2 means INFO_OUTPUT_FORMAT_CHANGED");
        }
        if (encoderOutputBufferIndex == -2 && this.num_track_in_muxer == 0) {
            initVideoMuxer();
            this.num_track_in_muxer++;
            this.muxer.addTrack(this.encoder.getOutputFormat());
            this.muxer.start();
        }
        if (encoderOutputBufferIndex >= 0) {
            this.numFrameMuxerReceived++;
            if (DJIVideoUtil.isDebug() && (this.numFrameMuxerReceived == 1 || this.numFrameMuxerReceived % 30 == 0)) {
                MediaLogger.show("Muxer has received " + this.numFrameMuxerReceived + " frames");
            }
            if ((this.encoderOutputBufferInfo.flags & 4) == 4) {
                this.isContinue = false;
                MediaLogger.show("muxer received flag of END_OF_STREAM");
            }
            this.muxer.writeSampleData(0, this.encoder.getOutputBuffers()[encoderOutputBufferIndex], this.encoderOutputBufferInfo, DJIVideoUtil.getDurationPerFrameUs());
            this.encoder.releaseOutputBuffer(encoderOutputBufferIndex, false);
        }
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFullFileName = outputFileName;
    }

    public void start(MediaCodec encoder2, int colorFormat2, int width2, int height2) {
        this.encoder = encoder2;
        this.colorFormat = colorFormat2;
        this.width = width2;
        this.height = height2;
        this.sample_index = 0;
        Log.i(this.TAG, "complete execution of start()");
    }

    public void setFrameJumped(int numFrameJumped2) {
        this.numFrameJumped = numFrameJumped2;
    }

    public boolean stopOutputMonitor() {
        this.isContinue = false;
        try {
            this.encoderOutputMonitor.join();
            return true;
        } catch (InterruptedException e) {
            MediaLogger.show(e);
            return false;
        }
    }

    public void stopAndReleaseEncoderMuxer() {
        if (this.encoder != null) {
            this.encoder.stop();
            this.encoder.release();
        }
        if (this.muxer != null) {
            this.muxer.stop();
            this.muxer.release();
        }
    }

    public void setColorFormatConv(ColorFormatConv conv2) {
        this.conv = conv2;
    }
}
