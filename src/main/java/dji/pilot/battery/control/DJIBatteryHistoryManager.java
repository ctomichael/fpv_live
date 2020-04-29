package dji.pilot.battery.control;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.model.P3.DataCenterGetBatteryHistory;
import dji.midware.data.model.P3.DataCenterGetPushBatteryCommon;
import dji.midware.interfaces.DJIDataCallBack;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIBatteryHistoryManager {
    public static final int FAIL_DATA = 1;
    private static final int MAX_HISTORY = 31;
    public static final int SUCCESS_DATA = 0;
    public static final int TYPE_HISTORY = 4096;
    private int mCurrentError;
    private final HistoryInfo mCurrentHistory;
    private DataCenterGetPushBatteryCommon.ConnStatus mCurrrentConnStatus;
    private final DJIDataCallBack mGetHistoryCB;
    private final DataCenterGetBatteryHistory mGetHistoryInstance;
    private final ArrayList<HistoryInfo> mHistoryCache;
    private final ArrayList<HistoryInfo> mHistoryInfos;
    private final ArrayList<OnResultListener> mOnResultListeners;
    /* access modifiers changed from: private */
    public final UIHandler mUIHandler;
    private boolean mbRunning;

    public interface OnResultListener {
        void onChange(int i, Object obj);

        void onFail(int i, Ccode ccode);

        void onStart(int i);

        void onSuccess(int i, Object obj);
    }

    public static DJIBatteryHistoryManager getInstance() {
        return SingletonHolder.mInstance;
    }

    public void registerListener(OnResultListener listener) {
        if (listener != null && !this.mOnResultListeners.contains(listener)) {
            this.mOnResultListeners.add(listener);
        }
    }

    public void unregisterListener(OnResultListener listener) {
        if (listener != null) {
            this.mOnResultListeners.remove(listener);
        }
    }

    public void start() {
        if (!this.mbRunning) {
            this.mbRunning = true;
            this.mCurrentHistory.parse(this.mCurrentError);
            this.mCurrentHistory.updateConnStatus(this.mCurrrentConnStatus);
            if (!this.mGetHistoryInstance.isGetted()) {
                clearHistory(this.mHistoryInfos, false);
                this.mGetHistoryInstance.start(this.mGetHistoryCB);
                notifyStart(4096);
            }
            EventBus.getDefault().register(this);
        }
    }

    public void stop() {
        if (this.mbRunning) {
            EventBus.getDefault().unregister(this);
            this.mbRunning = false;
        }
    }

    public void updateCurrent(int error) {
        if (this.mCurrentError != error) {
            this.mCurrentError = error;
            if (this.mbRunning) {
                this.mCurrentHistory.parse(error);
                notifyChange(4096, this.mHistoryInfos);
            }
        }
    }

    public void updateCurrentConnStatus(DataCenterGetPushBatteryCommon.ConnStatus cs) {
        if (this.mCurrrentConnStatus != cs) {
            this.mCurrrentConnStatus = cs;
            if (this.mbRunning) {
                this.mCurrentHistory.updateConnStatus(cs);
                notifyChange(4096, this.mHistoryInfos);
            }
        }
    }

    public List<HistoryInfo> getHistories() {
        return this.mHistoryInfos;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DataCameraEvent event) {
        if (DataCameraEvent.ConnectOK == event) {
            this.mGetHistoryInstance.start(this.mGetHistoryCB);
            notifyStart(4096);
        } else if (DataCameraEvent.ConnectLose == event) {
            clearHistory(this.mHistoryInfos, false);
            this.mCurrentError = 0;
            this.mCurrrentConnStatus = DataCenterGetPushBatteryCommon.ConnStatus.EXCEPTION;
            this.mCurrentHistory.parse(this.mCurrentError);
            this.mCurrentHistory.updateConnStatus(this.mCurrrentConnStatus);
            notifyChange(4096, this.mHistoryInfos);
        }
    }

    private DJIBatteryHistoryManager() {
        this.mGetHistoryInstance = DataCenterGetBatteryHistory.getInstance();
        this.mOnResultListeners = new ArrayList<>(2);
        this.mHistoryInfos = new ArrayList<>(31);
        this.mHistoryCache = new ArrayList<>(31);
        this.mbRunning = false;
        this.mCurrentError = 0;
        this.mCurrrentConnStatus = DataCenterGetPushBatteryCommon.ConnStatus.EXCEPTION;
        this.mUIHandler = new UIHandler(this);
        this.mGetHistoryCB = new DJIDataCallBack() {
            /* class dji.pilot.battery.control.DJIBatteryHistoryManager.AnonymousClass1 */

            public void onSuccess(Object model) {
                DJIBatteryHistoryManager.this.mUIHandler.obtainMessage(4096, 0, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIBatteryHistoryManager.this.mUIHandler.obtainMessage(4096, 1, 0, ccode).sendToTarget();
            }
        };
        this.mCurrentHistory = new HistoryInfo();
        this.mHistoryInfos.add(this.mCurrentHistory);
        for (int i = 0; i < 31; i++) {
            this.mHistoryCache.add(new HistoryInfo());
        }
    }

    /* access modifiers changed from: private */
    public void handleDataResult(int type, int result, int arg, Object obj) {
        if (result == 0) {
            if (4096 == type) {
                if (DJIProductManager.getInstance().getType() == ProductType.KumquatX || DJIProductManager.getInstance().getType() == ProductType.KumquatS) {
                    long[] errors = this.mGetHistoryInstance.getHistoryLong();
                    clearHistory(this.mHistoryInfos, false);
                    int i = 0;
                    while (i < 31 && i < errors.length) {
                        HistoryInfo hi = this.mHistoryCache.get(i);
                        hi.parse(errors[i]);
                        this.mHistoryInfos.add(hi);
                        i++;
                    }
                } else {
                    int[] errors2 = this.mGetHistoryInstance.getHistory();
                    clearHistory(this.mHistoryInfos, false);
                    int i2 = 0;
                    while (i2 < 31 && i2 < errors2.length) {
                        HistoryInfo hi2 = this.mHistoryCache.get(i2);
                        hi2.parse(errors2[i2]);
                        this.mHistoryInfos.add(hi2);
                        i2++;
                    }
                }
                notifySuccess(4096, this.mHistoryInfos);
            }
        } else if (1 == result) {
            notifyFail(type, obj instanceof Ccode ? (Ccode) obj : Ccode.UNDEFINED);
        }
    }

    private void clearHistory(List<HistoryInfo> list, boolean all) {
        if (all) {
            list.clear();
            return;
        }
        while (list.size() > 1) {
            list.remove(1);
        }
    }

    private void notifyStart(int type) {
        if (!this.mOnResultListeners.isEmpty()) {
            this.mOnResultListeners.get(0).onStart(type);
        }
    }

    private void notifySuccess(int type, Object result) {
        if (!this.mOnResultListeners.isEmpty()) {
            this.mOnResultListeners.get(0).onSuccess(type, result);
        }
    }

    private void notifyFail(int type, Ccode err) {
        if (!this.mOnResultListeners.isEmpty()) {
            this.mOnResultListeners.get(0).onFail(type, err);
        }
    }

    private void notifyChange(int type, Object result) {
        if (!this.mOnResultListeners.isEmpty()) {
            this.mOnResultListeners.get(0).onChange(type, result);
        }
    }

    private static final class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIBatteryHistoryManager mInstance = new DJIBatteryHistoryManager();

        private SingletonHolder() {
        }
    }

    private static final class UIHandler extends Handler {
        private final WeakReference<DJIBatteryHistoryManager> mOutCls;

        public UIHandler(DJIBatteryHistoryManager hm) {
            super(Looper.getMainLooper());
            this.mOutCls = new WeakReference<>(hm);
        }

        public void handleMessage(Message msg) {
            DJIBatteryHistoryManager hm = this.mOutCls.get();
            if (hm != null) {
                hm.handleDataResult(msg.what, msg.arg1, msg.arg2, msg.obj);
            }
        }
    }
}
