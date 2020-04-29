package dji.sdksharedlib.hardware.extension;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EXClassNullAway
public class DJISDKCacheCenterTimer {
    private static final int MAX_INTERVAL = 10000;
    private static final int MIN_INTERVAL = 100;
    private static final int MSG_ADD = 2;
    private static final int MSG_CLEAR = 4;
    private static final int MSG_REMOVE = 3;
    private static final int MSG_REPEAT = 1;
    private Map<Integer, List<Runnable>> intervalListMap;
    private int timerCount = 0;
    /* access modifiers changed from: private */
    public TimerHandler timerHandler;

    public void add(List<Runnable> list, int interval) {
        if (list != null && this.timerHandler != null) {
            Message msg = this.timerHandler.obtainMessage(2);
            msg.arg1 = interval;
            msg.obj = list;
            this.timerHandler.sendMessage(msg);
        }
    }

    public void add(Runnable runnable, int interval) {
        if (runnable != null) {
            List<Runnable> list = new ArrayList<>();
            list.add(runnable);
            add(list, interval);
        }
    }

    public void remove(Runnable runnable, int interval) {
        if (runnable != null) {
            List<Runnable> list = new ArrayList<>();
            list.add(runnable);
            remove(list, interval);
        }
    }

    public void remove(List<Runnable> list, int interval) {
        if (list != null && this.timerHandler != null) {
            Message msg = this.timerHandler.obtainMessage(3);
            msg.arg1 = interval;
            msg.obj = list;
            this.timerHandler.sendMessage(msg);
        }
    }

    public void clearList() {
        if (this.timerHandler != null) {
            this.timerHandler.sendMessage(this.timerHandler.obtainMessage(4));
        }
    }

    public void init(Looper looper) {
        this.intervalListMap = new ConcurrentHashMap();
        this.timerHandler = new TimerHandler(looper);
        this.timerHandler.sendMessage(this.timerHandler.obtainMessage(1));
    }

    public void destroy() {
        this.intervalListMap = null;
        if (this.timerHandler != null) {
            this.timerHandler.removeCallbacksAndMessages(null);
            this.timerHandler = null;
        }
    }

    private class TimerHandler extends Handler {
        public TimerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    DJISDKCacheCenterTimer.this.repeatRun();
                    if (DJISDKCacheCenterTimer.this.timerHandler != null) {
                        DJISDKCacheCenterTimer.this.timerHandler.sendEmptyMessageDelayed(1, 100);
                        return;
                    }
                    return;
                case 2:
                    DJISDKCacheCenterTimer.this.innerAdd((List) msg.obj, msg.arg1);
                    return;
                case 3:
                    DJISDKCacheCenterTimer.this.innerRemove((List) msg.obj, msg.arg1);
                    return;
                case 4:
                    DJISDKCacheCenterTimer.this.innerClear();
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void repeatRun() {
        Map<Integer, List<Runnable>> tempIntervalListMap = this.intervalListMap;
        if (tempIntervalListMap != null) {
            for (Integer interval : tempIntervalListMap.keySet()) {
                List<Runnable> list = tempIntervalListMap.get(interval);
                int tableLen = interval.intValue() / 100;
                int tableIndex = this.timerCount % tableLen;
                int delta = (list.size() / tableLen) + 1;
                for (int i = 0; i < delta; i++) {
                    int index = (i * tableLen) + tableIndex;
                    if (list.size() > index) {
                        ((Runnable) list.get(index)).run();
                    }
                }
            }
            this.timerCount++;
            if (this.timerCount > 1000000) {
                this.timerCount = 0;
            }
        }
    }

    /* access modifiers changed from: private */
    public void innerRemove(List<Runnable> list, int interval) {
        if (list != null && this.intervalListMap != null && this.intervalListMap.containsKey(Integer.valueOf(interval))) {
            this.intervalListMap.get(Integer.valueOf(interval)).removeAll(list);
            if (this.intervalListMap.get(Integer.valueOf(interval)).size() == 0) {
                this.intervalListMap.remove(Integer.valueOf(interval));
            }
        }
    }

    /* access modifiers changed from: private */
    public void innerAdd(List<Runnable> list, int interval) {
        if (list != null) {
            if (interval < 100 || interval >= 10000 || interval % 100 != 0) {
                throw new RuntimeException("interval is range is 100~10000, need divisible by 100, current is " + interval);
            }
            if (!this.intervalListMap.containsKey(Integer.valueOf(interval))) {
                this.intervalListMap.put(Integer.valueOf(interval), new ArrayList());
            }
            this.intervalListMap.get(Integer.valueOf(interval)).addAll(list);
        }
    }

    /* access modifiers changed from: private */
    public void innerClear() {
        this.intervalListMap.clear();
    }

    public int getCenterTimerIntervalSize(int interval) {
        if (this.intervalListMap == null || this.intervalListMap.get(Integer.valueOf(interval)) == null) {
            return 0;
        }
        return this.intervalListMap.get(Integer.valueOf(interval)).size();
    }

    public int getCenterTimerMapSize() {
        if (this.intervalListMap != null) {
            return this.intervalListMap.size();
        }
        return 0;
    }
}
