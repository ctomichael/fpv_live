package dji.midware.media;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import dji.log.DJILogHelper;
import dji.midware.media.DJIDecodeServer;
import java.util.LinkedList;
import java.util.Queue;

@TargetApi(21)
public class DJIDecodeServerApi21 extends DJIDecodeServer {
    private static final int DECODING_CHECK_INTERVAL = 500;
    private static final int MSG_CHECK_DECODING_RST = 1002;
    private static final int MSG_FRAME_IN = 1000;
    private static final int MSG_FRAME_OUTPUT = 1003;
    private static final int MSG_INPUT_BUFFER_AVAILABLE = 1001;
    private static final String TAG = "DJIDecodeServerImpl";
    private MediaCodec.Callback codecCallback = new MediaCodec.Callback() {
        /* class dji.midware.media.DJIDecodeServerApi21.AnonymousClass1 */

        public void onInputBufferAvailable(@NonNull MediaCodec mediaCodec, int i) {
            DJIDecodeServerApi21.this.sendMessage(DJIDecodeServerApi21.this.obtainMessage(1001, i, 0));
        }

        public void onOutputBufferAvailable(@NonNull MediaCodec mediaCodec, int i, @NonNull MediaCodec.BufferInfo bufferInfo) {
            DJIDecodeServerApi21.this.obtainMessage(1003, i, 0, bufferInfo).sendToTarget();
        }

        public void onError(@NonNull MediaCodec mediaCodec, @NonNull MediaCodec.CodecException e) {
            DJILogHelper.getInstance().LOGE(DJIDecodeServerApi21.TAG, "onError: " + e);
            DJIDecodeServerApi21.this.releaseDecoder();
        }

        public void onOutputFormatChanged(@NonNull MediaCodec mediaCodec, @NonNull MediaFormat mediaFormat) {
            DJIDecodeServerApi21.this.post(new Runnable() {
                /* class dji.midware.media.DJIDecodeServerApi21.AnonymousClass1.AnonymousClass1 */

                public void run() {
                    DJIDecodeServerApi21.this.onOutputFormatChanged();
                }
            });
        }
    };
    private Queue<Integer> inputIndexQueue = new LinkedList();

    public void handleMessage(Message msg) {
        DJIDecodeServer.InputFrame inputFrameFromQueue;
        switch (msg.what) {
            case 1000:
                DJIDecodeServer.InputFrame inputFrame = (DJIDecodeServer.InputFrame) msg.obj;
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
                } else if (!checkFrame(inputFrame)) {
                    DJIVideoDecoder.setErrorStatus(true);
                    inputFrame.recycle();
                    clearAndReleaseInputFrame();
                    return;
                } else {
                    this.inputIndexQueue.poll();
                    try {
                        onDecoderInput(inputFrame, index.intValue(), this.codec.getInputBuffer(index.intValue()));
                        return;
                    } catch (Exception e) {
                        DJIVideoDecoder.log2File("handleMessage: frame in" + e);
                        releaseDecoder();
                        return;
                    }
                }
            case 1001:
                if (this.codec != null && !isInitingCodec()) {
                    int index2 = msg.arg1;
                    if (index2 < 0) {
                        Integer indexFromQueue = this.inputIndexQueue.peek();
                        if (indexFromQueue != null && indexFromQueue.intValue() >= 0 && (inputFrameFromQueue = (DJIDecodeServer.InputFrame) this.inputQueue.peek()) != null) {
                            if (!checkFrame(inputFrameFromQueue)) {
                                DJIVideoDecoder.setErrorStatus(true);
                                clearAndReleaseInputFrame();
                                return;
                            }
                            this.inputIndexQueue.poll();
                            try {
                                onDecoderInput(inputFrameFromQueue, indexFromQueue.intValue(), this.codec.getInputBuffer(indexFromQueue.intValue()));
                            } catch (Exception e2) {
                                DJIVideoDecoder.log2File("handleMessage: input buffer from queue" + e2);
                                releaseDecoder();
                            }
                            sendMessage(obtainMessage(1001, -1, 0));
                            return;
                        }
                        return;
                    } else if (this.inputQueue.isEmpty()) {
                        this.inputIndexQueue.offer(Integer.valueOf(index2));
                        return;
                    } else {
                        DJIDecodeServer.InputFrame inputFrameFromQueue2 = (DJIDecodeServer.InputFrame) this.inputQueue.peek();
                        if (inputFrameFromQueue2 == null) {
                            this.inputIndexQueue.offer(Integer.valueOf(index2));
                            return;
                        } else if (!checkFrame(inputFrameFromQueue2)) {
                            DJIVideoDecoder.setErrorStatus(true);
                            clearAndReleaseInputFrame();
                            return;
                        } else {
                            try {
                                onDecoderInput(inputFrameFromQueue2, index2, this.codec.getInputBuffer(index2));
                                sendMessage(obtainMessage(1001, -1, 0));
                                return;
                            } catch (Exception e3) {
                                DJIVideoDecoder.log2File("handleMessage: input buffer available" + e3);
                                releaseDecoder();
                                return;
                            }
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
                try {
                    onDecoderOutput(msg.arg1, (msg.obj == null || !(msg.obj instanceof MediaCodec.BufferInfo)) ? null : (MediaCodec.BufferInfo) msg.obj);
                    return;
                } catch (Exception e5) {
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

    public DJIDecodeServerApi21(Looper looper, @NonNull DJIVideoDecoder decoder) {
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
        initVideoDecoder(true, true);
    }

    /* access modifiers changed from: protected */
    public void onCodecStarted() {
    }

    /* access modifiers changed from: protected */
    public void handleInOutMsg() {
    }

    /* access modifiers changed from: protected */
    public void queueInframe(@NonNull DJIDecodeServer.InputFrame inputFrame) {
        sendMessage(obtainMessage(1000, inputFrame));
    }
}
