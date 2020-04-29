package dji.midware.usb.P3;

import android.os.SystemClock;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.usb.P3.UsbAccessoryService;

public class VideoRawBufferReceiver {
    public static final String TAG = "VideoStream_Parse_Thread";
    /* access modifiers changed from: private */
    public volatile boolean isRunning;
    /* access modifiers changed from: private */
    public DJIServiceInterface mLinkService;
    private Runnable mParseVideoRunnable = new Runnable() {
        /* class dji.midware.usb.P3.VideoRawBufferReceiver.AnonymousClass1 */

        public void run() {
            boolean z = true;
            boolean unused = VideoRawBufferReceiver.this.isRunning = true;
            StringBuilder append = new StringBuilder().append(VideoRawBufferReceiver.this.mVideoStreamSource.name()).append(" ParseVideoRunnable ");
            if (!VideoRawBufferReceiver.this.mLinkService.isConnected() || !VideoRawBufferReceiver.this.isRunning) {
                z = false;
            }
            AoaLogUtil.printAndSave(append.append(z).toString());
            long last_log_time = -1;
            while (VideoRawBufferReceiver.this.mLinkService.isConnected() && VideoRawBufferReceiver.this.isRunning) {
                if (UsbAccessoryService.STREAM_DEBUG) {
                    if (last_log_time == -1) {
                        last_log_time = SystemClock.uptimeMillis();
                    }
                    if (SystemClock.uptimeMillis() - last_log_time > 1000) {
                        last_log_time = SystemClock.uptimeMillis();
                    }
                }
                UsbAccessoryService.VideoBufferInfo bufferInfo = VideoRawBufferReceiver.this.mVideoBufferQueue.dequeueData(-1);
                if (bufferInfo != null && bufferInfo.length > 0) {
                    VideoRawBufferReceiver.this.mVideoDataTransferor.toTransVideoData(bufferInfo.buffer, bufferInfo.length, VideoRawBufferReceiver.this.mVideoStreamSource);
                }
            }
            Thread unused2 = VideoRawBufferReceiver.this.mParseVideoThread = null;
            AoaLogUtil.printAndSave("ParseVideoRunnable.end");
        }
    };
    /* access modifiers changed from: private */
    public Thread mParseVideoThread;
    private String mThreadName;
    /* access modifiers changed from: private */
    public VideoBufferQueue mVideoBufferQueue = new VideoBufferQueue();
    /* access modifiers changed from: private */
    public VideoDataTransferor mVideoDataTransferor;
    /* access modifiers changed from: private */
    public UsbAccessoryService.VideoStreamSource mVideoStreamSource;

    public VideoRawBufferReceiver(UsbAccessoryService.VideoStreamSource videoStreamSource, DJIServiceInterface linkService, String threadName) {
        this.mVideoStreamSource = videoStreamSource;
        this.mLinkService = linkService;
        this.mThreadName = threadName;
    }

    public void setVideoDataTransferor(VideoDataTransferor videoDataTransferor) {
        this.mVideoDataTransferor = videoDataTransferor;
    }

    public void start() {
        this.mVideoBufferQueue.init();
        this.mParseVideoThread = new Thread(this.mParseVideoRunnable, this.mThreadName);
        this.mParseVideoThread.start();
    }

    public void stopAndNotify() {
        this.isRunning = false;
        this.mVideoBufferQueue.blockCondWaiting();
        this.mVideoBufferQueue.finalizeSelf();
    }

    public void offerData(byte[] buffer, int offset, int length) {
        this.mVideoBufferQueue.queueInData(buffer, offset, length);
    }
}
