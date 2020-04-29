package dji.midware.media;

import android.media.MediaCodec;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.DJIVideoUtil;
import dji.publics.DJIExecutor;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@EXClassNullAway
public class DJIVideoHardwareEncoder {
    private static final boolean DEBUG = false;
    private static final String MIME = "video/avc";
    private static final int MSG_QUEUE_FRAME_INTO_ENCODER = 0;
    private static final int MSG_RELEASE_ENCODER = 1;
    private static final int MSG_RESTART_ENCODER = 2;
    private static final String TAG = "DJIVideoHardwareEncoder";
    private MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
    DJIVideoUtil.TreeSetPool<ByteBuffer> byteBufferTreeSetPool = new DJIVideoUtil.TreeSetPool<>(5, new Comparator<ByteBuffer>() {
        /* class dji.midware.media.DJIVideoHardwareEncoder.AnonymousClass2 */

        public int compare(ByteBuffer buf0, ByteBuffer buf1) {
            if (buf0 == null) {
                if (buf1 == null) {
                    return 0;
                }
                return -1;
            } else if (buf1 == null) {
                return 1;
            } else {
                return buf0.capacity() - buf1.capacity();
            }
        }
    });
    private MediaCodec encoder;
    private Handler encoderHandler;
    private HandlerThread encoderHandlerThread;
    private Object encoderLock = new Object();
    public int height = 8;
    private ByteBuffer[] inputBuffers;
    private boolean isEncoderInited = false;
    /* access modifiers changed from: private */
    public List<VideoHardwareEncoderListener> listenerList = new LinkedList();
    /* access modifiers changed from: private */
    public List<VideoHardwareEncoderListener> listenersWaiting = new LinkedList();
    private Surface mInputSurface;
    private int originBitRate = 10485760;
    private int originKeyframeRate = 30;
    private ByteBuffer[] outputBuffers;
    public byte[] pps;
    public byte[] sps;
    public int width = 16;

    public interface VideoHardwareEncoderListener {
        void onEncodeData(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo, int i, int i2, boolean z);

        void onEncodeData(byte[] bArr, MediaCodec.BufferInfo bufferInfo, int i, int i2, boolean z);
    }

    public void addListener(VideoHardwareEncoderListener listener) {
        synchronized (this.listenersWaiting) {
            this.listenersWaiting.add(listener);
        }
    }

    public void removeListener(VideoHardwareEncoderListener listener) {
        synchronized (this.listenerList) {
            this.listenerList.remove(listener);
        }
    }

    public boolean isListenerListEmpty() {
        return this.listenerList.isEmpty();
    }

