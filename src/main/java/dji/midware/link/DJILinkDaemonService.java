package dji.midware.link;

import android.os.Handler;
import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.ble.BluetoothLeService;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.DJIConnectType;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.data.manager.P3.DJIVideoPackManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.natives.FPVController;
import dji.midware.sockets.P3.PhantomService;
import dji.midware.sockets.P3.WifiService;
import dji.midware.sockets.dpad.DPadWifiService;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.usbhost.P3.NativeRcController;
import dji.midware.usbhost.P3.UsbHostService;
import dji.midware.usbhost.P3.UsbHostServiceRC;
import dji.midware.util.MidwareBackgroundLooper;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class DJILinkDaemonService {
    private static DJILinkDaemonService instance = null;
    private DJIServiceInterface curServiceInterface;
    private Handler handler;
    private volatile boolean isAlive;
    private boolean isSupportOnlyForBluetoothDevice = false;
    private volatile DJILinkType linkType = DJILinkType.NON;

    public static synchronized DJILinkDaemonService getInstance() {
        DJILinkDaemonService dJILinkDaemonService;
        synchronized (DJILinkDaemonService.class) {
            if (instance == null) {
                instance = new DJILinkDaemonService();
            }
            dJILinkDaemonService = instance;
        }
        return dJILinkDaemonService;
    }

    private DJILinkDaemonService() {
        init();
    }

    public void startDaemon() {
        boolean linkByUsbRc = DJILinkUtil.usbUsbRc();
        boolean linkByUsbWifi = DJILinkUtil.useUsbWifi();
        boolean linkByUsbRcOrWifi = DJILinkUtil.useUsbRcOrWifi();
        if (!this.isSupportOnlyForBluetoothDevice) {
            UsbAccessoryService.getInstance();
            if (!UsbAccessoryService.getInstance().isConnected()) {
                if (!linkByUsbRcOrWifi) {
                    WifiService.getInstance();
                }
                if (linkByUsbRc) {
                    UsbHostServiceRC.getInstance().resume();
                }
                if (linkByUsbWifi) {
                    DPadWifiService.getInstance();
                }
            } else {
                return;
            }
        }
        if (!linkByUsbRcOrWifi) {
            BluetoothLeService.getInstance();
        }
    }

    private void stopDaemon() {
        DJILogHelper.getInstance().LOGE("linkDeamon", "stopDaemon " + this.linkType, false, false);
        if (this.linkType != DJILinkType.AOA) {
            UsbAccessoryService.Destroy();
        }
        if (this.linkType != DJILinkType.ADB) {
            PhantomService.Destroy();
        }
        if (this.linkType != DJILinkType.WIFI) {
            WifiService.Destroy();
        }
        if (this.linkType != DJILinkType.BLE) {
            BluetoothLeService.Destroy();
        }
        if (this.linkType != DJILinkType.HOSTRC) {
            UsbHostServiceRC.Pause();
        }
        if (this.linkType != DJILinkType.USB_WIFI) {
            DPadWifiService.Destroy();
        }
    }

    public void init() {
        this.handler = new Handler(MidwareBackgroundLooper.getLooper(), new Handler.Callback() {
            /* class dji.midware.link.DJILinkDaemonService.AnonymousClass1 */

            public boolean handleMessage(Message msg) {
                DJILinkDaemonService.this.startDaemon();
                return false;
            }
        });
        this.isAlive = true;
        this.handler.sendEmptyMessage(0);
        if (!this.isSupportOnlyForBluetoothDevice) {
            FPVController.native_setVideoDataRecver(DJIVideoDataRecver.getInstance());
            FPVController.native_setVideoPackObject(DJIVideoPackManager.getInstance());
        }
    }

    public void onDestroy() {
        this.isAlive = false;
        if (!this.isSupportOnlyForBluetoothDevice) {
            UsbAccessoryService.DestroyFinal();
            PhantomService.Destroy();
            WifiService.DestroyFinal();
        }
        BluetoothLeService.Destroy();
    }

    public DJILinkType getLinkType() {
        return this.linkType;
    }

    private void changeTo(DJIServiceInterface serviceInterface) {
        this.curServiceInterface = serviceInterface;
        stopDaemon();
        ServiceManager.getInstance().changeTo(serviceInterface);
        EventBus.getDefault().post(this.linkType);
    }

    public DJIServiceInterface getInterface() {
        return this.curServiceInterface;
    }

    public void setLinkType(DJILinkType linkType2) {
        this.linkType = linkType2;
        if (this.handler.hasMessages(0)) {
            this.handler.removeMessages(0);
        }
        if (this.isAlive) {
            switch (linkType2) {
                case AOA:
                    DJIPackManager.getInstance().setIsCheckConnect(true);
                    if (NativeRcController.useUsbdec()) {
                        DJIPackManager.getInstance().setConnectTypeDirectLy(DJIConnectType.MC);
                    }
                    changeTo(UsbAccessoryService.getInstance());
                    break;
                case ADB:
                    DJIPackManager.getInstance().setIsCheckConnect(true);
                    changeTo(PhantomService.getInstance());
                    break;
                case WIFI:
                    DJIPackManager.getInstance().setIsCheckConnect(true);
                    changeTo(WifiService.getInstance());
                    break;
                case HOST:
                    DJIPackManager.getInstance().setIsCheckConnect(true);
                    changeTo(UsbHostService.getInstance());
                    break;
                case HOSTRC:
                    DJIPackManager.getInstance().setIsCheckConnect(true);
                    if (DpadProductManager.getInstance().useUsbdec()) {
                        DJIPackManager.getInstance().setConnectTypeDirectLy(DJIConnectType.RC);
                    }
                    changeTo(UsbHostServiceRC.getInstance());
                    break;
                case BLE:
                    DJIPackManager.getInstance().setIsCheckConnect(false);
                    changeTo(BluetoothLeService.getInstance());
                    break;
                case USB_WIFI:
                    DJIPackManager.getInstance().setIsCheckConnect(true);
                    if (DpadProductManager.getInstance().useUsbWifiLink()) {
                        DJIPackManager.getInstance().setConnectTypeDirectLy(DJIConnectType.RC);
                    }
                    changeTo(DPadWifiService.getInstance());
                    break;
                case NON:
                    this.handler.sendEmptyMessageDelayed(0, 1000);
                    break;
            }
            DJILogHelper.getInstance().LOGD("linkDeamon", "linkType=" + linkType2, false, false);
        }
    }

    public void setSupportOnlyForBluetoothDevice(boolean isSupportOnlyForBluetoothDevice2) {
        this.isSupportOnlyForBluetoothDevice = isSupportOnlyForBluetoothDevice2;
    }
}
