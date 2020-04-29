package com.dji.video.framing.internal.decoder;

import android.media.MediaCodec;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.VideoFrame;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DJIDecodeServerImplApi24 extends AbsDjiDecodeServer {
    private static final String TAG = "DJIDecodeServerImplApi24";
    private ByteBuffer[] inputBuffers;
    private String outIndexPrefix = "Decoder output outputBufferIndex = ";
    private StringBuffer outIndexSb = new StringBuffer(this.outIndexPrefix);
    private ByteBuffer[] outputBuffers;

    public DJIDecodeServerImplApi24(Looper looper, @NonNull DJIVideoDecoder decoder) {
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
        if (!this.mDjiVideoDecoder.needLowFrequencyForSmoothing()) {
            sendEmptyMessageDelayed(2, 1);
        } else if (!hasMessages(2)) {
            sendEmptyMessageDelayed(2, 33);
        }
    }

    /* access modifiers changed from: protected */
    public void onDecoderOutput() throws Exception {
        if (this.codec != null && this.decoderConfigure && this.iframeIntoCodec) {
            this.outputBufferIndex = -1;
            try {
                this.outputBufferIndex = this.codec.dequeueOutputBuffer(this.bufferInfo, 0);
                VideoLog.i(TAG, this.outIndexSb.append(this.outputBufferIndex).toString(), new Object[0]);
                this.outIndexSb.delete(this.outIndexPrefix.length(), this.outIndexSb.length());
                onDecoderOutput(this.outputBufferIndex, this.bufferInfo);
            } catch (Exception e) {
                DJIVideoDecoder.log2File("dequeueOutputBuffer error" + e);
                throw e;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void queueInframe(@NonNull VideoFrame inputFrame) {
        offerFrameToInputQueue(inputFrame);
    }

    /* access modifiers changed from: protected */
    public void onDecoderInput() throws Exception {
        int inIndex;
        VideoFrame inputFrame = (VideoFrame) this.inputQueue.peek();
        if (inputFrame != null) {
            VideoLog.i(TAG, "before check codec status", new Object[0]);
            if (this.codec == null || !this.decoderConfigure) {
                VideoLog.i(TAG, "init decoder on data input", new Object[0]);
                initVideoDecoder();
            }
            VideoLog.i(TAG, "before codecSync", new Object[0]);
            if (this.saveLiveStream) {
                try {
                    this.liveStreamOutputStream.write(inputFrame.data, 0, inputFrame.size);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (!checkFrameIndex(inputFrame)) {
                ErrorStatusManager.getInstance().onErrorStatusChange(true);
                clearAndReleaseInputFrame();
                return;
            }
            VideoLog.i(TAG, "before dequeueInputBuffer", new Object[0]);
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    inIndex = this.codec.dequeueInputBuffer(10000);
                } else {
                    inIndex = this.codec.dequeueInputBuffer(0);
                }
                VideoLog.i(TAG, "after dequeueInputBuffer, inIndex: " + inIndex, new Object[0]);
                if (inIndex >= 0) {
                    this.codecDequeueFailCount = 0;
                    try {
                        onDecoderInput(inputFrame, inIndex, this.inputBuffers[inIndex]);
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
                    VideoLog.i(TAG, "after dequeueInputBuffer, cannot get buffer", new Object[0]);
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
            Log.e(TAG, "Reset decoder. Get INFO_OUTPUT_BUFFERS_CHANGED more than 10 times within a second.");
            this.bufferChangedQueue.clear();
            removeCallbacksAndMessages(null);
            sendEmptyMessage(0);
        } else if (this.outputBuffers != null) {
            this.outputBuffers = this.codec.getOutputBuffers();
            Log.e(TAG, "dequeueOutputBuffer INFO_OUTPUT_BUFFERS_CHANGED after");
        }
    }
}
