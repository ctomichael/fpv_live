package dji.pilot.fpv.control;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Keep;
import com.dji.frame.util.V_AppUtils;
import com.dji.frame.util.V_JsonUtil;
import com.dji.frame.util.V_StringUtils;
import dji.apppublic.reflect.AppPubInjectManager;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycPushRedundancyStatus;
import dji.midware.data.model.P3.DataFlycRedundancyStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.params.P3.ParamCfgName;
import dji.midware.interfaces.DJIDataCallBack;
import dji.pilot.publics.R;
import dji.pilot.publics.util.DJICommonUtils;
import dji.publics.utils.LanguageUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Keep
@EXClassNullAway
public class DJIRedundancySysController implements ParamCfgName {
    private static final int MSG_ID_GET_REDUNDANCYSYS = 4096;
    private static final String[] PARAMS_REDUNDANCY_SYSHAS = {ParamCfgName.GCONFIG_TOPOLOGY_SINGLE_MULT};
    public static boolean jumpToCompassPage = false;
    private static DJIRedundancySysController mInstance = null;
    public static RedundancyErrorCodeDesc mRedundancyErrorCodeDesc = null;
    /* access modifiers changed from: private */
    public Context mContext = null;
    private volatile int mFlycVersioin = Integer.MIN_VALUE;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        /* class dji.pilot.fpv.control.DJIRedundancySysController.AnonymousClass2 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 4096:
                    DJIRedundancySysController.this.updateHas();
                    return false;
                default:
                    return false;
            }
        }
    });
    /* access modifiers changed from: private */
    public volatile int mHasRedundancySys = 0;
    private List<DataFlycRedundancyStatus.IMUStatus> mIMU1StatusHistory = new ArrayList();
    private List<DataFlycRedundancyStatus.IMUStatus> mIMU2StatusHistory = new ArrayList();
    private List<DataFlycRedundancyStatus.IMUStatus> mIMU3StatusHistory = new ArrayList();
    private boolean[] mIMUNeedCalc = new boolean[3];
    private boolean mIsMotorUp = false;
    /* access modifiers changed from: private */
    public int mRetryUpdateStatusTimout = 0;
    /* access modifiers changed from: private */
    public SensorInUsing mSensorInUsing = new SensorInUsing();
    private List<DataFlycPushRedundancyStatus.RedundancySwitchInfo> mSwitchHistory = new ArrayList();
    private Thread mUpdateSensorCurUsingThread = null;

    @Keep
    public static class RedundancyErrorInfo {
        public int deviceID;
        public String deviceName = "";
        public RedundancyErrorCodeDesc.Element element;
        public int version;
    }

    @Keep
    public static class SensorInUsing {
        public int accIndex;
        public int baroIndex;
        public int gpsIndex;
        public int gyroIndex;
        public boolean isRTKUsed;
        public boolean isRadarUsed;
        public boolean isUSUsed;
        public boolean isVOUsed;
        public int magIndex;
        public int nsIndex;
    }

    static /* synthetic */ int access$1008(DJIRedundancySysController x0) {
        int i = x0.mRetryUpdateStatusTimout;
        x0.mRetryUpdateStatusTimout = i + 1;
        return i;
    }

    public static DJIRedundancySysController getInstance(Context cxt) {
        if (mInstance == null) {
            mInstance = new DJIRedundancySysController(cxt);
        }
        return mInstance;
    }

