package dji.pilot.fpv.control;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import com.dji.permission.Permission;
import dji.common.airlink.WiFiFrequencyBand;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.log.WM220LogUtil;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdIdWifi;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataCommonSetMultiParam;
import dji.midware.data.model.P3.DataWifiGetChannelList;
import dji.midware.data.model.P3.DataWifiGetPassword;
import dji.midware.data.model.P3.DataWifiGetSSID;
import dji.midware.data.model.P3.DataWifiRestart;
import dji.midware.data.model.P3.DataWifiSetPassword;
import dji.midware.data.model.P3.DataWifiSetSSID;
import dji.midware.interfaces.DJIDataCallBack;
import dji.pilot.publics.objects.DjiSharedPreferencesManager;
import dji.pilot.publics.util.DJICommonUtils;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIWifiController implements DJIParamAccessListener {
    private static final String DEFAULT_PWD = "";
    private static final String DEFAULT_SSID = "";
    private static final int FLAG_UPDATE_ALL = 3;
    private static final int FLAG_UPDATE_NONE = 0;
    private static final int FLAG_UPDATE_PWD = 2;
    private static final int FLAG_UPDATE_SSID = 1;
    private static final boolean IS_SUPPORT_SCAN_WIFI = true;
    public static final String KEY_PHONE_SUPPORT_5G = "key_phone_support_5g";
    private static final int MSG_ID_GET_PWD = 4097;
    private static final int MSG_ID_GET_SSID = 4096;
    private static final int MSG_ID_GET_WIFI_CHANNEL_LIST = 4102;
    private static final int MSG_ID_GET_WIFI_CUR_CODE_RATE = 4103;
    private static final int MSG_ID_RESTART_WIFI = 4101;
    private static final int MSG_ID_SET_PWD = 4099;
    private static final int MSG_ID_SET_SSID = 4098;
    private static final int MSG_ID_UPDATE_SETTING = 4100;
    private static final int RESULT_FAIL = 1;
    private static final int RESULT_OK = 0;
    private static final String TAG = DJIWifiController.class.getSimpleName();
    public boolean isMammothRcChangePw;
    /* access modifiers changed from: private */
    public final InnerHandler mHandler;
    private volatile boolean mInit;
    private OnWifiListener mOnWifiListener;
    private String mTmpPwd;
    private String mTmpSsid;
    private volatile int mUpdateFlag;
    private String mWifiPwd;
    private final DataWifiGetPassword mWifiPwdGetter;
    private final DataWifiSetPassword mWifiPwdSetter;
    private String mWifiSsid;
    private final DataWifiGetSSID mWifiSsidGetter;
    private final DataWifiSetSSID mWifiSsidSetter;

    public interface OnWifiListener {
        void onWifiChannelGetted(boolean z, Ccode ccode);

        void onWifiCodeRateGetted(boolean z, Ccode ccode);

        void onWifiPwdGetted(boolean z, String str, Ccode ccode, int i);

        void onWifiPwdSetted(boolean z, String str, Ccode ccode, int i);

        void onWifiSetted(boolean z, Ccode ccode, int i);

        void onWifiSsidGetted(boolean z, String str, Ccode ccode, int i);

        void onWifiSsidSetted(boolean z, String str, Ccode ccode, int i);
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (CacheHelper.isDataValid(key, newValue) && key.getParamKey().equals("FrequencyBand")) {
            WiFiFrequencyBand wiFiFrequencyBand = (WiFiFrequencyBand) CacheHelper.getWiFiAirLink("FrequencyBand");
            this.mHandler.obtainMessage(4102, 0, 0).sendToTarget();
        }
    }

    public static DJIWifiController getInstance() {
        return SingletonHolder.mInstance;
    }

    public synchronized void initializeManager() {
        if (!this.mInit) {
            this.mInit = true;
            CacheHelper.addWiFiAirlinkListener(this, "FrequencyBand");
        }
    }

    public synchronized void finalizeManager() {
        if (this.mInit) {
            CacheHelper.removeListener(this);
            this.mInit = false;
        }
    }

    public void setWifiListener(OnWifiListener listener) {
        this.mOnWifiListener = listener;
    }

    public String getWifiSsid() {
        return this.mWifiSsid;
    }

    public String getWifiPwd() {
        return this.mWifiPwd;
    }

    public void updateWifiSetting(String ssid, String pwd) {
        int ret = 0;
        if (!this.mWifiSsid.equals(ssid)) {
            ret = 0 | 1;
        }
        if (!this.mWifiPwd.equals(pwd)) {
            ret |= 2;
        }
        if (ret == 3) {
            this.mTmpSsid = ssid;
            this.mTmpPwd = pwd;
            byte[] ssidBytes = ssid.getBytes();
            byte[] pwdBytes = pwd.getBytes();
            byte[] cmdBytes = new byte[(ssidBytes.length + 3 + 2 + pwdBytes.length + 2)];
            cmdBytes[0] = 3;
            cmdBytes[1] = (byte) CmdIdWifi.CmdIdType.SetSSID.value();
            cmdBytes[2] = (byte) ssidBytes.length;
            System.arraycopy(ssidBytes, 0, cmdBytes, 3, ssidBytes.length);
            cmdBytes[ssidBytes.length + 3] = (byte) CmdIdWifi.CmdIdType.SetPassword.value();
            cmdBytes[ssidBytes.length + 4] = (byte) pwdBytes.length;
            System.arraycopy(pwdBytes, 0, cmdBytes, ssidBytes.length + 5, pwdBytes.length);
            cmdBytes[ssidBytes.length + 5] = (byte) CmdIdWifi.CmdIdType.RestartWifi.value();
            cmdBytes[ssidBytes.length + 6] = 0;
            updateSettingInner(cmdBytes);
        } else if (ret == 1) {
            this.mTmpSsid = ssid;
            this.mTmpPwd = this.mWifiPwd;
            byte[] ssidBytes2 = ssid.getBytes();
            byte[] cmdBytes2 = new byte[(ssidBytes2.length + 3 + 2)];
            cmdBytes2[0] = 2;
            cmdBytes2[1] = (byte) CmdIdWifi.CmdIdType.SetSSID.value();
            cmdBytes2[2] = (byte) ssidBytes2.length;
            System.arraycopy(ssidBytes2, 0, cmdBytes2, 3, ssidBytes2.length);
            cmdBytes2[ssidBytes2.length + 3] = (byte) CmdIdWifi.CmdIdType.RestartWifi.value();
            cmdBytes2[ssidBytes2.length + 4] = 0;
            updateSettingInner(cmdBytes2);
        } else if (ret == 2) {
            this.mTmpSsid = this.mWifiSsid;
            this.mTmpPwd = pwd;
            byte[] pwdBytes2 = pwd.getBytes();
            byte[] cmdBytes3 = new byte[(pwdBytes2.length + 3 + 2)];
            cmdBytes3[0] = 2;
            cmdBytes3[1] = (byte) CmdIdWifi.CmdIdType.SetPassword.value();
            cmdBytes3[2] = (byte) pwdBytes2.length;
            System.arraycopy(pwdBytes2, 0, cmdBytes3, 3, pwdBytes2.length);
            cmdBytes3[pwdBytes2.length + 3] = (byte) CmdIdWifi.CmdIdType.RestartWifi.value();
            cmdBytes3[pwdBytes2.length + 4] = 0;
            updateSettingInner(cmdBytes3);
        } else {
            notifyWifiSetted(true, Ccode.SUCCEED, 0);
        }
    }

    private void updateSettingInner(byte[] cmdBytes) {
        final DataCommonSetMultiParam setter = new DataCommonSetMultiParam();
        setter.setReceiverType(DeviceType.WIFI).setMultiParam(cmdBytes);
        setter.start(new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIWifiController.AnonymousClass1 */

            public void onSuccess(Object model) {
                DJIWifiController.this.mHandler.obtainMessage(4100, 0, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIWifiController.this.mHandler.obtainMessage(4100, 1, setter.getErrorCmdId(), ccode).sendToTarget();
            }
        });
    }

    public boolean isSsidPwdChanged(String ssid, String pwd) {
        return !this.mWifiSsid.equals(ssid) || !this.mWifiPwd.equals(pwd);
    }

    public void updateWifiSetting2(String ssid, String pwd) {
        this.mUpdateFlag = 0;
        this.isMammothRcChangePw = DJICommonUtils.isConnectedToMammothRC() || DJICommonUtils.isWM230();
        log("isMammothRcChangePw=" + this.isMammothRcChangePw);
        if (!this.mWifiSsid.equals(ssid)) {
            this.mUpdateFlag |= 1;
        }
        if (!this.mWifiPwd.equals(pwd)) {
            this.mUpdateFlag |= 2;
        }
        if (this.mUpdateFlag == 0) {
            notifyWifiSetted(true, Ccode.SUCCEED, 0);
        } else if ((this.mUpdateFlag & 1) != 0) {
            this.mTmpPwd = pwd;
            setWifiSsid(ssid);
        } else if ((this.mUpdateFlag & 2) != 0) {
            setWifiPwd(pwd);
        }
    }

    private void setWifiSsid(final String ssid) {
        this.mWifiSsidSetter.setSSID(ssid.getBytes()).start(new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIWifiController.AnonymousClass2 */

            public void onSuccess(Object model) {
                DJIWifiController.this.log("setWifiSsid success");
                DJIWifiController.this.mHandler.obtainMessage(4098, 0, 0, ssid).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIWifiController.this.log("setWifiSsid failure ccode" + ccode);
                if (!DJIWifiController.this.isMammothRcChangePw || ccode != Ccode.TIMEOUT) {
                    DJIWifiController.this.mHandler.obtainMessage(4098, 1, 0, ccode).sendToTarget();
                } else {
                    DJIWifiController.this.mHandler.obtainMessage(4098, 0, 0, ssid).sendToTarget();
                }
            }
        });
    }

    private void setWifiPwd(final String pwd) {
        this.mWifiPwdSetter.setPassword(pwd.getBytes()).start(new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIWifiController.AnonymousClass3 */

            public void onSuccess(Object model) {
                DJIWifiController.this.log("setWifiPwd success");
                DJIWifiController.this.mHandler.obtainMessage(4099, 0, 0, pwd).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIWifiController.this.log("setWifiPwd failure ccode" + ccode);
                if (!DJIWifiController.this.isMammothRcChangePw || ccode != Ccode.TIMEOUT) {
                    DJIWifiController.this.mHandler.obtainMessage(4099, 1, 0, ccode).sendToTarget();
                } else {
                    DJIWifiController.this.mHandler.obtainMessage(4099, 0, 0, pwd).sendToTarget();
                }
            }
        });
    }

    public void restartWifi() {
        DataWifiRestart.getInstance().start(new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIWifiController.AnonymousClass4 */

            public void onSuccess(Object model) {
                DJIWifiController.this.mHandler.obtainMessage(4101, 0, 0).sendToTarget();
                DJIWifiController.this.log("restart wifi success");
            }

            public void onFailure(Ccode ccode) {
                DJIWifiController.this.log("restart wifi failure ccode" + ccode + "");
                if (!DJIWifiController.this.isMammothRcChangePw || !(ccode == Ccode.TIMEOUT || ccode == Ccode.NOCONNECT)) {
                    DJIWifiController.this.mHandler.obtainMessage(4101, 1, 0, ccode).sendToTarget();
                } else {
                    DJIWifiController.this.mHandler.obtainMessage(4101, 0, 0).sendToTarget();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void log(String log) {
    }

    public void requestRefreshWiFiData() {
        this.mHandler.obtainMessage(4102, 0, 0).sendToTarget();
    }

    public void startGetData() {
        this.mWifiSsidGetter.start(new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIWifiController.AnonymousClass5 */

            public void onSuccess(Object model) {
                DJIWifiController.this.mHandler.obtainMessage(4096, 0, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIWifiController.this.mHandler.obtainMessage(4096, 1, 0, ccode).sendToTarget();
            }
        });
        this.mWifiPwdGetter.start(new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIWifiController.AnonymousClass6 */

            public void onSuccess(Object model) {
                DJIWifiController.this.mHandler.obtainMessage(4097, 0, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIWifiController.this.mHandler.obtainMessage(4097, 1, 0, ccode).sendToTarget();
            }
        });
        DataWifiGetChannelList.getInstance().start(new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIWifiController.AnonymousClass7 */

            public void onSuccess(Object model) {
                DJIWifiController.this.mHandler.obtainMessage(4102, 0, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIWifiController.this.mHandler.obtainMessage(4102, 1, 0, ccode).sendToTarget();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DataEvent event) {
        if (event == DataEvent.ConnectLose) {
        }
    }

    /* access modifiers changed from: private */
    public boolean canDo() {
        return this.mInit;
    }

    private void notifyWifiSetted(boolean ok, Ccode code, int arg) {
        if (this.mOnWifiListener != null) {
            this.mOnWifiListener.onWifiSetted(ok, code, arg);
        }
    }

    private void notifySsidGetted(boolean ok, String ssid, Ccode code, int arg) {
        if (this.mOnWifiListener != null) {
            this.mOnWifiListener.onWifiSsidGetted(ok, ssid, code, arg);
        }
    }

    private void notifyPwdGetted(boolean ok, String pwd, Ccode code, int arg) {
        if (this.mOnWifiListener != null) {
            this.mOnWifiListener.onWifiPwdGetted(ok, pwd, code, arg);
        }
    }

    private void notifySsidSetted(boolean ok, String ssid, Ccode code, int arg) {
        if (this.mOnWifiListener != null) {
            this.mOnWifiListener.onWifiSsidSetted(ok, ssid, code, arg);
        }
    }

    private void notifyPwdSetted(boolean ok, String pwd, Ccode code, int arg) {
        if (this.mOnWifiListener != null) {
            this.mOnWifiListener.onWifiPwdSetted(ok, pwd, code, arg);
        }
    }

    /* access modifiers changed from: private */
    public void handleSsidGet(int result, Ccode code) {
        boolean z;
        if (result == 0) {
            this.mWifiSsid = this.mWifiSsidGetter.getSSID();
        }
        if (result == 0) {
            z = true;
        } else {
            z = false;
        }
        notifySsidGetted(z, this.mWifiSsid, code, 0);
    }

    /* access modifiers changed from: private */
    public void handlePwdGet(int result, Ccode code) {
        boolean z;
        if (result == 0) {
            this.mWifiPwd = this.mWifiPwdGetter.getPassword();
        }
        if (result == 0) {
            z = true;
        } else {
            z = false;
        }
        notifyPwdGetted(z, this.mWifiPwd, code, 0);
    }

    /* access modifiers changed from: private */
    public void handleSsidSet(int result, Object obj) {
        DJILogHelper.getInstance().LOGD(TAG, "== Wifi SSID result[" + result + "]obj[" + obj + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (result == 0) {
            this.mWifiSsid = (String) obj;
            if ((this.mUpdateFlag & 1) != 0) {
                this.mUpdateFlag &= -2;
                if (this.mUpdateFlag == 0) {
                    restartWifi();
                } else if ((this.mUpdateFlag & 2) != 0) {
                    setWifiPwd(this.mTmpPwd);
                }
            } else {
                notifySsidSetted(true, this.mWifiSsid, null, 0);
            }
        } else if ((this.mUpdateFlag & 1) != 0) {
            this.mUpdateFlag = 0;
            notifyWifiSetted(false, obj instanceof Ccode ? (Ccode) obj : null, 0);
        } else {
            notifySsidSetted(false, this.mWifiSsid, obj instanceof Ccode ? (Ccode) obj : null, 0);
        }
    }

    /* access modifiers changed from: private */
    public void handlePwdSet(int result, Object obj) {
        DJILogHelper.getInstance().LOGD(TAG, "== Wifi Pwd result[" + result + "]obj[" + obj + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (result == 0) {
            this.mWifiPwd = (String) obj;
            if ((this.mUpdateFlag & 2) != 0) {
                this.mUpdateFlag &= -3;
                if (this.mUpdateFlag == 0) {
                    restartWifi();
                    return;
                }
                return;
            }
            notifyPwdSetted(true, this.mWifiPwd, null, 0);
        } else if ((this.mUpdateFlag & 2) != 0) {
            this.mUpdateFlag = 0;
            notifyWifiSetted(false, obj instanceof Ccode ? (Ccode) obj : null, 0);
        } else {
            notifyPwdSetted(false, this.mWifiPwd, obj instanceof Ccode ? (Ccode) obj : null, 0);
        }
    }

    /* access modifiers changed from: private */
    public void handleUpdateSetting(int result, int arg, Object obj) {
        if (result == 0) {
            this.mWifiSsid = this.mTmpSsid;
            this.mWifiPwd = this.mTmpPwd;
            notifyWifiSetted(true, null, arg);
            return;
        }
        Ccode errCode = obj instanceof Ccode ? (Ccode) obj : Ccode.UNDEFINED;
        if (CmdIdWifi.CmdIdType.SetSSID.value() != arg) {
            if (CmdIdWifi.CmdIdType.SetPassword.value() == arg) {
                this.mWifiSsid = this.mTmpSsid;
            } else if (CmdIdWifi.CmdIdType.RestartWifi.value() == arg) {
                this.mWifiSsid = this.mTmpSsid;
                this.mWifiPwd = this.mTmpPwd;
            }
        }
        notifyWifiSetted(false, errCode, arg);
    }

    /* access modifiers changed from: private */
    public void handleRestartWifi(int result, int arg, Object obj) {
        DJILogHelper.getInstance().LOGD(TAG, "==== Wifi Restart result[" + result + "]obj[" + obj + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (result == 0) {
            notifyWifiSetted(true, null, arg);
        } else {
            notifyWifiSetted(false, obj instanceof Ccode ? (Ccode) obj : Ccode.UNDEFINED, arg);
        }
    }

    /* access modifiers changed from: private */
    public void handleChannelListGetted(int result, Object obj) {
        if (this.mOnWifiListener != null) {
            if (result == 0) {
                this.mOnWifiListener.onWifiChannelGetted(true, null);
            } else {
                this.mOnWifiListener.onWifiChannelGetted(false, obj instanceof Ccode ? (Ccode) obj : Ccode.UNDEFINED);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleCodeRateGetted(int result, Object obj) {
        if (this.mOnWifiListener != null) {
            if (result == 0) {
                this.mOnWifiListener.onWifiCodeRateGetted(true, null);
            } else {
                this.mOnWifiListener.onWifiCodeRateGetted(false, obj instanceof Ccode ? (Ccode) obj : Ccode.UNDEFINED);
            }
        }
    }

    public void startGetWifiSweep() {
    }

    public void stopGetWifiSweep() {
    }

    private DJIWifiController() {
        this.mWifiSsid = "";
        this.mWifiPwd = "";
        this.mWifiSsidGetter = DataWifiGetSSID.getInstance();
        this.mWifiPwdGetter = DataWifiGetPassword.getInstance();
        this.mWifiSsidSetter = DataWifiSetSSID.getInstance();
        this.mWifiPwdSetter = DataWifiSetPassword.getInstance();
        this.mInit = false;
        this.mOnWifiListener = null;
        this.mTmpSsid = null;
        this.mTmpPwd = null;
        this.mUpdateFlag = 0;
        this.isMammothRcChangePw = false;
        this.mHandler = new InnerHandler();
    }

    private static final class InnerHandler extends Handler {
        private final WeakReference<DJIWifiController> mOutCls;

        private InnerHandler(DJIWifiController cls) {
            super(Looper.getMainLooper());
            this.mOutCls = new WeakReference<>(cls);
        }

        public void handleMessage(Message msg) {
            Ccode ccode = null;
            DJIWifiController cls = this.mOutCls.get();
            if (cls != null && cls.canDo()) {
                switch (msg.what) {
                    case 4096:
                        int i = msg.arg1;
                        if (msg.obj instanceof Ccode) {
                            ccode = (Ccode) msg.obj;
                        }
                        cls.handleSsidGet(i, ccode);
                        return;
                    case 4097:
                        int i2 = msg.arg1;
                        if (msg.obj instanceof Ccode) {
                            ccode = (Ccode) msg.obj;
                        }
                        cls.handlePwdGet(i2, ccode);
                        return;
                    case 4098:
                        cls.handleSsidSet(msg.arg1, msg.obj);
                        return;
                    case 4099:
                        cls.handlePwdSet(msg.arg1, msg.obj);
                        return;
                    case 4100:
                        cls.handleUpdateSetting(msg.arg1, msg.arg2, msg.obj);
                        return;
                    case 4101:
                        cls.handleRestartWifi(msg.arg1, msg.arg2, msg.obj);
                        return;
                    case 4102:
                        cls.handleChannelListGetted(msg.arg1, msg.obj);
                        return;
                    case 4103:
                        cls.handleCodeRateGetted(msg.arg1, msg.obj);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private static final class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIWifiController mInstance = new DJIWifiController();

        private SingletonHolder() {
        }
    }

    public static boolean isSupport5G(Context context) {
        if (context == null) {
            WM220LogUtil.LOGD("context == null,return false");
            return false;
        }
        boolean support5G = DjiSharedPreferencesManager.getBoolean(context, KEY_PHONE_SUPPORT_5G, false);
        WM220LogUtil.LOGD("**SharedPreferences :support5G= " + support5G);
        if (support5G) {
            return true;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager == null) {
            return false;
        }
        if (wifiManager.isWifiEnabled()) {
            WM220LogUtil.LOGD("** scan result");
            if (context instanceof Activity) {
                getInstance().verifyWifiPermissions((Activity) context);
            }
            List<ScanResult> scanResults = wifiManager.getScanResults();
            if (scanResults == null) {
                return false;
            }
            Iterator<ScanResult> it2 = scanResults.iterator();
            while (true) {
                if (it2.hasNext()) {
                    if (it2.next().frequency >= 5500) {
                        support5G = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            WM220LogUtil.LOGD("**scan if support 5g: " + support5G);
        }
        DjiSharedPreferencesManager.putBoolean(context, KEY_PHONE_SUPPORT_5G, support5G);
        return support5G;
    }

    public void verifyWifiPermissions(Activity context) {
        WM220LogUtil.LOGD("**verifyWifiPermissions: ");
        String[] PERMISSIONS_WIFI = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION};
        int permission = ActivityCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION);
        if (Build.VERSION.SDK_INT >= 23 && permission != 0) {
            ActivityCompat.requestPermissions(context, PERMISSIONS_WIFI, 0);
        }
    }
}
