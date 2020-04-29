package dji.logic.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataWifiSwitchSDR;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.link.DJILinkUtil;
import dji.midware.sockets.P3.WifiService;
import dji.midware.upgradeComponent.DJIUpgradeProductID;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIUSBWifiSwitchManager {
    private static final int CONNECT_CHECK_DELAY = 10000;
    private static final int MSG_NOT_CONNECT_REMOTE = 1;
    private static DJIUSBWifiSwitchManager mInstance = null;
    /* access modifiers changed from: private */
    public boolean isJustSwitchFromWifi = false;
    private AlertDialog mCannotSwitchDlg;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(BackgroundLooper.getLooper(), new Handler.Callback() {
        /* class dji.logic.manager.DJIUSBWifiSwitchManager.AnonymousClass1 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    EventBus.getDefault().post(SwitchUIAction.SHOW_NOT_CONNECT_DLG);
                    return false;
                default:
                    return false;
            }
        }
    });
    private boolean mNeedShowRCConnectDlg = false;
    private AlertDialog mSwitchPromptDlg;
    private WifiConnectType mWifiConnectType = WifiConnectType.NONE;
    private WifiService wifiService;

    @Deprecated
    public enum SwitchUIAction {
        SHOW_SWITCH_DLG,
        SHOW_NOT_CONNECT_DLG
    }

    public enum USBConnectAction {
        CONNECT_LOST
    }

    public enum WifiConnectType {
        CONTROL,
        NONE
    }

    public boolean isNeedShowRCConnectDlg() {
        return this.mNeedShowRCConnectDlg;
    }

    public void setNeedShowRCConnectDlg(boolean needShowRCConnectDlg) {
        this.mNeedShowRCConnectDlg = needShowRCConnectDlg;
    }

    private DJIUSBWifiSwitchManager() {
        DJIEventBusUtil.register(this);
    }

    public static synchronized DJIUSBWifiSwitchManager getInstance() {
        DJIUSBWifiSwitchManager dJIUSBWifiSwitchManager;
        synchronized (DJIUSBWifiSwitchManager.class) {
            if (mInstance == null) {
                mInstance = new DJIUSBWifiSwitchManager();
            }
            dJIUSBWifiSwitchManager = mInstance;
        }
        return dJIUSBWifiSwitchManager;
    }

    public boolean isWifiConnected() {
        if (DJILinkUtil.useUsbRcOrWifi()) {
            return false;
        }
        if (this.wifiService == null) {
            this.wifiService = WifiService.getInstance();
        }
        return this.wifiService.isConnected();
    }

    public boolean isUSBAoaConnected() {
        return UsbAccessoryService.getInstance().isConnected();
    }

    public boolean isProductWifiConnected(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return isWifiConnected() && (type == ProductType.KumquatX || type == ProductType.KumquatS || type == ProductType.Mammoth || type == ProductType.WM230 || type == ProductType.WM160);
    }

    public boolean isRcWifiConnected(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (this.wifiService != null && this.wifiService.isRcConnect() && type == ProductType.Mammoth) {
            return true;
        }
        return false;
    }

    public boolean isProductAoaConnected(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return isUSBAoaConnected() && (type == ProductType.KumquatX || type == ProductType.KumquatS || type == ProductType.Mammoth || type == ProductType.WM230 || type == ProductType.WM240 || type == ProductType.WM245);
    }

    public void showRCConnectedDlg(Context ctx, String title, String connectContent, String cannotConnectContent) {
        if (DataOsdGetPushCommon.getInstance().groundOrSky() == 2) {
            showCannotSwitchDlg(ctx, title, cannotConnectContent);
        } else {
            showSwitchPromptDlg(ctx, title, connectContent);
        }
        this.mNeedShowRCConnectDlg = false;
    }

    public void showCannotSwitchDlg(Context ctx, String title, String content) {
        if (this.mCannotSwitchDlg == null || !this.mCannotSwitchDlg.isShowing()) {
            if (this.mCannotSwitchDlg != null && !this.mCannotSwitchDlg.getContext().equals(ctx)) {
                this.mCannotSwitchDlg = null;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle(title);
            builder.setMessage(content);
            builder.setPositiveButton(17039379, new DialogInterface.OnClickListener() {
                /* class dji.logic.manager.DJIUSBWifiSwitchManager.AnonymousClass2 */

                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setCancelable(false);
            this.mCannotSwitchDlg = builder.create();
            if (this.mSwitchPromptDlg != null && this.mSwitchPromptDlg.isShowing()) {
                this.mSwitchPromptDlg.dismiss();
            }
            this.mCannotSwitchDlg.show();
        }
    }

    public void showSwitchPromptDlg(final Context ctx, String title, String content) {
        if (this.mSwitchPromptDlg == null || !this.mSwitchPromptDlg.isShowing()) {
            if (this.mSwitchPromptDlg != null && !this.mSwitchPromptDlg.getContext().equals(ctx)) {
                this.mSwitchPromptDlg = null;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle(title);
            builder.setMessage(content);
            builder.setPositiveButton(17039379, new DialogInterface.OnClickListener() {
                /* class dji.logic.manager.DJIUSBWifiSwitchManager.AnonymousClass3 */

                public void onClick(DialogInterface dialog, int which) {
                    DataWifiSwitchSDR.getInstance().start(new DJIDataCallBack() {
                        /* class dji.logic.manager.DJIUSBWifiSwitchManager.AnonymousClass3.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            ((WifiManager) ctx.getSystemService("wifi")).setWifiEnabled(false);
                            boolean unused = DJIUSBWifiSwitchManager.this.isJustSwitchFromWifi = true;
                            DJIUSBWifiSwitchManager.this.mHandler.sendEmptyMessageDelayed(1, 10000);
                        }

                        public void onFailure(Ccode ccode) {
                        }
                    });
                }
            });
            builder.setNegativeButton(17039369, new DialogInterface.OnClickListener() {
                /* class dji.logic.manager.DJIUSBWifiSwitchManager.AnonymousClass4 */

                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setCancelable(false);
            this.mSwitchPromptDlg = builder.create();
            if (this.mCannotSwitchDlg != null && this.mCannotSwitchDlg.isShowing()) {
                this.mCannotSwitchDlg.dismiss();
            }
            this.mSwitchPromptDlg.show();
        }
    }

    public void showNotConnectDlg(Context ctx, String content) {
        if (this.isJustSwitchFromWifi) {
            this.isJustSwitchFromWifi = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setMessage(content);
            builder.setPositiveButton(17039379, new DialogInterface.OnClickListener() {
                /* class dji.logic.manager.DJIUSBWifiSwitchManager.AnonymousClass5 */

                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
    }

    public void resetSwitchFromWifiFlag() {
        DJILogHelper.getInstance().LOGE(DJIUpgradeProductID.WM220, "****resetSwitchFromWifiFlag", false, true);
        this.isJustSwitchFromWifi = false;
        this.mHandler.removeMessages(1);
        if (this.mSwitchPromptDlg != null && this.mSwitchPromptDlg.isShowing()) {
            this.mSwitchPromptDlg.dismiss();
        }
        if (this.mCannotSwitchDlg != null && this.mCannotSwitchDlg.isShowing()) {
            this.mCannotSwitchDlg.dismiss();
        }
    }

    public WifiConnectType getWifiConnectType() {
        return this.mWifiConnectType;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        if (event == DataCameraEvent.ConnectOK) {
            resetSwitchFromWifiFlag();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        if (event != DataEvent.ConnectOK) {
            this.mWifiConnectType = WifiConnectType.NONE;
            EventBus.getDefault().post(WifiConnectType.NONE);
        } else if (isProductWifiConnected(DJIProductManager.getInstance().getType())) {
            this.mWifiConnectType = WifiConnectType.CONTROL;
            EventBus.getDefault().post(WifiConnectType.CONTROL);
        } else {
            this.mWifiConnectType = WifiConnectType.NONE;
            EventBus.getDefault().post(WifiConnectType.NONE);
        }
    }
}
