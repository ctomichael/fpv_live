package dji.midware.usb.P3;

import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.log.WM220LogUtil;
import dji.midware.usb.P3.UsbAccessoryService;
import java.util.ArrayList;

public class VideoBufferQueue {
    private static String TAG = "VideoBufferQueue";
    private static final int VIDEO_LIST_SIZE = 300;
    private static byte[] fillZeroBuffer;
    private Object cond = new Object();
    private int getVideoIndex = 0;
    private volatile boolean mbHasInited = false;
    private int oneBufferSize = 30720;
    private int setVideoIndex = 0;
    private ArrayList<UsbAccessoryService.VideoBufferInfo> videoBufferInfoList = new ArrayList<>(300);
    private ArrayList<byte[]> videoList = new ArrayList<>(300);
    private ArrayList<Integer> videoSizeList = new ArrayList<>(300);

    public void init() {
        if (!this.mbHasInited) {
            if (this.videoList.size() == 0) {
                for (int i = 0; i < 300; i++) {
                    this.videoList.add(new byte[this.oneBufferSize]);
                    this.videoSizeList.add(0);
                    this.videoBufferInfoList.add(new UsbAccessoryService.VideoBufferInfo());
                    this.setVideoIndex = 0;
                    this.getVideoIndex = 0;
                }
            }
            this.mbHasInited = true;
        }
    }

    public void finalizeSelf() {
        if (this.mbHasInited) {
            this.videoList.clear();
            this.videoSizeList.clear();
            this.videoBufferInfoList.clear();
            this.mbHasInited = false;
        }
    }

    public boolean isEmpty() {
        return this.getVideoIndex == this.setVideoIndex;
    }

