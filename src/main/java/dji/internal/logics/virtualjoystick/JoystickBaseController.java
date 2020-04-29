package dji.internal.logics.virtualjoystick;

import android.os.Handler;
import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.model.P3.DataFlycSetJoyStickParams;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class JoystickBaseController {
    private static final int ACTUAL_IDLE_JOYSTICK_INTERVAL = 42;
    public static final int BASE_CHANNEL_VALUE = 1024;
    private static final int DELAY_DOWN_STOP = 5000;
    private static final float DIVIDER_MAX = 0.89f;
    private static final int IDLE_JOYSTICK_INTERVAL = 50;
    private static final int MSG_DOWN_STOP = 1001;
    private static final int NORMAL_JOYSTICK_INTERVAL = 14;
    private static final int ORIGINAL_MAX_CHANNEL_VALUE = 660;
    public static final int maxChannelValue = 587;
    /* access modifiers changed from: private */
    public DataFlycSetJoyStickParams flycSetJoyStickParams;
    private Handler handler;
    private Timer joystickTimer;
    /* access modifiers changed from: private */
    public DataFlycSetJoyStickParams.FlycMode mode;
    /* access modifiers changed from: private */
    public int pitch;
    /* access modifiers changed from: private */
    public int roll;
    private int sendInterval;
    /* access modifiers changed from: private */
    public boolean showReachMaxDownThrottle;
    /* access modifiers changed from: private */
    public int throttle;
    /* access modifiers changed from: private */
    public int yaw;

    public static synchronized JoystickBaseController getInstance() {
        JoystickBaseController access$000;
        synchronized (JoystickBaseController.class) {
            access$000 = SingletonHolder.joystickBaseController;
        }
        return access$000;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static JoystickBaseController joystickBaseController = new JoystickBaseController();

        private SingletonHolder() {
        }
    }

    public void init() {
        startTimer();
        DJIEventBusUtil.register(this);
    }

    private JoystickBaseController() {
        this.flycSetJoyStickParams = new DataFlycSetJoyStickParams(false);
        this.mode = DataFlycSetJoyStickParams.FlycMode.P;
        this.sendInterval = 42;
        this.showReachMaxDownThrottle = false;
        this.joystickTimer = null;
        this.yaw = 1024;
        this.roll = 1024;
        this.pitch = 1024;
        this.throttle = 1024;
        this.handler = new Handler(BackgroundLooper.getLooper(), new Handler.Callback() {
            /* class dji.internal.logics.virtualjoystick.JoystickBaseController.AnonymousClass2 */

            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1001:
                        boolean unused = JoystickBaseController.this.showReachMaxDownThrottle = true;
                        return false;
                    default:
                        return false;
                }
            }
        });
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        stopTimer();
    }

    public void setNormalTimeInterval() {
        this.sendInterval = 14;
        startTimer();
    }

    public void setIdleTimeInterval() {
        this.sendInterval = 42;
        startTimer();
    }

    public int getMaxChannelVal() {
        return 587;
    }

    public void touchLeftJoystick(int upDown, int leftRight) {
        setThrottle(upDown);
        setYaw(leftRight);
    }

    public void setThrottle(int throttle2) {
        int height;
        this.throttle = throttle2;
        if (this.throttle > 1611) {
            this.throttle = 1611;
        } else if (this.throttle <= 442) {
            if (this.showReachMaxDownThrottle) {
                DataOsdGetPushCommon osdCommonObj = DataOsdGetPushCommon.getInstance();
                if (osdCommonObj.isSwaveWork()) {
                    height = osdCommonObj.getSwaveHeight();
                } else {
                    height = osdCommonObj.getHeight();
                }
                if (height < 5) {
                    this.throttle = 364;
                }
            } else {
                this.throttle = 437;
            }
            if (!this.handler.hasMessages(1001)) {
                this.handler.sendEmptyMessageDelayed(1001, 5000);
            }
        } else {
            this.handler.removeMessages(1001);
            this.showReachMaxDownThrottle = false;
        }
    }

    public void setYaw(int yaw2) {
        this.yaw = yaw2;
        if (this.yaw > 1611) {
            this.yaw = 1611;
        } else if (this.yaw < 437) {
            this.yaw = 437;
        }
    }

    public void touchRightJoystick(int upDown, int leftRight) {
        setPitch(upDown);
        setRoll(leftRight);
    }

    public void setPitch(int pitch2) {
        this.pitch = pitch2;
        if (this.pitch > 1611) {
            this.pitch = 1611;
        } else if (this.pitch < 437) {
            this.pitch = 437;
        }
    }

    public void setRoll(int roll2) {
        this.roll = roll2;
        if (this.roll > 1611) {
            this.roll = 1611;
        } else if (this.roll < 437) {
            this.roll = 437;
        }
    }

    public void setMode(DataFlycSetJoyStickParams.FlycMode mode2) {
        this.mode = mode2;
    }

    private void startTimer() {
        synchronized (this) {
            if (DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null)) {
                if (this.joystickTimer != null) {
                    this.joystickTimer.cancel();
                    this.joystickTimer = null;
                }
                this.joystickTimer = new Timer("joystickTimer");
                this.joystickTimer.schedule(new TimerTask() {
                    /* class dji.internal.logics.virtualjoystick.JoystickBaseController.AnonymousClass1 */

                    public void run() {
                        JoystickBaseController.this.flycSetJoyStickParams.setJoyStick(JoystickBaseController.this.throttle, JoystickBaseController.this.pitch, JoystickBaseController.this.roll, JoystickBaseController.this.yaw).setMode(JoystickBaseController.this.mode).start();
                    }
                }, 10, (long) this.sendInterval);
            }
        }
    }

    private void stopTimer() {
        synchronized (this) {
            if (this.joystickTimer != null) {
                this.joystickTimer.cancel();
                this.joystickTimer = null;
            }
        }
    }

    public boolean isConnected() {
        return this.joystickTimer != null;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
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
