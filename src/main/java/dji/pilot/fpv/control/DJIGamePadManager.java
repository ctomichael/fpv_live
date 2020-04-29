package dji.pilot.fpv.control;

import android.content.Context;
import android.hardware.input.InputManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.InputDeviceCompat;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.drew.lang.annotations.NotNull;
import dji.common.airlink.WiFiFrequencyBand;
import dji.common.gimbal.Rotation;
import dji.common.gimbal.RotationMode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.model.P3.DataRcSetCustomFuction;
import dji.pilot.fpv.model.IEventObjects;
import dji.pilot.publics.objects.DjiSharedPreferencesManager;
import dji.pilot.publics.util.DJICommonUtils;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.GimbalKeys;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class DJIGamePadManager {
    private static final int PITCH_SPEED = 27;
    private static final int SEND_DELAY = 100;
    private static final int SEND_INTERVAL = 40;
    private static final String SP_KEY_RC_BUTTON_A = "dji.pilot.fpv.control.KEY_RC_BUTTON_A";
    private static final String SP_KEY_RC_BUTTON_B = "dji.pilot.fpv.control.KEY_RC_BUTTON_B";
    private static final String SP_KEY_RC_BUTTON_X = "dji.pilot.fpv.control.KEY_RC_BUTTON_X";
    private static final String SP_KEY_RC_BUTTON_Y = "dji.pilot.fpv.control.KEY_RC_BUTTON_Y";
    private static final String TAG = "DJIGamePadManager";
    private static boolean isEnable = false;
    private static boolean needDualBand = true;
    private InputManager.InputDeviceListener inputDeviceListener = new InputManager.InputDeviceListener() {
        /* class dji.pilot.fpv.control.DJIGamePadManager.AnonymousClass1 */

        public void onInputDeviceAdded(int deviceId) {
            if (DJICommonUtils.isMammoth() && DJIGamePadManager.this.isHasGamePadConnected()) {
                EventBus.getDefault().post(IEventObjects.FpvGamePadEvent.ACTION_DEVICE_ADD);
                DJIGamePadManager.this.set();
            }
        }

        public void onInputDeviceRemoved(int deviceId) {
            if (DJICommonUtils.isMammoth() && !DJIGamePadManager.this.isHasGamePadConnected()) {
                EventBus.getDefault().post(IEventObjects.FpvGamePadEvent.ACTION_DEVICE_REMOVE);
                DJIGamePadManager.this.reset();
            }
        }

        public void onInputDeviceChanged(int deviceId) {
        }
    };
    private final InputManager mInputManager;
    private Timer mPitchTimer;
    private int pitchState = -1;

    public void set() {
        setSend71Hz();
    }

    public void reset() {
        setSend10Hz();
        sendLeftMedian(0.0d, 0.0d);
        sendRightMedian(0.0d, 0.0d);
        if (this.pitchState == 0) {
            stopGimbalPitch(true);
        } else if (this.pitchState == 1) {
            stopGimbalPitch(false);
        }
        this.pitchState = -1;
    }

    public DJIGamePadManager(Context context) {
        this.mInputManager = (InputManager) context.getSystemService("input");
    }

    public void onCreate() {
        if (isEnable) {
            registerInputDeviceListener(this.inputDeviceListener);
        }
    }

    public void onDestroy() {
        if (isEnable) {
            unregisterInputDeviceListener(this.inputDeviceListener);
            reset();
            if (this.mPitchTimer != null) {
                this.mPitchTimer.cancel();
                this.mPitchTimer = null;
            }
        }
    }

    private boolean isFromJoyStick(MotionEvent event) {
        return event != null && ((event.getSource() & InputDeviceCompat.SOURCE_JOYSTICK) == 16777232 || (event.getSource() & 1025) == 1025) && event.getAction() == 2;
    }

    private boolean isFromGameDpadDevice(InputEvent event) {
        return event != null && ((event.getSource() & 1025) == 1025 || (event.getSource() & 513) == 513);
    }

    public void setSend71Hz() {
        if (DJICommonUtils.isWM230() || DJICommonUtils.isMammoth()) {
            JoystickController.getInstance().setTimeInterval(33);
        } else {
            JoystickController.getInstance().setTimeInterval(14);
        }
    }

    public void setSend10Hz() {
        JoystickController.getInstance().restoreTimeInterval();
    }

    public boolean dispatchGenericMotionEvent(Context context, MotionEvent event) {
        if (!isEnable) {
            return false;
        }
        d("MotionEvent action=" + event.getAction() + "isFromJoyStick=" + isFromJoyStick(event));
        if (!DJICommonUtils.isMammoth() || !isHasGamePadConnected() || !isFromJoyStick(event)) {
            return false;
        }
        d("x=" + event.getX() + " y=" + event.getY());
        for (JoyStickEvent joyStickEvent : obtainJoyStickEvent(event, false)) {
            sendLeftMedian(joyStickEvent.xLeft, joyStickEvent.yLeft);
            sendRightMedian(joyStickEvent.xRight, joyStickEvent.yRight);
            if (!(Float.compare(joyStickEvent.hatX, -1.0f) == 0 || Float.compare(joyStickEvent.hatX, 1.0f) == 0)) {
                if (Float.compare(joyStickEvent.hatY, -1.0f) == 0) {
                    this.pitchState = 0;
                    startGimbalPitch(true);
                } else if (Float.compare(joyStickEvent.hatY, 1.0f) == 0) {
                    this.pitchState = 1;
                    startGimbalPitch(false);
                } else if (Float.compare(joyStickEvent.hatY, 0.0f) == 0) {
                    if (this.pitchState == 0) {
                        stopGimbalPitch(true);
                    } else if (this.pitchState == 1) {
                        stopGimbalPitch(false);
                    }
                    this.pitchState = -1;
                }
            }
        }
        return true;
    }

    private void startGimbalPitch(boolean up) {
        if (this.mPitchTimer != null) {
            this.mPitchTimer.cancel();
        }
        final Rotation upOrDownRotation = new Rotation.Builder().mode(RotationMode.SPEED).pitch(up ? 27.0f : -27.0f).build();
        this.mPitchTimer = new Timer();
        this.mPitchTimer.schedule(new TimerTask() {
            /* class dji.pilot.fpv.control.DJIGamePadManager.AnonymousClass2 */

            public void run() {
                DJISDKCache.getInstance().performAction(KeyHelper.getGimbalKey(GimbalKeys.ROTATE), null, upOrDownRotation);
            }
        }, 100, 40);
    }

    private void stopGimbalPitch(boolean up) {
        if (this.mPitchTimer != null) {
            this.mPitchTimer.cancel();
            this.mPitchTimer = null;
        }
        DJISDKCache.getInstance().performAction(KeyHelper.getGimbalKey(GimbalKeys.ROTATE), null, new Rotation.Builder().mode(RotationMode.SPEED).pitch(up ? 27.0f : -27.0f).build());
        DJISDKCache.getInstance().performAction(KeyHelper.getGimbalKey(GimbalKeys.ROTATE), null, new Rotation.Builder().mode(RotationMode.SPEED).build());
    }

    private void sendLeftMedian(double xLeft, double yLeft) {
        int yaw = ((int) (((double) JoystickController.MAX_CHANNEL_VALUE) * xLeft)) + 1024;
        JoystickController.getInstance().touchLeftJoystick(((int) (((double) JoystickController.MAX_CHANNEL_VALUE) * (-yLeft))) + 1024, yaw);
    }

    private void sendRightMedian(double xRight, double yRight) {
        int roll = ((int) (((double) JoystickController.MAX_CHANNEL_VALUE) * xRight)) + 1024;
        JoystickController.getInstance().touchRightJoystick(((int) (((double) JoystickController.MAX_CHANNEL_VALUE) * (-yRight))) + 1024, roll);
    }

    public boolean dispatchKeyEvent(Context context, KeyEvent event) {
        if (!isEnable) {
            return false;
        }
        d("KeyEvent action=" + event.getAction() + " keyCode=" + event.getKeyCode() + " isFromGameDpadDevice=" + isFromGameDpadDevice(event));
        if (!DJICommonUtils.isMammoth() || !isHasGamePadConnected() || !isFromGameDpadDevice(event)) {
            return false;
        }
        d("keyCode=" + event.getKeyCode());
        DataRcSetCustomFuction.DJICustomType customType = null;
        switch (event.getKeyCode()) {
            case 19:
                if (event.getAction() != 0) {
                    if (event.getAction() == 1) {
                        stopGimbalPitch(true);
                        break;
                    }
                } else {
                    startGimbalPitch(true);
                    break;
                }
                break;
            case 20:
                if (event.getAction() != 0) {
                    if (event.getAction() == 1) {
                        stopGimbalPitch(false);
                        break;
                    }
                } else {
                    startGimbalPitch(false);
                    break;
                }
                break;
            case 82:
            case 109:
                if (event.getAction() == 1) {
                    EventBus.getDefault().post(IEventObjects.CommonEventObj.LEFTMENU_JS_CLICK_FOR_SPARK_GAME_PAD);
                    break;
                }
                break;
            case 96:
                customType = DataRcSetCustomFuction.DJICustomType.find(getButtonA(context));
                break;
            case 97:
                customType = DataRcSetCustomFuction.DJICustomType.find(getButtonB(context));
                break;
            case 99:
                customType = DataRcSetCustomFuction.DJICustomType.find(getButtonX(context));
                break;
            case 100:
                customType = DataRcSetCustomFuction.DJICustomType.find(getButtonY(context));
                break;
            case 102:
                if (event.getAction() == 1) {
                    EventBus.getDefault().post(IEventObjects.FpvGamePadEvent.ACTION_RECORD);
                    break;
                }
                break;
            case 103:
                if (event.getAction() == 1) {
                    EventBus.getDefault().post(IEventObjects.FpvGamePadEvent.ACTION_SHUTTER);
                    break;
                }
                break;
        }
        if (customType != null && event.getAction() == 1) {
            EventBus.getDefault().post(customType);
        }
        return true;
    }

    private List<JoyStickEvent> obtainJoyStickEvent(MotionEvent event, boolean obtainHistory) {
        InputDevice inputDevice = event.getDevice();
        List<JoyStickEvent> joyStickEvents = new LinkedList<>();
        int size = obtainHistory ? 0 : event.getHistorySize();
        for (int position = -1; position < size; position++) {
            JoyStickEvent joyStickPoint = new JoyStickEvent();
            double xLeft = (double) getCenteredAxis(event, inputDevice, 0, position);
            double yLeft = (double) getCenteredAxis(event, inputDevice, 1, position);
            if (Math.abs(xLeft) < 0.02d) {
                xLeft = 0.0d;
            }
            if (Math.abs(yLeft) < 0.02d) {
                yLeft = 0.0d;
            }
            if (xLeft == Double.NEGATIVE_INFINITY || yLeft == Double.POSITIVE_INFINITY) {
                xLeft = 0.0d;
                yLeft = 0.0d;
            }
            float hatX = getCenteredAxis(event, inputDevice, 15, position);
            float hatY = getCenteredAxis(event, inputDevice, 16, position);
            double xRight = (double) getCenteredAxis(event, inputDevice, 11, position);
            double yRight = (double) getCenteredAxis(event, inputDevice, 14, position);
            if (Math.abs(xRight) < 0.02d) {
                xRight = 0.0d;
            }
            if (Math.abs(yRight) < 0.02d) {
                yRight = 0.0d;
            }
            if (xRight == Double.NEGATIVE_INFINITY || yRight == Double.POSITIVE_INFINITY) {
                xRight = 0.0d;
                yRight = 0.0d;
            }
            joyStickPoint.xLeft = xLeft;
            joyStickPoint.yLeft = yLeft;
            joyStickPoint.hatX = hatX;
            joyStickPoint.hatY = hatY;
            joyStickPoint.xRight = xRight;
            joyStickPoint.yRight = yRight;
            joyStickEvents.add(joyStickPoint);
        }
        return joyStickEvents;
    }

    private float getCenteredAxis(MotionEvent event, InputDevice device, int axis, int historyPos) {
        InputDevice.MotionRange range = device.getMotionRange(axis, event.getSource());
        if (range != null) {
            float flat = range.getFlat();
            float value = historyPos < 0 ? event.getAxisValue(axis) : event.getHistoricalAxisValue(axis, historyPos);
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0.0f;
    }

    public void registerInputDeviceListener(InputManager.InputDeviceListener inputDeviceListener2) {
        this.mInputManager.registerInputDeviceListener(inputDeviceListener2, new Handler(Looper.getMainLooper()));
    }

    public void unregisterInputDeviceListener(InputManager.InputDeviceListener inputDeviceListener2) {
        this.mInputManager.unregisterInputDeviceListener(inputDeviceListener2);
    }

    public boolean isHasGamePadConnected() {
        return checkHasGamePadConnected(this.mInputManager);
    }

    public static boolean isHasGamePadConnected(Context context) {
        if (context == null) {
            return false;
        }
        return checkHasGamePadConnected((InputManager) context.getSystemService("input"));
    }

    private static boolean checkHasGamePadConnected(@NotNull InputManager inputManager) {
        if (inputManager == null || !isEnable) {
            return false;
        }
        for (int id : inputManager.getInputDeviceIds()) {
            int source = inputManager.getInputDevice(id).getSources();
            if ((source & InputDeviceCompat.SOURCE_JOYSTICK) == 16777232 || (source & 1025) == 1025) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSupportGameController() {
        return DJICommonUtils.isMammoth();
    }

    public static boolean checkNeedDualBand(Context context) {
        if (!isEnable || context == null || !isSupportGameController() || !isHasGamePadConnected(context)) {
            return false;
        }
        boolean isShowSwitchChannel = getNeedDualBand();
        boolean is24Fb = isFREQUENCY_BAND_2_DOT_4_GHZ();
        d("isShowSwitchChannel=" + isShowSwitchChannel + " is24fb=" + is24Fb + " is5GhzSupported=" + isDualBandSupported(context));
        if (!isShowSwitchChannel || !is24Fb) {
            return false;
        }
        return true;
    }

    public static void setNeedDualBand(boolean need) {
        needDualBand = need;
    }

    public static boolean getNeedDualBand() {
        return needDualBand;
    }

    private static boolean isFREQUENCY_BAND_2_DOT_4_GHZ() {
        WiFiFrequencyBand freq = DJICommonUtils.getWiFiFrequencyBand();
        return freq == WiFiFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ || freq == WiFiFrequencyBand.FREQUENCY_BAND_ONLY_2_DOT_4;
    }

    public static boolean isDualBandSupported(Context context) {
        boolean is5GhzSupported = false;
        try {
            is5GhzSupported = ((Boolean) Class.forName("android.net.wifi.WifiManager").getMethod("is5GHzBandSupported", new Class[0]).invoke((WifiManager) context.getApplicationContext().getSystemService("wifi"), new Object[0])).booleanValue();
            d("is5GhzSupported = " + is5GhzSupported);
            return is5GhzSupported;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            d("isDualBandSupported exception " + e);
            return is5GhzSupported;
        }
    }

    private class JoyStickEvent {
        float hatX;
        float hatY;
        double xLeft;
        double xRight;
        double yLeft;
        double yRight;

        private JoyStickEvent() {
        }
    }

    public static DataRcSetCustomFuction.DJICustomType[] getGamePadCustomTypes() {
        return new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.Navigation, DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.PlayBack, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.OneKeyTakeOffLanding};
    }

    public static void setButtonX(Context context, int type) {
        DjiSharedPreferencesManager.putInt(context, SP_KEY_RC_BUTTON_X, type);
    }

    public static void setButtonY(Context context, int type) {
        DjiSharedPreferencesManager.putInt(context, SP_KEY_RC_BUTTON_Y, type);
    }

    public static void setButtonA(Context context, int type) {
        DjiSharedPreferencesManager.putInt(context, SP_KEY_RC_BUTTON_A, type);
    }

    public static void setButtonB(Context context, int type) {
        DjiSharedPreferencesManager.putInt(context, SP_KEY_RC_BUTTON_B, type);
    }

    public static int getButtonX(Context context) {
        return DjiSharedPreferencesManager.getInt(context, SP_KEY_RC_BUTTON_X, DataRcSetCustomFuction.DJICustomType.Navigation.value());
    }

    public static int getButtonY(Context context) {
        return DjiSharedPreferencesManager.getInt(context, SP_KEY_RC_BUTTON_Y, DataRcSetCustomFuction.DJICustomType.CameraSetting.value());
    }

    public static int getButtonA(Context context) {
        return DjiSharedPreferencesManager.getInt(context, SP_KEY_RC_BUTTON_A, DataRcSetCustomFuction.DJICustomType.OneKeyTakeOffLanding.value());
    }

    public static int getButtonB(Context context) {
        return DjiSharedPreferencesManager.getInt(context, SP_KEY_RC_BUTTON_B, DataRcSetCustomFuction.DJICustomType.GimbalCenter.value());
    }

    private static void d(String message) {
        DJILogHelper.getInstance().LOGD(TAG, message);
    }
}
