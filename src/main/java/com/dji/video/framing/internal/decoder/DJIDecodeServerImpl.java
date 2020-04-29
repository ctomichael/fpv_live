package com.dji.video.framing.internal.decoder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.VideoFrame;
import java.util.LinkedList;
import java.util.Queue;

public class DJIDecodeServerImpl extends AbsDjiDecodeServer {
    private static final int DECODING_CHECK_INTERVAL = 500;
    private static final int MSG_CHECK_DECODING_RST = 1002;
    private static final int MSG_FRAME_IN = 1000;
    private static final int MSG_FRAME_OUTPUT = 1003;
    private static final int MSG_INPUT_BUFFER_AVAILABLE = 1001;
    private static final String TAG = "DJIDecodeServerImpl";
    private MediaCodec.Callback codecCallback = new MediaCodec.Callback() {
        /* class com.dji.video.framing.internal.decoder.DJIDecodeServerImpl.AnonymousClass1 */

        public void onInputBufferAvailable(@NonNull MediaCodec mediaCodec, int i) {
            DJIDecodeServerImpl.this.sendMessage(DJIDecodeServerImpl.this.obtainMessage(1001, i, 0));
        }

        public void onOutputBufferAvailable(@NonNull MediaCodec mediaCodec, int i, @NonNull MediaCodec.BufferInfo bufferInfo) {
            DJIDecodeServerImpl.this.obtainMessage(1003, i, 0, bufferInfo).sendToTarget();
        }

        public void onError(@NonNull MediaCodec mediaCodec, @NonNull MediaCodec.CodecException e) {
            VideoLog.e(DJIDecodeServerImpl.TAG, "onError: ", e, new Object[0]);
            DJIDecodeServerImpl.this.releaseDecoder();
        }

        public void onOutputFormatChanged(@NonNull MediaCodec mediaCodec, @NonNull MediaFormat mediaFormat) {
            DJIDecodeServerImpl.this.post(new Runnable() {
                /* class com.dji.video.framing.internal.decoder.DJIDecodeServerImpl.AnonymousClass1.AnonymousClass1 */

                public void run() {
                    DJIDecodeServerImpl.this.onOutputFormatChanged();
                }
            });
        }
    };
    private Queue<Integer> inputIndexQueue = new LinkedList();

    public void handleMessage(Message msg) {
        VideoFrame inputFrameFromQueue;
        MediaCodec.BufferInfo bufferInfo = null;
        switch (msg.what) {
            case 1000:
                VideoFrame inputFrame = (VideoFrame) msg.obj;
                if (inputFrame == null) {
                    return;
                }
                if (this.codec == null || isInitingCodec() || this.inputIndexQueue.isEmpty()) {
                    offerFrameToInputQueue(inputFrame);
                    return;
                }
                Integer index = this.inputIndexQueue.peek();
                if (index == null || index.intValue() < 0) {
                    offerFrameToInputQueue(inputFrame);
                    return;
                }
                this.inputIndexQueue.poll();
                try {
                    if (!onDecoderInput(inputFrame, index.intValue(), null)) {
                        this.inputIndexQueue.offer(index);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    VideoLog.e(TAG, "handleMessage() MSG_FRAME_IN:" + e, new Object[0]);
                    DJIVideoDecoder.log2File("handleMessage: frame in" + e);
                    releaseDecoder();
                    return;
                }
            case 1001:
                if (this.codec != null && !isInitingCodec()) {
                    int index2 = msg.arg1;
                    VideoLog.d(TAG, "handleMessage: MSG_INPUT_BUFFER_AVAILABLE idx=" + index2 + " inputQueue.size:" + this.inputQueue.size(), new Object[0]);
                    if (index2 < 0) {
                        Integer indexFromQueue = this.inputIndexQueue.peek();
                        if (indexFromQueue != null && indexFromQueue.intValue() >= 0 && (inputFrameFromQueue = (VideoFrame) this.inputQueue.peek()) != null) {
                            this.inputIndexQueue.poll();
                            try {
                                if (!onDecoderInput(inputFrameFromQueue, index2, null)) {
                                    this.inputIndexQueue.offer(Integer.valueOf(index2));
                                    return;
                                } else {
                                    sendMessage(obtainMessage(1001, -1, 0));
                                    return;
                                }
                            } catch (Exception e2) {
                                DJIVideoDecoder.log2File("handleMessage: input buffer from queue" + e2);
                                releaseDecoder();
                                return;
                            }
                        } else {
                            return;
                        }
                    } else if (this.inputQueue.isEmpty()) {
                        this.inputIndexQueue.offer(Integer.valueOf(index2));
                        return;
                    } else {
                        VideoFrame inputFrameFromQueue2 = (VideoFrame) this.inputQueue.peek();
                        if (inputFrameFromQueue2 == null) {
                            this.inputIndexQueue.offer(Integer.valueOf(index2));
                            return;
                        }
                        try {
                            if (!onDecoderInput(inputFrameFromQueue2, index2, null)) {
                                this.inputIndexQueue.offer(Integer.valueOf(index2));
                                return;
                            } else {
                                sendMessage(obtainMessage(1001, -1, 0));
                                return;
                            }
                        } catch (Exception e3) {
                            VideoLog.e(TAG, "handleMessage() MSG_INPUT_BUFFER_AVAILABLE error:" + e3, new Object[0]);
                            DJIVideoDecoder.log2File("handleMessage: input buffer available" + e3);
                            releaseDecoder();
                            return;
                        }
                    }
                } else {
                    return;
                }
            case 1002:
                try {
                    onDecoderOutput(-1, null);
                    return;
                } catch (Exception e4) {
                    DJIVideoDecoder.log2File("handleMessage: check decode rst" + e4);
                    releaseDecoder();
                    return;
                } finally {
                    sendEmptyMessageDelayed(1002, 500);
                }
            case 1003:
                int outputBufferIndex = msg.arg1;
                try {
                    if (msg.obj != null && (msg.obj instanceof MediaCodec.BufferInfo)) {
                        bufferInfo = (MediaCodec.BufferInfo) msg.obj;
                    }
                    onDecoderOutput(outputBufferIndex, bufferInfo);
                    return;
                } catch (Exception e5) {
                    VideoLog.e(TAG, "handleMessage: ", e5, new Object[0]);
                    DJIVideoDecoder.log2File("handleMessage: output buffer from queue" + e5);
                    releaseDecoder();
                    return;
                } finally {
                    removeMessages(1002);
                    sendEmptyMessageDelayed(1002, 2000);
                }
            default:
                super.handleMessage(msg);
                return;
        }
    }

    public DJIDecodeServerImpl(Looper looper, @NonNull DJIVideoDecoder decoder) {
        super(looper, decoder);
        sendEmptyMessageDelayed(1002, 2000);
    }

    /* access modifiers changed from: protected */
    public void onCodecConfigured() {
        this.codec.setCallback(this.codecCallback);
    }

    /* access modifiers changed from: package-private */
    public void releaseDecoder() {
        super.releaseDecoder();
        removeMessages(1001);
        this.inputIndexQueue.clear();
    }

    /* access modifiers changed from: protected */
    public void initVideoDecoder() {
        initVideoDecoder(false, true);
    }

    /* access modifiers changed from: protected */
    public void onCodecStarted() {
    }

    /* access modifiers changed from: protected */
    public void handleInOutMsg() {
    }

    /* access modifiers changed from: protected */
    public void queueInframe(@NonNull VideoFrame inputFrame) {
        VideoLog.d(TAG, "queueInframe: 0", new Object[0]);
        sendMessage(obtainMessage(1000, inputFrame));
    }
}
