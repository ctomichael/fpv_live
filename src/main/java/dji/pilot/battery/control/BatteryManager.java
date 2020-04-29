package dji.pilot.battery.control;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataCenterGetPushBatteryCommon;
import dji.midware.data.model.P3.DataCenterGetSelfDischarge;
import dji.midware.data.model.P3.DataCenterSelfDischarge;
import dji.midware.data.model.P3.DataCenterSetBatteryCommon;
import dji.midware.data.model.P3.DataCenterSetSelfDischarge;
import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;
import dji.midware.data.model.P3.DataFlycGetVoltageWarnning;
import dji.midware.data.model.P3.DataFlycSetLVoltageWarnning;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataSmartBatteryGetPushCellVoltage;
import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;
import dji.midware.data.model.P3.DataSmartBatteryGetSetSelfDischargeDays;
import dji.midware.data.model.P3.DataSmartBatteryGetStaticData;
import dji.midware.interfaces.DJIDataCallBack;
import dji.pilot.publics.objects.DjiSharedPreferencesManager;
import dji.pilot.publics.util.DJICommonUtils;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class BatteryManager {
    private static final int[] ARRAY_LOWWARNING_SECTION = {15, 50};
    private static final long DELAY_DATASET = 9500;
    public static final int INVALID_VALUE = -1;
    private static final String KEY_SHOW_VOLTAGE = "key_show_voltage";
    private static final String KEY_SHOW_VOLTAGE_INSPIRE1_FIRST_CONNECT = "key_show_voltage_inspire1_first_connect";
    public static final int MAX_RECORD = 200;
    private static final int MAX_RETRY_TIMES = 0;
    private static final float MIN_NORMAL_PART_VOLTAGE = 3.5f;
    private static final int MIN_SERIOUS_LOWWARNING = 10;
    private static final float MIN_YELLOW_PART_VOLTAGE = 3.62f;
    private static final int MSD_ID_UPDATE_BATTERY = 4096;
    private static final int MSG_ID_DATASET = 4097;
    private static final int MSG_ID_GETDISCHARGE_FAIL = 4103;
    private static final int MSG_ID_GETDISCHARGE_SUCCESS = 4102;
    private static final int MSG_ID_GETLW_FAIL = 4099;
    private static final int MSG_ID_GETLW_SUCCESS = 4098;
    private static final int MSG_ID_LITCHI_GETSELF_FAIL = 4107;
    private static final int MSG_ID_LITCHI_GETSELF_SUCCESS = 4106;
    private static final int MSG_ID_LITCHI_SETSELF_FAIL = 4109;
    private static final int MSG_ID_LITCHI_SETSELF_SUCCESS = 4108;
    private static final int MSG_ID_SETDISCHARGE_FAIL = 4105;
    private static final int MSG_ID_SETDISCHARGE_SUCCESS = 4104;
    private static final int MSG_ID_SETLW_FAIL = 4101;
    private static final int MSG_ID_SETLW_SUCCESS = 4100;
    private static final int[] SECTION_DISCHARGE = {1, 10};
    private static final float[] SECTION_PART_VOLTAGE = {3.0f, 4.35f};
    private static final float TEMPERATURE_K2C = 273.15f;
    public static final int TYPE_LOW_WARNING = 0;
    public static final int TYPE_SERIOUS_LOW_WARNING = 1;
    private boolean isRegisted;
    private Context mAppCxt;
    /* access modifiers changed from: private */
    public final BMHandler mBMHandler;
    /* access modifiers changed from: private */
    public int mBatteryLife;
    private float mBatteryVoltage;
    private int mCells;
    /* access modifiers changed from: private */
    public int mChargeTimes;
    private DataCenterGetPushBatteryCommon.ConnStatus mConnStatus;
    private int mCurSubBatteryIndex;
    private int mCurVolume;
    private int mCurrent;
    private final DJIDataCallBack mDJIDataCB;
    private final DataCenterSelfDischarge mDataDischargeGetter;
    private final DataCenterSelfDischarge mDataDischargeSetter;
    private final DataFlycSetLVoltageWarnning mDataLWFirstSetInstance;
    private final DataFlycSetLVoltageWarnning mDataLWSecondSetInstance;
    private DataCenterGetSelfDischarge mDataLitchiSelfGetter;
    private DataCenterSetSelfDischarge mDataLitchiSelfSetter;
    private final DataCenterGetPushBatteryCommon mDataPushInstance;
    /* access modifiers changed from: private */
    public final DataCenterSetBatteryCommon mDataSetInstance;
    private final DJIDataCallBack mDischargeGetDataCB;
    private final DJIDataCallBack mDischargeSetDataCB;
    private int mFlyTime;
    private int mFullVolume;
    private final DJIBatteryHistoryManager mHistoryManager;
    private final DJIDataCallBack mLWFirstSetDataCB;
    private final DJIDataCallBack mLWSecondSetDataCB;
    private final ArrayList<OnBatteryChangeListener> mListeners;
    private final HistoryInfo mLoopError;
    private int mLowWarningThreshold;
    private float mNonSmartVoltage;
    private int mNonSmartVoltagePercent;
    private final PartInfo[] mPartVoltage;
    private String mProductDate;
    private int mRealErrorInfo;
    /* access modifiers changed from: private */
    public int mRetryTimes;
    private int mSelfDischarge;
    private String mSerialNo;
    private int mSeriousLowWarningThreshold;
    private boolean mStartByCheckList;
    private float mTemperature;
    private int mTmpLWThreshold;
    private int mTmpSelfDischarge;
    private int mTmpSeriousLWThreshold;
    private int mVolumePercent;
    private boolean mbInspire1HasConnected;
    private boolean mbLowWarningGoHome;
    private boolean mbSeriousLWLanding;
    private boolean mbShowVoltage;
    private boolean mbTmpLWGoHome;
    private boolean mbTmpSeriousLWLanding;

    public interface OnBatteryChangeListener {
        void onBatteryInfoChanged(int i);

        void onLowWarningChanged(int i, int i2, boolean z);

        void onLowWarningDataFail(int i, boolean z);

        void onSelfDischargeChanged(int i, boolean z);

        void onSelfDischargeFail(boolean z);
    }

    public static final class PartInfo {
        public int mProgress = 50;
        public float mVoltage = 0.0f;
        public boolean mbInvalid = false;
        public int mbLowWarning = 0;
    }

    public enum NonSmartBatteryChanged {
        CellChanged
    }

    public enum SHOW_VOLTAGE_STATUS {
        SHOW,
        HIDE
    }

    static /* synthetic */ int access$108(BatteryManager x0) {
        int i = x0.mRetryTimes;
        x0.mRetryTimes = i + 1;
        return i;
    }

    public static BatteryManager getInstance() {
        return SingletonHolder.mInstance;
    }

    public void initializeManager(Context context) {
        if (this.mAppCxt == null) {
            this.mAppCxt = context;
            this.mbShowVoltage = DjiSharedPreferencesManager.getBoolean(context, DJICommonUtils.generateProductSpKey(KEY_SHOW_VOLTAGE), this.mbShowVoltage);
            this.mbInspire1HasConnected = DjiSharedPreferencesManager.getBoolean(this.mAppCxt, DJICommonUtils.generateProductSpKey(KEY_SHOW_VOLTAGE_INSPIRE1_FIRST_CONNECT), false);
        }
    }

    public boolean canShowVoltage() {
        if (DJIProductManager.getInstance().getType() == ProductType.Orange && !this.mbInspire1HasConnected) {
            this.mbInspire1HasConnected = true;
            DjiSharedPreferencesManager.putBoolean(this.mAppCxt, DJICommonUtils.generateProductSpKey(KEY_SHOW_VOLTAGE_INSPIRE1_FIRST_CONNECT), this.mbInspire1HasConnected);
            updateShowVoltage(true);
        }
        return this.mbShowVoltage;
    }

    public void updateShowVoltage(boolean show) {
        if (this.mbShowVoltage != show) {
            this.mbShowVoltage = show;
            DjiSharedPreferencesManager.putBoolean(this.mAppCxt, DJICommonUtils.generateProductSpKey(KEY_SHOW_VOLTAGE), show);
            if (show) {
                EventBus.getDefault().post(SHOW_VOLTAGE_STATUS.SHOW);
            } else {
                EventBus.getDefault().post(SHOW_VOLTAGE_STATUS.HIDE);
            }
        }
    }

    public void startGetWarningNew() {
        int lowWarning;
        int seriousWarning;
        if (DataFlycGetPushSmartBattery.getInstance().isGetted()) {
            lowWarning = DataFlycGetPushSmartBattery.getInstance().getLowWarning();
            seriousWarning = DataFlycGetPushSmartBattery.getInstance().getSeriousLowWarning();
        } else {
            lowWarning = 35;
            seriousWarning = 20;
        }
        if (lowWarning != this.mLowWarningThreshold) {
            this.mLowWarningThreshold = lowWarning;
            boolean get = true;
            if (this.mTmpLWThreshold == lowWarning) {
                get = false;
                this.mTmpLWThreshold = 0;
            }
            notifyLowWarning(0, this.mLowWarningThreshold, get);
        }
        if (seriousWarning != this.mSeriousLowWarningThreshold) {
            this.mSeriousLowWarningThreshold = seriousWarning;
            boolean get2 = true;
            if (this.mTmpSeriousLWThreshold == seriousWarning) {
                get2 = false;
                this.mTmpSeriousLWThreshold = 0;
            }
            notifyLowWarning(1, this.mSeriousLowWarningThreshold, get2);
        }
        if (DataOsdGetPushCommon.getInstance().getBatteryType() == DataOsdGetPushCommon.BatteryType.NonSmart) {
            this.mNonSmartVoltage = (((float) DataFlycGetPushSmartBattery.getInstance().getVoltage()) * 1.0f) / 1000.0f;
            this.mNonSmartVoltagePercent = DataFlycGetPushSmartBattery.getInstance().getVoltagePercent();
            notifyBatteryInfoChanged(0);
        }
    }

    public void startGetSelfDischarge() {
        ProductType type = DJIProductManager.getInstance().getType();
        if (DJICommonUtils.useNewBattery()) {
            DataSmartBatteryGetSetSelfDischargeDays.getInstance().setType(false).setIndex(0).start(this.mDischargeGetDataCB);
        } else if (DJICommonUtils.isProductLitchi(type)) {
            this.mDataLitchiSelfGetter = new DataCenterGetSelfDischarge();
            this.mDataLitchiSelfGetter.setEncrypt(0);
            this.mDataLitchiSelfGetter.start(new DJIDataCallBack() {
                /* class dji.pilot.battery.control.BatteryManager.AnonymousClass1 */

                public void onSuccess(Object model) {
                    BatteryManager.this.mBMHandler.obtainMessage(4106, 0, 0).sendToTarget();
                }

                public void onFailure(Ccode ccode) {
                    BatteryManager.this.mBMHandler.obtainMessage(4107, 0, 0).sendToTarget();
                }
            });
        } else {
            this.mDataDischargeGetter.start(this.mDischargeGetDataCB);
        }
    }

    public void clearData() {
        this.mLowWarningThreshold = 35;
        this.mSeriousLowWarningThreshold = 20;
        this.mSelfDischarge = 7;
    }

    public synchronized void startUpdateTimer(boolean fromCheckList) {
        this.mStartByCheckList = fromCheckList;
        if (!fromCheckList) {
            startGetWarningNew();
            startGetSelfDischarge();
        }
        if (!DJICommonUtils.useNewBattery()) {
            if (this.mDataPushInstance.isGetted()) {
                updateBatteryInfo();
            }
        }
        if (!this.isRegisted) {
            this.isRegisted = true;
            this.mDataSetInstance.setRate(1).start(this.mDJIDataCB);
            EventBus.getDefault().register(this);
        }
    }

    public synchronized void stopUpdateTimer() {
        this.isRegisted = false;
        this.mBMHandler.removeMessages(4096);
        this.mBMHandler.removeMessages(4097);
        EventBus.getDefault().unregister(this);
    }

    public void clearAllInfos() {
        this.mConnStatus = DataCenterGetPushBatteryCommon.ConnStatus.EXCEPTION;
        this.mCurVolume = 0;
        this.mBatteryVoltage = 0.0f;
        this.mFullVolume = 0;
        this.mBatteryLife = 100;
        this.mChargeTimes = 0;
        this.mRealErrorInfo = 0;
        this.mVolumePercent = 0;
        this.mCurrent = 0;
        this.mFlyTime = 0;
        this.mTemperature = 0.0f;
        this.mSerialNo = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        this.mProductDate = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        clearPartInfos(this.mPartVoltage);
        this.mRetryTimes = 0;
        notifyBatteryInfoChanged(this.mCurVolume);
        this.mLoopError.parse(0);
        clearNonSmartInfo();
    }

    private void clearNonSmartInfo() {
        this.mNonSmartVoltage = 0.0f;
        this.mNonSmartVoltagePercent = 0;
    }

    public boolean registerChangeListener(OnBatteryChangeListener listener) {
        boolean ret = false;
        if (listener != null) {
            synchronized (this.mListeners) {
                if (!this.mListeners.contains(listener)) {
                    this.mListeners.add(listener);
                    ret = true;
                }
            }
        }
        return ret;
    }

    public boolean unregisterChangeListener(OnBatteryChangeListener listener) {
        boolean ret = false;
        if (listener != null) {
            synchronized (this.mListeners) {
                ret = this.mListeners.remove(listener);
            }
        }
        return ret;
    }

    public int getLowWarningThreshold() {
        return this.mLowWarningThreshold;
    }

    public boolean getLowWarningGoHome() {
        return this.mbLowWarningGoHome;
    }

    public int getSeriousLowWarningThreshold() {
        return this.mSeriousLowWarningThreshold;
    }

    public boolean getSeriousLWLanding() {
        return this.mbSeriousLWLanding;
    }

    public void setLowWarningThreshold(int threshold, boolean goHome) {
        if (this.mbLowWarningGoHome != goHome || this.mLowWarningThreshold != threshold) {
            this.mTmpLWThreshold = threshold;
            this.mbTmpLWGoHome = goHome;
            this.mDataLWFirstSetInstance.setWarnningLevel(DataFlycGetVoltageWarnning.WarnningLevel.First);
            this.mDataLWFirstSetInstance.setValue(threshold);
            this.mDataLWFirstSetInstance.setIsNeedGoHome(goHome);
            this.mDataLWFirstSetInstance.start(this.mLWFirstSetDataCB);
        }
    }

    public void setSeriousLowWarningThreshold(int threshold, boolean landing) {
        if (this.mbSeriousLWLanding != landing || this.mSeriousLowWarningThreshold != threshold) {
            this.mTmpSeriousLWThreshold = threshold;
            this.mbTmpSeriousLWLanding = landing;
            this.mDataLWSecondSetInstance.setWarnningLevel(DataFlycGetVoltageWarnning.WarnningLevel.Second);
            this.mDataLWSecondSetInstance.setValue(threshold);
            this.mDataLWSecondSetInstance.setIsNeedLanding(landing);
            this.mDataLWSecondSetInstance.start(this.mLWSecondSetDataCB);
        }
    }

    public int getSelfDischargeDays() {
        return this.mSelfDischarge - SECTION_DISCHARGE[0];
    }

    public final String[] generateSelfDischargeAr(Context context, int strId) {
        String[] ar = new String[SECTION_DISCHARGE[1]];
        for (int i = SECTION_DISCHARGE[0]; i <= SECTION_DISCHARGE[1]; i++) {
            ar[i - SECTION_DISCHARGE[0]] = context.getString(strId, Integer.valueOf(i));
        }
        return ar;
    }

    public void setSelfDischargeDays(int days) {
        DJILogHelper.getInstance().LOGD("", "setself day[" + days + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        int days2 = days + SECTION_DISCHARGE[0];
        if (this.mSelfDischarge != days2) {
            this.mTmpSelfDischarge = days2;
            ProductType type = DJIProductManager.getInstance().getType();
            if (DJICommonUtils.useNewBattery()) {
                DataSmartBatteryGetSetSelfDischargeDays.getInstance().setDays(days2).setType(true).setIndex(0).start(this.mDischargeSetDataCB);
            } else if (DJICommonUtils.isProductLitchi(type)) {
                this.mDataLitchiSelfSetter = new DataCenterSetSelfDischarge();
                this.mDataLitchiSelfSetter.setEncrypt(0);
                this.mDataLitchiSelfSetter.setDays(days2).start(new DJIDataCallBack() {
                    /* class dji.pilot.battery.control.BatteryManager.AnonymousClass2 */

                    public void onSuccess(Object model) {
                        BatteryManager.this.mBMHandler.obtainMessage(4108, 0, 0).sendToTarget();
                    }

                    public void onFailure(Ccode ccode) {
                        BatteryManager.this.mBMHandler.obtainMessage(4109, 0, 0).sendToTarget();
                    }
                });
            } else {
                this.mDataDischargeSetter.setDays(days2).start(this.mDischargeSetDataCB);
            }
        }
    }

    public DataCenterGetPushBatteryCommon.ConnStatus getConnStatus() {
        return this.mConnStatus;
    }

    public int getVolumePercent() {
        return this.mVolumePercent;
    }

    public float getBatteryVoltage() {
        return this.mBatteryVoltage;
    }

    public int getCurVolume() {
        return this.mCurVolume;
    }

    public int getFullVolume() {
        return this.mFullVolume;
    }

    public int getBattteryLife() {
        return this.mBatteryLife;
    }

    public int getChargeTimes() {
        return this.mChargeTimes;
    }

    public int getRealErrorInfo() {
        return this.mRealErrorInfo;
    }

    public HistoryInfo getStatus() {
        return this.mLoopError;
    }

    public int getCurrent() {
        return this.mCurrent;
    }

    public int getFlyTime() {
        return this.mFlyTime;
    }

    public float getTemperature() {
        return this.mTemperature;
    }

    public String getSerialNo() {
        return this.mSerialNo;
    }

    public String getProductDate() {
        return this.mProductDate;
    }

    public PartInfo[] getPartVoltage() {
        return this.mPartVoltage;
    }

    public float getNonSmartVoltage() {
        return this.mNonSmartVoltage;
    }

    public int getNonSmartVoltagePercent() {
        return this.mNonSmartVoltagePercent;
    }

    private void updateVolume(int volume) {
        this.mCurVolume = volume;
    }

    private void updateVoltage(float voltage) {
        this.mBatteryVoltage = voltage;
    }

    private void updateCurrent(int current) {
        this.mCurrent = current;
    }

    private void updateErrorInfo(int errorCode) {
        if (this.mRealErrorInfo != errorCode) {
            this.mHistoryManager.updateCurrent(errorCode);
        }
        this.mRealErrorInfo = errorCode;
    }

    private int calculateThreshold(int min, int max, int progress, int maxProgress) {
        if (maxProgress == 0) {
            return 0;
        }
        if (progress > maxProgress) {
            progress = maxProgress;
        }
        if (min > max) {
            min = max;
        }
        return (((max - min) * progress) / maxProgress) + min;
    }

    private int calculateProgress(int min, int max, int threshold, int maxProgress) {
        if (min >= max) {
            return 0;
        }
        if (threshold > max) {
            threshold = max;
        }
        return ((threshold - min) * maxProgress) / (max - min);
    }

    public int calculateLowWarningThreshold(int progress, int maxProgress) {
        return calculateThreshold(ARRAY_LOWWARNING_SECTION[0], ARRAY_LOWWARNING_SECTION[1], progress, maxProgress);
    }

    public int calculateSeriousLowWarningThreshold(int progress, int maxProgress) {
        return calculateThreshold(10, this.mLowWarningThreshold, progress, maxProgress);
    }

    public int calculateLowWarningProgress(int maxProgress) {
        return calculateProgress(ARRAY_LOWWARNING_SECTION[0], ARRAY_LOWWARNING_SECTION[1], this.mLowWarningThreshold, maxProgress);
    }

    public int calculateSeriousLowWarningProgress(int maxProgress) {
        return calculateProgress(10, this.mLowWarningThreshold, this.mSeriousLowWarningThreshold, maxProgress);
    }

    private int calculatePartVoltageProgress(float voltage, int max) {
        if (voltage >= SECTION_PART_VOLTAGE[1]) {
            return 100;
        }
        if (voltage <= SECTION_PART_VOLTAGE[0]) {
            return 0;
        }
        return (int) (((voltage - SECTION_PART_VOLTAGE[0]) * ((float) max)) / (SECTION_PART_VOLTAGE[1] - SECTION_PART_VOLTAGE[0]));
    }

    private BatteryManager() {
        this.mRetryTimes = 0;
        this.mHistoryManager = DJIBatteryHistoryManager.getInstance();
        this.mConnStatus = DataCenterGetPushBatteryCommon.ConnStatus.EXCEPTION;
        this.mLowWarningThreshold = 35;
        this.mbLowWarningGoHome = true;
        this.mSeriousLowWarningThreshold = 20;
        this.mbSeriousLWLanding = true;
        this.mVolumePercent = 0;
        this.mBatteryVoltage = 0.0f;
        this.mCurVolume = 0;
        this.mFullVolume = 0;
        this.mBatteryLife = 100;
        this.mChargeTimes = 0;
        this.mRealErrorInfo = 0;
        this.mCurrent = 0;
        this.mFlyTime = 0;
        this.mTemperature = 0.0f;
        this.mSerialNo = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        this.mProductDate = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        this.mPartVoltage = new PartInfo[6];
        this.mSelfDischarge = 7;
        this.mNonSmartVoltage = 0.0f;
        this.mNonSmartVoltagePercent = 0;
        this.mbShowVoltage = false;
        this.mbInspire1HasConnected = false;
        this.mAppCxt = null;
        this.mTmpLWThreshold = 0;
        this.mbTmpLWGoHome = false;
        this.mTmpSeriousLWThreshold = 0;
        this.mbTmpSeriousLWLanding = false;
        this.mTmpSelfDischarge = 0;
        this.mLoopError = new HistoryInfo();
        this.mStartByCheckList = false;
        this.isRegisted = false;
        this.mCurSubBatteryIndex = 0;
        this.mCells = 3;
        this.mListeners = new ArrayList<>(4);
        this.mBMHandler = new BMHandler(this);
        this.mDataPushInstance = DataCenterGetPushBatteryCommon.getInstance();
        this.mDataSetInstance = DataCenterSetBatteryCommon.getInstance();
        this.mDataLWFirstSetInstance = new DataFlycSetLVoltageWarnning();
        this.mDataLWSecondSetInstance = new DataFlycSetLVoltageWarnning();
        this.mDataDischargeGetter = new DataCenterSelfDischarge();
        this.mDataDischargeGetter.setFlag(true);
        this.mDataDischargeSetter = new DataCenterSelfDischarge();
        this.mDataDischargeSetter.setFlag(false);
        this.mDischargeGetDataCB = new DJIDataCallBack() {
            /* class dji.pilot.battery.control.BatteryManager.AnonymousClass3 */

            public void onSuccess(Object model) {
                BatteryManager.this.mBMHandler.obtainMessage(4102, 0, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                BatteryManager.this.mBMHandler.obtainMessage(4103, 0, 0).sendToTarget();
            }
        };
        this.mDischargeSetDataCB = new DJIDataCallBack() {
            /* class dji.pilot.battery.control.BatteryManager.AnonymousClass4 */

            public void onSuccess(Object model) {
                BatteryManager.this.mBMHandler.obtainMessage(4104, 0, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                BatteryManager.this.mBMHandler.obtainMessage(4105, 0, 0).sendToTarget();
            }
        };
        this.mDJIDataCB = new DJIDataCallBack() {
            /* class dji.pilot.battery.control.BatteryManager.AnonymousClass5 */

            public void onSuccess(Object model) {
                int unused = BatteryManager.this.mRetryTimes = 0;
            }

            public void onFailure(Ccode ccode) {
                if (BatteryManager.this.mRetryTimes < 0) {
                    BatteryManager.this.mDataSetInstance.start(this);
                    BatteryManager.access$108(BatteryManager.this);
                    return;
                }
                int unused = BatteryManager.this.mRetryTimes = 0;
            }
        };
        this.mLWFirstSetDataCB = new DJIDataCallBack() {
            /* class dji.pilot.battery.control.BatteryManager.AnonymousClass6 */

            public void onSuccess(Object model) {
                BatteryManager.this.mBMHandler.obtainMessage(4100, 0, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                BatteryManager.this.mBMHandler.obtainMessage(4101, 0, 0).sendToTarget();
            }
        };
        this.mLWSecondSetDataCB = new DJIDataCallBack() {
            /* class dji.pilot.battery.control.BatteryManager.AnonymousClass7 */

            public void onSuccess(Object model) {
                BatteryManager.this.mBMHandler.obtainMessage(4100, 1, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                BatteryManager.this.mBMHandler.obtainMessage(4101, 1, 0).sendToTarget();
            }
        };
        for (int i = 0; i < this.mPartVoltage.length; i++) {
            this.mPartVoltage[i] = new PartInfo();
            this.mPartVoltage[i].mbLowWarning = 0;
            this.mPartVoltage[i].mbInvalid = false;
            this.mPartVoltage[i].mProgress = 50;
            this.mPartVoltage[i].mVoltage = MIN_NORMAL_PART_VOLTAGE;
        }
    }

    /* access modifiers changed from: private */
    public void updateBatteryInfo() {
        boolean z;
        this.mConnStatus = this.mDataPushInstance.isGetted() ? this.mDataPushInstance.getConnStatus() : DataCenterGetPushBatteryCommon.ConnStatus.EXCEPTION;
        this.mTemperature = (((float) this.mDataPushInstance.getTemperature()) / 10.0f) - 273.15f;
        int volume = this.mDataPushInstance.getCurrentCapacity();
        if (!this.mStartByCheckList) {
            int error = this.mDataPushInstance.getErrorType();
            updateVolume(volume);
            updateVoltage(((float) this.mDataPushInstance.getCurrentPV()) / 1000.0f);
            updateErrorInfo(error);
            updateCurrent(Math.abs(this.mDataPushInstance.getCurrent()));
            this.mHistoryManager.updateCurrentConnStatus(this.mConnStatus);
            this.mFullVolume = this.mDataPushInstance.getFullCapacity();
            this.mBatteryLife = this.mDataPushInstance.getLife();
            this.mChargeTimes = this.mDataPushInstance.getLoopNum();
            this.mVolumePercent = this.mDataPushInstance.getRelativeCapacity();
            this.mSerialNo = String.format(Locale.US, "%06d", Integer.valueOf(this.mDataPushInstance.getSerialNo()));
            this.mLoopError.parseSimple(error);
            int[] parts = this.mDataPushInstance.getPartVoltages();
            for (int i = 0; i < this.mPartVoltage.length; i++) {
                this.mPartVoltage[i].mVoltage = ((float) parts[i]) / 1000.0f;
                this.mPartVoltage[i].mProgress = calculatePartVoltageProgress(this.mPartVoltage[i].mVoltage, 100);
                PartInfo partInfo = this.mPartVoltage[i];
                if (this.mLoopError.getInvalidNum() == i + 1) {
                    z = true;
                } else {
                    z = false;
                }
                partInfo.mbInvalid = z;
                if (this.mLoopError.getUnderVoltageNum() == i + 1) {
                    this.mPartVoltage[i].mbLowWarning = 2;
                } else {
                    this.mPartVoltage[i].mbLowWarning = checkPartVoltageStatus(this.mPartVoltage[i].mVoltage);
                }
            }
            this.mFlyTime = (int) (((float) DataOsdGetPushCommon.getInstance().getFlyTime()) * 0.1f);
            int[] date = this.mDataPushInstance.getProductDate();
            this.mProductDate = String.format(Locale.US, "%1$d-%2$02d-%3$02d", Integer.valueOf(date[0]), Integer.valueOf(date[1]), Integer.valueOf(date[2]));
        } else {
            this.mLoopError.parse(this.mDataPushInstance.getErrorType());
        }
        notifyBatteryInfoChanged(volume);
    }

    /* access modifiers changed from: private */
    public void updateBatteryInfoNew() {
        DataSmartBatteryGetPushDynamicData dd = DataSmartBatteryGetPushDynamicData.getInstance();
        this.mConnStatus = dd.isGetted() ? DataCenterGetPushBatteryCommon.ConnStatus.ofData((int) dd.getStatus()) : DataCenterGetPushBatteryCommon.ConnStatus.EXCEPTION;
        if (dd.getIndex() == 0) {
            this.mTemperature = ((float) dd.getTemperature()) / 10.0f;
            this.mFlyTime = (int) (((float) DataOsdGetPushCommon.getInstance().getFlyTime()) * 0.1f);
            this.mFullVolume = dd.getFullCapacity();
            this.mVolumePercent = dd.getRelativeCapacityPercentage();
            int volume = dd.getRemainCapacity();
            updateVolume(volume);
            updateVoltage(((float) dd.getVoltage()) / 1000.0f);
            updateCurrent(Math.abs(dd.getCurrent()));
            this.mHistoryManager.updateCurrentConnStatus(this.mConnStatus);
            final DataSmartBatteryGetStaticData staticData = new DataSmartBatteryGetStaticData();
            staticData.setIndex(0).start(new DJIDataCallBack() {
                /* class dji.pilot.battery.control.BatteryManager.AnonymousClass8 */

                public void onSuccess(Object model) {
                    int unused = BatteryManager.this.mChargeTimes = staticData.getCycleTimes();
                    int unused2 = BatteryManager.this.mBatteryLife = (int) staticData.getLifePercent();
                }

                public void onFailure(Ccode ccode) {
                }
            });
            notifyBatteryInfoChanged(volume);
        }
    }

    public static int getPartCount() {
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.KumquatX) {
            return 3;
        }
        if (DJICommonUtils.isProductLitchi(type)) {
            return 4;
        }
        return 6;
    }

    public static int checkVoltageStatus(float voltage) {
        int count = getPartCount();
        float normalVoltage = MIN_NORMAL_PART_VOLTAGE * ((float) count);
        float yellowVoltage = MIN_YELLOW_PART_VOLTAGE * ((float) count);
        if (voltage < normalVoltage) {
            return 2;
        }
        if (voltage < yellowVoltage) {
            return 1;
        }
        return 0;
    }

    public static int checkA3VoltageStatus(float voltage) {
        if (voltage < NoSmartBatteryVoltManager.getInstance().getSeriousLowVoltageThreshold()) {
            return 2;
        }
        if (voltage < NoSmartBatteryVoltManager.getInstance().getLowVoltageThreshold()) {
            return 1;
        }
        return 0;
    }

    public static int checkPartVoltageStatus(float voltage) {
        if (voltage < MIN_NORMAL_PART_VOLTAGE) {
            return 2;
        }
        if (voltage < MIN_YELLOW_PART_VOLTAGE) {
            return 1;
        }
        return 0;
    }

    private void clearPartInfos(PartInfo[] parts) {
        if (parts != null && parts.length > 0) {
            for (PartInfo part : parts) {
                part.mVoltage = MIN_NORMAL_PART_VOLTAGE;
                part.mbInvalid = false;
                part.mbLowWarning = 0;
                part.mProgress = 50;
            }
        }
    }

    /* access modifiers changed from: private */
    public void resetDataSet() {
        this.mDataSetInstance.start(this.mDJIDataCB);
        this.mBMHandler.sendEmptyMessageDelayed(4097, DELAY_DATASET);
    }

    /* access modifiers changed from: private */
    public void handleGetLWSuccess(int type, int arg2) {
    }

    /* access modifiers changed from: private */
    public void handleGetLWFail(int type, int arg2) {
        notifyLowWarningFail(type, true);
    }

    /* access modifiers changed from: private */
    public void handleSetLWSuccess(int type, int arg2) {
        if (type != 0 && 1 == type) {
        }
    }

    /* access modifiers changed from: private */
    public void handleSetLWFail(int type, int arg2) {
        notifyLowWarningFail(type, false);
    }

    /* access modifiers changed from: private */
    public void handleGetDischarge(boolean success) {
        if (success) {
            if (DJICommonUtils.useNewBattery()) {
                this.mSelfDischarge = DataSmartBatteryGetSetSelfDischargeDays.getInstance().getDays();
                if (this.mSelfDischarge < SECTION_DISCHARGE[0] || this.mSelfDischarge > SECTION_DISCHARGE[1]) {
                    this.mSelfDischarge = 7;
                }
            } else {
                this.mSelfDischarge = this.mDataDischargeGetter.getDay();
                if (this.mSelfDischarge < SECTION_DISCHARGE[0] || this.mSelfDischarge > SECTION_DISCHARGE[1]) {
                    this.mSelfDischarge = 7;
                }
            }
            notifyDischargeChanged(this.mSelfDischarge, true);
            return;
        }
        notifySelfDischargeFail(true);
    }

    /* access modifiers changed from: private */
    public void handleLitchiGetSelf(boolean success) {
        DJILogHelper.getInstance().LOGD("", "getself[" + success + "]day[" + this.mDataLitchiSelfGetter.getDay() + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (success) {
            this.mSelfDischarge = this.mDataLitchiSelfGetter.getDay();
            if (this.mSelfDischarge < SECTION_DISCHARGE[0] || this.mSelfDischarge > SECTION_DISCHARGE[1]) {
                this.mSelfDischarge = 7;
            }
            notifyDischargeChanged(this.mSelfDischarge, true);
            return;
        }
        notifySelfDischargeFail(true);
    }

    /* access modifiers changed from: private */
    public void handleSetDischarge(boolean success) {
        if (success) {
            this.mSelfDischarge = this.mTmpSelfDischarge;
            notifyDischargeChanged(this.mSelfDischarge, false);
            return;
        }
        notifySelfDischargeFail(false);
    }

    /* access modifiers changed from: private */
    public void handleLitchiSetSelf(boolean success) {
        DJILogHelper.getInstance().LOGD("", "setself[" + success + "]day[" + this.mTmpSelfDischarge + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (success) {
            this.mSelfDischarge = this.mTmpSelfDischarge;
            notifyDischargeChanged(this.mSelfDischarge, false);
            return;
        }
        notifySelfDischargeFail(false);
    }

    private void notifyDischargeChanged(int value, boolean getOrSet) {
        synchronized (this.mListeners) {
            Iterator<OnBatteryChangeListener> iter = this.mListeners.iterator();
            while (iter.hasNext()) {
                iter.next().onSelfDischargeChanged(value - SECTION_DISCHARGE[0], getOrSet);
            }
        }
    }

    private void notifySelfDischargeFail(boolean getOrSet) {
        synchronized (this.mListeners) {
            Iterator<OnBatteryChangeListener> iter = this.mListeners.iterator();
            while (iter.hasNext()) {
                iter.next().onSelfDischargeFail(getOrSet);
            }
        }
    }

    private void notifyBatteryInfoChanged(int volume) {
        synchronized (this.mListeners) {
            Iterator<OnBatteryChangeListener> iter = this.mListeners.iterator();
            while (iter.hasNext()) {
                iter.next().onBatteryInfoChanged(volume);
            }
        }
    }

    private void notifyLowWarning(int type, int threshold, boolean getOrSet) {
        synchronized (this.mListeners) {
            Iterator<OnBatteryChangeListener> iter = this.mListeners.iterator();
            while (iter.hasNext()) {
                iter.next().onLowWarningChanged(type, threshold, getOrSet);
            }
        }
    }

    private void notifyLowWarningFail(int type, boolean getOrSet) {
        synchronized (this.mListeners) {
            Iterator<OnBatteryChangeListener> iter = this.mListeners.iterator();
            while (iter.hasNext()) {
                iter.next().onLowWarningDataFail(type, getOrSet);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCenterGetPushBatteryCommon battery) {
        if (!DJICommonUtils.useNewBattery()) {
            this.mBMHandler.sendEmptyMessage(4096);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataSmartBatteryGetPushDynamicData battery) {
        if (DJICommonUtils.useNewBattery()) {
            this.mBMHandler.sendEmptyMessage(4096);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataSmartBatteryGetPushCellVoltage battery) {
        int[] parts = battery.getVoltages();
        int i = 0;
        while (i < parts.length && i < this.mPartVoltage.length) {
            this.mPartVoltage[i].mVoltage = ((float) parts[i]) / 1000.0f;
            this.mPartVoltage[i].mProgress = calculatePartVoltageProgress(this.mPartVoltage[i].mVoltage, 100);
            this.mPartVoltage[i].mbInvalid = this.mLoopError.getInvalidNum() == i + 1;
            if (this.mLoopError.getUnderVoltageNum() == i + 1) {
                this.mPartVoltage[i].mbLowWarning = 2;
            } else {
                this.mPartVoltage[i].mbLowWarning = checkPartVoltageStatus(this.mPartVoltage[i].mVoltage);
            }
            i++;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DataFlycGetPushSmartBattery smart) {
        startGetWarningNew();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(ProductType type) {
        if (type == ProductType.Orange && !this.mbInspire1HasConnected) {
            this.mbInspire1HasConnected = true;
            DjiSharedPreferencesManager.putBoolean(this.mAppCxt, DJICommonUtils.generateProductSpKey(KEY_SHOW_VOLTAGE_INSPIRE1_FIRST_CONNECT), this.mbInspire1HasConnected);
            updateShowVoltage(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DataEvent event) {
        if (DataEvent.ConnectOK == event) {
            this.mDataSetInstance.setRate(1).start(this.mDJIDataCB);
        } else if (DataEvent.ConnectLose == event) {
            clearAllInfos();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DataCameraEvent event) {
        if (DataCameraEvent.ConnectOK == event) {
            this.mDataSetInstance.setRate(1).start(this.mDJIDataCB);
        } else if (DataCameraEvent.ConnectLose == event) {
            clearAllInfos();
        }
    }

    private static final class BMHandler extends Handler {
        private WeakReference<BatteryManager> mOutRef = null;

        public BMHandler(BatteryManager bm) {
            super(Looper.getMainLooper());
            this.mOutRef = new WeakReference<>(bm);
        }

        public void handleMessage(Message msg) {
            BatteryManager bm = this.mOutRef.get();
            if (bm != null) {
                switch (msg.what) {
                    case 4096:
                        if (DJICommonUtils.useNewBattery()) {
                            bm.updateBatteryInfoNew();
                            return;
                        } else {
                            bm.updateBatteryInfo();
                            return;
                        }
                    case 4097:
                        bm.resetDataSet();
                        return;
                    case 4098:
                        bm.handleGetLWSuccess(msg.arg1, msg.arg2);
                        return;
                    case 4099:
                        bm.handleGetLWFail(msg.arg1, msg.arg2);
                        return;
                    case 4100:
                        bm.handleSetLWSuccess(msg.arg1, msg.arg2);
                        return;
                    case 4101:
                        bm.handleSetLWFail(msg.arg1, msg.arg2);
                        return;
                    case 4102:
                        bm.handleGetDischarge(true);
                        return;
                    case 4103:
                        bm.handleGetDischarge(false);
                        return;
                    case 4104:
                        bm.handleSetDischarge(true);
                        return;
                    case 4105:
                        bm.handleSetDischarge(false);
                        return;
                    case 4106:
                        bm.handleLitchiGetSelf(true);
                        return;
                    case 4107:
                        bm.handleLitchiGetSelf(false);
                        return;
                    case 4108:
                        bm.handleLitchiSetSelf(true);
                        return;
                    case 4109:
                        bm.handleLitchiSetSelf(false);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private static final class SingletonHolder {
        public static BatteryManager mInstance = new BatteryManager();

        private SingletonHolder() {
        }
    }

    public int getCurSubBatteryIndex() {
        return this.mCurSubBatteryIndex;
    }

    public void setCurSubBatteryIndex(int index) {
        this.mCurSubBatteryIndex = index;
    }

    public void setCells(int cells) {
        if (this.mCells != cells) {
            this.mCells = cells;
            EventBus.getDefault().post(NonSmartBatteryChanged.CellChanged);
        }
    }

    public int getCells() {
        return this.mCells;
    }
}
