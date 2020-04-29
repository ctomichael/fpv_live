package dji.midware.util.save;

import dji.log.DJILogHelper;
import dji.midware.media.DJIVideoUtil;
import dji.midware.util.BytesUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class VideoFrameObserver {
    private static final boolean ENABLE = false;
    private static final int H264_PARSE_KEY_OFFSET = 5;
    private static final int INFO_MAP_MAX_SIZE = 300;
    private static final String TAG = "VideoFrameObserver";
    private static VideoFrameObserver instance = null;
    private HashMap<Long, VideoFrameInfo> frameInfoMap = new HashMap<>();
    private ReentrantReadWriteLock frameInfoMapKeyLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock frameInfoMapKeyRLock = this.frameInfoMapKeyLock.readLock();
    private ReentrantReadWriteLock.WriteLock frameInfoMapKeyWLock = this.frameInfoMapKeyLock.writeLock();
    private Queue<Long> keyQueue = new LinkedList();

    public enum TimeSavingPoint {
        UsbGetBody,
        VideoDataRecv,
        BeforeQueueToCodec,
        OutputFrame
    }

    public interface VideoFrameObservingListener {
        void onProcessComplete(VideoFrameInfo videoFrameInfo);
    }

    private VideoFrameObserver() {
    }

    public static VideoFrameObserver getInstance() {
        if (instance == null) {
            instance = new VideoFrameObserver();
        }
        return instance;
    }

    public static void destroy() {
        if (instance != null) {
            instance.clean();
        }
        instance = null;
    }

    public static class VideoFrameInfo {
        public long comPts = -1;
        public long key;
        public long[] timeStampArr = new long[TimeSavingPoint.values().length];

        public VideoFrameInfo(long key2) {
            this.key = key2;
        }

        public void setTimeStamp(TimeSavingPoint timeSavingPoint, long timeStamp) {
            this.timeStampArr[timeSavingPoint.ordinal()] = timeStamp;
        }

        public void setTimeStamp(TimeSavingPoint timeSavingPoint) {
            setTimeStamp(timeSavingPoint, System.currentTimeMillis());
        }

        public long getTimeStamp(TimeSavingPoint timeSavingPoint) {
            return this.timeStampArr[timeSavingPoint.ordinal()];
        }
    }

    public VideoFrameInfo saveTimeStamp(TimeSavingPoint savingPoint, byte[] data, int offset, long comPts, boolean isProcessComplete) {
        return null;
    }

    public VideoFrameInfo saveTimeStamp(TimeSavingPoint savingPoint, byte[] data, int offset, boolean isProcessComplete) {
        return saveTimeStamp(savingPoint, data, offset, -1, isProcessComplete);
    }

    public VideoFrameInfo saveTimeStamp(TimeSavingPoint savingPoint, long comPts, boolean isProcessComplete) {
        return null;
    }

    public void clean() {
        this.frameInfoMapKeyWLock.lock();
        try {
            this.frameInfoMap.clear();
            this.keyQueue.clear();
        } finally {
            this.frameInfoMapKeyWLock.unlock();
        }
    }

    private long getKey(byte[] data, int offset) {
        if (data == null || data.length - offset <= 0) {
            return 0;
        }
        byte[] cache = new byte[Math.min(data.length - offset, 8)];
        System.arraycopy(data, Math.min(data.length - cache.length, offset + 5), cache, 0, cache.length);
        return BytesUtil.getLong(cache);
    }

    private static boolean isContainSubArr(byte[] src, byte[] sub, int offset) {
        if (src == null || sub == null || sub.length + offset >= src.length) {
            return false;
        }
        return Arrays.equals(Arrays.copyOfRange(src, offset, sub.length + offset), sub);
    }

    private int getAudLength(byte[] data, int offset) {
        byte[][] bArr = DJIVideoUtil.AUD_ARR;
        for (byte[] audArr : bArr) {
            if (isContainSubArr(data, audArr, offset)) {
                return audArr.length;
            }
        }
        return 0;
    }

    private String getByteString(byte[] data, int offset, int length) {
        if (data == null || data.length <= 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        int limit = Math.min(length, data.length) + offset;
        for (int i = offset; i < limit; i++) {
            sb.append(Integer.toHexString(data[i] & 255));
            if (i != limit - 1) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private void addFrameInfoToMap(long key, VideoFrameInfo frameInfo) {
        this.frameInfoMapKeyWLock.lock();
        try {
            if (this.frameInfoMap.size() >= 300) {
                clean();
            }
            boolean needEnqueue = !this.frameInfoMap.containsKey(Long.valueOf(key));
            this.frameInfoMap.put(Long.valueOf(key), frameInfo);
            if (needEnqueue) {
                this.keyQueue.offer(Long.valueOf(key));
            }
        } finally {
            this.frameInfoMapKeyWLock.unlock();
        }
    }

    private VideoFrameInfo pollFrameInfo(long key) {
        this.frameInfoMapKeyWLock.lock();
        while (!this.keyQueue.isEmpty()) {
            try {
                long keyInQueue = this.keyQueue.poll().longValue();
                VideoFrameInfo frameInfo = this.frameInfoMap.remove(Long.valueOf(keyInQueue));
                if (keyInQueue == key) {
                    if (frameInfo != null) {
                        long usbTime = frameInfo.getTimeStamp(TimeSavingPoint.UsbGetBody);
                        long videoRecvTime = frameInfo.getTimeStamp(TimeSavingPoint.VideoDataRecv);
                        long beforeDecodeTime = frameInfo.getTimeStamp(TimeSavingPoint.BeforeQueueToCodec);
                        long displayTime = frameInfo.getTimeStamp(TimeSavingPoint.OutputFrame);
                        DJILogHelper.getInstance().LOGD(TAG, "pollFrameInfo: parseCost\t" + (videoRecvTime - usbTime) + "\tbeforeDecodeCost\t" + (beforeDecodeTime - videoRecvTime) + "\tdecodeCost\t" + (displayTime - beforeDecodeTime) + " totalCost\t" + (displayTime - usbTime));
                    }
                    return frameInfo;
                } else if (frameInfo.getTimeStamp(TimeSavingPoint.VideoDataRecv) > 0) {
                    DJILogHelper.getInstance().LOGD(TAG, "pollFrameInfo: strange ");
                }
            } finally {
                this.frameInfoMapKeyWLock.unlock();
            }
        }
        this.frameInfoMapKeyWLock.unlock();
        return null;
    }
}