    private DJIRedundancySysController(Context cxt) {
        this.mContext = cxt;
        this.mSwitchHistory = V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).findAll(DataFlycPushRedundancyStatus.RedundancySwitchInfo.class);
        if (this.mSwitchHistory == null) {
            this.mSwitchHistory = new ArrayList();
        } else {
            Collections.sort(this.mSwitchHistory, new ComparatorRedundancySwitchInfo());
            while (this.mSwitchHistory.size() > 20) {
                try {
                    V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).delete(this.mSwitchHistory.remove(this.mSwitchHistory.size() - 1));
                } catch (Exception e) {
                }
            }
        }
        List<DataFlycRedundancyStatus.IMUStatus> imuStatus = V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).findAll(DataFlycRedundancyStatus.IMUStatus.class);
        if (imuStatus != null && !imuStatus.isEmpty()) {
            Collections.sort(imuStatus, new ComparatorIMUStatus());
            for (DataFlycRedundancyStatus.IMUStatus status : imuStatus) {
                if (status.imuIndex == 0) {
                    if (this.mIMU1StatusHistory.size() < 20) {
                        this.mIMU1StatusHistory.add(status);
                    } else {
                        V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).delete(status);
                    }
                } else if (status.imuIndex == 1) {
                    if (this.mIMU2StatusHistory.size() < 20) {
                        this.mIMU2StatusHistory.add(status);
                    } else {
                        V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).delete(status);
                    }
                } else if (status.imuIndex == 2) {
                    if (this.mIMU3StatusHistory.size() < 20) {
                        this.mIMU3StatusHistory.add(status);
                    } else {
                        V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).delete(status);
                    }
                }
            }
        }
        EventBus.getDefault().register(this);
        this.mUpdateSensorCurUsingThread = new Thread() {
            /* class dji.pilot.fpv.control.DJIRedundancySysController.AnonymousClass1 */
            private int count = 0;
            /* access modifiers changed from: private */
            public boolean mCurIMUStatusLastError = false;
            /* access modifiers changed from: private */
            public int mLastCurIMUStatusErrorIndex = -1;
            /* access modifiers changed from: private */
            public int mLastCurIMUStatusErrorType = -1;

            public void run() {
                while (true) {
                    if (DJICommonUtils.supportRedundancySenor()) {
                        int i = this.count;
                        this.count = i + 1;
                        if (i % 2 == 0) {
                            new DataFlycGetParams().setInfos(new String[]{"g_status.ns_busy_dev_0"}).start(new DJIDataCallBack() {
                                /* class dji.pilot.fpv.control.DJIRedundancySysController.AnonymousClass1.AnonymousClass1 */

                                public void onSuccess(Object model) {
                                    boolean z;
                                    boolean z2;
                                    boolean z3 = true;
                                    int val = DJIFlycParamInfoManager.read("g_status.ns_busy_dev_0").value.intValue();
                                    DJIRedundancySysController.this.mSensorInUsing.nsIndex = val & 3;
                                    DJIRedundancySysController.this.mSensorInUsing.accIndex = (val >> 2) & 3;
                                    DJIRedundancySysController.this.mSensorInUsing.gyroIndex = (val >> 4) & 3;
                                    DJIRedundancySysController.this.mSensorInUsing.magIndex = (val >> 6) & 3;
                                    DJIRedundancySysController.this.mSensorInUsing.gpsIndex = (val >> 8) & 3;
                                    DJIRedundancySysController.this.mSensorInUsing.baroIndex = (val >> 10) & 3;
                                    DJIRedundancySysController.this.mSensorInUsing.isRTKUsed = ((val >> 12) & 1) == 1;
                                    SensorInUsing access$200 = DJIRedundancySysController.this.mSensorInUsing;
                                    if (((val >> 13) & 1) == 1) {
                                        z = true;
                                    } else {
                                        z = false;
                                    }
                                    access$200.isVOUsed = z;
                                    SensorInUsing access$2002 = DJIRedundancySysController.this.mSensorInUsing;
                                    if (((val >> 14) & 1) == 1) {
                                        z2 = true;
                                    } else {
                                        z2 = false;
                                    }
                                    access$2002.isUSUsed = z2;
                                    SensorInUsing access$2003 = DJIRedundancySysController.this.mSensorInUsing;
                                    if (((val >> 15) & 1) != 1) {
                                        z3 = false;
                                    }
                                    access$2003.isRadarUsed = z3;
                                    EventBus.getDefault().post(DJIRedundancySysController.this.mSensorInUsing);
                                }

                                public void onFailure(Ccode ccode) {
                                }
                            });
                        } else {
                            final DataFlycRedundancyStatus reduSta = DataFlycRedundancyStatus.getInstance();
                            reduSta.setCmdType(DataFlycRedundancyStatus.RS_CMD_TYPE.ASK_ERR_STATE).start(new DJIDataCallBack() {
                                /* class dji.pilot.fpv.control.DJIRedundancySysController.AnonymousClass1.AnonymousClass2 */

                                public void onSuccess(Object model) {
                                    EventBus.getDefault().post(reduSta);
                                    if (reduSta.getCmdType() == DataFlycRedundancyStatus.RS_CMD_TYPE.ASK_ERR_STATE) {
                                        for (DataFlycRedundancyStatus.IMUStatus sta : reduSta.getIMUStatus()) {
                                            if (DataFlycRedundancyStatus.COLOR_STATUS.ofValue(sta.colorStatus) == DataFlycRedundancyStatus.COLOR_STATUS.RED_FLASH && sta.isRealInAir) {
                                                RedundancyErrorInfo rei = DJIRedundancySysController.getIMUErrInfo(DJIRedundancySysController.this.mContext, sta);
                                                if (rei.element != null && !V_StringUtils.isEmpty(rei.element.in_air_used_action)) {
                                                    if (AnonymousClass1.this.mCurIMUStatusLastError && AnonymousClass1.this.mLastCurIMUStatusErrorIndex == sta.imuIndex && AnonymousClass1.this.mLastCurIMUStatusErrorType == sta.devErrCode) {
                                                        AppPubInjectManager.getAppPubToP3Injectable().sensorPopWindow(rei);
                                                        boolean unused = AnonymousClass1.this.mCurIMUStatusLastError = false;
                                                        int unused2 = AnonymousClass1.this.mLastCurIMUStatusErrorIndex = -1;
                                                        return;
                                                    }
                                                    int unused3 = AnonymousClass1.this.mLastCurIMUStatusErrorIndex = sta.imuIndex;
                                                    int unused4 = AnonymousClass1.this.mLastCurIMUStatusErrorType = sta.devErrCode;
                                                    boolean unused5 = AnonymousClass1.this.mCurIMUStatusLastError = true;
                                                    return;
                                                }
                                            }
                                        }
                                        boolean unused6 = AnonymousClass1.this.mCurIMUStatusLastError = false;
                                    }
                                }

                                public void onFailure(Ccode ccode) {
                                }
                            });
                        }
                    }
                    try {
                        sleep(1500);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };
        this.mUpdateSensorCurUsingThread.start();
    }

    @Keep
    private static final class ComparatorRedundancySwitchInfo implements Comparator<DataFlycPushRedundancyStatus.RedundancySwitchInfo> {
        private ComparatorRedundancySwitchInfo() {
        }

        public int compare(DataFlycPushRedundancyStatus.RedundancySwitchInfo object1, DataFlycPushRedundancyStatus.RedundancySwitchInfo object2) {
            if (object1.time > object2.time) {
                return -1;
            }
            if (object1.time < object2.time) {
                return 1;
            }
            return 0;
        }
    }

    @Keep
    private static final class ComparatorIMUStatus implements Comparator<DataFlycRedundancyStatus.IMUStatus> {
        private ComparatorIMUStatus() {
        }

        public int compare(DataFlycRedundancyStatus.IMUStatus object1, DataFlycRedundancyStatus.IMUStatus object2) {
            if (object1.time > object2.time) {
                return -1;
            }
            if (object1.time < object2.time) {
                return 1;
            }
            return 0;
        }
    }

    public SensorInUsing getSensorInUsing() {
        return this.mSensorInUsing;
    }

    public void destroy() {
        EventBus.getDefault().unregister(this);
        mInstance = null;
        if (this.mUpdateSensorCurUsingThread != null) {
            this.mUpdateSensorCurUsingThread.interrupt();
            this.mUpdateSensorCurUsingThread = null;
        }
    }

    public List<DataFlycPushRedundancyStatus.RedundancySwitchInfo> getSwitchHistory() {
        return this.mSwitchHistory;
    }

    public void clearSwitchHistory() {
        for (int i = this.mSwitchHistory.size() - 1; i >= 0; i--) {
            try {
                V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).delete(this.mSwitchHistory.remove(i));
            } catch (Exception e) {
            }
        }
    }

    public List<DataFlycRedundancyStatus.IMUStatus> getIMU1StatusHistory() {
        return this.mIMU1StatusHistory;
    }

    public void clearIMU1StatusHistory() {
        for (int i = this.mIMU1StatusHistory.size() - 1; i >= 0; i--) {
            try {
                V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).delete(this.mIMU1StatusHistory.remove(i));
            } catch (Exception e) {
            }
        }
    }

    public List<DataFlycRedundancyStatus.IMUStatus> getIMU2StatusHistory() {
        return this.mIMU2StatusHistory;
    }

    public void clearIMU2StatusHistory() {
        for (int i = this.mIMU2StatusHistory.size() - 1; i >= 0; i--) {
            try {
                V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).delete(this.mIMU2StatusHistory.remove(i));
            } catch (Exception e) {
            }
        }
    }

    public List<DataFlycRedundancyStatus.IMUStatus> getIMU3StatusHistory() {
        return this.mIMU3StatusHistory;
    }

    public void clearIMU3StatusHistory() {
        for (int i = this.mIMU3StatusHistory.size() - 1; i >= 0; i--) {
            try {
                V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).delete(this.mIMU3StatusHistory.remove(i));
            } catch (Exception e) {
            }
        }
    }

    public void addIMUStatusHistory(DataFlycRedundancyStatus.IMUStatus sta, boolean fromPush) {
        if (sta.devErrCode != 0) {
            RedundancyErrorInfo info = getIMUErrInfo(this.mContext, sta);
            if (info.element == null || info.element.history_enable != 0) {
                sta.time = System.currentTimeMillis();
                if (sta.imuIndex == 0) {
                    if (fromPush) {
                        if (this.mIMU1StatusHistory.size() >= 20) {
                            try {
                                V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).delete(this.mIMU1StatusHistory.remove(this.mIMU1StatusHistory.size() - 1));
                            } catch (Exception e) {
                            }
                        }
                        this.mIMU1StatusHistory.add(0, sta);
                        V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).save(sta);
                    }
                } else if (sta.imuIndex == 1) {
                    if (fromPush) {
                        if (this.mIMU2StatusHistory.size() >= 20) {
                            try {
                                V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).delete(this.mIMU2StatusHistory.remove(this.mIMU2StatusHistory.size() - 1));
                            } catch (Exception e2) {
                            }
                        }
                        this.mIMU2StatusHistory.add(0, sta);
                        V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).save(sta);
                    }
                } else if (sta.imuIndex == 2 && fromPush) {
                    if (this.mIMU3StatusHistory.size() >= 20) {
                        try {
                            V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).delete(this.mIMU3StatusHistory.remove(this.mIMU3StatusHistory.size() - 1));
                        } catch (Exception e3) {
                        }
                    }
                    this.mIMU3StatusHistory.add(0, sta);
                    V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).save(sta);
                }
            }
        }
    }

    public void setIMUNeedCalc(int index, boolean needCalc) {
        if (index >= 0 && index < this.mIMUNeedCalc.length) {
            this.mIMUNeedCalc[index] = needCalc;
        }
    }

    public boolean[] getIMUNeedCalc() {
        return this.mIMUNeedCalc;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycPushRedundancyStatus status) {
        DataFlycRedundancyStatus.RS_CMD_TYPE type = status.getCmdType();
        if (type == DataFlycRedundancyStatus.RS_CMD_TYPE.SEND_SWITCH_STATE) {
            DataFlycPushRedundancyStatus.RedundancySwitchInfo item = status.getSwitchInfo();
            if (item.resultCode == 6) {
                if (this.mSwitchHistory.size() >= 20) {
                    try {
                        V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).delete(this.mSwitchHistory.remove(this.mSwitchHistory.size() - 1));
                    } catch (Exception e) {
                    }
                }
                item.time = System.currentTimeMillis();
                this.mSwitchHistory.add(0, item);
                V_AppUtils.getFinalDb(this.mContext.getApplicationContext()).save(item);
                if (item.reqID == 2) {
                    AppPubInjectManager.getAppPubToP3Injectable().sensorPopWindow(Integer.valueOf(R.string.fpv_redundancy_switch_warning));
                }
            }
        } else if (type == DataFlycRedundancyStatus.RS_CMD_TYPE.SEND_ERR_STATE) {
            addIMUStatusHistory(status.getIMUStatus(), true);
        }
    }

    /* access modifiers changed from: private */
    public boolean isStatusOK(int status) {
        switch (DataFlycRedundancyStatus.COLOR_STATUS.ofValue(status)) {
            case GRAY:
            case GREEN:
            case GREEN_FLASH:
                return true;
            default:
                return false;
        }
    }

    /* access modifiers changed from: private */
    public void updateHas() {
        if (ServiceManager.getInstance().isConnected()) {
            new DataFlycGetParams().setInfos(PARAMS_REDUNDANCY_SYSHAS).start(new DJIDataCallBack() {
                /* class dji.pilot.fpv.control.DJIRedundancySysController.AnonymousClass3 */

                public void onSuccess(Object model) {
                    int unused = DJIRedundancySysController.this.mHasRedundancySys = DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_TOPOLOGY_SINGLE_MULT).value.intValue();
                    DJIRedundancySysController.this.mHandler.sendEmptyMessageDelayed(4096, 1000);
                }

                public void onFailure(Ccode ccode) {
                    DJIRedundancySysController.this.mHandler.sendEmptyMessageDelayed(4096, 200);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void updateStatus() {
        DataFlycRedundancyStatus.getInstance().setCmdType(DataFlycRedundancyStatus.RS_CMD_TYPE.ASK_ERR_STATE).start(new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIRedundancySysController.AnonymousClass4 */

            public void onSuccess(Object model) {
                int unused = DJIRedundancySysController.this.mRetryUpdateStatusTimout = 0;
                boolean isOK = true;
                for (DataFlycRedundancyStatus.IMUStatus sta : DataFlycRedundancyStatus.getInstance().getIMUStatus()) {
                    if (!DJIRedundancySysController.this.isStatusOK(sta.colorStatus)) {
                        isOK = false;
                    }
                }
                if (!isOK && DJIRedundancySysController.this.mHasRedundancySys == 1) {
                    AppPubInjectManager.getAppPubToP3Injectable().sensorPopWindow(Integer.valueOf(R.string.fpv_check_redundancy_failed_when_motor_up));
                }
            }

            public void onFailure(Ccode ccode) {
                if (DJIRedundancySysController.access$1008(DJIRedundancySysController.this) < 10) {
                    DJIRedundancySysController.this.updateStatus();
                } else {
                    int unused = DJIRedundancySysController.this.mRetryUpdateStatusTimout = 0;
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        if (DataCameraEvent.ConnectLose == event) {
            this.mFlycVersioin = Integer.MIN_VALUE;
            this.mHandler.removeMessages(4096);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        if (DataEvent.ConnectLose == event) {
            this.mFlycVersioin = Integer.MIN_VALUE;
            this.mHandler.removeMessages(4096);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon status) {
        if (DJICommonUtils.isA3()) {
            int version = status.getFlycVersion();
            if (version != this.mFlycVersioin) {
                this.mFlycVersioin = version;
                updateHas();
            }
            boolean isMotorUp = status.isMotorUp();
            if (this.mIsMotorUp != isMotorUp) {
                this.mIsMotorUp = isMotorUp;
                if (this.mIsMotorUp) {
                    updateStatus();
                }
            }
        }
    }

    public static RedundancyErrorInfo getIMUErrInfo(Context cxt, DataFlycRedundancyStatus.IMUStatus sta) {
        return getIMUErrInfo(cxt, sta.devType, sta.devErrCode);
    }

    public static RedundancyErrorInfo getIMUErrInfo(Context cxt, int devID, int errCode) {
        loadJson(cxt);
        RedundancyErrorInfo info = new RedundancyErrorInfo();
        info.deviceID = devID;
        if (mRedundancyErrorCodeDesc != null) {
            Iterator<RedundancyErrorCodeDesc.DevType> it2 = mRedundancyErrorCodeDesc.devices.iterator();
            while (true) {
                if (it2.hasNext()) {
                    RedundancyErrorCodeDesc.DevType device = it2.next();
                    if (device != null && device.id == devID) {
                        info.deviceName = device.getName();
                        info.version = device.version;
                        Iterator<RedundancyErrorCodeDesc.Element> it3 = device.element.iterator();
                        while (true) {
                            if (!it3.hasNext()) {
                                break;
                            }
                            RedundancyErrorCodeDesc.Element ele = it3.next();
                            if (ele.code == errCode) {
                                info.element = ele;
                                break;
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        }
        return info;
    }

    public static String enToCnIMUErrDesc(Context cxt, String en) {
        loadJson(cxt);
        if (mRedundancyErrorCodeDesc != null) {
            for (List<String> item : mRedundancyErrorCodeDesc.en_2_ch) {
                if (((String) item.get(0)).compareTo(en) == 0) {
                    return (String) item.get(1);
                }
            }
        }
        return "";
    }

    private static void loadJson(Context cxt) {
        if (mRedundancyErrorCodeDesc == null) {
            mRedundancyErrorCodeDesc = (RedundancyErrorCodeDesc) V_JsonUtil.toBean(InputStream2String(cxt.getResources().openRawResource(R.raw.redundancy_error_code_desc), "utf-8"), RedundancyErrorCodeDesc.class);
        }
        if (mRedundancyErrorCodeDesc != null && cxt.getResources().getConfiguration().locale.getLanguage().endsWith(LanguageUtils.ZH_DJI_LANG_CODE)) {
            for (RedundancyErrorCodeDesc.DevType device : mRedundancyErrorCodeDesc.devices) {
                String str = getCN(device.name);
                if (!V_StringUtils.isEmpty(str)) {
                    device.name = str;
                }
                for (RedundancyErrorCodeDesc.Element ele : device.element) {
                    String s = getCN(ele.usr_err_tips);
                    if (!V_StringUtils.isEmpty(s)) {
                        ele.usr_err_tips = s;
                    }
                    String s2 = getCN(ele.ground_action);
                    if (!V_StringUtils.isEmpty(s2)) {
                        ele.ground_action = s2;
                    }
                    String s3 = getCN(ele.in_air_used_action);
                    if (!V_StringUtils.isEmpty(s3)) {
                        ele.in_air_used_action = s3;
                    }
                }
            }
        }
    }

    private static String getCN(String en) {
        if (mRedundancyErrorCodeDesc != null && !V_StringUtils.isEmpty(en)) {
            for (List<String> item : mRedundancyErrorCodeDesc.en_2_ch) {
                if (((String) item.get(0)).compareTo(en) == 0) {
                    return (String) item.get(1);
                }
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000c, code lost:
        if (r9.equals("") != false) goto L_0x000e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String InputStream2String(java.io.InputStream r8, java.lang.String r9) {
        /*
            java.lang.String r4 = ""
            if (r9 == 0) goto L_0x000e
            java.lang.String r6 = ""
            boolean r6 = r9.equals(r6)     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            if (r6 == 0) goto L_0x0011
        L_0x000e:
            java.lang.String r9 = "utf-8"
        L_0x0011:
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            r6.<init>(r8, r9)     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            r2.<init>(r6)     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            java.lang.StringBuffer r3 = new java.lang.StringBuffer     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            r3.<init>()     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
        L_0x0020:
            java.lang.String r4 = r2.readLine()     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            if (r4 == 0) goto L_0x0038
            java.lang.StringBuffer r6 = r3.append(r4)     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            java.lang.String r7 = "\n"
            r6.append(r7)     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            goto L_0x0020
        L_0x0031:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0035:
            r5 = r4
            r6 = r4
        L_0x0037:
            return r6
        L_0x0038:
            java.lang.String r6 = r3.toString()     // Catch:{ UnsupportedEncodingException -> 0x0031, IOException -> 0x003e }
            r5 = r4
            goto L_0x0037
        L_0x003e:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0035
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.fpv.control.DJIRedundancySysController.InputStream2String(java.io.InputStream, java.lang.String):java.lang.String");
    }

    @Keep
    public class RedundancyErrorCodeDesc {
        public List<DevType> devices;
        public List<List<String>> en_2_ch;

        public RedundancyErrorCodeDesc() {
        }

        public class DevType {
            public List<Element> element;
            public int id;
            public String name = "";
            public int version;

            public DevType() {
            }

            public String getName() {
                return this.name;
            }
        }

        public class Element {
            public int code;
            public String detail_ch_tips = "";
            public int free_repair;
            public String ground_action = "";
            public int history_enable;
            public String in_air_used_action = "";
            public String usr_err_tips = "";
            public int usr_show_enable;

            public Element() {
            }

            public String getUserErrTips() {
                return this.usr_err_tips;
            }

            public String getGroundAction() {
                return this.ground_action;
            }

            public String getAirAction() {
                return this.in_air_used_action;
            }
        }
    }
}
