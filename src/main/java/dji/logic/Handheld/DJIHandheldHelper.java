package dji.logic.Handheld;

import android.content.Context;
import android.os.Handler;
import dji.log.DJILogHelper;
import dji.logic.utils.DJIProductSupportUtil;
import dji.midware.ble.BluetoothLeService;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataCommonPushHeart;
import dji.midware.data.model.P3.DataOsdSetLED;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.midware.util.RepeatDataBase;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIHandheldHelper {
    public static String ISMCU303 = "is_mcu_303";
    public static String MCUCONFIRMED = "mcu_confirmed";
    private static final String TAG = "DJIHandheldHelper";
    private static Runnable sendLEDCmdRunnable = new Runnable() {
        /* class dji.logic.Handheld.DJIHandheldHelper.AnonymousClass3 */

        public void run() {
            new DataOsdSetLED().reset().setBlueUnit(1, -1, 32, 255).start(new DJIDataCallBack() {
                /* class dji.logic.Handheld.DJIHandheldHelper.AnonymousClass3.AnonymousClass1 */

                public void onSuccess(Object model) {
                }

                public void onFailure(Ccode ccode) {
                    DJILogHelper.getInstance().LOGE(DJIHandheldHelper.TAG, "set led failed" + ccode, true, true);
                }
            });
        }
    };
    public boolean McuConfirmed;
    public boolean isMcu303;
    /* access modifiers changed from: private */
    public Context mCtx;
    private Timer mHeartTimer;

    private static final class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIHandheldHelper mInstance = new DJIHandheldHelper();

        private SingletonHolder() {
        }
    }

    public static DJIHandheldHelper getInstance() {
        return SingletonHolder.mInstance;
    }

    private DJIHandheldHelper() {
        this.isMcu303 = true;
        this.McuConfirmed = false;
    }

    public void setContext(Context context) {
        this.mCtx = context;
        this.McuConfirmed = DjiSharedPreferencesManager.getBoolean(this.mCtx, MCUCONFIRMED, false);
        if (this.McuConfirmed) {
            this.isMcu303 = DjiSharedPreferencesManager.getBoolean(this.mCtx, ISMCU303, true);
        }
        DJIEventBusUtil.register(this);
    }

    public void sendHeartPack() {
        if (this.mHeartTimer == null) {
            this.mHeartTimer = new Timer();
        }
        final DataCommonPushHeart pushHeart = new DataCommonPushHeart().setReceiver(DeviceType.OFDM);
        this.mHeartTimer.schedule(new TimerTask() {
            /* class dji.logic.Handheld.DJIHandheldHelper.AnonymousClass1 */

            public void run() {
                pushHeart.start((DJIDataCallBack) null);
            }
        }, 0, 1000);
    }

    public void sendHeartPack(DeviceType device, int receiverId) {
        if (this.mHeartTimer == null) {
            this.mHeartTimer = new Timer();
        }
        final DataCommonPushHeart pushHeart = new DataCommonPushHeart().setReceiver(device);
        pushHeart.setReceiverId(receiverId);
        this.mHeartTimer.schedule(new TimerTask() {
            /* class dji.logic.Handheld.DJIHandheldHelper.AnonymousClass2 */

            public void run() {
                pushHeart.start((DJIDataCallBack) null);
            }
        }, 0, 1000);
    }

    public void stopHeartPack() {
        if (this.mHeartTimer != null) {
            this.mHeartTimer.cancel();
            this.mHeartTimer = null;
        }
    }

    public void sendLEDControlCMD() {
        new Handler(BackgroundLooper.getLooper()).postDelayed(sendLEDCmdRunnable, 2000);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        if (event == DataEvent.ConnectOK && BluetoothLeService.getInstance().isConnected() && DJIProductSupportUtil.isLonganMobile(null)) {
            checkMcu();
        }
    }

    private void checkMcu() {
        final DataCommonGetVersion getter = new DataCommonGetVersion();
        getter.setDeviceType(DeviceType.find(9));
        new RepeatDataBase(getter, 10, new DJIDataCallBack() {
            /* class dji.logic.Handheld.DJIHandheldHelper.AnonymousClass4 */

            public void onSuccess(Object model) {
                if (getter.getLoaderByte(2) == 1) {
                    DJIHandheldHelper.this.isMcu303 = true;
                } else {
                    DJIHandheldHelper.this.isMcu303 = false;
                }
                DJIHandheldHelper.this.McuConfirmed = true;
                DJILogHelper.getInstance().LOGD(getClass().getSimpleName(), "get mcu type:" + DJIHandheldHelper.this.isMcu303);
                DjiSharedPreferencesManager.putBoolean(DJIHandheldHelper.this.mCtx, DJIHandheldHelper.MCUCONFIRMED, DJIHandheldHelper.this.McuConfirmed);
                DjiSharedPreferencesManager.putBoolean(DJIHandheldHelper.this.mCtx, DJIHandheldHelper.ISMCU303, DJIHandheldHelper.this.isMcu303);
            }

            public void onFailure(Ccode ccode) {
                DJILogHelper.getInstance().LOGD(getClass().getSimpleName(), "get mcu type failed");
            }
        }).start();
    }

    public void setMcu303(boolean isMcu3032) {
        this.isMcu303 = isMcu3032;
    }
}
