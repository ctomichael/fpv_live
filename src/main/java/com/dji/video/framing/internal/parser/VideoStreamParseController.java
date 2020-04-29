package com.dji.video.framing.internal.parser;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.dji.video.framing.DJIVideoHEVCFomatManager;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.VideoFrame;
import com.dji.video.framing.internal.parser.IFrameParser;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class VideoStreamParseController {
    private static final int MSG_INVOKE_CALLBACK = 0;
    private static final String TAG = "VideoStreamParseControl";
    private int fps;
    private long frameIndex;
    private int frameQueueSizeLimit;
    /* access modifiers changed from: private */
    public Queue<VideoFrame> framesCacheQueue;
    private AtomicBoolean isNotifyOutput;
    private IFrameParser.OnFrameParserListener mNormalFrameParserListener;
    private IFrameParser mNormalParser;
    private IFrameParser.OnFrameParserListener mSecondFrameParserListener;
    private IFrameParser mSecondParser;
    /* access modifiers changed from: private */
    public FrameDataOutputCallback outputCallback;
    private Handler outputHandler;
    private OutputMode outputMode;
    private ScheduledExecutorService scheduledExecutorService;

    public enum OutputMode {
        Directly,
        FpsFromSps,
        FpsFromUserSetting,
        Notify
    }

    public interface FrameDataOutputCallback {
        boolean onFrameOutput(VideoFrame videoFrame);
    }

    public void setOutputCallback(FrameDataOutputCallback outputCallback2) {
        this.outputCallback = outputCallback2;
    }

    /* access modifiers changed from: private */
    public void invokeOutputCallback(VideoFrame frame) {
        if (this.outputHandler != null) {
            this.outputHandler.obtainMessage(0, frame).sendToTarget();
        } else if (this.outputCallback != null && this.outputCallback.onFrameOutput(frame)) {
            frame.recycle();
        }
    }

    public int getFps() {
        return this.fps;
    }

    public void setFps(int fps2) {
        if (this.outputMode == OutputMode.FpsFromUserSetting) {
            _setFps(fps2);
        }
    }

    private void _setFps(int fps2) {
        if (this.fps != fps2) {
            this.fps = fps2;
            if (this.fps > 0) {
                restartTimer();
            } else {
                stopTimer();
            }
        }
    }

    public OutputMode getOutputMode() {
        return this.outputMode;
    }

    public void setOutputMode(OutputMode outputMode2) {
        if (this.outputMode != outputMode2) {
            if (outputMode2 == OutputMode.Directly || outputMode2 == OutputMode.Notify) {
                stopTimer();
            } else if (this.scheduledExecutorService == null) {
                startTimer();
            }
            if (outputMode2 == OutputMode.Directly && !this.framesCacheQueue.isEmpty()) {
                pollAllFrames();
            }
            this.outputMode = outputMode2;
        }
    }

    public int getFrameQueueSizeLimit() {
        return this.frameQueueSizeLimit;
    }

    public void setFrameQueueSizeLimit(int frameQueueSizeLimit2) {
        this.frameQueueSizeLimit = frameQueueSizeLimit2;
    }

    public VideoStreamParseController(OutputMode outputMode2, int frameQueueSizeLimit2) {
        this.framesCacheQueue = new LinkedList();
        this.fps = -1;
        this.frameIndex = 0;
        this.frameQueueSizeLimit = 60;
        this.outputMode = OutputMode.Directly;
        this.isNotifyOutput = new AtomicBoolean(false);
        this.mNormalFrameParserListener = new VideoStreamParseController$$Lambda$0(this);
        this.mSecondFrameParserListener = new VideoStreamParseController$$Lambda$1(this);
        this.outputMode = outputMode2;
        this.frameQueueSizeLimit = frameQueueSizeLimit2;
        init();
    }

    public VideoStreamParseController(OutputMode outputMode2, int frameQueueSizeLimit2, Looper looper) {
        this(outputMode2, frameQueueSizeLimit2);
        this.outputHandler = new Handler(looper) {
            /* class com.dji.video.framing.internal.parser.VideoStreamParseController.AnonymousClass1 */

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        VideoFrame frame = (VideoFrame) msg.obj;
                        if (VideoStreamParseController.this.outputCallback != null && VideoStreamParseController.this.outputCallback.onFrameOutput(frame)) {
                            frame.recycle();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
    }

    private void init() {
        this.mNormalParser = IFrameParser.CREATOR.create(false);
        this.mNormalParser.setFrameParserListener(this.mNormalFrameParserListener);
        if (DJIVideoHEVCFomatManager.getInstance().isSupportHevcMode()) {
            this.mSecondParser = IFrameParser.CREATOR.create(true);
            this.mSecondParser.setFrameParserListener(this.mSecondFrameParserListener);
        }
    }

    public void resetCheckStatus() {
        if (this.mNormalParser != null) {
            this.mNormalParser.resetCheckStatus();
        }
        if (this.mSecondParser != null) {
            this.mSecondParser.resetCheckStatus();
        }
    }

    public void destroy() {
        if (this.outputHandler != null) {
            this.outputHandler.removeCallbacksAndMessages(null);
        }
        if (this.mNormalParser != null) {
            this.mNormalParser.stop();
        }
        if (this.mSecondParser != null) {
            this.mSecondParser.stop();
        }
        stopTimer();
    }

    private void startTimer() {
        if (this.scheduledExecutorService == null && this.fps > 0) {
            VideoLog.d(TAG, "startTimer: fps=" + this.fps, new Object[0]);
            this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            this.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                /* class com.dji.video.framing.internal.parser.VideoStreamParseController.AnonymousClass2 */

                public void run() {
                    VideoFrame frame = (VideoFrame) VideoStreamParseController.this.framesCacheQueue.poll();
                    VideoLog.d(VideoStreamParseController.TAG, "run: timer frame out: frame=" + frame, new Object[0]);
                    if (frame != null) {
                        VideoStreamParseController.this.invokeOutputCallback(frame);
                    }
                }
            }, 0, (long) (1000 / this.fps), TimeUnit.MILLISECONDS);
        }
    }

    private void stopTimer() {
        if (this.scheduledExecutorService != null) {
            this.scheduledExecutorService.shutdown();
            this.scheduledExecutorService = null;
        }
    }

    private void restartTimer() {
        stopTimer();
        startTimer();
    }

    public int getCachedFrameNum() {
        return this.framesCacheQueue.size();
    }

    public void feedData(byte[] data, int offset, int len) {
        if (!DJIVideoHEVCFomatManager.getInstance().isInHevcMode()) {
            if (this.mNormalParser != null) {
                this.mNormalParser.feedData(data, offset, len);
            }
        } else if (this.mSecondParser != null) {
            this.mSecondParser.feedData(data, offset, len);
        }
    }

    private void offerFrameIntoQueue(VideoFrame frame) {
        while (this.framesCacheQueue.size() >= this.frameQueueSizeLimit) {
            this.framesCacheQueue.poll();
        }
        this.framesCacheQueue.offer(frame);
    }

    private void pollAllFrames() {
        while (!this.framesCacheQueue.isEmpty()) {
            VideoFrame frame = this.framesCacheQueue.poll();
            if (frame != null) {
                invokeOutputCallback(frame);
            }
        }
    }

    public void notifyOutputFrame() {
        if (this.outputMode == OutputMode.Notify) {
            VideoFrame frame = this.framesCacheQueue.poll();
            if (frame != null) {
                invokeOutputCallback(frame);
            } else {
                this.isNotifyOutput.set(true);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$VideoStreamParseController(byte[] videoBuffer, int size, boolean isKeyFrame, int width, int height, int frameNum, int spsPos, int spsLen, int ppsPos, int ppsLen, int fps2, boolean isHevcFrame) {
        if (!DJIVideoHEVCFomatManager.getInstance().isInHevcMode()) {
            onRecFrameData(videoBuffer, size, isKeyFrame, width, height, frameNum, spsPos, spsLen, ppsPos, ppsLen, fps2, false);
        } else {
            VideoLog.w(TAG, "mNormalFrameParserListener frame not match", new Object[0]);
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$1$VideoStreamParseController(byte[] videoBuffer, int size, boolean isKeyFrame, int width, int height, int frameNum, int spsPos, int spsLen, int ppsPos, int ppsLen, int fps2, boolean isHevcFrame) {
        if (DJIVideoHEVCFomatManager.getInstance().isInHevcMode()) {
            onRecFrameData(videoBuffer, size, isKeyFrame, width, height, frameNum, spsPos, spsLen, ppsPos, ppsLen, fps2, true);
        } else {
            VideoLog.w(TAG, "mSecondFrameParserListener frame not match", new Object[0]);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0078  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onRecFrameData(byte[] r20, int r21, boolean r22, int r23, int r24, int r25, int r26, int r27, int r28, int r29, int r30, boolean r31) {
        /*
            r19 = this;
            long r2 = java.lang.System.currentTimeMillis()
            r0 = r19
            long r4 = r0.frameIndex
            r0 = r25
            long r6 = (long) r0
            long r4 = com.dji.video.framing.utils.DJIVideoUtil.getComprehensivePts(r2, r4, r6)
            r0 = r19
            long r10 = r0.frameIndex
            r2 = r20
            r3 = r21
            r6 = r22
            r7 = r23
            r8 = r24
            r9 = r25
            r12 = r26
            r13 = r27
            r14 = r28
            r15 = r29
            r16 = r30
            r17 = r31
            com.dji.video.framing.internal.VideoFrame r18 = com.dji.video.framing.internal.VideoFrame.obtain(r2, r3, r4, r6, r7, r8, r9, r10, r12, r13, r14, r15, r16, r17)
            r0 = r19
            long r2 = r0.frameIndex
            r4 = 1
            long r2 = r2 + r4
            r0 = r19
            r0.frameIndex = r2
            int[] r2 = com.dji.video.framing.internal.parser.VideoStreamParseController.AnonymousClass3.$SwitchMap$com$dji$video$framing$internal$parser$VideoStreamParseController$OutputMode
            r0 = r19
            com.dji.video.framing.internal.parser.VideoStreamParseController$OutputMode r3 = r0.outputMode
            int r3 = r3.ordinal()
            r2 = r2[r3]
            switch(r2) {
                case 1: goto L_0x004a;
                case 2: goto L_0x0065;
                case 3: goto L_0x0076;
                case 4: goto L_0x0080;
                default: goto L_0x0049;
            }
        L_0x0049:
            return
        L_0x004a:
            r0 = r19
            java.util.concurrent.atomic.AtomicBoolean r2 = r0.isNotifyOutput
            r3 = 0
            boolean r2 = r2.getAndSet(r3)
            if (r2 == 0) goto L_0x005d
            r0 = r19
            r1 = r18
            r0.invokeOutputCallback(r1)
            goto L_0x0049
        L_0x005d:
            r0 = r19
            r1 = r18
            r0.offerFrameIntoQueue(r1)
            goto L_0x0049
        L_0x0065:
            r0 = r18
            int r2 = r0.fps
            r0 = r30
            if (r0 == r2) goto L_0x0076
            r0 = r18
            int r2 = r0.fps
            r0 = r19
            r0._setFps(r2)
        L_0x0076:
            if (r30 <= 0) goto L_0x0080
            r0 = r19
            r1 = r18
            r0.offerFrameIntoQueue(r1)
            goto L_0x0049
        L_0x0080:
            r0 = r19
            r1 = r18
            r0.invokeOutputCallback(r1)
            goto L_0x0049
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dji.video.framing.internal.parser.VideoStreamParseController.onRecFrameData(byte[], int, boolean, int, int, int, int, int, int, int, int, boolean):void");
    }
}
