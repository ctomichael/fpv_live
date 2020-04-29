package dji.midware.util.save;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import dji.midware.util.BytesUtil;
import dji.midware.util.save.TimeStampSaver;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TimeStampSavingManager {
    private static final int LISTENERS_INVOKING_INTERVAL = 1000;
    private static final int MSG_INVOKE_LISTENERS = 0;
    private static TimeStampSavingManager instance;
    private boolean isRunning = false;
    /* access modifiers changed from: private */
    public Handler listenerHandler;
    private List<TimeStampSavingListener> listenerList = new LinkedList();
    private Object listenerLock = new Object();
    private HandlerThread listenerThread;
    /* access modifiers changed from: private */
    public TimeStampSaver[] saverList = new TimeStampSaver[TimeStampSavePoint.values().length];

    public enum TimeStampSavePoint {
        UsbOnGetBodyStream,
        UsbPutVideoBuffer,
        UsbToTrans,
        VideoRecv,
        DecoderQueueIn,
        DecoderInput,
        GlYuvSurfaceDisplay,
        Unknown
    }

    public interface TimeStampSavingListener {
        void onDataUpdated(List<TimeStampSaver.TimeStampSaverData> list);
    }

    public static TimeStampSavingManager getInstance() {
        if (instance == null) {
            instance = new TimeStampSavingManager();
        }
        return instance;
    }

    private TimeStampSaver getSaverInstance(TimeStampSavePoint savePoint) {
        if (this.saverList[savePoint.ordinal()] == null) {
            this.saverList[savePoint.ordinal()] = new TimeStampSaver(savePoint);
        }
        return this.saverList[savePoint.ordinal()];
    }

    public void addListener(TimeStampSavingListener listener) {
        synchronized (this.listenerLock) {
            this.listenerList.add(listener);
        }
    }

    public void removeListener(TimeStampSavingListener listener) {
        synchronized (this.listenerLock) {
            this.listenerList.remove(listener);
        }
    }

    public void clearListeners() {
        synchronized (this.listenerLock) {
            this.listenerList.clear();
        }
    }

    /* access modifiers changed from: private */
    public void invokeListeners(List<TimeStampSaver.TimeStampSaverData> dataList) {
        synchronized (this.listenerLock) {
            for (TimeStampSavingListener listener : this.listenerList) {
                listener.onDataUpdated(dataList);
            }
        }
    }

    private void startListenerThread() {
        stopListenerThread();
        this.listenerThread = new HandlerThread("Timestamp_saver_listener");
        this.listenerThread.start();
        this.listenerHandler = new Handler(this.listenerThread.getLooper()) {
            /* class dji.midware.util.save.TimeStampSavingManager.AnonymousClass1 */

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        List<TimeStampSaver.TimeStampSaverData> dataList = new ArrayList<>(TimeStampSavePoint.values().length);
                        TimeStampSaver[] access$000 = TimeStampSavingManager.this.saverList;
                        for (TimeStampSaver saver : access$000) {
                            if (saver != null) {
                                dataList.add(saver.fetchData());
                            }
                        }
                        TimeStampSavingManager.this.invokeListeners(dataList);
                        if (TimeStampSavingManager.this.listenerHandler != null) {
                            TimeStampSavingManager.this.listenerHandler.sendEmptyMessageDelayed(0, 1000);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.listenerHandler.sendEmptyMessageDelayed(0, 1000);
    }

    private void stopListenerThread() {
        if (this.listenerHandler != null) {
            this.listenerHandler.removeCallbacksAndMessages(null);
            this.listenerHandler = null;
        }
        if (this.listenerThread != null && this.listenerThread.isAlive()) {
            if (Build.VERSION.SDK_INT >= 18) {
                this.listenerThread.quitSafely();
            } else {
                this.listenerThread.quit();
            }
            this.listenerThread = null;
        }
    }

    public void start() {
        startListenerThread();
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;
        stopListenerThread();
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void saveTimeStamp(TimeStampSavePoint savePoint, long key, long timeStampInMs) {
        if (isRunning()) {
            getSaverInstance(savePoint).saveTimeStamp(key, timeStampInMs);
        }
    }

    public void saveTimeStamp(TimeStampSavePoint savePoint, byte[] data, int offset, int count) {
        long trackKey;
        if (isRunning() && data != null) {
            if (count <= 4) {
                trackKey = BytesUtil.getLong(data, offset, count);
            } else {
                trackKey = BytesUtil.getLong(data, offset + 4, Math.min(count, 12));
            }
            saveTimeStamp(savePoint, trackKey, System.currentTimeMillis());
        }
    }
}
