package dji.pilot.fpv.control;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.WM220LogUtil;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycSetJoyStickParams;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.params.P3.ParamCfgName;
import dji.midware.interfaces.DJIDataCallBack;
import dji.pilot.publics.objects.DjiSharedPreferencesManager;
import dji.pilot.publics.util.DJICommonUtils;
import java.util.Timer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class JoystickController implements JoystickPredefinedParams {
    private static final int DELAY_DOWN_STOP = 5000;
    private static final int DELAY_DOWN_STOP_MAMMOTH = 3000;
    public static final String KEY_WIFI_RC_STICK_MODE = "key_wifi_rc_stick_mode";
    public static int MAX_CHANNEL_VALUE = 660;
    public static final int MAX_DIST = 80;
    public static final int MAX_HEIGHT = 50;
    private static final int MSG_DOWN_STOP = 1001;
    private static JoystickController instance = null;
    private final int MAX_TIME_INTERVAL = 100;
    /* access modifiers changed from: private */
    public boolean isBeginnerMode = false;
    boolean isControlledByGimbal = false;
    private JoyStickCurrentData joyStickCurrentData = new JoyStickCurrentData();
    private Timer joystickTimer = null;
    private Context mContext;
    /* access modifiers changed from: private */
    public int mCurDistLimit = 0;
    /* access modifiers changed from: private */
    public int mCurHeightLimit = 0;
    /* access modifiers changed from: private */
    public DataFlycSetJoyStickParams mFlycSetJoyStickParams = new DataFlycSetJoyStickParams(false);
    private Handler mHandler = new Handler(new Handler.Callback() {
        /* class dji.pilot.fpv.control.JoystickController.AnonymousClass3 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    boolean unused = JoystickController.this.mShowReachMaxDownThrottle = true;
                    return false;
                default:
                    return false;
            }
        }
    });
    /* access modifiers changed from: private */
    public int mPitch = 1024;
    /* access modifiers changed from: private */
    public int mRecordTime = 0;
    /* access modifiers changed from: private */
    public int mRoll = 1024;
    private int mSendInterval = 100;
    /* access modifiers changed from: private */
    public boolean mShowReachMaxDownThrottle = false;
    /* access modifiers changed from: private */
    public int mThrottle = 1024;
    private WifiRcStickMode mWifiRcStickMode = WifiRcStickMode.AmericanMode;
    /* access modifiers changed from: private */
    public int mYaw = 1024;
    /* access modifiers changed from: private */
    public DataFlycSetJoyStickParams.FlycMode mode = DataFlycSetJoyStickParams.FlycMode.P;

    public interface OnHeightOrDistLimitSettedListenner {
        void onBothSetted();

        void onDistLimitSetted();

        void onHeightLimitSetted();
    }

    public enum WifiStickModeChanged {
        TRUE
    }

    public enum WifiRcStickMode {
        AmericanMode,
        JapaneseMode,
        ChineseMode
    }

    static /* synthetic */ int access$1008(JoystickController x0) {
        int i = x0.mRecordTime;
        x0.mRecordTime = i + 1;
        return i;
    }

    public static synchronized JoystickController getInstance() {
        JoystickController joystickController;
        synchronized (JoystickController.class) {
            if (instance == null) {
                instance = new JoystickController();
            }
            joystickController = instance;
        }
        return joystickController;
    }

    public void init(Context ctx) {
        this.mContext = ctx.getApplicationContext();
        this.mWifiRcStickMode = WifiRcStickMode.values()[DjiSharedPreferencesManager.getInt(ctx, KEY_WIFI_RC_STICK_MODE, 0)];
    }

    private JoystickController() {
        EventBus.getDefault().register(this);
    }

    public class JoyStickCurrentData {
        private int leftHorizontal = 0;
        private int leftVertical = 0;
        private int rightHorizontal = 0;
        private int rightVertical = 0;

        public JoyStickCurrentData(int leftHorizontal2, int leftVertical2, int rightHorizontal2, int rightVertical2) {
            this.leftHorizontal = leftHorizontal2;
            this.leftVertical = leftVertical2;
            this.rightHorizontal = rightHorizontal2;
            this.rightVertical = rightVertical2;
        }

        public JoyStickCurrentData() {
        }

        public int getLeftHorizontal() {
            return this.leftHorizontal;
        }

        public int getLeftVertical() {
            return this.leftVertical;
        }

        public int getRightHorizontal() {
            return this.rightHorizontal;
        }

        public int getRightVertical() {
            return this.rightVertical;
        }

        /* access modifiers changed from: private */
        public void setLeftHorizontal(int leftHorizontal2) {
            this.leftHorizontal = leftHorizontal2;
        }

        /* access modifiers changed from: private */
        public void setLeftVertical(int leftVertical2) {
            this.leftVertical = leftVertical2;
        }

        /* access modifiers changed from: private */
        public void setRightHorizontal(int rightHorizontal2) {
            this.rightHorizontal = rightHorizontal2;
        }

        /* access modifiers changed from: private */
        public void setRightVertical(int rightVertical2) {
            this.rightVertical = rightVertical2;
        }
    }

    public JoyStickCurrentData getJoyStickCurrentData() {
        return this.joyStickCurrentData;
    }

    public void setTimeInterval(int timeInterval) {
        this.mSendInterval = timeInterval;
        stopTimer();
        startTimer();
    }

    public void restoreTimeInterval() {
        this.mSendInterval = 100;
        stopTimer();
        startTimer();
    }

    public int getMaxChannelVal() {
        return MAX_CHANNEL_VALUE;
    }

    public void setMaxChannelVal(int val) {
        MAX_CHANNEL_VALUE = val;
    }

    public void touchLeftJoystick(int _updown, int _leftright) {
        if (this.mWifiRcStickMode == WifiRcStickMode.AmericanMode) {
            setThrottle(_updown);
            setYaw(_leftright);
        } else if (this.mWifiRcStickMode == WifiRcStickMode.ChineseMode) {
            setPitch(_updown);
            setRoll(_leftright);
        } else if (this.mWifiRcStickMode == WifiRcStickMode.JapaneseMode) {
            setPitch(_updown);
            setYaw(_leftright);
        }
        this.joyStickCurrentData.setLeftHorizontal(_leftright);
        this.joyStickCurrentData.setLeftVertical(_updown);
    }

    private void setThrottle(int _throttle) {
        int height;
        this.mThrottle = _throttle;
        if (this.mThrottle > MAX_CHANNEL_VALUE + 1024) {
            this.mThrottle = MAX_CHANNEL_VALUE + 1024;
        } else if (this.mThrottle <= (1024 - MAX_CHANNEL_VALUE) + 5) {
            if (this.mShowReachMaxDownThrottle) {
                DataOsdGetPushCommon osdCommonObj = DataOsdGetPushCommon.getInstance();
                if (osdCommonObj.isSwaveWork()) {
                    height = osdCommonObj.getSwaveHeight();
                } else {
                    height = osdCommonObj.getHeight();
                }
                if (height < 5) {
                    this.mThrottle = 364;
                }
            } else {
                this.mThrottle = 1024 - MAX_CHANNEL_VALUE;
            }
            if (this.mHandler.hasMessages(1001)) {
                return;
            }
            if (DJIProductManager.getInstance().getType() == ProductType.Mammoth) {
                this.mHandler.sendEmptyMessageDelayed(1001, 3000);
            } else {
                this.mHandler.sendEmptyMessageDelayed(1001, 5000);
            }
        } else {
            this.mHandler.removeMessages(1001);
            this.mShowReachMaxDownThrottle = false;
        }
    }

    private void setYaw(int _yaw) {
        if (!this.isControlledByGimbal) {
            this.mYaw = _yaw;
            if (this.mYaw > MAX_CHANNEL_VALUE + 1024) {
                this.mYaw = MAX_CHANNEL_VALUE + 1024;
            } else if (this.mYaw < 1024 - MAX_CHANNEL_VALUE) {
                this.mYaw = 1024 - MAX_CHANNEL_VALUE;
            }
            if (DJICommonUtils.isMammoth()) {
                this.mYaw = ((int) ((((float) (this.mYaw - 1024)) / 3.0f) * 2.0f)) + 1024;
            }
        }
    }

    public void controlYaw(boolean isControlledByGimbal2, int yaw) {
        this.isControlledByGimbal = isControlledByGimbal2;
        this.mYaw = yaw;
    }

    public void touchRightJoystick(int _updown, int _leftright) {
        if (this.mWifiRcStickMode == WifiRcStickMode.AmericanMode) {
            setPitch(_updown);
            setRoll(_leftright);
        } else if (this.mWifiRcStickMode == WifiRcStickMode.ChineseMode) {
            setThrottle(_updown);
            setYaw(_leftright);
        } else if (this.mWifiRcStickMode == WifiRcStickMode.JapaneseMode) {
            setThrottle(_updown);
            setRoll(_leftright);
        }
        this.joyStickCurrentData.setRightHorizontal(_leftright);
        this.joyStickCurrentData.setRightVertical(_updown);
    }

    private void setPitch(int _pitch) {
        this.mPitch = _pitch;
        if (this.mPitch > MAX_CHANNEL_VALUE + 1024) {
            this.mPitch = MAX_CHANNEL_VALUE + 1024;
        } else if (this.mPitch < 1024 - MAX_CHANNEL_VALUE) {
            this.mPitch = 1024 - MAX_CHANNEL_VALUE;
        }
    }

    private void setRoll(int _roll) {
        this.mRoll = _roll;
        if (this.mRoll > MAX_CHANNEL_VALUE + 1024) {
            this.mRoll = MAX_CHANNEL_VALUE + 1024;
        } else if (this.mRoll < 1024 - MAX_CHANNEL_VALUE) {
            this.mRoll = 1024 - MAX_CHANNEL_VALUE;
        }
    }

    public void setMode(DataFlycSetJoyStickParams.FlycMode _mode) {
        this.mode = _mode;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void startTimer() {
        /*
            r6 = this;
            r0 = 0
            r6.mRecordTime = r0
            monitor-enter(r6)
            dji.logic.manager.DJIUSBWifiSwitchManager r0 = dji.logic.manager.DJIUSBWifiSwitchManager.getInstance()     // Catch:{ all -> 0x0037 }
            r1 = 0
            boolean r0 = r0.isProductWifiConnected(r1)     // Catch:{ all -> 0x0037 }
            if (r0 == 0) goto L_0x001a
            dji.logic.manager.DJIUSBWifiSwitchManager r0 = dji.logic.manager.DJIUSBWifiSwitchManager.getInstance()     // Catch:{ all -> 0x0037 }
            r1 = 0
            boolean r0 = r0.isRcWifiConnected(r1)     // Catch:{ all -> 0x0037 }
            if (r0 == 0) goto L_0x001c
        L_0x001a:
            monitor-exit(r6)     // Catch:{ all -> 0x0037 }
        L_0x001b:
            return
        L_0x001c:
            java.util.Timer r0 = new java.util.Timer     // Catch:{ all -> 0x0037 }
            java.lang.String r1 = "joystickTimer"
            r0.<init>(r1)     // Catch:{ all -> 0x0037 }
            r6.joystickTimer = r0     // Catch:{ all -> 0x0037 }
            java.util.Timer r0 = r6.joystickTimer     // Catch:{ all -> 0x0037 }
            dji.pilot.fpv.control.JoystickController$1 r1 = new dji.pilot.fpv.control.JoystickController$1     // Catch:{ all -> 0x0037 }
            r1.<init>()     // Catch:{ all -> 0x0037 }
            r2 = 10
            int r4 = r6.mSendInterval     // Catch:{ all -> 0x0037 }
            long r4 = (long) r4     // Catch:{ all -> 0x0037 }
            r0.schedule(r1, r2, r4)     // Catch:{ all -> 0x0037 }
            monitor-exit(r6)     // Catch:{ all -> 0x0037 }
            goto L_0x001b
        L_0x0037:
            r0 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0037 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.fpv.control.JoystickController.startTimer():void");
    }

    private void stopTimer() {
        this.mRecordTime = 0;
        synchronized (this) {
            if (this.joystickTimer != null) {
                this.joystickTimer.cancel();
                this.joystickTimer = null;
            }
        }
    }

    public void checkDistHeightLimit(final OnHeightOrDistLimitSettedListenner callback) {
        DataFlycGetParams.getInstance().setInfos(new String[]{ParamCfgName.GCONFIG_BEGINNER_MODE, ParamCfgName.GCONFIG_LIMIT_HEIGHT, ParamCfgName.GCONFIG_FLY_LIMIT}).start(new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.JoystickController.AnonymousClass2 */

            public void onSuccess(Object model) {
                boolean z = true;
                JoystickController joystickController = JoystickController.this;
                if (DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_BEGINNER_MODE).value.intValue() != 1) {
                    z = false;
                }
                boolean unused = joystickController.isBeginnerMode = z;
                if (!JoystickController.this.isBeginnerMode) {
                    boolean isHeightSetted = false;
                    boolean isDistSetted = false;
                    int unused2 = JoystickController.this.mCurHeightLimit = DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_LIMIT_HEIGHT).value.intValue();
                    if (JoystickController.this.mCurHeightLimit > 50) {
                        int unused3 = JoystickController.this.mCurHeightLimit = 50;
                        JoystickController.this.setHeightLimit();
                        isHeightSetted = true;
                    }
                    int unused4 = JoystickController.this.mCurDistLimit = DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_FLY_LIMIT).value.intValue();
                    if (JoystickController.this.mCurDistLimit > 80) {
                        int unused5 = JoystickController.this.mCurDistLimit = 80;
                        JoystickController.this.setDistLimit();
                        isDistSetted = true;
                    }
                    if (isHeightSetted || isDistSetted) {
                        callback.onBothSetted();
                    }
                }
            }

            public void onFailure(Ccode ccode) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void setHeightLimit() {
        new DataFlycSetParams().setInfo(ParamCfgName.GCONFIG_LIMIT_HEIGHT, Integer.valueOf(this.mCurHeightLimit)).start((DJIDataCallBack) null);
    }

    /* access modifiers changed from: private */
    public void setDistLimit() {
        new DataFlycSetParams().setInfo(ParamCfgName.GCONFIG_FLY_LIMIT, Integer.valueOf(this.mCurDistLimit)).start((DJIDataCallBack) null);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(WifiStickModeChanged event) {
        this.mWifiRcStickMode = WifiRcStickMode.values()[DjiSharedPreferencesManager.getInt(this.mContext, KEY_WIFI_RC_STICK_MODE, WifiRcStickMode.AmericanMode.ordinal())];
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        WM220LogUtil.LOGD("into joystickController DataCameraEvent");
        if (!DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null) || DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(null)) {
            if (!DJIUSBWifiSwitchManager.getInstance().isWifiConnected() || DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(null)) {
                stopTimer();
            }
        } else if (event == DataCameraEvent.ConnectOK) {
            setMode(this.mode);
            startTimer();
        } else if (event == DataCameraEvent.ConnectLose) {
            stopTimer();
        }
    }
}