    public void queueInData(byte[] buffer, int offset, int length) {
        if (this.mbHasInited) {
            this.videoSizeList.set(this.setVideoIndex, Integer.valueOf(length));
            byte[] bytes = this.videoList.get(this.setVideoIndex);
            if (bytes == null) {
                DJILog.logWriteE(TAG, "queueInData get item buffer null!!! return!!!", TAG, new Object[0]);
                return;
            }
            if (length + 1024 > bytes.length) {
                DJILogHelper.getInstance().LOGD(TAG, "putVideoBuffer length超过100k=" + ((((float) length) * 1.0f) / 1024.0f), false, true);
                byte[] bytes2 = new byte[(length + 1024)];
                this.videoList.set(this.setVideoIndex, bytes2);
                System.arraycopy(buffer, offset, bytes2, 0, length);
            } else {
                if (length + 1024 >= this.oneBufferSize || bytes.length <= this.oneBufferSize) {
                    if (fillZeroBuffer == null) {
                        fillZeroBuffer = new byte[1024];
                    }
                    System.arraycopy(fillZeroBuffer, 0, bytes, length, 1024);
                } else {
                    DJILogHelper.getInstance().LOGD(TAG, "putVideoBuffer length超过100k=" + ((((float) length) * 1.0f) / 1024.0f), false, true);
                    bytes = new byte[this.oneBufferSize];
                    this.videoList.set(this.setVideoIndex, bytes);
                }
                System.arraycopy(buffer, offset, bytes, 0, length);
            }
            if (this.getVideoIndex - this.setVideoIndex > 0 && this.getVideoIndex - this.setVideoIndex < 4) {
                WM220LogUtil.LOGI("***getVideoIndex: " + this.getVideoIndex + " setVideoIndex: " + this.setVideoIndex + " length: " + length);
            }
            if (this.setVideoIndex == this.videoList.size() - 1) {
                this.setVideoIndex = 0;
            } else {
                this.setVideoIndex++;
                if (this.setVideoIndex == this.getVideoIndex) {
                    WM220LogUtil.LOGD("**set catch get");
                }
            }
            synchronized (this.cond) {
                this.cond.notifyAll();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001c, code lost:
        if (isEmpty() == false) goto L_0x001e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public dji.midware.usb.P3.UsbAccessoryService.VideoBufferInfo dequeueData(int r8) {
        /*
            r7 = this;
            r0 = 0
            boolean r2 = r7.mbHasInited
            if (r2 != 0) goto L_0x0006
        L_0x0005:
            return r0
        L_0x0006:
            boolean r2 = r7.isEmpty()
            if (r2 == 0) goto L_0x001e
            if (r8 <= 0) goto L_0x007a
            java.lang.Object r3 = r7.cond
            monitor-enter(r3)
            java.lang.Object r2 = r7.cond     // Catch:{ InterruptedException -> 0x0058 }
            long r4 = (long) r8     // Catch:{ InterruptedException -> 0x0058 }
            r2.wait(r4)     // Catch:{ InterruptedException -> 0x0058 }
        L_0x0017:
            monitor-exit(r3)     // Catch:{ all -> 0x0077 }
        L_0x0018:
            boolean r2 = r7.isEmpty()
            if (r2 != 0) goto L_0x0005
        L_0x001e:
            boolean r2 = r7.mbHasInited
            if (r2 == 0) goto L_0x0005
            java.util.ArrayList<dji.midware.usb.P3.UsbAccessoryService$VideoBufferInfo> r2 = r7.videoBufferInfoList
            int r3 = r7.getVideoIndex
            java.lang.Object r0 = r2.get(r3)
            dji.midware.usb.P3.UsbAccessoryService$VideoBufferInfo r0 = (dji.midware.usb.P3.UsbAccessoryService.VideoBufferInfo) r0
            java.util.ArrayList<byte[]> r2 = r7.videoList
            int r3 = r7.getVideoIndex
            java.lang.Object r2 = r2.get(r3)
            byte[] r2 = (byte[]) r2
            r0.buffer = r2
            java.util.ArrayList<java.lang.Integer> r2 = r7.videoSizeList
            int r3 = r7.getVideoIndex
            java.lang.Object r2 = r2.get(r3)
            java.lang.Integer r2 = (java.lang.Integer) r2
            int r2 = r2.intValue()
            r0.length = r2
            int r2 = r7.getVideoIndex
            java.util.ArrayList<byte[]> r3 = r7.videoList
            int r3 = r3.size()
            int r3 = r3 + -1
            if (r2 != r3) goto L_0x00a8
            r2 = 0
            r7.getVideoIndex = r2
            goto L_0x0005
        L_0x0058:
            r1 = move-exception
            dji.log.DJILogHelper r2 = dji.log.DJILogHelper.getInstance()     // Catch:{ all -> 0x0077 }
            java.lang.String r4 = dji.midware.usb.P3.VideoBufferQueue.TAG     // Catch:{ all -> 0x0077 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0077 }
            r5.<init>()     // Catch:{ all -> 0x0077 }
            java.lang.String r6 = "dequeueData: "
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x0077 }
            java.lang.StringBuilder r5 = r5.append(r1)     // Catch:{ all -> 0x0077 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0077 }
            r2.LOGE(r4, r5)     // Catch:{ all -> 0x0077 }
            goto L_0x0017
        L_0x0077:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0077 }
            throw r2
        L_0x007a:
            if (r8 >= 0) goto L_0x0018
            java.lang.Object r3 = r7.cond
            monitor-enter(r3)
            java.lang.Object r2 = r7.cond     // Catch:{ InterruptedException -> 0x0089 }
            r2.wait()     // Catch:{ InterruptedException -> 0x0089 }
        L_0x0084:
            monitor-exit(r3)     // Catch:{ all -> 0x0086 }
            goto L_0x0018
        L_0x0086:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0086 }
            throw r2
        L_0x0089:
            r1 = move-exception
            dji.log.DJILogHelper r2 = dji.log.DJILogHelper.getInstance()     // Catch:{ all -> 0x0086 }
            java.lang.String r4 = dji.midware.usb.P3.VideoBufferQueue.TAG     // Catch:{ all -> 0x0086 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0086 }
            r5.<init>()     // Catch:{ all -> 0x0086 }
            java.lang.String r6 = "dequeueData: "
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x0086 }
            java.lang.StringBuilder r5 = r5.append(r1)     // Catch:{ all -> 0x0086 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0086 }
            r2.LOGE(r4, r5)     // Catch:{ all -> 0x0086 }
            goto L_0x0084
        L_0x00a8:
            int r2 = r7.getVideoIndex
            int r2 = r2 + 1
            r7.getVideoIndex = r2
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.usb.P3.VideoBufferQueue.dequeueData(int):dji.midware.usb.P3.UsbAccessoryService$VideoBufferInfo");
    }

    public void blockCondWaiting() {
        synchronized (this.cond) {
            this.cond.notifyAll();
        }
    }
}
