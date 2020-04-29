package dji.midware.media;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogUtils;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.util.DJIEventBusUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class SmoothFilter {
    private static final int CHECK_TIME = 2000;
    private static final int DELAY_TIME = 80;
    public static boolean enableSmoothFilter = true;
    static SimpleDateFormat formatter = new SimpleDateFormat(DJILogUtils.FORMAT_2);
    public final String TAG = "SmoothFilter";
    /* access modifiers changed from: private */
    public int cacheNum = -1;
    private Callback callback;
    private DataCameraGetMode.MODE cameraMode = null;
    private int dataCount = 0;
    /* access modifiers changed from: private */
    public BlockingQueue<Object> dataQueue = new ArrayBlockingQueue(1000);
    /* access modifiers changed from: private */
    public int interval = -1;
    /* access modifiers changed from: private */
    public boolean isStop = true;
    private long lastCheckTime = -1;
    private long lastInputTime;
    private long lastOutputDataTime = 0;
    /* access modifiers changed from: private */
    public long lastTime = 0;
    private Runnable runnable = new Runnable() {
        /* class dji.midware.media.SmoothFilter.AnonymousClass1 */

        public void run() {
            while (!SmoothFilter.this.isStop) {
                int len = SmoothFilter.this.dataQueue.size();
                if (SmoothFilter.this.cacheNum < 0) {
                    SmoothFilter.this.outputData();
                } else if (System.currentTimeMillis() - SmoothFilter.this.lastTime > 80) {
                    SmoothFilter.this.outputData();
                } else if (len < SmoothFilter.this.cacheNum) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    SmoothFilter.this.outputData();
                    long sleepTime = (long) SmoothFilter.this.interval;
                    if (len > SmoothFilter.this.cacheNum) {
                        sleepTime = (long) (SmoothFilter.this.interval - ((len - SmoothFilter.this.cacheNum) * 3));
                    }
                    if (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }
    };
    private Thread thread;

    public interface Callback {
        void outputData(Object obj);
    }

    public boolean isSupportProduct() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.Longan || type == ProductType.LonganPro || type == ProductType.LonganRaw || type == ProductType.LonganZoom || type == ProductType.Orange2 || type == ProductType.Mammoth;
    }

    public void init() {
        this.isStop = false;
        this.thread = new Thread(this.runnable, "smooth-filter-thread");
        this.thread.start();
        DJIEventBusUtil.register(this);
        onEvent3MainThread(DataCameraGetPushStateInfo.getInstance());
    }

    public void uninit() {
        this.isStop = true;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void setCallback(Callback cb) {
        this.callback = cb;
    }

    public synchronized void putData(Object data) {
        long startTime = System.currentTimeMillis();
        this.lastInputTime = startTime;
        updateInterval(startTime);
        if (data == null) {
            Log.d("SmoothFilter", "data is null");
        } else if (this.cameraMode == DataCameraGetMode.MODE.TAKEPHOTO || this.cameraMode == DataCameraGetMode.MODE.RECORD) {
            if (enableSmoothFilter && isSupportProduct()) {
                this.lastTime = System.currentTimeMillis();
                try {
                    this.dataQueue.put(data);
                } catch (Exception e) {
                }
            } else if (this.callback != null) {
                invokeCallback(data);
            }
        } else if (this.callback != null) {
            Log.e("H1Playback", "run here callback");
            invokeCallback(data);
        }
    }

    /* access modifiers changed from: private */
    public void outputData() {
        Object data = null;
        try {
            data = this.dataQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        invokeCallback(data);
    }

    private void updateInterval(long curTime) {
        this.dataCount++;
        if (this.lastCheckTime == -1) {
            this.lastCheckTime = curTime;
        } else if (curTime - this.lastCheckTime > 2000) {
            this.interval = (int) ((curTime - this.lastCheckTime) / ((long) (this.dataCount - 1)));
            if (this.interval > 40) {
                this.interval = -1;
                this.cacheNum = -1;
            } else {
                this.cacheNum = 80 / this.interval;
            }
            this.lastCheckTime = -1;
            this.dataCount = 0;
        }
    }

    private void log(String log) {
        Log.d("SmoothFilter", log);
    }

    private void invokeCallback(Object data) {
        if (!(this.callback == null || data == null)) {
            this.callback.outputData(data);
        }
        this.lastOutputDataTime = System.currentTimeMillis();
    }

    public static synchronized String getStringDate() {
        String dateString;
        synchronized (SmoothFilter.class) {
            dateString = formatter.format(new Date());
        }
        return dateString;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3MainThread(DataCameraGetPushStateInfo event) {
        if (event.getMode() != this.cameraMode) {
            this.cameraMode = event.getMode();
            if (this.cameraMode == DataCameraGetMode.MODE.TAKEPHOTO || this.cameraMode == DataCameraGetMode.MODE.RECORD) {
                this.interval = -1;
                this.cacheNum = -1;
            }
        }
    }
}
