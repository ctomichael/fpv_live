package dji.logic.wifi;

import android.os.Handler;
import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataWifiRequestPushSnr;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIWifiHelper {
    private static final long DELAY_REQUEST_PUSHSNR = 100;
    private static final int MSG_ID_REQUEST_G_PUSHSNR = 4096;
    private static final int MSG_ID_REQUEST_PUSHSNR = 4097;
    private static final boolean REQUEST_SNRPUSH = true;
    /* access modifiers changed from: private */
    public static final String TAG = DJIWifiHelper.class.getSimpleName();
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private volatile boolean mMultipleChannel;
    private ProductType mProductType;

    public static DJIWifiHelper getInstance() {
        if (!EventBus.getDefault().isRegistered(SingletonHolder.mInstance)) {
            SingletonHolder.mInstance.init();
        }
        return SingletonHolder.mInstance;
    }

    public void setmMultipleChannelOpen(boolean open) {
        this.mMultipleChannel = open;
        if (open) {
            handleRequestGSnrPush();
            handleRequestSnrPush();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(ProductType type) {
        if (type != this.mProductType) {
            this.mProductType = type;
            handleRequestGSnrPush();
            handleRequestSnrPush();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        if (DataEvent.ConnectOK == event) {
            handleRequestGSnrPush();
        } else {
            this.mHandler.removeMessages(4096);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        if (DataCameraEvent.ConnectOK == event) {
            handleRequestSnrPush();
        } else {
            this.mHandler.removeMessages(4097);
        }
    }

    /* access modifiers changed from: private */
    public void handleRequestSnrPush() {
        DJILogHelper.getInstance().LOGD(TAG, "request wifi snr push-" + this.mMultipleChannel + ";" + ServiceManager.getInstance().isConnected() + ";" + this.mProductType + ";", false, true);
        if (this.mMultipleChannel && ServiceManager.getInstance().isRemoteOK()) {
            if (ProductType.P34K == this.mProductType || ProductType.litchiC == this.mProductType) {
                DJILogHelper.getInstance().LOGD(TAG, "request wifi snr push start", false, true);
                new DataWifiRequestPushSnr().setPush(true).setReceiver(DeviceType.WIFI).start(new DJIDataCallBack() {
                    /* class dji.logic.wifi.DJIWifiHelper.AnonymousClass2 */

                    public void onSuccess(Object model) {
                        DJILogHelper.getInstance().LOGD(DJIWifiHelper.TAG, "request wifi snr push success", false, true);
                    }

                    public void onFailure(Ccode ccode) {
                        if (Ccode.TIMEOUT == ccode && !DJIWifiHelper.this.mHandler.hasMessages(4097)) {
                            DJIWifiHelper.this.mHandler.sendEmptyMessageDelayed(4097, DJIWifiHelper.DELAY_REQUEST_PUSHSNR);
                        }
                        DJILogHelper.getInstance().LOGD(DJIWifiHelper.TAG, "request wifi snr push fail-" + ccode, false, true);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleRequestGSnrPush() {
        DJILogHelper.getInstance().LOGD(TAG, "request wifi-g snr push-" + this.mMultipleChannel + ";" + ServiceManager.getInstance().isConnected() + ";" + this.mProductType + ";", false, true);
        if (this.mMultipleChannel && ServiceManager.getInstance().isConnected()) {
            if (ProductType.P34K == this.mProductType || ProductType.litchiC == this.mProductType) {
                DJILogHelper.getInstance().LOGD(TAG, "request wifi-g snr push start", false, true);
                new DataWifiRequestPushSnr().setPush(true).setReceiver(DeviceType.WIFI_G).start(new DJIDataCallBack() {
                    /* class dji.logic.wifi.DJIWifiHelper.AnonymousClass3 */

                    public void onSuccess(Object model) {
                        DJILogHelper.getInstance().LOGD(DJIWifiHelper.TAG, "request wifi-g snr push success", false, true);
                    }

                    public void onFailure(Ccode ccode) {
                        if (Ccode.TIMEOUT == ccode && !DJIWifiHelper.this.mHandler.hasMessages(4096)) {
                            DJIWifiHelper.this.mHandler.sendEmptyMessageDelayed(4096, DJIWifiHelper.DELAY_REQUEST_PUSHSNR);
                        }
                        DJILogHelper.getInstance().LOGD(DJIWifiHelper.TAG, "request wifi-g snr push fail-" + ccode, false, true);
                    }
                });
            }
        }
    }

    private DJIWifiHelper() {
        this.mProductType = ProductType.OTHER;
        this.mMultipleChannel = false;
        this.mHandler = new Handler(BackgroundLooper.getLooper(), new Handler.Callback() {
            /* class dji.logic.wifi.DJIWifiHelper.AnonymousClass1 */

            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 4096:
                        DJIWifiHelper.this.handleRequestGSnrPush();
                        return false;
                    case 4097:
                        DJIWifiHelper.this.handleRequestSnrPush();
                        return false;
                    default:
                        return false;
                }
            }
        });
    }

    public void init() {
        DJIEventBusUtil.register(this);
        onEvent3BackgroundThread(DJIProductManager.getInstance().getType());
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        this.mHandler.removeCallbacksAndMessages(null);
    }

    private static final class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIWifiHelper mInstance = new DJIWifiHelper();

        private SingletonHolder() {
        }
    }
}