    /* access modifiers changed from: private */
    public void invokeOnEncodeData(byte[] data, MediaCodec.BufferInfo bufferInfo2, int width2, int height2, boolean isKeyFrame) {
        synchronized (this.listenerList) {
            for (VideoHardwareEncoderListener listener : this.listenerList) {
                if (listener != null) {
                    listener.onEncodeData(data, bufferInfo2, width2, height2, isKeyFrame);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void invokeOnEncodeData(ByteBuffer data, MediaCodec.BufferInfo bufferInfo2, int width2, int height2, boolean isKeyFrame) {
        synchronized (this.listenerList) {
            for (VideoHardwareEncoderListener listener : this.listenerList) {
                if (listener != null) {
                    data.position(bufferInfo2.offset);
                    data.limit(bufferInfo2.size - bufferInfo2.offset);
                    listener.onEncodeData(data, bufferInfo2, width2, height2, isKeyFrame);
                }
            }
        }
    }

    private void logd(String log) {
    }

    private class RawFrame {
        private byte[] data;
        private long updateTime = System.currentTimeMillis();

        public RawFrame(byte[] data2) {
            this.data = data2;
        }

        public byte[] getData() {
            return this.data;
        }

        public long getUpdateTime() {
            return this.updateTime;
        }
    }

    private void startEncodingHandler() {
        if (this.encoderHandler == null) {
            this.encoderHandlerThread = new HandlerThread("video hardware encoder thread");
            this.encoderHandlerThread.start();
            this.encoderHandler = new Handler(this.encoderHandlerThread.getLooper()) {
                /* class dji.midware.media.DJIVideoHardwareEncoder.AnonymousClass1 */

                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            try {
                                DJIVideoHardwareEncoder.this.dequeueDataFromEncoder();
                                sendEmptyMessageDelayed(0, 1);
                                return;
                            } catch (IllegalStateException e) {
                                Log.e(DJIVideoHardwareEncoder.TAG, "handleMessage: ", e);
                                return;
                            }
                        case 1:
                            DJIVideoHardwareEncoder.this.releaseEncoder();
                            removeCallbacksAndMessages(null);
                            DJIVideoHardwareEncoder.this.byteBufferTreeSetPool.clear();
                            new Thread(new Runnable() {
                                /* class dji.midware.media.DJIVideoHardwareEncoder.AnonymousClass1.AnonymousClass1 */

                                public void run() {
                                    DJIVideoHardwareEncoder.this.stopEncodingHandler();
                                }
                            }, "stop_hardware_encoding_thread").start();
                            return;
                        case 2:
                            DJIVideoHardwareEncoder.this.releaseEncoder();
                            removeCallbacksAndMessages(null);
                            DJIVideoHardwareEncoder.this.start(DJIVideoHardwareEncoder.this.width, DJIVideoHardwareEncoder.this.height);
                            return;
                        default:
                            return;
                    }
                }
            };
        }
    }

    /* access modifiers changed from: private */
    public void stopEncodingHandler() {
        if (this.encoderHandler != null) {
            this.encoderHandler.removeCallbacksAndMessages(null);
        }
        if (this.encoderHandlerThread != null && this.encoderHandlerThread.isAlive()) {
            if (Build.VERSION.SDK_INT >= 18) {
                this.encoderHandlerThread.quitSafely();
            } else {
                this.encoderHandlerThread.quit();
            }
            try {
                this.encoderHandlerThread.join(3000);
            } catch (InterruptedException e) {
                logd("stopEncodingHandler: encoder thread join error: " + e);
            }
            this.encoderHandlerThread = null;
            this.encoderHandler = null;
        }
    }

    public Surface getInputSurface() {
        return this.mInputSurface;
    }

    public void start(int width2, int height2) {
        start(width2, height2, this.originKeyframeRate, this.originBitRate);
    }

    public void start(int width2, int height2, int keyFrameRate) {
        start(width2, height2, keyFrameRate, this.originBitRate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00ca, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00d3, code lost:
        android.util.Log.e(dji.midware.media.DJIVideoHardwareEncoder.TAG, "start: create input surface error: \n" + ((android.media.MediaCodec.CodecException) r3).getDiagnosticInfo());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00f5, code lost:
        android.util.Log.e(dji.midware.media.DJIVideoHardwareEncoder.TAG, "start: create input surface error", r3);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    @android.annotation.TargetApi(21)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void start(int r12, int r13, int r14, int r15) {
        /*
            r11 = this;
            r10 = 21
            java.lang.Object r6 = r11.encoderLock
            monitor-enter(r6)
            r11.originKeyframeRate = r14     // Catch:{ all -> 0x0091 }
            r11.originBitRate = r15     // Catch:{ all -> 0x0091 }
            android.media.MediaCodec r5 = r11.encoder     // Catch:{ all -> 0x0091 }
            if (r5 == 0) goto L_0x0010
            r11.stop()     // Catch:{ all -> 0x0091 }
        L_0x0010:
            r11.startEncodingHandler()     // Catch:{ all -> 0x0091 }
            java.lang.String r5 = "encoder start"
            r11.logd(r5)     // Catch:{ all -> 0x0091 }
            r11.width = r12     // Catch:{ all -> 0x0091 }
            r11.height = r13     // Catch:{ all -> 0x0091 }
            java.lang.String r5 = "video/avc"
            android.media.MediaFormat r4 = android.media.MediaFormat.createVideoFormat(r5, r12, r13)     // Catch:{ all -> 0x0091 }
            r2 = 0
            java.lang.String r5 = "video/avc"
            android.media.MediaCodec r5 = android.media.MediaCodec.createEncoderByType(r5)     // Catch:{ IOException -> 0x0085 }
            r11.encoder = r5     // Catch:{ IOException -> 0x0085 }
            android.media.MediaCodec r5 = r11.encoder     // Catch:{ IOException -> 0x0085 }
            android.media.MediaCodecInfo r2 = r5.getCodecInfo()     // Catch:{ IOException -> 0x0085 }
            java.lang.String r5 = "color-format"
            r7 = 2130708361(0x7f000789, float:1.701803E38)
            r4.setInteger(r5, r7)     // Catch:{ all -> 0x0091 }
            java.lang.String r5 = "bitrate"
            int r7 = r11.originBitRate     // Catch:{ all -> 0x0091 }
            r4.setInteger(r5, r7)     // Catch:{ all -> 0x0091 }
            java.lang.String r5 = "frame-rate"
            int r7 = r11.originKeyframeRate     // Catch:{ all -> 0x0091 }
            r4.setInteger(r5, r7)     // Catch:{ all -> 0x0091 }
            java.lang.String r5 = "i-frame-interval"
            r7 = 1
            r4.setInteger(r5, r7)     // Catch:{ all -> 0x0091 }
            android.media.MediaCodec r5 = r11.encoder     // Catch:{ Exception -> 0x0094 }
            r7 = 0
            r8 = 0
            r9 = 1
            r5.configure(r4, r7, r8, r9)     // Catch:{ Exception -> 0x0094 }
        L_0x005c:
            android.media.MediaCodec r5 = r11.encoder     // Catch:{ Exception -> 0x00ca }
            android.view.Surface r5 = r5.createInputSurface()     // Catch:{ Exception -> 0x00ca }
            r11.mInputSurface = r5     // Catch:{ Exception -> 0x00ca }
        L_0x0064:
            android.media.MediaCodec r5 = r11.encoder     // Catch:{ all -> 0x0091 }
            r5.start()     // Catch:{ all -> 0x0091 }
            r5 = 1
            r11.isEncoderInited = r5     // Catch:{ all -> 0x0091 }
            android.media.MediaCodec r5 = r11.encoder     // Catch:{ all -> 0x0091 }
            java.nio.ByteBuffer[] r5 = r5.getOutputBuffers()     // Catch:{ all -> 0x0091 }
            r11.outputBuffers = r5     // Catch:{ all -> 0x0091 }
            android.os.Handler r5 = r11.encoderHandler     // Catch:{ all -> 0x0091 }
            r7 = 0
            boolean r5 = r5.hasMessages(r7)     // Catch:{ all -> 0x0091 }
            if (r5 != 0) goto L_0x0083
            android.os.Handler r5 = r11.encoderHandler     // Catch:{ all -> 0x0091 }
            r7 = 0
            r5.sendEmptyMessage(r7)     // Catch:{ all -> 0x0091 }
        L_0x0083:
            monitor-exit(r6)     // Catch:{ all -> 0x0091 }
        L_0x0084:
            return
        L_0x0085:
            r3 = move-exception
            java.lang.String r5 = "DJIVideoHardwareEncoder"
            java.lang.String r7 = "start: create MediaCodec error: "
            android.util.Log.e(r5, r7, r3)     // Catch:{ all -> 0x0091 }
            monitor-exit(r6)     // Catch:{ all -> 0x0091 }
            goto L_0x0084
        L_0x0091:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0091 }
            throw r5
        L_0x0094:
            r3 = move-exception
            int r5 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0091 }
            if (r5 < r10) goto L_0x00c0
            boolean r5 = r3 instanceof android.media.MediaCodec.CodecException     // Catch:{ all -> 0x0091 }
            if (r5 == 0) goto L_0x00c0
            r0 = r3
            android.media.MediaCodec$CodecException r0 = (android.media.MediaCodec.CodecException) r0     // Catch:{ all -> 0x0091 }
            r1 = r0
            java.lang.String r5 = "DJIVideoHardwareEncoder"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0091 }
            r7.<init>()     // Catch:{ all -> 0x0091 }
            java.lang.String r8 = "asstart: encoder configure codec exception: \n"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ all -> 0x0091 }
            java.lang.String r8 = r1.getDiagnosticInfo()     // Catch:{ all -> 0x0091 }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ all -> 0x0091 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0091 }
            android.util.Log.e(r5, r7)     // Catch:{ all -> 0x0091 }
            goto L_0x005c
        L_0x00c0:
            java.lang.String r5 = "DJIVideoHardwareEncoder"
            java.lang.String r7 = "start: encoder configure error"
            android.util.Log.e(r5, r7, r3)     // Catch:{ all -> 0x0091 }
            goto L_0x005c
        L_0x00ca:
            r3 = move-exception
            int r5 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0091 }
            if (r5 < r10) goto L_0x00f5
            boolean r5 = r3 instanceof android.media.MediaCodec.CodecException     // Catch:{ all -> 0x0091 }
            if (r5 == 0) goto L_0x00f5
            r0 = r3
            android.media.MediaCodec$CodecException r0 = (android.media.MediaCodec.CodecException) r0     // Catch:{ all -> 0x0091 }
            r1 = r0
            java.lang.String r5 = "DJIVideoHardwareEncoder"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0091 }
            r7.<init>()     // Catch:{ all -> 0x0091 }
            java.lang.String r8 = "start: create input surface error: \n"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ all -> 0x0091 }
            java.lang.String r8 = r1.getDiagnosticInfo()     // Catch:{ all -> 0x0091 }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ all -> 0x0091 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0091 }
            android.util.Log.e(r5, r7)     // Catch:{ all -> 0x0091 }
        L_0x00f5:
            java.lang.String r5 = "DJIVideoHardwareEncoder"
            java.lang.String r7 = "start: create input surface error"
            android.util.Log.e(r5, r7, r3)     // Catch:{ all -> 0x0091 }
            goto L_0x0064
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.DJIVideoHardwareEncoder.start(int, int, int, int):void");
    }

    public boolean isEncoding() {
        return (this.encoderHandler == null || this.encoderHandlerThread == null || !this.encoderHandlerThread.isAlive()) ? false : true;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, boolean):void}
     arg types: [int, int]
     candidates:
      ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, long):void}
      ClspMth{android.media.MediaCodec.releaseOutputBuffer(int, boolean):void} */
    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dequeueDataFromEncoder() throws java.lang.IllegalStateException {
        /*
            r8 = this;
            java.lang.Object r4 = r8.encoderLock
            monitor-enter(r4)
            android.media.MediaCodec r3 = r8.encoder     // Catch:{ all -> 0x0030 }
            if (r3 == 0) goto L_0x000b
            boolean r3 = r8.isEncoderInited     // Catch:{ all -> 0x0030 }
            if (r3 != 0) goto L_0x000d
        L_0x000b:
            monitor-exit(r4)     // Catch:{ all -> 0x0030 }
        L_0x000c:
            return
        L_0x000d:
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            android.media.MediaCodec r3 = r8.encoder     // Catch:{ Exception -> 0x0033 }
            android.media.MediaCodec$BufferInfo r5 = r8.bufferInfo     // Catch:{ Exception -> 0x0033 }
            r6 = 0
            int r2 = r3.dequeueOutputBuffer(r5, r6)     // Catch:{ Exception -> 0x0033 }
        L_0x0019:
            if (r2 < 0) goto L_0x003e
            java.nio.ByteBuffer[] r3 = r8.outputBuffers     // Catch:{ all -> 0x0030 }
            r1 = r3[r2]     // Catch:{ all -> 0x0030 }
            android.media.MediaCodec$BufferInfo r3 = r8.bufferInfo     // Catch:{ all -> 0x0030 }
            r8.onOutputFrame(r1, r3)     // Catch:{ all -> 0x0030 }
            android.media.MediaCodec r3 = r8.encoder     // Catch:{ all -> 0x0030 }
            if (r3 == 0) goto L_0x002e
            android.media.MediaCodec r3 = r8.encoder     // Catch:{ all -> 0x0030 }
            r5 = 0
            r3.releaseOutputBuffer(r2, r5)     // Catch:{ all -> 0x0030 }
        L_0x002e:
            monitor-exit(r4)     // Catch:{ all -> 0x0030 }
            goto L_0x000c
        L_0x0030:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0030 }
            throw r3
        L_0x0033:
            r0 = move-exception
            java.lang.String r3 = "DJIVideoHardwareEncoder"
            java.lang.String r5 = "dequeueDataFromEncoder: dequeue output buffer error: "
            android.util.Log.e(r3, r5, r0)     // Catch:{ all -> 0x0030 }
            goto L_0x0019
        L_0x003e:
            r3 = -3
            if (r2 != r3) goto L_0x0045
            r8.onOutputBufferChanged()     // Catch:{ all -> 0x0030 }
            goto L_0x002e
        L_0x0045:
            r3 = -2
            if (r2 != r3) goto L_0x002e
            r8.onOutputFormatChanged()     // Catch:{ all -> 0x0030 }
            goto L_0x002e
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.DJIVideoHardwareEncoder.dequeueDataFromEncoder():void");
    }

    public static byte[] byteArrAdd(byte[] a, byte[] b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        byte[] rst = new byte[(a.length + b.length)];
        System.arraycopy(a, 0, rst, 0, a.length);
        System.arraycopy(b, 0, rst, a.length, b.length);
        return rst;
    }

    private static int findPpsHeadIndex(byte[] spspps) {
        if (spspps.length < 3) {
            return -1;
        }
        for (int i = 0; i < spspps.length - 3; i++) {
            if (spspps[i] == 0 && spspps[i + 1] == 0) {
                if (spspps[i + 2] == 0) {
                    if (spspps[i + 3] == 1 && (spspps[i + 4] & 31) == 8) {
                        return i;
                    }
                } else if (spspps[i + 2] == 1 && (spspps[i + 4] & 31) == 8) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void onOutputFrame(ByteBuffer buffer, MediaCodec.BufferInfo bufferInfo2) {
        byte[] rst;
        final boolean isKeyfram = true;
        buffer.position(bufferInfo2.offset);
        buffer.limit(bufferInfo2.size - bufferInfo2.offset);
        byte[] data = new byte[(bufferInfo2.size - bufferInfo2.offset)];
        buffer.get(data);
        if ((bufferInfo2.flags & 1) != 1) {
            isKeyfram = false;
        }
        if (data == null || data.length <= 5 || (data[4] & 31) != 7) {
            if (isKeyfram) {
                rst = byteArrAdd(byteArrAdd(this.sps, this.pps), data);
            } else {
                rst = data;
            }
            buffer.position(bufferInfo2.offset);
            buffer.limit(bufferInfo2.size - bufferInfo2.offset);
            ByteBuffer outputBuf = this.byteBufferTreeSetPool.obtain(buffer);
            if (outputBuf == null) {
                outputBuf = ByteBuffer.allocate(buffer.capacity());
            }
            outputBuf.clear();
            outputBuf.put(buffer);
            final ByteBuffer finalOutputBuf = outputBuf;
            final byte[] finalRst = rst;
            final MediaCodec.BufferInfo finalBufferInfo = new MediaCodec.BufferInfo();
            finalBufferInfo.presentationTimeUs = bufferInfo2.presentationTimeUs;
            finalBufferInfo.size = bufferInfo2.size;
            finalBufferInfo.offset = bufferInfo2.offset;
            finalBufferInfo.flags = bufferInfo2.flags;
            DJIExecutor.getExecutor().execute(new Runnable() {
                /* class dji.midware.media.DJIVideoHardwareEncoder.AnonymousClass3 */

                public void run() {
                    if (isKeyfram && !DJIVideoHardwareEncoder.this.listenersWaiting.isEmpty()) {
                        synchronized (DJIVideoHardwareEncoder.this.listenersWaiting) {
                            DJIVideoHardwareEncoder.this.listenerList.addAll(DJIVideoHardwareEncoder.this.listenersWaiting);
                            DJIVideoHardwareEncoder.this.listenersWaiting.clear();
                        }
                    }
                    DJIVideoHardwareEncoder.this.invokeOnEncodeData(finalOutputBuf, finalBufferInfo, DJIVideoHardwareEncoder.this.width, DJIVideoHardwareEncoder.this.height, isKeyfram);
                    DJIVideoHardwareEncoder.this.invokeOnEncodeData(finalRst, finalBufferInfo, DJIVideoHardwareEncoder.this.width, DJIVideoHardwareEncoder.this.height, isKeyfram);
                    DJIVideoHardwareEncoder.this.byteBufferTreeSetPool.recycle(finalOutputBuf);
                }
            });
            return;
        }
        int ppspos = findPpsHeadIndex(data);
        this.sps = new byte[ppspos];
        this.pps = new byte[(data.length - ppspos)];
        System.arraycopy(data, 0, this.sps, 0, this.sps.length);
        System.arraycopy(data, ppspos, this.pps, 0, this.pps.length);
    }

    private void onOutputBufferChanged() {
        this.outputBuffers = this.encoder.getOutputBuffers();
    }

    private void onOutputFormatChanged() {
        Log.e(TAG, "dequeueOutputBuffer INFO_OUTPUT_FORMAT_CHANGED");
    }

    /* access modifiers changed from: private */
    public void releaseEncoder() {
        synchronized (this.encoderLock) {
            logd("encoder release");
            if (this.encoder != null) {
                try {
                    this.encoder.flush();
                    this.encoder.release();
                    this.encoder = null;
                    this.isEncoderInited = false;
                    if (this.mInputSurface != null) {
                        this.mInputSurface.release();
                        this.mInputSurface = null;
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    this.encoder = null;
                    this.isEncoderInited = false;
                    if (this.mInputSurface != null) {
                        this.mInputSurface.release();
                        this.mInputSurface = null;
                    }
                } catch (Throwable th) {
                    this.encoder = null;
                    this.isEncoderInited = false;
                    if (this.mInputSurface != null) {
                        this.mInputSurface.release();
                        this.mInputSurface = null;
                    }
                    throw th;
                }
            }
        }
    }

    public void stop() {
        if (this.encoderHandler != null) {
            this.encoderHandler.sendEmptyMessage(1);
        }
    }

    public void pause() {
        stopEncodingHandler();
    }

    public void resume() {
        startEncodingHandler();
    }
}
