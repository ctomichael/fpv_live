package dji.midware.media;

import android.media.MediaCodec;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import dji.log.DJILogHelper;
import dji.midware.media.DJIDecodeServer;
import dji.midware.stat.StatLatest;
import dji.midware.stat.StatRate;
import dji.midware.stat.StatService;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DJIDecodeServerApi16 extends DJIDecodeServer {
    private static final String TAG = "DJIDecodeServerImplApi24";
    private ByteBuffer[] inputBuffers;
    private String outIndexPrefix = "Decoder output outputBufferIndex = ";
    private StringBuffer outIndexSb = new StringBuffer(this.outIndexPrefix);
    private ByteBuffer[] outputBuffers;

    public DJIDecodeServerApi16(Looper looper, @NonNull DJIVideoDecoder decoder) {
        super(looper, decoder);
    }

    /* access modifiers changed from: protected */
    public void onCodecConfigured() {
    }

    /* access modifiers changed from: protected */
    public void onCodecStarted() {
        this.inputBuffers = this.codec.getInputBuffers();
        this.outputBuffers = this.codec.getOutputBuffers();
    }

    /* access modifiers changed from: protected */
    public void handleInOutMsg() {
        if (!isInitingCodec()) {
            try {
                onDecoderInput();
            } catch (Exception e) {
                DJIVideoDecoder.log2File("dec input error:" + e);
                releaseDecoder();
            }
            try {
                onDecoderOutput();
            } catch (Exception e2) {
                DJIVideoDecoder.log2File("dec output error:" + e2);
                releaseDecoder();
            }
        }
        if (!this.decoder.needLowFrequencyForSmoothing()) {
            sendEmptyMessageDelayed(2, 1);
        } else if (!hasMessages(2)) {
            sendEmptyMessageDelayed(2, 33);
        }
    }

    /* access modifiers changed from: protected */
    public void onDecoderOutput() throws Exception {
        if (DJIVideoDecoder.TEST_RESTART_MECHANISM && this.time_initialized.get() % 3 == 1 && System.currentTimeMillis() - this.start_time > 5000) {
            MediaLogger.show(TAG, "\n ... decoder thread quits");
        } else if (this.codec != null && this.decoderConfigure && this.iframeIntoCodec) {
            this.outputBufferIndex = -1;
            try {
                this.outputBufferIndex = this.codec.dequeueOutputBuffer(this.bufferInfo, 0);
                MediaLogger.i(false, TAG, this.outIndexSb.append(this.outputBufferIndex).toString());
                this.outIndexSb.delete(this.outIndexPrefix.length(), this.outIndexSb.length());
                onDecoderOutput(this.outputBufferIndex, this.bufferInfo);
            } catch (Exception e) {
                DJIVideoDecoder.log2File("dequeueOutputBuffer error" + e);
                throw e;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void queueInframe(@NonNull DJIDecodeServer.InputFrame inputFrame) {
        offerFrameToInputQueue(inputFrame);
    }

    /* access modifiers changed from: protected */
    public void onDecoderInput() throws Exception {
        int inIndex;
        DJIDecodeServer.InputFrame inputFrame = (DJIDecodeServer.InputFrame) this.inputQueue.peek();
        if (inputFrame != null) {
            MediaLogger.i(TAG, "before check codec status");
            if (this.codec == null || !this.decoderConfigure) {
                MediaLogger.i(TAG, "init decoder on data input");
                initVideoDecoder();
            }
            MediaLogger.i(false, TAG, "before codecSync");
            if (this.saveLiveStream) {
                try {
                    this.liveStreamOutputStream.write(inputFrame.videoBuffer, 0, inputFrame.size);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (!checkFrame(inputFrame)) {
                DJIVideoDecoder.setErrorStatus(true);
                clearAndReleaseInputFrame();
                return;
            }
            MediaLogger.i(false, TAG, "before dequeueInputBuffer");
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    inIndex = this.codec.dequeueInputBuffer(10000);
                } else {
                    inIndex = this.codec.dequeueInputBuffer(0);
                }
                MediaLogger.i(false, TAG, "after dequeueInputBuffer, inIndex: " + inIndex);
                if (inIndex >= 0) {
                    this.codecDequeueFailCount = 0;
                    try {
                        ByteBuffer buffer = this.inputBuffers[inIndex];
                        StatService.getInstance().postEvent(StatLatest.class, "Input_Native_Buffer_Size", (float) this.inputBuffers.length);
                        StatService.getInstance().postEvent(StatRate.class, "Input_Codec_FPS", 1.0f);
                        onDecoderInput(inputFrame, inIndex, buffer);
                    } catch (Exception e) {
                        DJIVideoDecoder.log2File("after dequeueInputBuffer, exception: " + e);
                        throw e;
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= 23) {
                        this.codecDequeueFailCount++;
                        if (this.codecDequeueFailCount >= this.codecFailResetThreshold) {
                            resetCodec();
                        }
                    }
                    MediaLogger.i(false, TAG, "after dequeueInputBuffer, cannot get buffer");
                    StatService.getInstance().postEvent(StatRate.class, "Input_Native_Buffer_Full_Try", 1.0f);
                }
            } catch (Exception e2) {
                DJIVideoDecoder.log2File("dequeueInputBuffer:" + e2);
                throw e2;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDecoderOutput(int outputBufferIndex, MediaCodec.BufferInfo bufferInfo) throws Exception {
        if (outputBufferIndex == -3) {
            onOutputBufferChanged();
        } else {
            super.onDecoderOutput(outputBufferIndex, bufferInfo);
        }
    }

    private void onOutputBufferChanged() {
        Log.e(TAG, "dequeueOutputBuffer INFO_OUTPUT_BUFFERS_CHANGED");
        long curTime = System.currentTimeMillis();
        this.bufferChangedQueue.addLast(Long.valueOf(curTime));
        if (this.bufferChangedQueue.size() >= 10 && curTime - ((Long) this.bufferChangedQueue.pollFirst()).longValue() < 1000) {
            DJILogHelper.getInstance().LOGE(TAG, "Reset decoder. Get INFO_OUTPUT_BUFFERS_CHANGED more than 10 times within a second.", true, true);
            this.bufferChangedQueue.clear();
            removeCallbacksAndMessages(null);
            sendEmptyMessage(0);
        } else if (this.outputBuffers != null) {
            this.outputBuffers = this.codec.getOutputBuffers();
            Log.e(TAG, "dequeueOutputBuffer INFO_OUTPUT_BUFFERS_CHANGED after");
        }
    }
}
