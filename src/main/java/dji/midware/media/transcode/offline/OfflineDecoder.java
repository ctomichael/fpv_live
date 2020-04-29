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
import dji.midware.media.record.H264FrameListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;

@EXClassNullAway
public class OfflineDecoder implements H264FrameListener {
    /* access modifiers changed from: private */
    public static String TAG = "OfflineDecoder";
    private int colorFormat;
    public MediaCodec decoder;
    private ByteBuffer decoderOutputBuffer;
    private MediaCodec.BufferInfo decoderOutputBufferInfo = new MediaCodec.BufferInfo();
    private EncoderMuxer encoderMuxer;
    boolean hasConfigEncoder = false;
    int height;
    /* access modifiers changed from: private */
    public boolean isContinue = true;
    MediaFormat mediaFormatDe;
    DecoderOutputMonitor outputMonitor = null;
    int sample_flag;
    int sample_index;
    int sample_time;
    int width;

    protected class DecoderOutputMonitor extends Thread {
        protected DecoderOutputMonitor() {
        }

        public void run() {
            try {
                Log.i(OfflineDecoder.TAG, "thread DecoderOutputMonitor has started");
                while (OfflineDecoder.this.isContinue) {
                    OfflineDecoder.this.onDecoderOutput();
                }
            } catch (Exception e) {
                MediaLogger.show(e);
            }
            MediaLogger.show("", "Decoder monitor thread ends");
        }
    }

    public void setVideoDataListener(EncoderMuxer listener) {
        this.encoderMuxer = listener;
    }

    public void start(MediaCodec decoder2, int colorFormat2, int width2, int height2) {
        Log.i(TAG, "Starting");
        this.decoder = decoder2;
        this.colorFormat = colorFormat2;
        this.width = width2;
        this.height = height2;
        this.sample_time = 0;
        this.sample_flag = 0;
        this.sample_index = 0;
        this.isContinue = true;
        initDecoder();
        this.outputMonitor = new DecoderOutputMonitor();
        this.outputMonitor.start();
        Log.i(TAG, "Has started");
    }

    private void initDecoder() {
        this.mediaFormatDe = MediaFormat.createVideoFormat(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0], this.width, this.height);
        Log.i(TAG, "set color of decoder as " + this.colorFormat);
        MediaLogger.show("set color of decoder as " + this.colorFormat);
        this.mediaFormatDe.setInteger("color-format", this.colorFormat);
        if (TranscoderManager.sps != null) {
            this.mediaFormatDe.setByteBuffer("csd-0", ByteBuffer.wrap(TranscoderManager.sps));
            Log.i(TAG, "set csd-0 for decoder as " + Arrays.toString(TranscoderManager.sps));
        }
        if (TranscoderManager.pps != null) {
            this.mediaFormatDe.setByteBuffer("csd-1", ByteBuffer.wrap(TranscoderManager.pps));
            Log.i(TAG, "set csd-1 for decoder as " + Arrays.toString(TranscoderManager.pps));
        }
        this.decoder.configure(this.mediaFormatDe, (Surface) null, (MediaCrypto) null, 0);
        this.decoder.start();
    }

    public boolean stopOutputMonitor() {
        this.isContinue = false;
        try {
            this.outputMonitor.join();
            return true;
        } catch (InterruptedException e) {
            MediaLogger.e(TAG, e);
            return false;
        }
    }

    public void stopAndReleaseDecoder() {
        if (this.decoder != null) {
            synchronized (this.decoder) {
                this.decoder.stop();
                this.decoder.release();
            }
        }
    }

    private void onDecoderInput(byte[] videoBuffer, int size) {
        int i = 0;
        synchronized (this.decoder) {
            try {
                if (DJIVideoUtil.isDebug()) {
                    Log.i(TAG, "get a frame from upstream and will place it in the input");
                }
                this.sample_time = this.sample_index * (Defaults.SECOND_IN_NANOS / DJIVideoUtil.getFPS());
                if (this.sample_index == 0) {
                    i = 1;
                }
                this.sample_flag = i;
                this.sample_index++;
                try {
                    int decoderInputBufferIndex = this.decoder.dequeueInputBuffer(-1);
                    ByteBuffer decoderInputBuffer = this.decoder.getInputBuffers()[decoderInputBufferIndex];
                    decoderInputBuffer.clear();
                    decoderInputBuffer.put(videoBuffer);
                    this.decoder.queueInputBuffer(decoderInputBufferIndex, 0, size, (long) this.sample_time, this.sample_flag);
                    if (DJIVideoUtil.isDebug()) {
                        Log.i(TAG, "has place a frame into the input");
                    }
                } catch (Exception e) {
                    MediaLogger.show(TAG, "Input thread reports:" + e.toString() + ". going to reset the decoder");
                    this.decoder.stop();
                    this.decoder.configure(this.mediaFormatDe, (Surface) null, (MediaCrypto) null, 0);
                    this.decoder.start();
                    return;
                }
            } catch (Exception e2) {
                e2.printStackTrace(new PrintWriter(new StringWriter()));
                Log.e(TAG, e2.toString());
            }
        }
        return;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, boolean):void}
     arg types: [int, int]
     candidates:
      ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, long):void}
      ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, boolean):void} */
    /* access modifiers changed from: private */
    public void onDecoderOutput() throws Exception {
        try {
            int decoderOutputBufferIndex = this.decoder.dequeueOutputBuffer(this.decoderOutputBufferInfo, 50);
            if (!this.hasConfigEncoder && decoderOutputBufferIndex == -2) {
                this.hasConfigEncoder = true;
                this.encoderMuxer.initVideoEncoder(this.decoder.getOutputFormat());
            }
            if (decoderOutputBufferIndex >= 0) {
                if (DJIVideoUtil.isDebug()) {
                    Log.i(TAG, "DecoderOutputMonitor get a frame from the output buffer and will pass it to downstream");
                }
                this.decoderOutputBuffer = this.decoder.getOutputBuffers()[decoderOutputBufferIndex];
                this.encoderMuxer.onEncoderInput(this.decoderOutputBuffer, this.decoderOutputBufferInfo);
                this.decoder.releaseOutputBuffer(decoderOutputBufferIndex, false);
            }
        } catch (Exception e) {
            Log.i(TAG, "output monitor exception when calling dequeue");
        }
    }

    public void onH264FrameInput(byte[] videoBuffer, int size, long pts, boolean isKeyFrame) {
        onDecoderInput(videoBuffer, size);
    }
}
